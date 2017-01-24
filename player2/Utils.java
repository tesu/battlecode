package player2;

import battlecode.common.*;

public class Utils {
    public static void dodgeBullets(RobotController rc) throws GameActionException {
        MapLocation myLocation = rc.getLocation();

        BulletInfo[] bullets = rc.senseNearbyBullets();
        if (bullets.length > 0){
            BulletInfo nearestBullet = null;
            float distance = 99;
            for (BulletInfo bullet : bullets){
                MapLocation newLocation = new MapLocation(bullet.location.add(bullet.dir, (float) 0.1).x,
                        bullet.location.add(bullet.dir, (float) 0.1).y);
                if (myLocation.distanceTo(bullet.location) < distance &&
                        (myLocation.distanceTo(newLocation) < myLocation.distanceTo(bullet.location) ||
                                myLocation.distanceTo(bullet.location) <= rc.getType().strideRadius)){
                    distance = myLocation.distanceTo(bullet.location);
                    nearestBullet = bullet;
                }
            }

            if (rc.getMoveCount() == 0 && nearestBullet != null) {
                Direction direction = nearestBullet.location.directionTo(myLocation);
                tryMove(rc, direction);
            }
        }
    }

    public static void flee(RobotController rc) throws GameActionException {
        MapLocation myLocation = rc.getLocation();

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
    }

    //this method sucks and should be completely redone
    public static void moveToSpace(RobotController rc) throws GameActionException {
        MapLocation myLocation = rc.getLocation();

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
    }

    public static void shakeTrees(RobotController rc) throws GameActionException {
        TreeInfo[] trees = rc.senseNearbyTrees(1, Team.NEUTRAL);
        for (TreeInfo tree : trees){
            if (rc.canShake(tree.ID)){
                rc.shake(tree.ID);
                break;
            }
        }
    }

    public static void attack(RobotController rc) throws GameActionException {
        Team enemy = rc.getTeam().opponent();
        MapLocation myLocation = rc.getLocation();

        // See if there are any nearby enemy robots
        RobotInfo[] enemies = rc.senseNearbyRobots(-1, enemy);

        // If there are some...
        if (enemies.length > 0){
            RobotInfo lowestEnemy = enemies[0];
            for (RobotInfo target : enemies){
                if (target.health < lowestEnemy.health){
                    lowestEnemy = target;
                }
            }
            // And we have enough bullets, and haven't attacked yet this turn...
            if (rc.getMoveCount() == 0) {
                if (lowestEnemy.type != RobotType.LUMBERJACK){
                    Utils.tryMove(rc, myLocation.directionTo(lowestEnemy.location));
                }
                else {
                    Utils.tryMove(rc, lowestEnemy.location.directionTo(myLocation));
                }
            }
            if (rc.canFireSingleShot()) {
                // ...Then fire a bullet in the direction of the enemy.
                myLocation = rc.getLocation();
                rc.fireSingleShot(myLocation.directionTo(lowestEnemy.location));
            }
        }

        TreeInfo[] enemyTrees = rc.senseNearbyTrees(-1, enemy);

        // If there are some...
        if (enemyTrees.length > 0){
            TreeInfo lowestEnemy = enemyTrees[0];
            for (TreeInfo target : enemyTrees){
                if (target.health < lowestEnemy.health){
                    lowestEnemy = target;
                }
            }
            // And we have enough bullets, and haven't attacked yet this turn...
            if (rc.getMoveCount() == 0) {
                Utils.tryMove(rc, myLocation.directionTo(lowestEnemy.location));
            }
            if (rc.canFireSingleShot()) {
                // ...Then fire a bullet in the direction of the enemy.
                myLocation = rc.getLocation();
                rc.fireSingleShot(myLocation.directionTo(lowestEnemy.location));
            }
        }
    }

    public static boolean tryMove(RobotController rc, Direction dir) throws GameActionException {
        return tryMove(rc, dir,15,9);
    }

    public static boolean tryMove(RobotController rc, Direction dir, float degreeOffset, int checksPerSide) throws GameActionException {

        // First, try intended direction
        if (rc.canMove(dir)) {
            rc.move(dir);
            return true;
        }

        // Now try a bunch of similar angles
        boolean moved = false;
        int currentCheck = 1;

        while(currentCheck<=checksPerSide) {
            // Try the offset of the left side
            if(rc.canMove(dir.rotateLeftDegrees(degreeOffset*currentCheck))) {
                rc.move(dir.rotateLeftDegrees(degreeOffset*currentCheck));
                return true;
            }
            // Try the offset on the right side
            if(rc.canMove(dir.rotateRightDegrees(degreeOffset*currentCheck))) {
                rc.move(dir.rotateRightDegrees(degreeOffset*currentCheck));
                return true;
            }
            // No move performed, try slightly further
            currentCheck++;
        }

        // A move never happened, so return false.
        return false;
    }

    static Direction randomDirection() {
        return new Direction((float)Math.random() * 2 * (float)Math.PI);
    }

}
