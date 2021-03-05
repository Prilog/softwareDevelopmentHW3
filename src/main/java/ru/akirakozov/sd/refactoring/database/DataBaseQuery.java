package ru.akirakozov.sd.refactoring.database;

public class DataBaseQuery {
    public static String createTable() {
        return "CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)";
    }

    public static String clearTable() {
        return "DELETE FROM PRODUCT";
    }

    public static String insertIntoProduct(String name, Long price) {
        return "INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
    }

    public static String selectAllFromProduct() {
        return "SELECT * FROM PRODUCT";
    }

    public static String selectFromProductWithMaxPrice() {
        return "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
    }

    public static String selectFromProductWithMinPrice() {
        return "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
    }

    public static String sumPrice() {
        return "SELECT SUM(price) FROM PRODUCT";
    }

    public static String countProduct() {
        return "SELECT COUNT(*) FROM PRODUCT";
    }
}
