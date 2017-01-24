package skeleton;

import battlecode.common.*;

public class Utils {
    public static void alwaysRun(RobotController rc) throws GameActionException {
        for (TreeInfo tree : rc.senseNearbyTrees()) {
            if (tree.containedBullets > 0 && rc.canShake(tree.ID)) {
                rc.shake(tree.ID);
                break;
            }
        }

        if (rc.getTeamBullets() / 10 >= 1000 - rc.getTeamVictoryPoints()) {
            rc.donate((1000 - rc.getTeamVictoryPoints())*10);
        }
    }

    public static class RobotAnalysis {
        public int archons = 0;
        public int gardeners = 0;
        public int lumberjacks = 0;
        public int scouts = 0;
        public int soldiers = 0;
        public int tanks = 0;

        public RobotAnalysis(RobotInfo[] robots) {
            for (RobotInfo robot : robots) {
                switch (robot.getType()) {
                    case ARCHON:
                        archons++;
                        break;
                    case GARDENER:
                        gardeners++;
                        break;
                    case LUMBERJACK:
                        lumberjacks++;
                        break;
                    case SCOUT:
                        scouts++;
                        break;
                    case SOLDIER:
                        soldiers++;
                        break;
                    case TANK:
                        tanks++;
                        break;
                }
            }
        }
    }
}
