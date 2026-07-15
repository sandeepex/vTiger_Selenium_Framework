package org.saroz.vtigercrm.utilities;

import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonReaderUtil {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Reads the entire JSON file and returns it as a JsonNode tree structure.
     * @param fileName The name of the JSON file located inside src/test/resources/testdata/
     * @return JsonNode representing the root node of the JSON tree.
     */
    public JsonNode getJsonData(String fileName) {
        try {
            File file = new File("src/test/resources/testdata/" + fileName);
            if (!file.exists()) {
                throw new RuntimeException("JSON data file not found at: " + file.getAbsolutePath());
            }
            return mapper.readTree(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON tree structure from file: " + fileName, e);
        }
    }
    
    
    /**
     * Retrieves a direct string value from a specific index within a JSON array.
     * * @param fileName The name of the JSON file located inside src/test/resources/testdata/
     * @param index    The 0-based index of the target object inside the JSON array.
     * @param key      The key whose value needs to be fetched.
     * @return String  The value mapped to the specified key as text.
     * * Example: getArrayValue("leads.json", 0, "firstName") -> Returns "Rahul"
     */
    public String getArrayValue(String fileName, int index, String key) {
        // Load the JSON file into a tree structure
        JsonNode jsonNode = getJsonData(fileName);
        
        // Ensure that the root element of the JSON is actually an array
        if (!jsonNode.isArray()) {
            throw new RuntimeException("The root of JSON is not an array in file: " + fileName);
        }
        
        // Fetch the specific object block at the given index
        JsonNode elementNode = jsonNode.get(index);
        if (elementNode == null) {
            throw new RuntimeException("Index " + index + " not found in JSON array.");
        }
        
        // Retrieve the target key from the selected object block
        JsonNode targetNode = elementNode.get(key);
        if (targetNode == null) {
            throw new RuntimeException("The key '" + key + "' was not found at index " + index);
        }
        
        // Return the value converted to a clean String
        return targetNode.asText();
    }

    /**
     * Retrieves a text value from a nested object block located at a specific index within a JSON array.
     * * @param fileName  The name of the JSON file located inside src/test/resources/testdata/
     * @param index     The 0-based index of the target object inside the JSON array.
     * @param parentKey The key of the inner parent object block (e.g., "address").
     * @param childKey  The key inside the parent object whose value needs to be fetched (e.g., "city").
     * @return String   The nested value as text.
     * * Example: getArrayNestedValue("leads.json", 0, "address", "city") -> Returns "Noida"
     */
    public String getArrayNestedValue(String fileName, int index, String parentKey, String childKey) {
        // Load the JSON file into a tree structure
        JsonNode jsonNode = getJsonData(fileName);
        
        // Ensure that the root element of the JSON is actually an array
        if (!jsonNode.isArray()) {
            throw new RuntimeException("The root of JSON is not an array.");
        }
        
        // Fetch the specific object block at the given index
        JsonNode elementNode = jsonNode.get(index);
        if (elementNode == null) {
            throw new RuntimeException("Index " + index + " not found in JSON array.");
        }
        
        // Extract the nested parent object block (e.g., "address")
        JsonNode parentNode = elementNode.get(parentKey);
        if (parentNode == null) {
            throw new RuntimeException("Parent key '" + parentKey + "' not found at index " + index);
        }
        
        // Extract the child key from inside the parent object block
        JsonNode childNode = parentNode.get(childKey);
        if (childNode == null) {
            throw new RuntimeException("Child key '" + childKey + "' inside '" + parentKey + "' not found.");
        }
        
        // Return the nested value converted to a clean String
        return childNode.asText();
    }

    /**
     * Retrieves a top-level string value directly mapped to a specific key.
     */
    public String getValue(String fileName, String key) {
        JsonNode jsonNode = getJsonData(fileName);
        JsonNode targetNode = jsonNode.get(key);
        if (targetNode == null) {
            throw new RuntimeException("The root key '" + key + "' was not found in " + fileName);
        }
        return targetNode.asText();
    }

    
    /**
     * Retrieves a text value located nested inside a specific parent object block.
     */
    public String getNestedValue(String fileName, String parentKey, String childKey) {
        JsonNode jsonNode = getJsonData(fileName);
        JsonNode parentNode = jsonNode.get(parentKey);
        
        if (parentNode == null) {
            throw new RuntimeException("The parent key '" + parentKey + "' was not found in " + fileName);
        }
        
        JsonNode childNode = parentNode.get(childKey);
        if (childNode == null) {
            throw new RuntimeException("The child key '" + childKey + "' inside parent object '" + parentKey + "' was not found.");
        }
        
        return childNode.asText();
    }
}