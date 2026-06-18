package com.algo.base;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * Base class for YAML-driven algorithm tests.
 * Subclasses parameterize C and call loadTestData(yamlPath) to load test cases.
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

            // Construct TestData<C> parameterized type and deserialize
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
}
