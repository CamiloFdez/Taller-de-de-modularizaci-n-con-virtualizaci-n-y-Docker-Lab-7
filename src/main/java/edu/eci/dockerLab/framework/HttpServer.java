package edu.eci.dockerLab.framework;

import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {

    private static String staticFilesPath = "webroot";
    private static volatile boolean running = true;

    private static void handleRequest(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream()));

            OutputStream outStream = clientSocket.getOutputStream();
            PrintWriter out = new PrintWriter(outStream, true);

            Map<String, String> parameters = new HashMap<>();

            String inputLine;
            boolean isFirstLine = true;
            String reqpath = "";

            while ((inputLine = in.readLine()) != null) {

                if (isFirstLine) {

                    String[] requestParts = inputLine.split(" ");
                    String uriStr = requestParts[1];

                    URI uri = new URI(uriStr);
                    reqpath = uri.getPath();

                    String query = uri.getQuery();

                    if (query != null) {
                        String[] params = query.split("&");

                        for (String param : params) {

                            String[] keyValue = param.split("=");

                            if (keyValue.length == 2) {
                                parameters.put(keyValue[0], keyValue[1]);
                            }
                        }
                    }

                    isFirstLine = false;
                }

                if (!in.ready()) {
                    break;
                }
            }

            HttpRequest request = new HttpRequest(parameters);
            WebMethod currentwm = WebFramework.getRoute(reqpath);

            if (currentwm != null) {

                String responseBody = currentwm.execute(request, null);

                String response =
                        "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n\r\n" +
                        "<!DOCTYPE html>" +
                        "<html><body>" +
                        responseBody +
                        "</body></html>";

                out.println(response);

            } else {

                String filePath = staticFilesPath + reqpath;

                InputStream fileStream = HttpServer.class.getResourceAsStream("/" + filePath);

                if (fileStream != null) {

                    String contentType = "text/html";

                    if (filePath.endsWith(".png")) contentType = "image/png";
                    else if (filePath.endsWith(".css")) contentType = "text/css";
                    else if (filePath.endsWith(".js")) contentType = "application/javascript";
                    else if (filePath.endsWith(".html")) contentType = "text/html";

                    String headers =
                            "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + contentType + "\r\n\r\n";

                    outStream.write(headers.getBytes());
                    fileStream.transferTo(outStream);
                    outStream.flush();

                } else {

                    String notFound =
                            "HTTP/1.1 404 Not Found\r\n\r\n" +
                            "File not found";

                    out.println(notFound);
                }
            }

            in.close();
            out.close();
            clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, URISyntaxException {

        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(8080);
            System.out.println("Servidor iniciado en puerto 8080");
        } catch (IOException e) {
            System.err.println("Could not listen on port: 8080.");
            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Apagando servidor...");
            running = false;
        }));

        while (running) {
            System.out.println("Listo para recibir ...");

            Socket clientSocket = serverSocket.accept();

            new Thread(() -> handleRequest(clientSocket)).start();
        }

    }

    public static void get(String path, WebMethod wm) {
        WebFramework.get(path, wm);
    }

    public static void staticfiles(String path) {
        staticFilesPath = path;
    }
}