package gardenerTest;

import battlecode.common.*;
import skeleton.Utils;

import java.util.Random;

public class Gardener {
    final static float octa_con = 0.46926627053f;
    final static float octa_con2 = 2.61312592975f;
    static int status = 0;
    static int timer = 0;
    static boolean planted = false;
    static boolean moved = false;

    public static void run(RobotController rc) {
        while (true) {
            try {
                Utils.alwaysRun(rc);

                Random rand = new Random(rc.getID());
                Direction face = new Direction(2 * (float) Math.PI * rand.nextFloat());

                Utils.alwaysRun(rc);

                System.out.println(status);
                timer++;

                switch (status) {
                    case 0:
                        MapLocation center = rc.getLocation().add(face.opposite(), octa_con2 - 2);
                        if (goodSpot(rc, center)) {
                            nextStage();
                        } else {
                            if (rc.canMove(face)) rc.move(face);
                            else {
                                while (!rc.canMove(face)) {
                                    face = new Direction(2 * (float) Math.PI * rand.nextFloat());
                                }
                                rc.move(face);
                            }
                        }
                        if (status == 0) break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                        if (rc.canPlantTree(face) && !planted) {
                            rc.plantTree(face);
                            planted = true;
                        }
                        if (planted && rc.canMove(face.rotateRightDegrees(112.5f), octa_con)) {
                            rc.move(face.rotateRightDegrees(112.5f), octa_con);
                            face = face.rotateRightDegrees(45);
                            nextStage();
                            planted = false;
                        }
                        break;
                    case 8:
                        if (rc.canMove(face.opposite(), octa_con2 - 2)) {
                            rc.move(face.opposite(), octa_con2 - 2);
                            nextStage();
                        }
                    case 9:
                    case 11:
                        timer = 0;
                        Utils.RobotAnalysis R = new Utils.RobotAnalysis(rc.senseNearbyRobots());

                        if (status == 9) {
                            if (rc.isBuildReady() && !moved && rc.canMove(face, octa_con2 - 2)) {
                                rc.move(face, octa_con2 - 2);
                                moved = true;
                            } else if (!rc.isBuildReady() && moved && rc.canMove(face.opposite(), octa_con2 - 2)) {
                                rc.move(face.opposite(), octa_con2 - 2);
                                moved = false;
                            }
                        }

                        // do some analysis
                        if (rc.canBuildRobot(RobotType.SCOUT, face)) {
                            rc.buildRobot(RobotType.SCOUT, face);
                        }

                        break;
                    case 10:
                        timer = 0;
                        for (int i = 1; i < 6; i++) {
                            Direction d = face.rotateRightDegrees(60 * i);
                            if (rc.canPlantTree(d)) {
                                rc.plantTree(d);
                                break;
                            }
                            if (i == 5 && rc.isBuildReady() && rc.getTeamBullets() >= 50) nextStage();
                        }
                        break;
                    default:

                }

                if (timer > 100) {
                    status = 10;
                }

                TreeInfo[] myTrees = rc.senseNearbyTrees(-1, rc.getTeam());
                if (myTrees.length > 0) {
                    TreeInfo lowest = myTrees[0];

                    for (TreeInfo tree : myTrees) {
                        if (tree.getHealth() < lowest.getHealth() && rc.canWater(tree.getID())) lowest = tree;
                    }
                    if (rc.canWater(lowest.getID())) rc.water(lowest.getID());
                }

                Clock.yield();
            } catch (GameActionException e) {
                System.out.println(e.getMessage());
                run(rc); // this may be a really bad idea lol
            }
        }

    }

    public static void nextStage() {
        status++;
        timer = 0;
    }

    public static boolean goodSpot(RobotController rc, MapLocation center) throws GameActionException {
        if (!rc.onTheMap(center, octa_con2+1)) return false;
        if (rc.isCircleOccupiedExceptByThisRobot(center, octa_con2+1)) return false;
        for (RobotInfo robot : rc.senseNearbyRobots(center, 2*octa_con2+2, rc.getTeam())) {
            if (robot.getType() == RobotType.GARDENER && robot.getID() != rc.getID()) return false;
        }
        return true;
    }
}
