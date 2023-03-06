package Utilities;

import Exceptions.TestExecutionException;
import com.google.gson.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static Utilities.StringUtil.interpolate;
import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
import static com.google.gson.ToNumberPolicy.LONG_OR_DOUBLE;
import static java.text.MessageFormat.format;

public class JsonUtil {
    private static final Logger LOGGER = Logger.getLogger(JsonUtil.class.getName());
    private static final Gson GSON;

    static {
        GSON = new GsonBuilder().setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
                .setObjectToNumberStrategy(LONG_OR_DOUBLE)
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    private JsonUtil() {
        // util class.
    }

    public static void saveJsonToFile(Map<String, Map> jsonMap, String fileName) {
        LOGGER.info("\tSave the following json to file: " + fileName + "   with jsonmap:  " + jsonMap);
        File file = new File(fileName);
        if (file.exists()) {
            LOGGER.info("File: " + file + "  exixts.  Delete it first");
            boolean isFileDeleted = file.delete();
            LOGGER.info("File deleted? " + isFileDeleted);
            if (!isFileDeleted) {
                throw new TestExecutionException("Unable to delete older, already existing capabilities file: " + fileName);
            }
        } else {
            LOGGER.info("File " + file + " does not exist. Create it\n");
        }
        try (Writer writer = new FileWriter(fileName)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(jsonMap, writer);
        } catch (IOException e) {
            throw new TestExecutionException(String.format("Unable to save following json to file: '%s'%n'%s'%n", jsonMap, fileName) + " : " + e);
        }
    }

    public static Map<String, Map> getNodeValueAsMapFromJsonFile(String node, String fileName) {
        Map<String, Map> map = loadJsonFileChildAsMap(fileName);
        LOGGER.info("\tNode: " + node);
        Map<String, Map> envMap = map.get(node);
        LOGGER.info("\tLoaded map: " + envMap);
        if (null == envMap) {
            throw new TestExecutionException(String.format("Node: '%s' not found in file: '%s'", node, fileName));
        }
        return envMap;
    }

    public static Map<String, Map> loadJsonFileChildAsMap(String fileName) {
        LOGGER.info("\tLoading Json file: " + fileName);
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(fileName));
            Map<String, Map> map = gson.fromJson(reader, Map.class);
            reader.close();
            return map;
        } catch (IOException e) {
            throw new TestExecutionException(String.format("Unable to load json file: '%s'", fileName) + " : " + e);
        }
    }

    public static String getNodeValueAsStringFromJsonFile(String fileName, String[] nodeTree) {
        Map<String, Map> map = loadJsonFileChildAsMap(fileName);

        StringBuilder nodePath = new StringBuilder();
        for (int nodeCount = 0; nodeCount < nodeTree.length - 1; nodeCount++) {
            LOGGER.info("\tFinding node: " + nodeTree[nodeCount]);
            nodePath.append(nodeTree[nodeCount])
                    .append(" -> ");
            map = map.get(nodeTree[nodeCount]);
            if (null == map) {
                throw new TestExecutionException(String.format("Node: '%s' not found in file: '%s'", nodePath, fileName));
            }
        }
        String retValue = String.valueOf(map.get(nodeTree[nodeTree.length - 1]));
        LOGGER.info("\tFound value: " + retValue);
        return retValue;
    }

    public static ArrayList<Map> getNodeValueAsArrayListFromJsonFile(String fileName, String node) {
        Map<String, Map> map = loadJsonFileChildAsMap(fileName);
        LOGGER.info("\tPlatform: " + node);
        ArrayList<Map> envMap = (ArrayList<Map>) map.get(node);
        LOGGER.info("\tLoaded arraylist: " + envMap);
        return envMap;
    }

    public static JsonObject convertToMap(String jsonAsString) {
        return JsonParser.parseString(jsonAsString)
                .getAsJsonObject();
    }

    public static JsonArray convertToArray(String jsonAsString) {
        return JsonParser.parseString(jsonAsString)
                .getAsJsonArray();
    }

    public static Map<String, Object> loadJsonFile(String filePath) {
        try (final var reader = new FileReader(filePath)) {
            return GSON.<Map<String, Object>>fromJson(reader, Map.class);
        } catch (final JsonSyntaxException | IOException e) {
            var message = format("Error loading expectation [{0}]...", filePath);
            LOGGER.log(Level.SEVERE, message, e);
            throw new RuntimeException(message, e);
        }
    }

    public static String fromFile(String baseDir, String filePath, Map<String, Object> dynamicValues) {
        final var path = format("{0}/{1}", baseDir, filePath);
        try (final var reader = new FileReader(path)) {
            var json = GSON.fromJson(reader, Map.class);
            return interpolate(GSON.toJson(json), dynamicValues);
        } catch (final JsonSyntaxException | IOException e) {
            var message = format("Error parsing JSON file [{0}]...", filePath);
            LOGGER.log(Level.SEVERE, message, e);
            throw new RuntimeException(message, e);
        }
    }
}
