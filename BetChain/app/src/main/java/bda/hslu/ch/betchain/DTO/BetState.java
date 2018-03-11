package bda.hslu.ch.betchain.DTO;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bruno Fischlin on 11/03/2018.
 */

public enum BetState {
    PENDING(0), LOCKED(1), EVALUATION(2), CONCLUDED(3), ABORTED(4);

    private final int betSate;

    private static Map<Integer, BetState> map = new HashMap<Integer, BetState>();

    static {
        for (BetState betStateEnum : BetState.values()) {
            map.put(betStateEnum.betSate, betStateEnum);
        }
    }


    BetState(final int betState) {
        this.betSate = betState;
    }

    public static BetState valueOfInt(int betState) {
        return map.get(betState);
    }

    public int getBetState() {
        return this.betSate;
    }

    @Override
    public String toString(){
        switch(this.getBetState()){
            case 0: return "Pending";
            case 1: return "Locked";
            case 2: return "Evaluation";
            case 3: return "Concluded";
            case 4: return "Aborted";
            default: return "unknown";
        }
    }
}
