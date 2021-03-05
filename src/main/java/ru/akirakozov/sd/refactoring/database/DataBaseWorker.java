package ru.akirakozov.sd.refactoring.database;

import ru.akirakozov.sd.refactoring.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseWorker {
    private final String database;

    public DataBaseWorker(String name) {
        database = "jdbc:sqlite:" + name;
    }

    public void createDataBase() throws SQLException {
        try (Connection c = DriverManager.getConnection(database)) {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(DataBaseQuery.createTable());
            stmt.close();
        }
    }

    public void clearDataBase() throws SQLException {
        try (Connection c = DriverManager.getConnection(database)) {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(DataBaseQuery.clearTable());
            stmt.close();
        }
    }

    public void insertProduct(String name, Long price) throws SQLException {
        try (Connection c = DriverManager.getConnection(database)) {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(DataBaseQuery.insertIntoProduct(name, price));
            stmt.close();
        }
    }

    public List<Product> selectProducts(String query) throws SQLException {
        try (Connection c = DriverManager.getConnection(database)) {
            Statement stmt = c.createStatement();
            ResultSet rs;
            switch (query) {
                case ("get"):
                    rs = stmt.executeQuery(DataBaseQuery.selectAllFromProduct());
                    break;
                case ("min"):
                    rs = stmt.executeQuery(DataBaseQuery.selectFromProductWithMinPrice());
                    break;
                case ("max"):
                    rs = stmt.executeQuery(DataBaseQuery.selectFromProductWithMaxPrice());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + query);
            }

            List<Product> result = new ArrayList<>();
            while (rs.next()) {
                String name = rs.getString("name");
                long price = rs.getLong("price");
                result.add(new Product(name, price));
            }

            rs.close();
            stmt.close();

            return result;
        }
    }

    public Long aggregateProducts(String query) throws SQLException {
        try (Connection c = DriverManager.getConnection(database)) {
            Statement stmt = c.createStatement();
            ResultSet rs;
            switch (query) {
                case ("sum"):
                    rs = stmt.executeQuery(DataBaseQuery.sumPrice());
                    break;
                case ("count"):
                    rs = stmt.executeQuery(DataBaseQuery.countProduct());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + query);
            }

            Long result = null;
            if (rs.next()) {
                result = rs.getLong(1);
            }
            rs.close();
            stmt.close();
            return result;
        }
    }

}
