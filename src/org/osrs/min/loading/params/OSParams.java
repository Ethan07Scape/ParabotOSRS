package org.osrs.min.loading.params;

import org.parabot.api.io.WebUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class OSParams {

    private final Map<String, String> PARAMETER_MAP;
    private final String PARAMETER_BASE_URL = "runescape.com/l=0/jav_config.ws";
    private int worldId;

    public OSParams(final int worldId) {
        this.PARAMETER_MAP = new HashMap<>();
        this.worldId = worldId;
        configure(worldId);
    }

    public void configure(int world) {
        try {
            if (world <= 0)
                throw new IllegalArgumentException();
            final URLConnection connection = WebUtil.getConnection(new URL("http://oldschool" + world + "." + PARAMETER_BASE_URL));
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            PARAMETER_MAP.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("\">'", "\"").replaceAll("'", "")
                        .replaceAll("\\(", "").replaceAll("\\)", "")
                        .replaceAll("\"", "").replaceAll(" ", "")
                        .replaceAll("param=", "").replaceAll(";", "")
                        .replaceAll("value", "");
                final String[] splitted = line.split("=");
                if (splitted.length == 1) {
                    PARAMETER_MAP.put(splitted[0], "");
                } else if (splitted.length == 2) {
                    PARAMETER_MAP.put(splitted[0], splitted[1]);
                } else if (splitted.length == 3) {
                    PARAMETER_MAP.put(splitted[0], splitted[1] + "=" + splitted[2]);
                } else if (splitted.length == 4) {
                    PARAMETER_MAP.put(splitted[0], splitted[1] + "=" + splitted[2] + "=" + splitted[3]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void add(String key, String val) {
        PARAMETER_MAP.put(key, val);
    }

    public String get(String key) {
        return PARAMETER_MAP.containsKey(key) ? PARAMETER_MAP.get(key) : "";
    }

    public String getJarLink() {
        return get("initial_jar");
    }

    public int getWorldId() {
        return worldId;
    }

    public Map<String, String> getParameters() {
        return PARAMETER_MAP;
    }
}
