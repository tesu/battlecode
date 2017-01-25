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

        if (rc.getTeamBullets() / rc.getVictoryPointCost() >= 1000 - rc.getTeamVictoryPoints()) {
            rc.donate((1000 - rc.getTeamVictoryPoints())*rc.getVictoryPointCost());
        }

        if (rc.getRoundNum() == rc.getRoundLimit()-1) {
            rc.donate(rc.getTeamBullets());
        }
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

        if (rc.canMove(d) && directions[radiansToInt(d.radians, directions.length)] >= 0) {
            rc.move(d);
            return true;
        }
        for (int i = 1; i < 10; i++) {
            Direction t = d.rotateRightDegrees(i*10);
            if (rc.canMove(t) && directions[radiansToInt(t.radians, directions.length)] >= 0) {
                rc.move(t);
                return true;
            }
            t = d.rotateLeftDegrees(i*10);
            if (rc.canMove(t) && directions[radiansToInt(t.radians, directions.length)] >= 0) {
                rc.move(t);
                return true;
            }
        }
        return false;
    }

    public static int radiansToInt(float r, int i) {
        return Math.floorMod((int)(r/(2*Math.PI)*i),i);
    }

    public static boolean willCollide(RobotController rc, BulletInfo b, MapLocation m) {
        if (m.distanceTo(b.location) < rc.getType().bodyRadius) return true;

        Direction directionToRobot = b.location.directionTo(m);
        float theta = b.dir.radiansBetween(directionToRobot);

        if (Math.abs(theta) > Math.PI/2) return false;

        return ((float)Math.abs(b.location.distanceTo(m) * Math.sin(theta)) <= rc.getType().bodyRadius);
    }

    public static boolean attack(RobotController rc) throws GameActionException {
        if (!rc.canFireSingleShot()) return false;

        RobotInfo[] enemies = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
        if (enemies.length > 0) {
            RobotInfo target = enemies[0];
            for (RobotInfo t : enemies) {
                if (t.health < target.health) target = t;
            }
            if (rc.canFireSingleShot()) {
                rc.fireSingleShot(rc.getLocation().directionTo(target.location));
                return true;
            }
        }

        TreeInfo[] trees = rc.senseNearbyTrees(-1, rc.getTeam().opponent());
        if (trees.length > 0) {
            TreeInfo target = trees[0];
            for (TreeInfo t : trees) {
                if (t.health < target.health) target = t;
            }
            if (rc.canFireSingleShot()) {
                rc.fireSingleShot(rc.getLocation().directionTo(target.location));
                return true;
            }
        }

        return false;
    }

    public static class Radio {
        RobotController rc;

        public Radio(RobotController myrc) {
            rc = myrc;
        }

        public int targetCount() throws GameActionException {
            return rc.readBroadcast(0);
        }

        public MapLocation getTarget(int i) throws GameActionException {
            if (targetCount() == 0) return null;
            i = i % targetCount();
            float x = rc.readBroadcastFloat(i*2+1);
            float y = rc.readBroadcastFloat(i*2+2);
            return new MapLocation(x,y);
        }

        public void deleteTarget(MapLocation m) throws GameActionException {
            for (int i = 0; i < targetCount(); i++) {
                if (rc.readBroadcastFloat(i*2+1)==m.x && rc.readBroadcastFloat(i*2+2)==m.y) {
                    del(i);
                    rc.broadcast(0, targetCount()-1);
                    break;
                }
            }
        }

        private void del(int i) throws GameActionException {
            for (int j = i; j < targetCount()-1; j++) {
                rc.broadcastFloat(j*2+1,rc.readBroadcastFloat(j*2+3));
                rc.broadcastFloat(j*2+2,rc.readBroadcastFloat(j*2+4));
            }
        }

        public void addTarget(MapLocation m) throws GameActionException {
            for (int i = 0; i < targetCount(); i++) {
                if (getTarget(i).distanceTo(m) < 0.1) return;
            }
            rc.broadcastFloat(targetCount()*2+1, m.x);
            rc.broadcastFloat(targetCount()*2+2,m.y);
            rc.broadcast(0,targetCount()+1);
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
