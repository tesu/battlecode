package skeleton;

import battlecode.common.*;

import java.awt.*;

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

    public static boolean moveTowards(RobotController rc, Direction d) throws GameActionException {
        int[] directions = {0,0,0,0,0,0,0,0,0,0};

        if (rc.hasMoved()) return false;

        for (BulletInfo b : rc.senseNearbyBullets(5)) {
            for (int i = 0; i < directions.length; i++) {
                if (willCollide(rc, b, rc.getLocation().add(new Direction((float)i/directions.length*2*(float)Math.PI)))) {
                    directions[i] -= 10;
                }
            }
        }

        if (rc.canMove(d) && directions[(int)d.getAngleDegrees()/directions.length] >= 0) {
            rc.move(d);
            return true;
        }
        for (int i = 1; i < 10; i++) {
            Direction t = d.rotateRightDegrees(i*10);
            if (rc.canMove(t) && directions[(int)t.getAngleDegrees()/directions.length] >= 0) {
                rc.move(t);
                return true;
            }
            t = d.rotateLeftDegrees(i*10);
            if (rc.canMove(t) && directions[(int)t.getAngleDegrees()/directions.length] >= 0) {
                rc.move(t);
                return true;
            }
        }
        return false;
    }

    public static boolean willCollide(RobotController rc, BulletInfo b, MapLocation m) {
        if (m.distanceTo(b.location) < rc.getType().bodyRadius) return true;

        Direction directionToRobot = b.location.directionTo(m);
        float theta = b.dir.radiansBetween(directionToRobot);

        if (Math.abs(theta) > Math.PI/2) return false;

        return ((float)Math.abs(b.location.distanceTo(m) * Math.sin(theta)) <= rc.getType().bodyRadius);
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
