package edu.eci.dockerLab.framework;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class HttpServerTest {

    @BeforeAll
    public static void startServer() {
        new Thread(() -> {
            try {
                RestServiceApplication.main(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String sendRequest(String endpoint) throws Exception {

        URL url = new URL("http://localhost:8080" + endpoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuilder content = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();

        return content.toString();
    }

    @Test
    public void testHelloEndpoint() throws Exception {

        String response = sendRequest("/hello");

        assertTrue(response.contains("Hello"));
    }

    @Test
    public void testGreetingEndpointWithName() throws Exception {

        String response = sendRequest("/greeting?name=Camilo");

        assertTrue(response.contains("Camilo"));
    }

    @Test
    public void testGreetingEndpointWithoutName() throws Exception {

        String response = sendRequest("/greeting");

        assertNotNull(response);
    }

    @Test
    public void testPiEndpoint() throws Exception {

        String response = sendRequest("/pi");

        assertTrue(response.contains("3.14"));
    }

    @Test
    public void testRootEndpoint() throws Exception {

        String response = sendRequest("/");

        assertNotNull(response);
    }

    @Test
    public void testInvalidEndpoint() throws Exception {

        try {

            URL url = new URL("http://localhost:8080/noExiste");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();

            assertEquals(404, responseCode);

        } catch (Exception e) {

            assertTrue(true);
        }
    }

    @Test
    public void testMultipleRequests() throws Exception {

        for (int i = 0; i < 10; i++) {

            String response = sendRequest("/hello");

            assertTrue(response.contains("Hello"));
        }
    }

    @Test
    public void testConcurrentRequests() throws Exception {

        Runnable task = () -> {
            try {
                String response = sendRequest("/hello");
                assertTrue(response.contains("Hello"));
            } catch (Exception e) {
                fail();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        Thread t3 = new Thread(task);

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();
    }
}