package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Places;
import store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class PlacesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String placesTable = generateTable(Integer.parseInt(req.getParameter("hallId")));
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain");
        resp.getWriter().write(placesTable);
    }

    private String generateTable(int hallId) {
        List<Places> list = (List<Places>) PsqlStore.instOf().findPlacesByHalls(hallId);
        StringJoiner sj = new StringJoiner(System.lineSeparator());
        if (list.size() > 0) {
            int maxX = list.stream().max(Comparator.comparingInt(Places::getX)).get().getX();
            int maxY = list.stream().max(Comparator.comparingInt(Places::getY)).get().getY();
            sj.add("<table class=\"table\">");
            for (int y = 0; y <= maxY; y++) {
                sj.add("<tr>");
                for (int x = 0; x <= maxX; x++) {
                    if (y == 0) {
                        if (x == 0) {
                            sj.add("<th>Ряд\\Место</th>");
                        } else {
                            sj.add("<th>" + x + "</th>");
                        }
                    } else {
                        if (x == 0) {
                            sj.add("<th>" + y + "</th>");
                        } else {
                            int finalX = x;
                            int finalY = y;
                            Optional<Places> places = list.stream().filter(places1 -> places1.getX() == finalX && places1.getY() == finalY).findAny();
                            if (places.isPresent()) {
                                sj.add("<td>" + generateCheckBox(places.get()) + "</td>");
                            } else {
                                sj.add("<td>-</td>");
                            }
                        }
                    }
                }
                sj.add("</tr>");
            }
            sj.add("</table>");
        } else {
            sj.add("<h3>В зале нет стульев</h3>");
        }

        return  sj.toString();
    }

    private String generateCheckBox(Places places) {
        String checkBox = String.format("<input type=\"checkbox\" id=\"{}\" {}>",
                places.getId(), places.isBussy() ? "checked disabled" : "");
        return  checkBox;
    }
}
