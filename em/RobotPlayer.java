package em;
import battlecode.common.*;

public class RobotPlayer {
    static final int A = 0;
    static final int G = 1;
    static final int L = 2;
    static final int SC = 3;
    static final int SO = 4;
    static final int T = 5;

    static Direction d;
    static RobotController rc;

    public static void run(RobotController rc) throws GameActionException {
        RobotPlayer.rc = rc;

        switch (rc.getType()) {
            case ARCHON:
                runArchon();
                break;
            case GARDENER:
                runGardener();
                break;
        }
	}

    static void runArchon() throws GameActionException {
        while (true) {
            int[] rcounts = {0, 0, 0, 0, 0, 0};
            for (RobotInfo r : rc.senseNearbyRobots()) {
                rcounts[classToId(r.getType())]++;
            }
            if (rcounts[G] == 0) {
                d = new Direction((float) (Math.random() * 2 * Math.PI));
                if (rc.canHireGardener(d)) {
                    rc.hireGardener(d);
                }
            }
            d = new Direction((float) (Math.random() * 2 * Math.PI));
            if (rc.canMove(d)) rc.move(d);

            Clock.yield();
        }
    }

    static void runGardener() throws GameActionException {
        while (true) {
            Clock.yield();
        }

    }

    static int classToId(RobotType r) {
        switch(r) {
            case ARCHON: return A;
            case GARDENER: return G;
            case LUMBERJACK: return L;
            case SCOUT: return SC;
            case SOLDIER: return SO;
            case TANK: return T;
        }
        return 420;
    }
}
