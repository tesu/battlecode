package skeleton;

import battlecode.common.*;

public class RobotPlayer {
    public static void run(RobotController rc) {
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
