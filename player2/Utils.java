package player2;

import battlecode.common.*;

/**
 * Created by Jason on 1/11/2017.
 */
public class Utils {
    public static void dodgeBullets(RobotController rc) throws GameActionException {
        MapLocation myLocation = rc.getLocation();

        BulletInfo[] bullets = rc.senseNearbyBullets();
        if (bullets.length > 0){
            BulletInfo nearestBullet = bullets[0];
            float distance = myLocation.distanceTo(nearestBullet.location);
            for (BulletInfo bullet : bullets){
                MapLocation newLocation = new MapLocation(bullet.location.add(bullet.dir, (float) 0.1).x,
                        bullet.location.add(bullet.dir, (float) 0.1).y);
                if (myLocation.distanceTo(bullet.location) < distance &&
                        myLocation.distanceTo(newLocation) <= myLocation.distanceTo(bullet.location)){
                    distance = myLocation.distanceTo(bullet.location);
                    nearestBullet = bullet;
                }
            }
            MapLocation newLocation = new MapLocation(nearestBullet.location.add(nearestBullet.dir, (float) 0.1).x,
                    nearestBullet.location.add(nearestBullet.dir, (float) 0.1).y);
            if (rc.getMoveCount() == 0 &&
                    myLocation.distanceTo(newLocation) <= myLocation.distanceTo(nearestBullet.location)) {
                Direction direction = nearestBullet.location.directionTo(myLocation);
                tryMove(rc, direction);
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
}
