package bda.hslu.ch.betchain.DTO;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bruno Fischlin on 11/03/2018.
 */

public enum BetRole {
    OWNER(0), SUPPORTER(1), OPPOSER(2), NOTAR(3);

    private final int betRole;

    private static Map<Integer, BetRole> map = new HashMap<Integer, BetRole>();

    static {
        for (BetRole betRoleEnum : BetRole.values()) {
            map.put(betRoleEnum.betRole, betRoleEnum);
        }
    }


    BetRole(final int betRole) {
        this.betRole = betRole;
    }

    public static BetRole valueOfInt(int betRole) {
        return map.get(betRole);
    }

    public int getBetRole() {
        return betRole;
    }

    @Override
    public String toString(){
        switch(this.getBetRole()){
            case 0: return "Owner";
            case 1: return "Supporter";
            case 2: return "Opposer";
            case 3: return "Notar";
            default: return "unknown";
        }
    }

}
