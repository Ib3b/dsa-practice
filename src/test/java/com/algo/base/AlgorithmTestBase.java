package com.algo.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Base class for YAML-driven algorithm tests.
 * Subclasses parameterize C and call loadTestData(yamlPath) to load test cases,
 * then verifyAll() to run all cases with pass/fail summary.
 *
 * Supports two YAML formats:
 * <ul>
 *   <li><b>Full format</b> — each case is a named map (inputs + expected)</li>
 *   <li><b>Compact format</b> — cases are positional arrays, structure defined
 *       via a {@code columns} block; detected by presence of {@code columns} key</li>
 * </ul>
 */
public abstract class AlgorithmTestBase<C extends TestCase> {

    private static final ObjectMapper YAML_MAPPER = new ObjectMapper(new YAMLFactory());

    protected TestData<C> loadTestData(String yamlPath) {
        try {
            InputStream in = getClass().getClassLoader().getResourceAsStream(yamlPath);
            if (in == null) {
                throw new IllegalArgumentException("Test data not found: " + yamlPath);
            }
            JsonNode root = YAML_MAPPER.readTree(in);

            JsonNode typeNode = root.get("type");
            if (typeNode == null) {
                throw new IllegalArgumentException("Missing 'type' field in " + yamlPath);
            }
            String typeName = typeNode.asText();

            // Load and validate the concrete TestCase class
            Class<?> rawType = Class.forName(typeName);
            if (!TestCase.class.isAssignableFrom(rawType)) {
                throw new IllegalArgumentException(
                    "Class " + typeName + " does not implement TestCase");
            }
            @SuppressWarnings("unchecked")
            Class<C> caseType = (Class<C>) rawType.asSubclass(TestCase.class);

            // Detect format: columns present → compact positional format
            if (root.has("columns")) {
                return loadCompactTestData(root, caseType);
            }

            // Old format: direct Jackson deserialization
            JavaType testDataType = TypeFactory.defaultInstance()
                .constructParametricType(TestData.class, caseType);
            return YAML_MAPPER.readValue(
                getClass().getClassLoader().getResourceAsStream(yamlPath),
                testDataType
            );
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load test data from: " + yamlPath, e);
        }
    }

    /**
     * Parse compact format: positional values + columns schema.
     * Rebuilds a structured JsonNode matching the named format, then
     * delegates to Jackson's normal Record deserialization.
     */
    private TestData<C> loadCompactTestData(JsonNode root, Class<C> caseType) throws IOException {
        JsonNode columnsNode = root.get("columns");
        JsonNode casesArray = root.get("cases");

        // Column order is the insertion order of inputs sub-fields
        JsonNode inputsCols = columnsNode.get("inputs");
        List<String> inputFields = new ArrayList<>();
        Iterator<String> it = inputsCols.fieldNames();
        while (it.hasNext()) {
            inputFields.add(it.next());
        }

        List<C> cases = new ArrayList<>();
        for (JsonNode caseArray : casesArray) {
            if (!caseArray.isArray()) {
                throw new IllegalArgumentException(
                    "Each case must be an array in compact format");
            }

            // Build { inputs: { field1: val1, ... }, expected: val }
            ObjectNode structuredCase = YAML_MAPPER.createObjectNode();
            ObjectNode inputsObj = YAML_MAPPER.createObjectNode();

            for (int i = 0; i < inputFields.size(); i++) {
                inputsObj.set(inputFields.get(i), caseArray.get(i));
            }
            structuredCase.set("inputs", inputsObj);
            structuredCase.set("expected", caseArray.get(inputFields.size()));

            // Let Jackson convert the structured tree to the typed Record
            cases.add(YAML_MAPPER.treeToValue(structuredCase, caseType));
        }

        String algorithm = root.get("algorithm").asText();
        String type = root.get("type").asText();
        String description = root.has("description") ? root.get("description").asText() : "";
        return new TestData<>(algorithm, type, description, cases);
    }

    /**
     * Run all test cases, printing a X/Y passed summary and failure details.
     * Each case runs independently — failures in one case don't skip subsequent ones.
     *
     * @param testData loaded test data
     * @param verifier  callback that runs the algorithm and assertions for one case
     */
    protected void verifyAll(TestData<C> testData, Consumer<C> verifier) {
        List<C> cases = testData.cases();
        int total = cases.size();
        int passed = 0;
        List<String> failures = new ArrayList<>();

        for (int i = 0; i < total; i++) {
            C tc = cases.get(i);
            try {
                verifier.accept(tc);
                passed++;
            } catch (AssertionError | Exception e) {
                String msg = e instanceof AssertionError ae
                    ? ae.getMessage() : e.toString();
                failures.add(String.format("  Case %d:%n" +
                    "    Inputs:   %s%n" +
                    "    Expected: %s%n" +
                    "    %s",
                    i + 1,
                    describe(tc),
                    formatValue(tryGetExpected(tc)),
                    msg));
            }
        }

        System.out.printf("%s: %d/%d passed%n", testData.algorithm(), passed, total);

        if (!failures.isEmpty()) {
            System.out.println("Failures:");
            failures.forEach(System.out::println);
            fail(String.format("%d/%d test(s) failed", total - passed, total));
        }
    }

    // --- helpers for readable failure output ---

    private static String tryGetExpected(TestCase tc) {
        try {
            var m = tc.getClass().getMethod("expected");
            return formatValue(m.invoke(tc));
        } catch (Exception e) {
            return "?";
        }
    }

    private static String describe(Object obj) {
        if (obj == null) return "null";
        if (isRecord(obj)) {
            var sb = new StringBuilder(obj.getClass().getSimpleName()).append("[");
            var fields = obj.getClass().getRecordComponents();
            boolean first = true;
            for (var f : fields) {
                if (!first) sb.append(", ");
                first = false;
                sb.append(f.getName()).append("=");
                try {
                    sb.append(formatValue(f.getAccessor().invoke(obj)));
                } catch (Exception e) {
                    sb.append("?");
                }
            }
            return sb.append("]").toString();
        }
        return formatValue(obj);
    }

    private static boolean isRecord(Object obj) {
        return obj.getClass().isRecord();
    }

    private static String formatValue(Object val) {
        if (val == null) return "null";
        Class<?> clz = val.getClass();
        if (clz.isArray()) {
            if (val instanceof int[] a)      return Arrays.toString(a);
            if (val instanceof long[] a)     return Arrays.toString(a);
            if (val instanceof double[] a)   return Arrays.toString(a);
            if (val instanceof float[] a)    return Arrays.toString(a);
            if (val instanceof boolean[] a)  return Arrays.toString(a);
            if (val instanceof char[] a)     return Arrays.toString(a);
            if (val instanceof short[] a)    return Arrays.toString(a);
            if (val instanceof byte[] a)     return Arrays.toString(a);
            return Arrays.deepToString((Object[]) val);
        }
        if (isRecord(val)) return describe(val);
        return val.toString();
    }
}
