package main.java.model;

public class HorizontalShipCondition implements ShipPlacementCondition{
    @Override
    public boolean check(int currentNumOfFieldsTaken, int currentSelectedField, int previousSelectedField, int secondPreviousSelectedField) {
        return currentNumOfFieldsTaken == 0
                || (currentSelectedField < previousSelectedField && ((previousSelectedField + 1) == currentSelectedField || (previousSelectedField - 1) == currentSelectedField || (secondPreviousSelectedField < previousSelectedField && (previousSelectedField - currentNumOfFieldsTaken) == currentSelectedField)))
                || (currentSelectedField > previousSelectedField && ((previousSelectedField + 1) == currentSelectedField || (previousSelectedField - 1) == currentSelectedField || (secondPreviousSelectedField > previousSelectedField && (previousSelectedField + currentNumOfFieldsTaken) == currentSelectedField)));
    }

    @Override
    public int setCurrentNumOfFields(int currentNumOfFieldsTaken) {
        return ++currentNumOfFieldsTaken;
    }

    @Override
    public boolean setSecondPreviousField(int currentNumOfFieldsTaken) {
        return currentNumOfFieldsTaken>1;
    }
}
