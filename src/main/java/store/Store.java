package store;

import model.Accounts;
import model.Halls;
import model.Places;

import java.util.Collection;

public interface Store {
    Collection<Halls> findAllHalls();

    void save(Halls halls);

    Halls findById(int id);

    void delete(Halls post);

    Collection<Accounts> findAllAccounts();

    void save(Accounts accounts);

    Accounts findAccountsById(int id);

    void delete(Accounts accounts);

    Collection<Places> findAllPlaces();

    void save(Places places);

    Places findPlacesById(int id);

    void delete(Places places);

    Collection<Places> findPlacesByHalls(int hallsId);
}
