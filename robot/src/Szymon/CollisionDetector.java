import javax.media.j3d.*;
import java.util.Enumeration;

public class CollisionDetector extends Behavior {
    public Shape3D collidingShape;

    public WakeupCriterion[] theCriteria;

    public WakeupOr oredCriteria;

    public World world;

    public Robot robot;

    public CollisionDetector(Shape3D theShape, Bounds theBounds, World world, Robot robot){
        collidingShape = theShape;
        setSchedulingBounds(theBounds);
        this.world = world;
        this.robot = robot;
    }

    public void initialize(){
        theCriteria = new WakeupCriterion[3];
        WakeupOnCollisionEntry startsCollision = new WakeupOnCollisionEntry(collidingShape);
        WakeupOnCollisionExit endsCollision = new WakeupOnCollisionExit(collidingShape);
        WakeupOnCollisionMovement moveCollision = new WakeupOnCollisionMovement(collidingShape);
        theCriteria[0] = startsCollision;
        theCriteria[1] = endsCollision;
        theCriteria[2] = moveCollision;
        oredCriteria = new WakeupOr(theCriteria);
        wakeupOn(oredCriteria);
    }

    public void processStimulus(Enumeration criteria) {
        while (criteria.hasMoreElements()) {
            WakeupCriterion theCriterion = (WakeupCriterion) criteria.nextElement();
            if(theCriterion instanceof WakeupOnCollisionEntry){
                toNotAllow();
            } else if(theCriterion instanceof WakeupOnCollisionExit){
                toAllow();
            }
//            if (theCriterion instanceof WakeupOnCollisionEntry || theCriterion instanceof WakeupOnCollisionMovement) {
//                makeMove();
//            }
            wakeupOn(oredCriteria);
        }
    }

    public void toNotAllow(){
        if(robot.lastMove == 'a'){
            robot.notAllow[0] = true;
        }
        if(robot.lastMove == 'd'){
            robot.notAllow[1] = true;
        }
        if(robot.lastMove == 'w'){
            robot.notAllow[2] = true;
        }
        if(robot.lastMove == 's'){
            robot.notAllow[3] = true;
        }
        if(robot.lastMove == 'q'){
            robot.notAllow[4] = true;
        }
        if(robot.lastMove == 'e'){
            robot.notAllow[5] = true;
        }
        if(robot.lastMove == 'u'){
            robot.notAllow[6] = true;
        }
        if(robot.lastMove == 'o'){
            robot.notAllow[7] = true;
        }
        if(robot.lastMove == 'i'){
            robot.notAllow[8] = true;
        }
        if(robot.lastMove == 'k'){
            robot.notAllow[9] = true;
        }
        if(robot.lastMove == 'j'){
            robot.notAllow[10] = true;
        }
        if(robot.lastMove == 'l'){
            robot.notAllow[11] = true;
        }
        if(collidingShape.getUserData() != "object"){
            world.objColl = true;
        }
    }

    public void toAllow(){
        for(int i=0; i<12; i++){
            robot.notAllow[i] = false;
        }
        if(collidingShape.getUserData() != "object"){
            world.objColl = false;
        }
//        System.out.println("wyszlo");
//        if(robot.lastMove == 'a'){
//            robot.notAllow[0] = false;
//        }
//        if(robot.lastMove == 'd'){
//            robot.notAllow[1] = false;
//        }
//        if(robot.lastMove == 'w'){
//            robot.notAllow[2] = false;
//            System.out.println("odbroniono w");
//        }
//        if(robot.lastMove == 's'){
//            robot.notAllow[3] = false;
//            System.out.println("odbroniono s");
//        }
//        if(robot.lastMove == 'q'){
//            robot.notAllow[4] = false;
//        }
//        if(robot.lastMove == 'e'){
//            robot.notAllow[5] = false;
//        }
//        if(robot.lastMove == 'u'){
//            robot.notAllow[6] = false;
//        }
//        if(robot.lastMove == 'o'){
//            robot.notAllow[7] = false;
//        }
//        if(robot.lastMove == 'i'){
//            robot.notAllow[8] = false;
//        }
//        if(robot.lastMove == 'k'){
//            robot.notAllow[9] = false;
//        }
//        if(robot.lastMove == 'j'){
//            robot.notAllow[10] = false;
//        }
//        if(robot.lastMove == 'l'){
//            robot.notAllow[11] = false;
//        }
    }

    public void makeMove(){
        if(robot.lastMove == 'a'){
            robot.tmp.rotY(-Math.PI / robot.robotSpeed);
            robot.transformArm[0].mul(robot.tmp);
            robot.tgArm[0].setTransform(robot.transformArm[0]);
            robot.angles[0] += Math.PI / robot.robotSpeed;
        }
        if(robot.lastMove == 'd'){
            robot.tmp.rotY(Math.PI / robot.robotSpeed);
            robot.transformArm[0].mul(robot.tmp);
            robot.tgArm[0].setTransform(robot.transformArm[0]);
            robot.angles[0] -= Math.PI / robot.robotSpeed;
        }
        if(robot.lastMove == 'w'){
            robot.tmp.rotZ(-Math.PI / robot.robotSpeed);
            robot.transformJoint[0].mul(robot.tmp);
            robot.tgJoint[0].setTransform(robot.transformJoint[0]);
            robot.angles[1] += Math.PI / robot.robotSpeed;
        }
        if(robot.lastMove == 's'){
            robot.tmp.rotZ(Math.PI / robot.robotSpeed);
            robot.transformJoint[0].mul(robot.tmp);
            robot.tgJoint[0].setTransform(robot.transformJoint[0]);
            robot.angles[1] -= Math.PI / robot.robotSpeed;
        }
        if(robot.lastMove == 'q'){
            robot.tmp.rotZ(-Math.PI / robot.robotSpeed);
            robot.transformJoint[1].mul(robot.tmp);
            robot.tgJoint[1].setTransform(robot.transformJoint[1]);
            robot.angles[2] += Math.PI / robot.robotSpeed;
        }
        if(robot.lastMove == 'e'){
            robot.tmp.rotZ(Math.PI / robot.robotSpeed);
            robot.transformJoint[1].mul(robot.tmp);
            robot.tgJoint[1].setTransform(robot.transformJoint[1]);
            robot.angles[2] -= Math.PI / robot.robotSpeed;
        }
        if(robot.lastMove == 'u'){
            robot.tmp.rotY(-Math.PI / robot.robotSpeed);
            robot.transformArm[3].mul(robot.tmp);
            robot.tgArm[3].setTransform(robot.transformArm[3]);
            robot.angles[3] += Math.PI / robot.robotSpeed;
        }
        if(robot.lastMove == 'o'){
            robot.tmp.rotY(Math.PI / robot.robotSpeed);
            robot.transformArm[3].mul(robot.tmp);
            robot.tgArm[3].setTransform(robot.transformArm[3]);
            robot.angles[3] -= Math.PI / robot.robotSpeed;
        }
        if(robot.lastMove == 'i'){
            robot.tmp.rotZ(-Math.PI / robot.robotSpeed);
            robot.transformArm[4].mul(robot.tmp);
            robot.tgArm[4].setTransform(robot.transformArm[4]);
            robot.angles[4] += Math.PI / robot.robotSpeed;
        }
        if(robot.lastMove == 'k'){
            robot.tmp.rotZ(Math.PI / robot.robotSpeed);
            robot.transformArm[4].mul(robot.tmp);
            robot.tgArm[4].setTransform(robot.transformArm[4]);
            robot.angles[4] -= Math.PI / robot.robotSpeed;
        }
        if(robot.lastMove == 'j'){
            robot.tmp.rotX(-Math.PI / robot.robotSpeed);
            robot.transformJoint[2].mul(robot.tmp);
            robot.tgJoint[2].setTransform(robot.transformJoint[2]);
            robot.angles[5] += Math.PI / robot.robotSpeed;
        }
        if(robot.lastMove == 'l'){
            robot.tmp.rotX(Math.PI / robot.robotSpeed);
            robot.transformJoint[2].mul(robot.tmp);
            robot.tgJoint[2].setTransform(robot.transformJoint[2]);
            robot.angles[5] -= Math.PI / robot.robotSpeed;
        }
    }
}