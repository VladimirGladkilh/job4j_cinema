package servlet;

import model.Accounts;
import model.Places;
import store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class PaymentServlet  extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String placesIds = req.getParameter("places");
        Accounts accounts = new Accounts(name, phone);
        PsqlStore.instOf().save(accounts);
        Arrays.stream(placesIds.split(";")).forEach(s -> {
            Places places = PsqlStore.instOf().findPlacesById(Integer.parseInt(s));
            places.setBussy(true);
            PsqlStore.instOf().save(places);
        });
        resp.sendRedirect(req.getContextPath() + "/index.do");
    }
}
