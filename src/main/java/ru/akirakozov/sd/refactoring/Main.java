package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.database.DataBaseWorker;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;
import ru.akirakozov.sd.refactoring.database.DataBaseQuery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new Exception("Database name needed");
        }
        String databaseName = args[0];
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + databaseName)) {
            String sql = DataBaseQuery.createTable();
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }

        Server server = new Server(8081);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        DataBaseWorker worker = new DataBaseWorker(databaseName);

        context.addServlet(new ServletHolder(new AddProductServlet(worker)), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet(worker)),"/get-products");
        context.addServlet(new ServletHolder(new QueryServlet(worker)),"/query");

        server.start();
        server.join();
    }
}
