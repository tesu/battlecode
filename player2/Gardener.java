package player2;

import battlecode.common.*;
import static player2.Utils.randomDirection;

public class Gardener {
    public static void run(RobotController rc) {
        System.out.println("I'm a gardener!");
        int treeCount = 0;

        while (true) {
            try {
                Utils.dodgeBullets(rc);
                Utils.flee(rc);
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

                dir = randomDirection();
                for (int i = 0; i < 25; i++) {
                    if (rc.canBuildRobot(RobotType.SCOUT, dir)) {
                        rc.buildRobot(RobotType.SCOUT, dir);
                    }
                    dir.rotateLeftDegrees(15);
                }

                TreeInfo[] trees = rc.senseNearbyTrees(-1, rc.getTeam());
                if (trees.length > 0) {
                    TreeInfo minTree = trees[0];
                    for (TreeInfo tree : trees) {
                        if (tree.health < minTree.health) {
                            minTree = tree;
                        }
                    }
                    dir = rc.getLocation().directionTo(minTree.location);
                    if (rc.getMoveCount() == 0) {
                        Utils.tryMove(rc, dir);
                    }

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

                Utils.shakeTrees(rc);

                Utils.moveToSpace(rc);

                if (rc.getMoveCount() == 0) {
                    Utils.tryMove(rc, randomDirection());
                }

                if (rc.getTeamBullets() >= 10000) {
                    rc.donate(10000);
                }

                Clock.yield();
            } catch (Exception e) {
                System.out.println("Gardener Exception");
                e.printStackTrace();
            }
        }
    }
}