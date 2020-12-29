package store;

import model.*;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {
    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            System.out.println("30   " + e);
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            System.out.println("37   " + e);
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }


    @Override
    public Collection<Halls> findAllHalls() {
        List<Halls> hallsList = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM halls")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    hallsList.add(new Halls(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            System.out.println("pizdec");
            e.printStackTrace();

        }
        return hallsList;
    }

    @Override
    public void save(Halls halls) {
        if (halls.getId() == 0) {
            create(halls);
        } else {
            update(halls);
        }
    }

    private void update(Halls halls) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("UPDATE halls set name = ? where id = ?", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, halls.getName());
            ps.setInt(2, halls.getId());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void create(Halls halls) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO halls(name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, halls.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    halls.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Halls findById(int id) {
        Halls halls = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM halls where id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    halls = new Halls(it.getInt("id"), it.getString("name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return halls;
    }

    @Override
    public void delete(Halls halls) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("DELETE FROM halls where id = ?")
        ) {
            ps.setInt(1, halls.getId());
            ps.executeQuery();
        } catch (SQLException s) {
            s.printStackTrace();
        }
    }

    @Override
    public Collection<Accounts> findAllAccounts() {
        List<Accounts> accounts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM accounts")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    accounts.add(new Accounts(it.getInt("id"), it.getString("name"), it.getString("phone")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public void save(Accounts accounts) {
        if (accounts.getId() == 0) {
            create(accounts);
        } else {
            update(accounts);
        }
    }

    private void update(Accounts accounts) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("UPDATE accounts set name = ?, phone = ? where id = ?", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, accounts.getName());
            ps.setString(2, accounts.getPhone());
            ps.setInt(3, accounts.getId());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void create(Accounts accounts) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO accounts(name, phone) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, accounts.getName());
            ps.setString(2, accounts.getPhone());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    accounts.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Accounts findAccountsById(int id) {
        Accounts accounts = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM accounts where id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    accounts = new Accounts(it.getInt("id"), it.getString("name"), it.getString("phone"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public void delete(Accounts accounts) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("DELETE FROM accounts where id = ?")
        ) {
            ps.setInt(1, accounts.getId());
            ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<Places> findAllPlaces() {
        List<Places> places = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM places ")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    places.add(new Places(it.getInt("id"),
                            it.getString("name"), it.getInt("hallId"), it.getInt("x"),
                            it.getInt("y"), it.getBoolean("isBussy")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return places;
    }

    @Override
    public void save(Places places) {
        if (places.getId() == 0) {
            create(places);
        } else {
            update(places);
        }
    }

    private void create(Places places) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO places(hallId, name, x, y, bussy ) VALUES (?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, places.getHalls().getId());
            ps.setString(2, places.getName());
            ps.setInt(3, places.getX());
            ps.setInt(4, places.getY());
            ps.setBoolean(5, places.isBussy());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    places.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void update(Places places) {
        try (Connection cn = pool.getConnection();

             PreparedStatement ps =  cn.prepareStatement("UPDATE places set hallId = ?, name = ?, x = ?, y = ?, bussy = ? where id = ?", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, places.getHalls().getId());
            ps.setString(2, places.getName());
            ps.setInt(3, places.getX());
            ps.setInt(4, places.getY());
            ps.setBoolean(5, places.isBussy());
            ps.setInt(6, places.getId());
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Places findPlacesById(int id) {
        return null;
    }

    @Override
    public void delete(Places places) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("DELETE FROM places where id = ?")
        ) {
            ps.setInt(1, places.getId());
            ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<Places> findPlacesByHalls(int hallsId) {
        List<Places> places = new ArrayList<>();
        Halls halls = findById(hallsId);
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM places where hallId = ? order by y, x")
        ) {
            ps.setInt(1, hallsId);
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    places.add(new Places(it.getInt("id"),
                            it.getString("name"), halls, it.getInt("x"),
                            it.getInt("y"), it.getBoolean("bussy")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return places;
    }
}
