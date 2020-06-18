import javax.media.j3d.*;
import java.awt.*;
import java.awt.event.KeyEvent;
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
        WakeupOnAWTEvent ev;
        AWTEvent[] events;

        while (criteria.hasMoreElements()) {
            WakeupCriterion theCriterion = (WakeupCriterion) criteria.nextElement();
            if (theCriterion instanceof WakeupOnCollisionEntry || theCriterion instanceof WakeupOnCollisionMovement) {
                makeMove();
            }
            wakeupOn(oredCriteria);
        }
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