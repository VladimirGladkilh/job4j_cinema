package model;

import java.util.Objects;

public class Ticket {
    private int id;
    private Accounts account;
    private Places place;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Accounts getAccount() {
        return account;
    }

    public void setAccount(Accounts account) {
        this.account = account;
    }

    public Places getPlace() {
        return place;
    }

    public void setPlace(Places place) {
        this.place = place;
    }

    public Ticket(int id, Accounts account, Places place) {
        this.id = id;
        this.account = account;
        this.place = place;
    }

    public Ticket(Accounts account, Places place) {
        this.account = account;
        this.place = place;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ticket ticket = (Ticket) obj;
        return id == ticket.id;
    }
}
