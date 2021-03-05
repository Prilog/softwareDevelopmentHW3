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
    public void establish() throws SQLException {
        if (!setUpDone) {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + testDatabaseName)) {
                String clear = DataBaseQuery.clearTable();
                Statement stmt = c.createStatement();

                stmt.executeUpdate(clear);
                stmt.close();
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

    private void testReq(String req, String res) {
        try {
            URL query = new URL(req);
            URLConnection connection = query.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;
            StringBuilder result = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                result.append(inputLine);
            in.close();
            assertEquals(res, result.toString());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    @Order(1)
    @DisplayName("Test add query")
    public void addQuery() {
        testReq("http://localhost:8081/add-product?name=iphone6&price=300", "OK");
    }

    @Test
    @Order(2)
    @DisplayName("Test get query")
    public void getQuery() {
        testReq("http://localhost:8081/get-products", "<html><body>iphone6\t300</br></body></html>");
    }

    @Test
    @Order(3)
    @DisplayName("Test sum query")
    public void sumQuery() {
        testReq("http://localhost:8081/query?command=sum", "<html><body>Summary price: 300</body></html>");
    }

    @Test
    @Order(4)
    @DisplayName("Test max query")
    public void maxQuery() {
        testReq("http://localhost:8081/query?command=max",
                "<html><body><h1>Product with max price: </h1>iphone6\t300</br></body></html>");
    }

    @Test
    @Order(5)
    @DisplayName("Test min query")
    public void minQuery() {
        testReq("http://localhost:8081/query?command=min",
                "<html><body><h1>Product with min price: </h1>iphone6\t300</br></body></html>");
    }

    @Test
    @Order(6)
    @DisplayName("Test count query")
    public void countQuery() {
        testReq("http://localhost:8081/query?command=count",
                "<html><body>Number of products: 1</body></html>");
    }

    @Test
    @Order(7)
    @DisplayName("Multi add query")
    public void multiAdd() {
        testReq("http://localhost:8081/add-product?name=iphone7&price=600", "OK");
        testReq("http://localhost:8081/add-product?name=iphone8&price=900", "OK");
        testReq("http://localhost:8081/add-product?name=iphone9&price=900", "OK");
        testReq("http://localhost:8081/add-product?name=iphone10&price=900", "OK");
    }

    @Test
    @Order(8)
    @DisplayName("Big get query")
    public void bigGet() {
        testReq("http://localhost:8081/get-products",
                "<html><body>iphone6\t300</br>iphone7\t600</br>iphone8\t900</br>iphone9\t900</br>" +
                        "iphone10\t900</br></body></html>");
    }

    @Test
    @Order(9)
    @DisplayName("Big max query")
    public void bigMax() {
        testReq("http://localhost:8081/query?command=max",
                "<html><body><h1>Product with max price: </h1>iphone8\t900</br></body></html>");
    }

    @Test
    @Order(10)
    @DisplayName("Big count query")
    public void bigCount() {
        testReq("http://localhost:8081/query?command=count",
                "<html><body>Number of products: 5</body></html>");
    }
}
