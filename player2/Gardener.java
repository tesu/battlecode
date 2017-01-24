package player2;

import battlecode.common.*;
import static player2.Utils.randomDirection;

public class Gardener {
    public static void run(RobotController rc) {
        System.out.println("I'm a gardener!");
        int treeCount = 0;
        // The code you want your robot to perform every round should be in this loop
        while (true) {

            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {
                //Dodge bullets
                Utils.dodgeBullets(rc);

                //Run away from enemies
                Utils.flee(rc);

                //plant tree
                Direction dir = randomDirection();

                if (treeCount < 4) {
                    for (int i = 0; i < 25; i++) {
                        if (rc.canPlantTree(dir)) {
                            rc.plantTree(dir);
                            treeCount++;
                        }
                        dir.rotateLeftDegrees(15);
                    }
                }
                //build soldier
                dir = randomDirection();
                for (int i = 0; i < 25; i++) {
                    if (rc.canBuildRobot(RobotType.SCOUT, dir)) {
                        rc.buildRobot(RobotType.SCOUT, dir);
                    }
                    dir.rotateLeftDegrees(15);
                }

                //find tree
                TreeInfo[] trees = rc.senseNearbyTrees(-1, rc.getTeam());
                if (trees.length > 0) {
                    TreeInfo minTree = trees[0];
                    for (TreeInfo tree : trees) {
                        if (tree.health < minTree.health) {
                            minTree = tree;
                        }
                    }
                    //path to tree
                    dir = rc.getLocation().directionTo(minTree.location);
                    if (rc.getMoveCount() == 0) {
                        Utils.tryMove(rc, dir);
                    }

                    //water tree
                    minTree = new TreeInfo(-1, rc.getTeam(), rc.getLocation(), 1, 999, 0, null);
                    for (TreeInfo tree : trees) {
                        if (tree.health < minTree.health && rc.canWater(tree.ID)
                                && rc.canInteractWithTree(tree.location)) {
                            minTree = tree;
                        }
                    }
                    if (rc.canWater(minTree.ID) && rc.canInteractWithTree(minTree.location)) {
                        rc.water(minTree.ID);
                    }
                }

                //shake trees
                Utils.shakeTrees(rc);

                //Try to move to empty space
                Utils.moveToSpace(rc);

                // Move randomly
                if (rc.getMoveCount() == 0) {
                    Utils.tryMove(rc, randomDirection());
                }

                if (rc.getTeamBullets() >= 10000) {
                    rc.donate(10000);
                }

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println("Gardener Exception");
                e.printStackTrace();
            }
        }
    }
}