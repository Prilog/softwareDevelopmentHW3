package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.Product;
import ru.akirakozov.sd.refactoring.database.DataBaseWorker;
import ru.akirakozov.sd.refactoring.html.HTMLWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    private final DataBaseWorker worker;

    public QueryServlet(DataBaseWorker dataBaseWorker) {
        worker = dataBaseWorker;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        List<String> body = new ArrayList<>();

        if ("max".equals(command)) {
            try {
                List<Product> products = worker.selectProducts("max");
                body.add("<h1>Product with max price: </h1>");
                for (Product item : products) {
                    body.add(item.name + "\t" + item.price + "</br>");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("min".equals(command)) {
            try {
                List<Product> products = worker.selectProducts("min");
                body.add("<h1>Product with min price: </h1>");
                for (Product item : products) {
                    body.add(item.name + "\t" + item.price + "</br>");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("sum".equals(command)) {
            try {
                Long result = worker.agregateProducts("sum");
                body.add("Summary price: " + (result == null ? "" : result));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("count".equals(command)) {
            try {
                Long result = worker.agregateProducts("count");
                body.add("Number of products: " + (result == null ? "" : result));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            response.getWriter().println("Unknown command: " + command);
        }
        new HTMLWriter(response).writeResponce(body);
    }

}
