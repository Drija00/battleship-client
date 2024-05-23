package main.java.shared.protocol;

import java.io.Serializable;

public enum MsgType implements Serializable {
    SET_ID,
    ID_IS_SET,
    PLACE_SHIPS,
    SHIPS_PLACED,
    SHIP_SUNK,
    ENEMY_SHIP_SUNK,
    WAIT_FOR_MOVE,
    WAITING,
    MAKE_MOVE,
    SHOT_PERFORMED,
    HIT_MAKE_MOVE,
    HIT_WAIT_FOR_MOVE,
    MISS_MAKE_MOVE,
    MISS_WAIT_FOR_MOVE,
    WIN,
    LOSE,
    LOGIN,
    REGISTER,
    UPDATE,
    INIT_SET_ID,
    START_GAME,
    GET_PLAYERS,
    RESET

}
