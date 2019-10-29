package org.osrs.min.utils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class Utils {
    private static Utils instance = new Utils();

    public static Utils getInstance() {
        return instance;
    }

    public void addToSystemClassLoader(File jar) {
        try {
            File file = jar;
            if (!file.exists()) {
                System.out.println("No File!");
                System.exit(0);
            }
            System.out.println("Adding " + file.getName() + " to classloader.");
            URL url = file.toURI().toURL();
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
