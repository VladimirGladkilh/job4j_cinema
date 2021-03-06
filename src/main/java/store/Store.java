package store;

import model.Accounts;
import model.Halls;
import model.Places;
import model.Ticket;

import java.util.Collection;

public interface Store {
    Collection<Halls> findAllHalls();

    void save(Halls halls);

    Halls findById(int id);

    void delete(Halls post);

    Collection<Accounts> findAllAccounts();

    void save(Accounts accounts);

    Accounts findAccountsById(int id);

    Accounts findAccountsByPhone(String phone);

    void delete(Accounts accounts);

    Collection<Places> findAllPlaces();

    void save(Places places);

    Places findPlacesById(int id);

    void delete(Places places);

    Collection<Places> findPlacesByHalls(int hallsId);

    Collection<Ticket> findAllTickets();

    void save(Ticket ticket);


    void delete(Ticket ticket);

}
