package main.java.model;

public class VerticalShipCondition implements ShipPlacementCondition{
    @Override
    public boolean check(int currentNumOfFieldsTaken, int currentSelectedField, int previousSelectedField, int secondPreviousSelectedField) {
        return currentNumOfFieldsTaken==0
                || (currentSelectedField<previousSelectedField && ((previousSelectedField-10)==currentSelectedField || (previousSelectedField+10)==currentSelectedField || (secondPreviousSelectedField<previousSelectedField && (previousSelectedField-(currentNumOfFieldsTaken*(-10)))==currentSelectedField)))
                || (currentSelectedField>previousSelectedField && ((previousSelectedField-10)==currentSelectedField || (previousSelectedField+10)==currentSelectedField || (secondPreviousSelectedField>previousSelectedField && (previousSelectedField+(currentNumOfFieldsTaken*(-10)))==currentSelectedField)));
    }

    @Override
    public int setCurrentNumOfFields(int currentNumOfFieldsTaken) {
        return --currentNumOfFieldsTaken;
    }

    @Override
    public boolean setSecondPreviousField(int currentNumOfFieldsTaken) {
        return currentNumOfFieldsTaken<-1;
    }
}
