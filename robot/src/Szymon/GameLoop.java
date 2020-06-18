import javax.media.j3d.*;
import javax.vecmath.Vector3f;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

public class GameLoop extends Behavior {

    private WakeupOr oredCriteria;
    private World world;
    private Robot robot;

    public GameLoop(World world, Robot robot){
        this.world = world;
        this.robot = robot;
    }

    public void initialize(){
        WakeupCriterion[] theCriteria = new WakeupCriterion[1];
        theCriteria[0] = new WakeupOnElapsedTime(1000/60);
        oredCriteria = new WakeupOr(theCriteria);
        wakeupOn(oredCriteria);
    }

    public void processStimulus(Enumeration criteria) {
        while (criteria.hasMoreElements()) {
            WakeupCriterion theCriterion = (WakeupCriterion) criteria.nextElement();
            if (theCriterion instanceof WakeupOnElapsedTime) {
                if(world.objY > 0.16f && !world.objColl)
                    world.objY -= 0.01f;
                world.objectTransform.setTranslation(new Vector3f(world.objX, world.objY, world.objZ));
                world.objectTg.setTransform(world.objectTransform);
                move();
            }
        }
        wakeupOn(oredCriteria);
    }

    public void move(){
        if (robot.key[KeyEvent.VK_A] && !robot.notAllow[0]) {
            robot.angles[0] -= Math.PI / robot.robotSpeed;
            robot.tmp.rotY(Math.PI / robot.robotSpeed);
            robot.transformArm[0].mul(robot.tmp);
            robot.tgArm[0].setTransform(robot.transformArm[0]);
            robot.lastMove = 'a';
        }
        if (robot.key[KeyEvent.VK_D] && !robot.notAllow[1]) {
            robot.angles[0] += Math.PI / robot.robotSpeed;
            robot.tmp.rotY(-Math.PI / robot.robotSpeed);
            robot.transformArm[0].mul(robot.tmp);
            robot.tgArm[0].setTransform(robot.transformArm[0]);
            robot.lastMove = 'd';
        }
        // sterowanie położeniem barku
        if (robot.key[KeyEvent.VK_W] && !robot.notAllow[2]) {
            robot.angles[1] -= Math.PI / robot.robotSpeed;
            if (robot.angles[1] < -Math.PI / 6) robot.angles[1] = (float) (-30 * Math.PI / 180);
            else{
                robot.tmp.rotZ(Math.PI / robot.robotSpeed);
                robot.transformJoint[0].mul(robot.tmp);
                robot.tgJoint[0].setTransform(robot.transformJoint[0]);
            }
            robot.lastMove = 'w';
        }
        if (robot.key[KeyEvent.VK_S] && !robot.notAllow[3]) {
            robot.angles[1] += Math.PI / robot.robotSpeed;
            if (robot.angles[1] > Math.PI / 2) robot.angles[1] = (float) (Math.PI / 2);
            else {
                robot.tmp.rotZ(-Math.PI / robot.robotSpeed);
                robot.transformJoint[0].mul(robot.tmp);
                robot.tgJoint[0].setTransform(robot.transformJoint[0]);
            }
            robot.lastMove = 's';
        }
        // sterowanie położeniem łokcia
        if (robot.key[KeyEvent.VK_Q] && !robot.notAllow[4]) {
            robot.angles[2] -= Math.PI / robot.robotSpeed;
            if(robot.angles[2]<0) robot.angles[2]=0;
            else {
                robot.tmp.rotZ(Math.PI / robot.robotSpeed);
                robot.transformJoint[1].mul(robot.tmp);
                robot.tgJoint[1].setTransform(robot.transformJoint[1]);
            }
            robot.lastMove = 'q';
        }
        if (robot.key[KeyEvent.VK_E] && !robot.notAllow[5]) {
            robot.angles[2] += Math.PI / robot.robotSpeed;
            if(robot.angles[2]>105*Math.PI/180) robot.angles[2]=(float)(105*Math.PI/180);
            else {
                robot.tmp.rotZ(-Math.PI / robot.robotSpeed);
                robot.transformJoint[1].mul(robot.tmp);
                robot.tgJoint[1].setTransform(robot.transformJoint[1]);
            }
            robot.lastMove = 'e';
        }
        // sterowaniem obrotem roll
        if (robot.key[KeyEvent.VK_U] && !robot.notAllow[6]) {
            robot.angles[3] -= Math.PI / robot.robotSpeed;
            robot.tmp.rotY(Math.PI / robot.robotSpeed);
            robot.transformArm[3].mul(robot.tmp);
            robot.tgArm[3].setTransform(robot.transformArm[3]);
            robot.lastMove = 'u';
        }
        if (robot.key[KeyEvent.VK_O] && !robot.notAllow[7]) {
            robot.angles[3] += Math.PI / robot.robotSpeed;
            robot.tmp.rotY(-Math.PI / robot.robotSpeed);
            robot.transformArm[3].mul(robot.tmp);
            robot.tgArm[3].setTransform(robot.transformArm[3]);
            robot.lastMove = 'o';
        }
        // sterowanie pitch
        if (robot.key[KeyEvent.VK_I] && !robot.notAllow[8]) {
            robot.angles[4] -= Math.PI / robot.robotSpeed;
            if(robot.angles[4]<-Math.PI/2) robot.angles[4]=(float)(-Math.PI/2);
            else {
                robot.tmp.rotZ(Math.PI / robot.robotSpeed);
                robot.transformArm[4].mul(robot.tmp);
                robot.tgArm[4].setTransform(robot.transformArm[4]);
            }
            robot.lastMove = 'i';
        }
        if (robot.key[KeyEvent.VK_K] && !robot.notAllow[9]) {
            robot.angles[4] += Math.PI / robot.robotSpeed;
            if(robot.angles[4]>Math.PI/2) robot.angles[4]=(float)(Math.PI/2);
            else {
                robot.tmp.rotZ(-Math.PI / robot.robotSpeed);
                robot.transformArm[4].mul(robot.tmp);
                robot.tgArm[4].setTransform(robot.transformArm[4]);
            }
            robot.lastMove = 'k';
        }
        // sterowanie yaw
        if (robot.key[KeyEvent.VK_J] && !robot.notAllow[10]) {
            robot.angles[5] -= Math.PI / robot.robotSpeed;
            if(robot.angles[5]<-Math.PI/4) robot.angles[5]=(float)(-Math.PI/4);
            else {
                robot.tmp.rotX(Math.PI / robot.robotSpeed);
                robot.transformJoint[2].mul(robot.tmp);
                robot.tgJoint[2].setTransform(robot.transformJoint[2]);
            }
            robot.lastMove = 'j';
        }
        if (robot.key[KeyEvent.VK_L] && !robot.notAllow[11]) {
            robot.angles[5] += Math.PI / robot.robotSpeed;
            if(robot.angles[5]>Math.PI/4)robot.angles[5]=(float)(Math.PI/4);
            else {
                robot.tmp.rotX(-Math.PI / robot.robotSpeed);
                robot.transformJoint[2].mul(robot.tmp);
                robot.tgJoint[2].setTransform(robot.transformJoint[2]);
            }
            robot.lastMove = 'l';
        }
    }
}
