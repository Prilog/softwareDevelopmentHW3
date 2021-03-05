package ru.akirakozov.sd.refactoring.servlet;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.Product;
import ru.akirakozov.sd.refactoring.database.DataBaseWorker;

import java.sql.SQLException;
import java.util.List;


public class TestDBWorker {
    private final String database = "test.db";
    private final DataBaseWorker dataBaseWorker = new DataBaseWorker(database);

    @BeforeEach
    public void init() throws SQLException {
        dataBaseWorker.createDataBase();
    }

    @AfterEach
    public void clear() throws SQLException {
        dataBaseWorker.clearDataBase();
    }

    @Test
    @DisplayName("Simple check of select")
    void selectSimple() throws SQLException {
        dataBaseWorker.insertProduct("a", 1L);
        dataBaseWorker.insertProduct("b", 2L);
        List<Product> products = dataBaseWorker.selectProducts("get");
        Product[] expected = { new Product("a", 1L), new Product("b", 2L) };
        assertArrayEquals(expected, products.toArray());
    }

    @Test
    @DisplayName("Simple check for max")
    void maxSimple() throws SQLException {
        dataBaseWorker.insertProduct("a", 1L);
        dataBaseWorker.insertProduct("b", 2L);
        List<Product> products = dataBaseWorker.selectProducts("max");
        Product[] expected = { new Product("b", 2L) };
        assertArrayEquals(expected, products.toArray());
    }

    @Test
    @DisplayName("Simple check for min")
    void minSimple() throws SQLException {
        dataBaseWorker.insertProduct("a", 1L);
        dataBaseWorker.insertProduct("b", 2L);
        List<Product> products = dataBaseWorker.selectProducts("min");
        Product[] expected = { new Product("a", 1L) };
        assertArrayEquals(expected, products.toArray());
    }

    @Test
    @DisplayName("Simple check for count")
    void countSimple() throws SQLException {
        dataBaseWorker.insertProduct("a", 1L);
        dataBaseWorker.insertProduct("b", 2L);
        Long res = dataBaseWorker.aggregateProducts("count");
        Long expected = 2L;
        assertEquals(expected, res);
    }

    @Test
    @DisplayName("Simple check for sum")
    void sumSimple() throws SQLException {
        dataBaseWorker.insertProduct("a", 1L);
        dataBaseWorker.insertProduct("b", 2L);
        Long res = dataBaseWorker.aggregateProducts("sum");
        Long expected = 3L;
        assertEquals(expected, res);
    }

    @Test
    @DisplayName("Select all above empty DB")
    void selectEmpty() throws SQLException {
        List<Product> products = dataBaseWorker.selectProducts("get");
        Product[] expected = {};
        assertArrayEquals(expected, products.toArray());
    }

    @Test
    @DisplayName("Select max above empty DB")
    void maxEmpty() throws SQLException {
        List<Product> products = dataBaseWorker.selectProducts("max");
        Product[] expected = {};
        assertArrayEquals(expected, products.toArray());
    }

    @Test
    @DisplayName("Select min above empty DB")
    void minEmpty() throws SQLException {
        List<Product> products = dataBaseWorker.selectProducts("min");
        Product[] expected = {};
        assertArrayEquals(expected, products.toArray());
    }

    @Test
    @DisplayName("Count above empty DB")
    void countEmpty() throws SQLException {
        Long res = dataBaseWorker.aggregateProducts("count");
        Long expected = 0L;
        assertEquals(expected, res);
    }

    @Test
    @DisplayName("Sum above empty DB")
    void sumEmpty() throws SQLException {
        Long res = dataBaseWorker.aggregateProducts("sum");
        Long expected = 0L;
        assertEquals(expected, res);
    }
}
