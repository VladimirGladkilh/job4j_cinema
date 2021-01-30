package servlet;

import model.Accounts;
import model.Places;
import model.Ticket;
import store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringJoiner;

public class PaymentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String placesIds = req.getParameter("places");
        Accounts accounts = PsqlStore.instOf().findAccountsByPhone(phone);
        if (accounts == null) {
            accounts = new Accounts(name, phone);
            PsqlStore.instOf().save(accounts);
        }
        StringJoiner badSave = new StringJoiner(System.lineSeparator());
        Accounts finalAccounts = accounts;
        Arrays.stream(placesIds.split(";")).forEach(s -> {

            Places places = PsqlStore.instOf().findPlacesById(Integer.parseInt(s));

            Ticket ticket = new Ticket(0, finalAccounts, places);
            if (!places.isBussy()) {
                PsqlStore.instOf().save(ticket);
            }
            if (ticket.getId() == 0) {
                badSave.add(places.getName());
            } else {
                places.setBussy(true);
                PsqlStore.instOf().save(places);
            }

        });
        if (badSave.length() > 0) {
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("text/plain");
            resp.getWriter().write("Не удалось сохранить " + badSave.toString());
        } else {
            resp.sendRedirect(req.getContextPath() + "/index.do");
        }
    }
}
