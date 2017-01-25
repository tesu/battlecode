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

    public static boolean dodge(RobotController rc) {
        return false;
    }

    public static MapLocation location(RobotController rc, int i) {
        return rc.getLocation().add(new Direction((float)i/10*2*(float)Math.PI), rc.getType().strideRadius);
    }

    public static boolean moveTowards(RobotController rc, Direction d) throws GameActionException {
        int[] directions = {0,0,0,0,0,0,0,0,0,0};

        for (BulletInfo b : rc.senseNearbyBullets()) {

        }

        if (rc.canMove(d)) {
            rc.move(d);
            return true;
        }
        for (int i = 1; i < 10; i++) {
            if (rc.canMove(d.rotateRightDegrees(i*10))) {
                rc.move(d.rotateRightDegrees(i*10));
                return true;
            }
            if (rc.canMove(d.rotateLeftDegrees(i*10))) {
                rc.move(d.rotateLeftDegrees(i*10));
                return true;
            }
        }
        return false;
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
