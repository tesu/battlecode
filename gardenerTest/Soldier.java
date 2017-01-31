package gardenerTest;

import battlecode.common.*;
import skeleton.Utils;

import java.util.Random;

public class Soldier {
    static int status = 0;
    static MapLocation target = null;
    static int timer = 0;

    public static void run(RobotController rc) {
        Random rand = new Random(rc.getID());
        Utils.Radio radio = new Utils.Radio(rc);

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
                timer++;

                RobotInfo[] enemies = rc.senseNearbyRobots(-1, rc.getTeam().opponent());

                switch (status) {
                    case 0:
                        for (RobotInfo r : enemies) {
                            if (r.getType() == RobotType.ARCHON || r.getType() == RobotType.GARDENER) {
                                radio.addTarget(r.getLocation());
                                target = r.getLocation();
                                face = rc.getLocation().directionTo(target);
                            }
                        }
                        if (radio.targetCount() == 0) {
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
                            target = radio.closestTarget();
                            if (target == null) {
                                status = 0;
                                break;
                            }
                        }
                        face = rc.getLocation().directionTo(target);
                        if (!rc.canSenseLocation(target)) {
                            while (!Utils.moveTowards(rc, face)) {
                                face = new Direction(2 * (float) Math.PI * rand.nextFloat());
                            }
                            if (timer >= 100) status = 3;
                            break;
                        } else {
                            nextStage();
                        }
                    case 2:
                        if (enemies.length == 0) {
                            radio.deleteTarget(target);
                            target = null;
                            status = 1;
                            break;
                        }
                        Utils.dodgeBullets(rc);
                        Utils.moveTowards(rc, rc.getLocation().directionTo(enemies[0].location));
                        break;
                    case 3:
                        MapLocation newtarget = radio.closestTarget();
                        if (newtarget != null && radio.closestTarget().distanceTo(target) > .1) {
                            target = newtarget;
                            status = 1;
                            timer = 0;
                            break;
                        }
                        while (!Utils.moveTowards(rc, face)) {
                            face = new Direction(2 * (float) Math.PI * rand.nextFloat());
                        }
                    default:
                }

                Utils.attack(rc);

                if (target != null) rc.setIndicatorLine(rc.getLocation(), target, 255, 0, 0);
                if (face != null) rc.setIndicatorLine(rc.getLocation(), rc.getLocation().add(face),0,255,0);

                Clock.yield();
            } catch (GameActionException e) {
                System.out.println("EXCEPTION");
                e.printStackTrace();
            }
        }
    }

    public static void nextStage() {
        status++;
        timer = 0;
    }
}
