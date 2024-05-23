package main.java.model;

public interface ShipPlacementCondition {
    boolean check(int currentNumOfFieldsTaken,int currentSelectedField, int previousSelectedField,int secondPreviousSelectedField);
    int setCurrentNumOfFields(int currentNumOfFieldsTaken);
    boolean setSecondPreviousField(int currentNumOfFieldsTaken);
}
