package gardenerTest;

import battlecode.common.*;
import skeleton.Utils;

import java.util.Random;

public class Soldier {
    static int status = 0;
    static MapLocation target = null;

    public static void run(RobotController rc) {
        Random rand = new Random(rc.getID());

        Direction face = null;
        for (RobotInfo robot : rc.senseNearbyRobots(2, rc.getTeam())) {
            if (robot.getType() == RobotType.GARDENER) {
                face = robot.getLocation().directionTo(rc.getLocation());
            }
        }
        if (face == null) face = new Direction(2 * (float)Math.PI * rand.nextFloat());

        while (true) {
            try {
                Utils.alwaysRun(rc);

                System.out.println(status);

                int foundEnemies = rc.readBroadcast(0);
                RobotInfo[] enemies = rc.senseNearbyRobots(-1, rc.getTeam().opponent());

                switch (status) {
                    case 0:
                        boolean found = false;
                        for (RobotInfo r : enemies) {
                            if (r.getType() == RobotType.ARCHON || r.getType() == RobotType.GARDENER) {
                                found = true;
                                rc.broadcast(foundEnemies*2+1, Math.round(r.getLocation().x));
                                rc.broadcast(foundEnemies*2+2, Math.round(r.getLocation().y));
                                target = r.getLocation();
                                face = rc.getLocation().directionTo(target);
                                foundEnemies++;
                            }
                        }
                        if (found) rc.broadcast(0, foundEnemies);
                        if (foundEnemies == 0) {
                            MapLocation[] m = rc.senseBroadcastingRobotLocations();
                            if (m.length == 0) {
                                while (!Utils.moveTowards(rc, face)) {
                                    face = new Direction(2 * (float) Math.PI * rand.nextFloat());
                                }
                                break;
                            }
                            else {
                                target = m[0];
                                for (MapLocation l : m) {
                                    if (rc.getLocation().distanceTo(l) < rc.getLocation().distanceTo(target)) target = l;
                                }
                                nextStage();
                            }
                        } else {
                            nextStage();
                        }
                    case 1:
                        if (target == null) {
                            int t = rc.getID() % foundEnemies;
                            int x = rc.readBroadcast(t * 2 + 1);
                            int y = rc.readBroadcast(t * 2 + 2);
                            target = new MapLocation(x, y);
                            face = rc.getLocation().directionTo(target);
                        }
                        if (!rc.canSenseLocation(target)) {
                            while (!Utils.moveTowards(rc, face)) {
                                face = new Direction(2 * (float) Math.PI * rand.nextFloat());
                            }
                            break;
                        } else {
                            nextStage();
                        }
                    case 2:
                        if (enemies.length == 0) {
                            target = null;
                            status = 1;
                            break;
                        }
                    default:
                }

                Utils.attack(rc);

                Clock.yield();
            } catch (GameActionException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    public static void nextStage() {
        status++;
    }
}
