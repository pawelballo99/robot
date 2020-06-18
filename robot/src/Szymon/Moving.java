import javax.media.j3d.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

public class Moving extends Behavior {

    private WakeupCriterion[] theCriteria;
    private WakeupOr oredCriteria;
    private Robot robot;

    public Moving(Robot robot){
        this.robot = robot;
    }

    public void initialize(){
        theCriteria = new WakeupCriterion[2];
        WakeupOnAWTEvent wakeupPress = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
        WakeupOnAWTEvent wakeupRelease = new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED);
        theCriteria[0] = wakeupPress;
        theCriteria[1] = wakeupRelease;
        oredCriteria = new WakeupOr(theCriteria);
        wakeupOn(oredCriteria);

    }

    public void processStimulus(Enumeration criteria) {
        WakeupOnAWTEvent ev;
        AWTEvent[] events;

        while (criteria.hasMoreElements()) {
            WakeupCriterion theCriterion = (WakeupCriterion) criteria.nextElement();
            if (theCriterion instanceof WakeupOnAWTEvent) {
                ev  = (WakeupOnAWTEvent) theCriterion;
                events = ev.getAWTEvent();
                processAWTEvent(events);
            }
        }
        wakeupOn(oredCriteria);

    }

    public void processAWTEvent(AWTEvent[] events){
        for(int n=0; n<events.length; n++){
            if(events[n] instanceof KeyEvent){
                KeyEvent key = (KeyEvent) events[n];

                if(key.getID() == KeyEvent.KEY_PRESSED){
                    int keyChar = key.getKeyChar();
                    int keyCode = key.getKeyCode();

                    robot.key[keyCode] = true;

//                    if(keyChar == 'a' && !robot.notAllow[0]){
//                        robot.key[keyCode] = true;
//                    }
//                    else if(keyChar == 'd' && !robot.notAllow[1]){
//                        robot.key[keyCode] = true;
//                    }
//                    else if(keyChar == 'w' && !robot.notAllow[2]){
//                        robot.key[keyCode] = true;
//                    }
//                    else if(keyChar == 's' && !robot.notAllow[3]){
//                        robot.key[keyCode] = true;
//                    }
//                    else if(keyChar == 'q' && !robot.notAllow[4]){
//                        robot.key[keyCode] = true;
//                    }
//                    else if(keyChar == 'e' && !robot.notAllow[5]){
//                        robot.key[keyCode] = true;
//                    }
//                    else if(keyChar == 'u' && !robot.notAllow[6]){
//                        robot.key[keyCode] = true;
//                    }
//                    else if(keyChar == 'o' && !robot.notAllow[7]){
//                        robot.key[keyCode] = true;
//                    }
//                    else if(keyChar == 'i' && !robot.notAllow[8]){
//                        robot.key[keyCode] = true;
//                    }
//                    else if(keyChar == 'k' && !robot.notAllow[9]){
//                        robot.key[keyCode] = true;
//                    }
//                    else if(keyChar == 'j' && !robot.notAllow[10]){
//                        robot.key[keyCode] = true;
//                    }
//                    else if(keyChar == 'l' && !robot.notAllow[11]){
//                        robot.key[keyCode] = true;
//                    } else {
//                        robot.key[keyCode] = false;
//                    }


                } else if(key.getID() == KeyEvent.KEY_RELEASED){
                    int keyChar = key.getKeyChar();
                    int keyCode = key.getKeyCode();

                    robot.key[keyCode] = false;
                }

//                if(key.getID() == KeyEvent.KEY_PRESSED){
//                    int keyChar = key.getKeyChar();
//
//                    if (keyChar == 'a') {
//                        robot.angles[0] -= Math.PI / robot.robotSpeed;
//                        robot.tmp.rotY(Math.PI / robot.robotSpeed);
//                        robot.transformArm[0].mul(robot.tmp);
//                        robot.tgArm[0].setTransform(robot.transformArm[0]);
//                        robot.lastMove = 'a';
//                    }
//                    if (keyChar == 'd') {
//                        robot.angles[0] += Math.PI / robot.robotSpeed;
//                        robot.tmp.rotY(-Math.PI / robot.robotSpeed);
//                        robot.transformArm[0].mul(robot.tmp);
//                        robot.tgArm[0].setTransform(robot.transformArm[0]);
//                        robot.lastMove = 'd';
//                    }
//                    // sterowanie położeniem barku
//                    if (keyChar == 'w') {
//                        robot.angles[1] -= Math.PI / robot.robotSpeed;
//                        if (robot.angles[1] < -Math.PI / 6) robot.angles[1] = (float) (-30 * Math.PI / 180);
//                        else{
//                            robot.tmp.rotZ(Math.PI / robot.robotSpeed);
//                            robot.transformJoint[0].mul(robot.tmp);
//                            robot.tgJoint[0].setTransform(robot.transformJoint[0]);
//                        }
//                        robot.lastMove = 'w';
//                    }
//                    if (keyChar == 's') {
//                        robot.angles[1] += Math.PI / robot.robotSpeed;
//                        if (robot.angles[1] > Math.PI / 2) robot.angles[1] = (float) (Math.PI / 2);
//                        else {
//                            robot.tmp.rotZ(-Math.PI / robot.robotSpeed);
//                            robot.transformJoint[0].mul(robot.tmp);
//                            robot.tgJoint[0].setTransform(robot.transformJoint[0]);
//                        }
//                        robot.lastMove = 's';
//                    }
//                    // sterowanie położeniem łokcia
//                    if (keyChar == 'q') {
//                        robot.angles[2] -= Math.PI / robot.robotSpeed;
//                        if(robot.angles[2]<0) robot.angles[2]=0;
//                        else {
//                            robot.tmp.rotZ(Math.PI / robot.robotSpeed);
//                            robot.transformJoint[1].mul(robot.tmp);
//                            robot.tgJoint[1].setTransform(robot.transformJoint[1]);
//                        }
//                        robot.lastMove = 'q';
//                    }
//                    if (keyChar == 'e') {
//                        robot.angles[2] += Math.PI / robot.robotSpeed;
//                        if(robot.angles[2]>105*Math.PI/180) robot.angles[2]=(float)(105*Math.PI/180);
//                        else {
//                            robot.tmp.rotZ(-Math.PI / robot.robotSpeed);
//                            robot.transformJoint[1].mul(robot.tmp);
//                            robot.tgJoint[1].setTransform(robot.transformJoint[1]);
//                        }
//                        robot.lastMove = 'e';
//                    }
//                    // sterowaniem obrotem roll
//                    if (keyChar == 'u') {
//                        robot.angles[3] -= Math.PI / robot.robotSpeed;
//                        robot.tmp.rotY(Math.PI / robot.robotSpeed);
//                        robot.transformArm[3].mul(robot.tmp);
//                        robot.tgArm[3].setTransform(robot.transformArm[3]);
//                        robot.lastMove = 'u';
//                    }
//                    if (keyChar == 'o') {
//                        robot.angles[3] += Math.PI / robot.robotSpeed;
//                        robot.tmp.rotY(-Math.PI / robot.robotSpeed);
//                        robot.transformArm[3].mul(robot.tmp);
//                        robot.tgArm[3].setTransform(robot.transformArm[3]);
//                        robot.lastMove = 'o';
//                    }
//                    // sterowanie pitch
//                    if (keyChar == 'i') {
//                        robot.angles[4] -= Math.PI / robot.robotSpeed;
//                        if(robot.angles[4]<-Math.PI/2) robot.angles[4]=(float)(-Math.PI/2);
//                        else {
//                            robot.tmp.rotZ(Math.PI / robot.robotSpeed);
//                            robot.transformArm[4].mul(robot.tmp);
//                            robot.tgArm[4].setTransform(robot.transformArm[4]);
//                        }
//                        robot.lastMove = 'i';
//                    }
//                    if (keyChar == 'k') {
//                        robot.angles[4] += Math.PI / robot.robotSpeed;
//                        if(robot.angles[4]>Math.PI/2) robot.angles[4]=(float)(Math.PI/2);
//                        else {
//                            robot.tmp.rotZ(-Math.PI / robot.robotSpeed);
//                            robot.transformArm[4].mul(robot.tmp);
//                            robot.tgArm[4].setTransform(robot.transformArm[4]);
//                        }
//                        robot.lastMove = 'k';
//                    }
//                    // sterowanie yaw
//                    if (keyChar == 'j') {
//                        robot.angles[5] -= Math.PI / robot.robotSpeed;
//                        if(robot.angles[5]<-Math.PI/4) robot.angles[5]=(float)(-Math.PI/4);
//                        else {
//                            robot.tmp.rotX(Math.PI / robot.robotSpeed);
//                            robot.transformJoint[2].mul(robot.tmp);
//                            robot.tgJoint[2].setTransform(robot.transformJoint[2]);
//                        }
//                        robot.lastMove = 'j';
//                    }
//                    if (keyChar == 'l') {
//                        robot.angles[5] += Math.PI / robot.robotSpeed;
//                        if(robot.angles[5]>Math.PI/4)robot.angles[5]=(float)(Math.PI/4);
//                        else {
//                            robot.tmp.rotX(-Math.PI / robot.robotSpeed);
//                            robot.transformJoint[2].mul(robot.tmp);
//                            robot.tgJoint[2].setTransform(robot.transformJoint[2]);
//                        }
//                        robot.lastMove = 'l';
//                    }
//                }
            }
        }
    }
}
