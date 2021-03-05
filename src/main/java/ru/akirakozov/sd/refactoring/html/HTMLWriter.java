package ru.akirakozov.sd.refactoring.html;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class HTMLWriter {

    private final HttpServletResponse response;

    public HTMLWriter(HttpServletResponse r) {
        response = r;
    }

    public void OK() throws IOException {
        response.setContentType("text/html");
        response.getWriter().println("OK");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void writeResponce(List<String> body) throws IOException {
        response.getWriter().println("<html><body>");
        for (String row : body) {
            response.getWriter().println(row);
        }
        response.getWriter().println("</body></html>");
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
