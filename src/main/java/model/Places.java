package model;

import store.PsqlStore;

import java.util.Objects;

public class Places {
    private int id;
    private String name;
    private Halls halls;
    private int x;
    private int y;
    private boolean bussy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Halls getHalls() {
        return halls;
    }

    public void setHalls(Halls halls) {
        this.halls = halls;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isBussy() {
        return bussy;
    }

    public void setBussy(boolean bussy) {
        this.bussy = bussy;
    }

    public Places(int id, String name, Halls halls, int x, int y, boolean bussy) {
        this.id = id;
        this.name = name;
        this.halls = halls;
        this.x = x;
        this.y = y;
        this.bussy = bussy;
    }

    public Places(int id, String name, int hallId, int x, int y, boolean bussy) {
        this.id = id;
        this.name = name;
        Halls halls = new Halls(0, "");
        if (hallId > 0) {
            halls = PsqlStore.instOf().findById(hallId);
        }
        this.halls = halls;
        this.x = x;
        this.y = y;
        this.bussy = bussy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Places places = (Places) o;
        return id == places.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
