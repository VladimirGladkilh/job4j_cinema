package store;

import model.Halls;
import model.Places;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class PsqlStoreMain {
        public static void main(String[] args) {
            List<Places> list = (List<Places>) PsqlStore.instOf().findPlacesByHalls(Integer.parseInt("1"));
            StringJoiner sj = new StringJoiner(System.lineSeparator());

            int maxX = list.stream().max(Comparator.comparingInt(Places::getX)).get().getX();
            int maxY = list.stream().max(Comparator.comparingInt(Places::getY)).get().getY();
            sj.add("<table class=\"table\">");
            for (int y = 0; y <= maxY; y++) {
                sj.add("<tr>");
                for (int x = 0; x <= maxX; x ++) {
                    if (y == 0) {
                        if (x == 0) {
                            sj.add("<th>Ряд\\Место</th>");
                        } else {
                            sj.add("<th>"+ x + "</th>");
                        }
                    } else {
                        if (x == 0) {
                            sj.add("<th>"+ y + "</th>");
                        } else {
                            int finalX = x;
                            int finalY = y;
                            Optional<Places> places = list.stream().filter(places1 -> places1.getX()== finalX && places1.getY()== finalY).findAny();
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
            System.out.println(sj.toString());
        }

    private static String generateCheckBox(Places places) {
            String checkBox = String.format("<input type=\"checkbox\" id=\"{}\" checked=\"{}\" {}>",
                    places.getId(), places.isBussy() ? "true" : "false",
                    places.getId(), places.isBussy() ? "disabled" : "");
            return  checkBox;
    }

}
