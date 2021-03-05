package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import ru.akirakozov.sd.refactoring.Product;
import ru.akirakozov.sd.refactoring.database.DataBaseWorker;
import ru.akirakozov.sd.refactoring.html.HTMLWriter;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {
    private final DataBaseWorker worker;

    public GetProductsServlet(DataBaseWorker dataBaseWorker) {
        worker = dataBaseWorker;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Product> products;
        try {
            products = worker.selectProducts("get");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        new HTMLWriter(response).writeResponse(products.stream().
                map(item -> item.name + "\t" + item.price + "</br>").collect(Collectors.toList()));
    }
}
