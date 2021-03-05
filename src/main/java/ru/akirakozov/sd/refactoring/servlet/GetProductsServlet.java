package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.List;

import ru.akirakozov.sd.refactoring.Product;
import ru.akirakozov.sd.refactoring.database.DataBaseWorker;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {
    private final DataBaseWorker worker;

    public GetProductsServlet(DataBaseWorker dataBaseWorker) {
        worker = dataBaseWorker;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Product> products = worker.selectProducts("get");
            response.getWriter().println("<html><body>");
            writeResponse(response, products);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    static void writeResponse(HttpServletResponse response, List<Product> products) throws IOException {
        for (Product item : products) {
            response.getWriter().println(item.name + "\t" + item.price + "</br>");
        }
        response.getWriter().println("</body></html>");
    }
}
