package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.*;

import ru.akirakozov.sd.refactoring.Main;
import ru.akirakozov.sd.refactoring.database.DataBaseQuery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestService {
    private static final String testDatabaseName = "test.db";
    private static boolean setUpDone = false;

    Thread serverThread;

    @BeforeEach
    public void establish() {
        if (!setUpDone) {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + testDatabaseName)) {
                String clear = DataBaseQuery.clearTable();
                Statement stmt = c.createStatement();

                stmt.executeUpdate(clear);
                stmt.close();
            } catch (SQLException e) {
                System.out.println(e);
                fail();
            }
            setUpDone = true;
        }
        serverThread = new Thread(() -> {
            try {
                String[] args = new String[1];
                args[0] = testDatabaseName;
                Main.main(args);
            } catch (Exception ignored) {}
        });
        serverThread.start();
    }

    @AfterEach
    public void kill() {
        serverThread.interrupt();
    }

    @Test
    @Order(1)
    @DisplayName("Test add query")
    public void addQuery() {
        try {
            URL query = new URL("http://localhost:8081/add-product?name=iphone6&price=300");
            URLConnection connection = query.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;
            StringBuilder result = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                result.append(inputLine);
            in.close();
            assertEquals("OK", result.toString());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    @Order(2)
    @DisplayName("Test get query")
    public void getQuery() {
        try {
            URL get = new URL("http://localhost:8081/get-products");
            URLConnection getConnection = get.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    getConnection.getInputStream()));
            String inputLine;
            StringBuilder result = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                result.append(inputLine);
            in.close();
            assertEquals("<html><body>iphone6\t300</br></body></html>", result.toString());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    @Order(3)
    @DisplayName("Test sum query")
    public void sumQuery() {
        try {
            URL sum = new URL("http://localhost:8081/query?command=sum");
            URLConnection getConnection = sum.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    getConnection.getInputStream()));
            String inputLine;
            StringBuilder result = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                result.append(inputLine);
            in.close();
            assertEquals("<html><body>Summary price: 300</body></html>", result.toString());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    @Order(4)
    @DisplayName("Test max query")
    public void maxQuery() {
        try {
            URL max = new URL("http://localhost:8081/query?command=max");
            URLConnection getConnection = max.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    getConnection.getInputStream()));
            String inputLine;
            StringBuilder result = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                result.append(inputLine);
            in.close();
            assertEquals("<html><body><h1>Product with max price: </h1>iphone6\t300</br></body></html>",
                    result.toString());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    @Order(5)
    @DisplayName("Test min query")
    public void minQuery() {
        try {
            URL min = new URL("http://localhost:8081/query?command=min");
            URLConnection getConnection = min.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    getConnection.getInputStream()));
            String inputLine;
            StringBuilder result = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                result.append(inputLine);
            in.close();
            assertEquals("<html><body><h1>Product with min price: </h1>iphone6\t300</br></body></html>",
                    result.toString());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    @Order(6)
    @DisplayName("Test count query")
    public void countQuery() {
        try {
            URL count = new URL("http://localhost:8081/query?command=count");
            URLConnection getConnection = count.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    getConnection.getInputStream()));
            String inputLine;
            StringBuilder result = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                result.append(inputLine);
            in.close();
            assertEquals("<html><body>Number of products: 1</body></html>", result.toString());
        } catch (IOException e) {
            fail();
        }
    }
}
