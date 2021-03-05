package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import ru.akirakozov.sd.refactoring.database.DataBaseQuery;
import ru.akirakozov.sd.refactoring.database.DataBaseWorker;

/**
 * @author akirakozov
 */
public class AddProductServlet extends HttpServlet {
    private final DataBaseWorker worker;

    public AddProductServlet(DataBaseWorker dataBaseWorker) {
        worker = dataBaseWorker;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        try {
            worker.insertProduct(name, price);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("OK");
    }
}
