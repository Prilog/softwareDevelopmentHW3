package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

import ru.akirakozov.sd.refactoring.database.DataBaseQuery;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(DataBaseQuery.selectAllFromProduct());
                response.getWriter().println("<html><body>");

                writeResponse(response, stmt, rs);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    static void writeResponse(HttpServletResponse response, Statement stmt, ResultSet rs) throws SQLException, IOException {
        while (rs.next()) {
            String  name = rs.getString("name");
            int price  = rs.getInt("price");
            response.getWriter().println(name + "\t" + price + "</br>");
        }
        response.getWriter().println("</body></html>");

        rs.close();
        stmt.close();
    }
}
