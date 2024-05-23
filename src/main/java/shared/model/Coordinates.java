package main.java.shared.model;

import java.io.Serializable;
import java.util.Objects;

public class Coordinates implements Serializable {
    private Integer row;
    private Integer col;

    public Coordinates(Integer row, Integer col) {
        this.row = row;
        this.col = col;
    }

    public Coordinates(Coordinates coordinates) {
        if (coordinates == null) return;

        this.row = coordinates.getRow();
        this.col = coordinates.getCol();
    }

    public Integer getRow() {
        return row;
    }

    public Integer getCol() {
        return col;
    }

    public boolean equals(int row,int col) {
        return this.row==row && this.col==col;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}
