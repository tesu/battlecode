package gardenerTest;

import battlecode.common.*;

public class RobotPlayer {
    public static void run(RobotController rc) {
        try {
            if (rc.getTeamBullets() / 10 >= 1000 - rc.getTeamVictoryPoints()) {
                rc.donate((1000 - rc.getTeamVictoryPoints())*10);
            }
        } catch (GameActionException e) {
            System.out.println(e.getMessage());
        }

        switch(rc.getType()) {
            case ARCHON:
                Archon.run(rc);
                break;
            case GARDENER:
                Gardener.run(rc);
                break;
            case LUMBERJACK:
                Lumberjack.run(rc);
                break;
            case SCOUT:
                Scout.run(rc);
                break;
            case SOLDIER:
                Soldier.run(rc);
                break;
            case TANK:
                Tank.run(rc);
                break;
            default:
                System.out.println("bush did 9/11");
        }
    }
}
