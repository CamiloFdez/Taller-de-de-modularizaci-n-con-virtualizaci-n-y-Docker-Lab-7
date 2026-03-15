package edu.eci.dockerLab.framework;

import java.util.HashMap;
import java.util.Map;

public class WebFramework {

    private static Map<String, WebMethod> getRoutes = new HashMap<>();

    public static void get(String path, WebMethod method) {
        getRoutes.put(path, method);
    }

    public static WebMethod getRoute(String path) {
        return getRoutes.get(path);
    }
}