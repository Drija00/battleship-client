package main.java.model;

import main.java.shared.model.Coordinates;

import java.util.List;

public class Ship {
    private Integer id;
    private List<Coordinates> positions;
    private int size;
    private int hits;
    private boolean sunk;

    public Ship(Integer id, List<Coordinates> positions, int size) {
        this.id = id;
        this.positions = positions;
        this.size = size;
        this.hits = 0;
        this.sunk=false;
    }

    public void setSunk(boolean sunk) {
        this.sunk = sunk;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public List<Coordinates> getPositions() {
        return positions;
    }

    public void setPositions(List<Coordinates> positions) {
        this.positions = positions;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean incrementHits(){
        ++hits;
        if(hits==size) sunk=true;
        System.out.println(toString());
        return sunk;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "id=" + id +
                ", positions=" + positions +
                ", size=" + size +
                ", hits=" + hits +
                ", sunk=" + sunk +
                '}';
    }
}

