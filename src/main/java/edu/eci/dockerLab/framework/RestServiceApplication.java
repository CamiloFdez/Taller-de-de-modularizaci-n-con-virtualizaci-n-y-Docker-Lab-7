package edu.eci.dockerLab.framework;

import java.io.File;
import java.lang.reflect.Method;

import edu.eci.dockerLab.annotations.GetMapping;
import edu.eci.dockerLab.annotations.RequestParam;
import edu.eci.dockerLab.annotations.RestController;

public class RestServiceApplication {

    public static void main(String[] args) throws Exception {

        System.out.println("Escaneando controllers...");

        scanControllers("edu.eci.dockerLab.controllers");

        HttpServer.staticfiles("webroot/public");

        HttpServer.main(new String[]{});
    }

    private static void scanControllers(String packageName) throws Exception {

        String path = packageName.replace('.', '/');

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        var resource = classLoader.getResource(path);

        if (resource == null) return;

        File folder = new File(resource.toURI());

        for (File file : folder.listFiles()) {

            if (file.getName().endsWith(".class")) {

                String className = packageName + "." + file.getName().replace(".class", "");

                loadController(className);
            }
        }
    }

    private static void loadController(String className) throws Exception {

        Class<?> c = Class.forName(className);

        if (c.isAnnotationPresent(RestController.class)) {

            Object controllerInstance = c.getDeclaredConstructor().newInstance();

            for (Method m : c.getDeclaredMethods()) {

                if (m.isAnnotationPresent(GetMapping.class)) {

                    GetMapping annotation = m.getAnnotation(GetMapping.class);
                    String path = annotation.value();

                    System.out.println("Registrando endpoint: " + path);

                    HttpServer.get(path, (req, res) -> {

                        try {

                            var params = m.getParameters();
                            Object[] argsValues = new Object[params.length];

                            for (int i = 0; i < params.length; i++) {

                                if (params[i].isAnnotationPresent(RequestParam.class)) {

                                    RequestParam rp = params[i].getAnnotation(RequestParam.class);

                                    String paramName = rp.value();
                                    String defaultValue = rp.defaultValue();

                                    String value = req.getValue(paramName);

                                    if (value == null) value = defaultValue;

                                    argsValues[i] = value;
                                }
                            }

                            return (String) m.invoke(controllerInstance, argsValues);

                        } catch (Exception e) {
                            e.printStackTrace();
                            return "Error ejecutando método";
                        }
                    });
                }
            }
        }
    }
}