package player2;
import battlecode.common.*;
import battlecode.schema.MatchHeader;

import java.util.*;

public strictfp class RobotPlayer {
    static RobotController rc;

    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * If this method returns, the robot dies!
    **/
    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {

        // This is the RobotController object. You use it to perform actions from this robot,
        // and to get information on its current status.
        RobotPlayer.rc = rc;

        // Here, we've separated the controls into a different method for each RobotType.
        // You can add the missing ones or rewrite this into your own control structure.
        switch (rc.getType()) {
            case ARCHON:
                runArchon();
                break;
            case GARDENER:
                runGardener();
                break;
            case SOLDIER:
                runSoldier();
                break;
            case LUMBERJACK:
                runLumberjack();
                break;
        }
	}

    static void runArchon() throws GameActionException {
        System.out.println("I'm an archon!");

        // The code you want your robot to perform every round should be in this loop
        while (true) {

            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {

                MapLocation myLocation = rc.getLocation();

                //Dodge bullets
                Utils.dodgeBullets(rc);

                //Run away from enemies
                RobotInfo[] enemies = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
                if (enemies.length > 0){
                    RobotInfo nearestEnemy = enemies[0];
                    float distance = myLocation.distanceTo(nearestEnemy.location);
                    for (RobotInfo enemy : enemies){
                        if (myLocation.distanceTo(enemy.location) < distance){
                            distance = myLocation.distanceTo(enemy.location);
                            nearestEnemy = enemy;
                        }
                    }
                    Direction direction = nearestEnemy.location.directionTo(myLocation);
                    if (rc.getMoveCount() == 0) {
                        Utils.tryMove(rc, direction);
                    }
                }

                // Attempt to build a gardener
                Direction dir = randomDirection();

                for (int i = 0; i < 9; i++) {
                    if (rc.canHireGardener(dir)) {
                        rc.hireGardener(dir);
                    }
                    dir.rotateLeftDegrees(45);
                }

                //Try to move to empty space
                RobotInfo[] allies = rc.senseNearbyRobots(-1, rc.getTeam());
                if (allies.length > 0){
                    RobotInfo nearestAlly = allies[0];
                    float distance = myLocation.distanceTo(nearestAlly.location);
                    for (RobotInfo ally : allies){
                        if (myLocation.distanceTo(ally.location) < distance){
                            distance = myLocation.distanceTo(ally.location);
                            nearestAlly = ally;
                        }
                    }
                    Direction direction = nearestAlly.location.directionTo(myLocation);
                    if (rc.getMoveCount() == 0) {
                        Utils.tryMove(rc, direction);
                    }
                }

                // Move randomly
                if (rc.getMoveCount() == 0) {
                    Utils.tryMove(rc, randomDirection());
                }

                if (rc.getTeamBullets() >= 10000){
                    rc.donate(10000);
                }

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again

                Clock.yield();

            } catch (Exception e) {
                System.out.println("Archon Exception");
                e.printStackTrace();
            }
        }
    }

	static void runGardener() throws GameActionException {
        System.out.println("I'm a gardener!");
        int treeCount = 0;
        // The code you want your robot to perform every round should be in this loop
        while (true) {

            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {
                /*
                // Listen for home archon's location
                int xPos = rc.readBroadcast(0);
                int yPos = rc.readBroadcast(1);
                MapLocation archonLoc = new MapLocation(xPos,yPos);
                */

                MapLocation myLocation = rc.getLocation();

                //Dodge bullets
                Utils.dodgeBullets(rc);

                //Run away from enemies
                RobotInfo[] enemies = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
                if (enemies.length > 0){
                    RobotInfo nearestEnemy = enemies[0];
                    float distance = myLocation.distanceTo(nearestEnemy.location);
                    for (RobotInfo enemy : enemies){
                        if (myLocation.distanceTo(enemy.location) < distance){
                            distance = myLocation.distanceTo(enemy.location);
                            nearestEnemy = enemy;
                        }
                    }
                    Direction direction = nearestEnemy.location.directionTo(myLocation);
                    if (rc.getMoveCount() == 0) {
                        Utils.tryMove(rc, direction);
                    }
                }

                //plant tree
                Direction dir = randomDirection();

                if (treeCount < 4) {
                    for (int i = 0; i < 9; i++) {
                        if (rc.canPlantTree(dir)) {
                            rc.plantTree(dir);
                            treeCount++;
                        }
                        dir.rotateLeftDegrees(45);
                    }
                }
                //build soldier
                dir = randomDirection();
                for (int i = 0; i < 9; i++) {
                    if (rc.canBuildRobot(RobotType.SOLDIER, dir)) {
                        rc.buildRobot(RobotType.SOLDIER, dir);
                    }
                    dir.rotateLeftDegrees(45);
                }

                //find tree
                TreeInfo[] trees = rc.senseNearbyTrees();
                if (trees.length > 0){
                    TreeInfo minTree = trees[0];
                    for (int i = 0; i < trees.length; i++) {
                        if (trees[i].health < minTree.health) {
                            minTree = trees[i];
                        }
                    }
                    //path to tree
                    dir = rc.getLocation().directionTo(minTree.location);
                    if (rc.getMoveCount() == 0) {
                        Utils.tryMove(rc, dir);
                    }

                    //water tree
                    minTree = new TreeInfo(-1,rc.getTeam(),rc.getLocation(),1,999,0,null);
                    for (int i = 0; i < trees.length; i++) {
                        if (trees[i].health < minTree.health && rc.canWater(trees[i].ID)) {
                            minTree = trees[i];
                        }
                    }
                    if (rc.canWater(minTree.ID)){
                        rc.water(minTree.ID);
                    }
                }

                //Try to move to empty space
                RobotInfo[] allies = rc.senseNearbyRobots(-1, rc.getTeam());
                if (allies.length > 0){
                    RobotInfo nearestAlly = allies[0];
                    float distance = myLocation.distanceTo(nearestAlly.location);
                    for (RobotInfo ally : allies){
                        if (myLocation.distanceTo(ally.location) < distance){
                            distance = myLocation.distanceTo(ally.location);
                            nearestAlly = ally;
                        }
                    }
                    Direction direction = nearestAlly.location.directionTo(myLocation);
                    if (rc.getMoveCount() == 0) {
                        Utils.tryMove(rc, direction);
                    }
                }

                // Move randomly
                if (rc.getMoveCount() == 0) {
                    Utils.tryMove(rc, randomDirection());
                }

                if (rc.getTeamBullets() >= 10000){
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

    static void runSoldier() throws GameActionException {
        System.out.println("I'm a soldier!");
        Team enemy = rc.getTeam().opponent();

        // The code you want your robot to perform every round should be in this loop
        while (true) {

            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {
                MapLocation myLocation = rc.getLocation();

                //Dodge bullets
                Utils.dodgeBullets(rc);

                // See if there are any nearby enemy robots
                RobotInfo[] enemies = rc.senseNearbyRobots(-1, enemy);

                // If there are some...
                if (enemies.length > 0){
                    RobotInfo nearestEnemy = enemies[0];
                    float distance = myLocation.distanceTo(nearestEnemy.location);
                    for (RobotInfo target : enemies){
                        if (myLocation.distanceTo(target.location) < distance){
                            distance = myLocation.distanceTo(target.location);
                            nearestEnemy = target;
                        }
                    }
                    // And we have enough bullets, and haven't attacked yet this turn...
                    Direction direction = myLocation.directionTo(nearestEnemy.location);
                    if (rc.getMoveCount() == 0) {
                        Utils.tryMove(rc, direction);
                    }
                    if (rc.canFireSingleShot()) {
                        // ...Then fire a bullet in the direction of the enemy.
                        myLocation = rc.getLocation();
                        rc.fireSingleShot(myLocation.directionTo(nearestEnemy.location));
                    }
                }

                TreeInfo[] enemyTrees = rc.senseNearbyTrees(-1, enemy);

                // If there are some...
                if (enemyTrees.length > 0){
                    TreeInfo nearestEnemy = enemyTrees[0];
                    float distance = myLocation.distanceTo(nearestEnemy.location);
                    for (TreeInfo target : enemyTrees){
                        if (myLocation.distanceTo(target.location) < distance){
                            distance = myLocation.distanceTo(target.location);
                            nearestEnemy = target;
                        }
                    }
                    // And we have enough bullets, and haven't attacked yet this turn...
                    Direction direction = myLocation.directionTo(nearestEnemy.location);
                    if (rc.getMoveCount() == 0) {
                        Utils.tryMove(rc, direction);
                    }
                    if (rc.canFireSingleShot()) {
                        // ...Then fire a bullet in the direction of the enemy.
                        myLocation = rc.getLocation();
                        rc.fireSingleShot(myLocation.directionTo(nearestEnemy.location));
                    }
                }

                // Move randomly
                if (rc.getMoveCount() == 0) {
                    Utils.tryMove(rc, randomDirection());
                }

                if (rc.getTeamBullets() >= 200){
                    rc.donate(50);
                }

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println("Soldier Exception");
                e.printStackTrace();
            }
        }
    }

    static void runLumberjack() throws GameActionException {
        System.out.println("I'm a lumberjack!");
        Team enemy = rc.getTeam().opponent();

        // The code you want your robot to perform every round should be in this loop
        while (true) {

            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {

                // See if there are any enemy robots within striking range (distance 1 from lumberjack's radius)
                RobotInfo[] robots = rc.senseNearbyRobots(RobotType.LUMBERJACK.bodyRadius+GameConstants.LUMBERJACK_STRIKE_RADIUS, enemy);

                if(robots.length > 0 && !rc.hasAttacked()) {
                    // Use strike() to hit all nearby robots!
                    rc.strike();
                } else {
                    // No close robots, so search for robots within sight radius
                    robots = rc.senseNearbyRobots(-1,enemy);

                    // If there is a robot, move towards it
                    if(robots.length > 0) {
                        MapLocation myLocation = rc.getLocation();
                        MapLocation enemyLocation = robots[0].getLocation();
                        Direction toEnemy = myLocation.directionTo(enemyLocation);

                        Utils.tryMove(rc, toEnemy);
                    } else {
                        // Move Randomly
                        Utils.tryMove(rc, randomDirection());
                    }
                }

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println("Lumberjack Exception");
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns a random Direction
     * @return a random Direction
     */
    static Direction randomDirection() {
        return new Direction((float)Math.random() * 2 * (float)Math.PI);
    }

    /**
     * A slightly more complicated example function, this returns true if the given bullet is on a collision
     * course with the current robot. Doesn't take into account objects between the bullet and this robot.
     *
     * @param bullet The bullet in question
     * @return True if the line of the bullet's path intersects with this robot's current position.
     */
    static boolean willCollideWithMe(BulletInfo bullet) {
        MapLocation myLocation = rc.getLocation();

        // Get relevant bullet information
        Direction propagationDirection = bullet.dir;
        MapLocation bulletLocation = bullet.location;

        // Calculate bullet relations to this robot
        Direction directionToRobot = bulletLocation.directionTo(myLocation);
        float distToRobot = bulletLocation.distanceTo(myLocation);
        float theta = propagationDirection.radiansBetween(directionToRobot);

        // If theta > 90 degrees, then the bullet is traveling away from us and we can break early
        if (Math.abs(theta) > Math.PI/2) {
            return false;
        }

        // distToRobot is our hypotenuse, theta is our angle, and we want to know this length of the opposite leg.
        // This is the distance of a line that goes from myLocation and intersects perpendicularly with propagationDirection.
        // This corresponds to the smallest radius circle centered at our location that would intersect with the
        // line that is the path of the bullet.
        float perpendicularDist = (float)Math.abs(distToRobot * Math.sin(theta)); // soh cah toa :)

        return (perpendicularDist <= rc.getType().bodyRadius);
    }
}
