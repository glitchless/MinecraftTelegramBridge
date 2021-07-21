package ru.glitchless.telegrambridge.config;

import com.cedarsoftware.util.io.JsonWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import ru.glitchless.telegrambridge.core.config.AbstractConfig;
import ru.glitchless.telegrambridge.core.config.ConfigPath;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JSONConfig implements AbstractConfig {
    private final File jsonConfig;
    private JSONObject config = new JSONObject();

    public JSONConfig(File jsonConfig) {
        this.jsonConfig = jsonConfig;
        read();
    }

    private static ConfigPath getRootPath(ConfigPath path) {
        ConfigPath rootPath = path;
        while (rootPath.getParent() != null) {
            rootPath = rootPath.getParent();
        }
        return rootPath;
    }

    private static ConfigPath excludePath(ConfigPath from, ConfigPath exclude) {
        final List<ConfigPath> newPaths = new ArrayList<>();
        ConfigPath currentPath = from;
        while (currentPath != null && !currentPath.equals(exclude)) {
            newPaths.add(currentPath);
            currentPath = currentPath.getParent();
        }
        currentPath = null;
        for (int i = newPaths.size() - 1; i >= 0; i--) {
            currentPath = new ConfigPath(currentPath, newPaths.get(i).getName());
        }
        return currentPath;
    }

    /**
     * Merge "source" into "target". If fields have equal name, merge them recursively.
     *
     * @return the merged object (target).
     */
    private static JSONObject deepMerge(JSONObject source, JSONObject target) {
        for (Object key : source.keySet()) {
            Object value = source.get(key);
            if (!target.containsKey(key)) {
                // new value for "key":
                target.put(key, value);
            } else {
                // existing value for "key" - recursively deep merge:
                if (value instanceof JSONObject) {
                    JSONObject valueJson = (JSONObject) value;
                    deepMerge(valueJson, (JSONObject) target.get(key));
                } else {
                    target.put(key, value);
                }
            }
        }
        return target;
    }

    @Override
    public void setList(ConfigPath path, List<?> value, String... comment) {
        final JSONArray jsonArray = new JSONArray();
        for (Object toArray : value) {
            jsonArray.add(toArray);
        }
        getParentJsonObject(path).put(path.getName(), jsonArray);
    }

    @Override
    public void setValue(ConfigPath path, Object value, String... comment) {
        getParentJsonObject(path).put(path.getName(), value);
    }

    @Override
    public Object getValue(ConfigPath path) {
        final JSONObject jsonObject = getParentJsonObject(path);
        if (!jsonObject.containsKey(path.getName())) {
            return null;
        }
        return jsonObject.get(path.getName());
    }

    public void save() {
        try (Writer jsonWriter = new FileWriter(jsonConfig)) {
            final String formattedJson = JsonWriter.formatJson(config.toJSONString());
            jsonWriter.write(formattedJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject getParentJsonObject(ConfigPath path) {
        final ArrayList<ConfigPath> paths = path.flatten();
        JSONObject targetObj = config;
        for (int i = 0; i < paths.size() - 1; i++) {
            final Object tmpValue = targetObj.get(paths.get(i).getName());
            if (tmpValue instanceof JSONObject) {
                targetObj = (JSONObject) tmpValue;
            } else {
                final JSONObject newObject = new JSONObject();
                targetObj.put(paths.get(i).getName(), newObject);
                targetObj = newObject;
            }
        }
        return targetObj;
    }

    public void read() {
        if (!jsonConfig.exists()) {
            return;
        }
        try (Reader jsonReader = new FileReader(jsonConfig)) {
            deepMerge((JSONObject) JSONValue.parse(jsonReader), config);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

}
