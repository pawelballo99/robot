import javax.media.j3d.*;
import javax.vecmath.Matrix3d;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

public class GameLoop extends Behavior {

    private WakeupOr oredCriteria;
    private World world;
    private Robot robot;

    private float angles[][];
    private int recordingLimit = 60; // ile sekund maksymalnie mozna nagrywac

    public GameLoop(World world, Robot robot){
        this.world = world;
        this.robot = robot;
        angles = new float[60*recordingLimit][6];
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
//                robot.handPos();
                if(world.objPos.y > 0.16f && !world.objHold) {
                    world.objPos.y -= 0.01f;
                    world.objectTransform.setTranslation(world.objPos);
                    world.objectTg.setTransform(world.objectTransform);
                }
                if(!world.recordingPlay) {
                    move();
                }
                if(world.recording){
                    for(int i=0; i<6; i++){
                        angles[world.recordingCount][i] = robot.angles[i];
                    }
                    world.recordingCount++;
                    if(world.recordingCount == recordingLimit*60){
                        world.recording = false;
                        System.out.println("Koniec nagrwania");

                    }
//                    if(world.recordingCount%60 == 0){
//                        System.out.println("sekunda");
//                    }
                }
                if(world.recordingPlay){
                    playRecord(world.playingRecordCount);
                    world.playingRecordCount++;
                    if(world.playingRecordCount == world.recordingCount){
                        world.recordingPlay = false;
                        System.out.println("koniec odtwarzania");
                    }
                }
            }
        }
        wakeupOn(oredCriteria);
    }

    public void move(){
        if (robot.key[KeyEvent.VK_A] && !robot.notAllow[0]) {
            robot.angles[0] += Math.PI / robot.robotSpeed;
            robot.transformArm[0].setRotation(new Matrix3d(Math.cos(robot.angles[0]), 0, Math.sin(robot.angles[0]),
                    0, 1, 0,
                    -Math.sin(robot.angles[0]), 0, Math.cos(robot.angles[0])));
            robot.tgArm[0].setTransform(robot.transformArm[0]);
            robot.lastMove = 'a';
        }
        if (robot.key[KeyEvent.VK_D] && !robot.notAllow[1]) {
            robot.angles[0] -= Math.PI / robot.robotSpeed;
            robot.transformArm[0].setRotation(new Matrix3d(Math.cos(robot.angles[0]), 0, Math.sin(robot.angles[0]),
                    0, 1, 0,
                    -Math.sin(robot.angles[0]), 0, Math.cos(robot.angles[0])));
            robot.tgArm[0].setTransform(robot.transformArm[0]);
            robot.lastMove = 'd';
        }
        // sterowanie położeniem barku
        if (robot.key[KeyEvent.VK_W] && !robot.notAllow[2]) {
            robot.angles[1] += Math.PI / robot.robotSpeed;
            if (robot.angles[1] > Math.PI / 6) robot.angles[1] = (float) (30 * Math.PI / 180);
            else{
                robot.transformJoint[0].setRotation(new Matrix3d(Math.cos(robot.angles[1]), -Math.sin(robot.angles[1]), 0,
                        Math.sin(robot.angles[1]), Math.cos(robot.angles[1]), 0,
                        0, 0, 1));
                robot.tgJoint[0].setTransform(robot.transformJoint[0]);
            }
            robot.lastMove = 'w';
        }
        if (robot.key[KeyEvent.VK_S] && !robot.notAllow[3]) {
            robot.angles[1] -= Math.PI / robot.robotSpeed;
            if (robot.angles[1] < -Math.PI / 2) robot.angles[1] = (float) (-Math.PI / 2);
            else {
                robot.transformJoint[0].setRotation(new Matrix3d(Math.cos(robot.angles[1]), -Math.sin(robot.angles[1]), 0,
                        Math.sin(robot.angles[1]), Math.cos(robot.angles[1]), 0,
                        0, 0, 1));
                robot.tgJoint[0].setTransform(robot.transformJoint[0]);
            }
            robot.lastMove = 's';
        }
        // sterowanie położeniem łokcia
        if (robot.key[KeyEvent.VK_Q] && !robot.notAllow[4]) {
            robot.angles[2] += Math.PI / robot.robotSpeed;
            if(robot.angles[2]>0) robot.angles[2]=0;
            else {
                robot.transformJoint[1].setRotation(new Matrix3d(Math.cos(robot.angles[2]), -Math.sin(robot.angles[2]), 0,
                        Math.sin(robot.angles[2]), Math.cos(robot.angles[2]), 0,
                        0, 0, 1));
                robot.tgJoint[1].setTransform(robot.transformJoint[1]);
            }
            robot.lastMove = 'q';
        }
        if (robot.key[KeyEvent.VK_E] && !robot.notAllow[5]) {
            robot.angles[2] -= Math.PI / robot.robotSpeed;
            if(robot.angles[2]<-105*Math.PI/180) robot.angles[2]=(float)(-105*Math.PI/180);
            else {
                robot.transformJoint[1].setRotation(new Matrix3d(Math.cos(robot.angles[2]), -Math.sin(robot.angles[2]), 0,
                        Math.sin(robot.angles[2]), Math.cos(robot.angles[2]), 0,
                        0, 0, 1));
                robot.tgJoint[1].setTransform(robot.transformJoint[1]);
            }
            robot.lastMove = 'e';
        }
        // sterowaniem obrotem roll
        if (robot.key[KeyEvent.VK_U] && !robot.notAllow[6]) {
            robot.angles[3] -= Math.PI / robot.robotSpeed;
            robot.transformArm[3].setRotation(new Matrix3d(Math.cos(robot.angles[3]), 0, Math.sin(robot.angles[3]),
                    0, 1, 0,
                    -Math.sin(robot.angles[3]), 0, Math.cos(robot.angles[3])));
            robot.tgArm[3].setTransform(robot.transformArm[3]);
            robot.lastMove = 'u';
        }
        if (robot.key[KeyEvent.VK_O] && !robot.notAllow[7]) {
            robot.angles[3] += Math.PI / robot.robotSpeed;
            robot.transformArm[3].setRotation(new Matrix3d(Math.cos(robot.angles[3]), 0, Math.sin(robot.angles[3]),
                    0, 1, 0,
                    -Math.sin(robot.angles[3]), 0, Math.cos(robot.angles[3])));
            robot.tgArm[3].setTransform(robot.transformArm[3]);
            robot.lastMove = 'o';
        }
        // sterowanie pitch
        if (robot.key[KeyEvent.VK_I] && !robot.notAllow[8]) {
            robot.angles[4] += Math.PI / robot.robotSpeed;
            if(robot.angles[4]>Math.PI/2) robot.angles[4]=(float)(Math.PI/2);
            else {
                robot.transformArm[4].setRotation(new Matrix3d(Math.cos(robot.angles[4]), -Math.sin(robot.angles[4]), 0,
                        Math.sin(robot.angles[4]), Math.cos(robot.angles[4]), 0,
                        0, 0, 1));
                robot.tgArm[4].setTransform(robot.transformArm[4]);
            }
            robot.lastMove = 'i';
        }
        if (robot.key[KeyEvent.VK_K] && !robot.notAllow[9]) {
            robot.angles[4] -= Math.PI / robot.robotSpeed;
            if(robot.angles[4]<-Math.PI/2) robot.angles[4]=(float)(-Math.PI/2);
            else {
                robot.transformArm[4].setRotation(new Matrix3d(Math.cos(robot.angles[4]), -Math.sin(robot.angles[4]), 0,
                        Math.sin(robot.angles[4]), Math.cos(robot.angles[4]), 0,
                        0, 0, 1));
                robot.tgArm[4].setTransform(robot.transformArm[4]);
            }
            robot.lastMove = 'k';
        }
        // sterowanie yaw
        if (robot.key[KeyEvent.VK_J] && !robot.notAllow[10]) {
            robot.angles[5] -= Math.PI / robot.robotSpeed;
            if(robot.angles[5]<-Math.PI/4) robot.angles[5]=(float)(-Math.PI/4);
            if(false){}
            else {
                robot.transformJoint[2].setRotation(new Matrix3d(1, 0, 0,
                        0, Math.cos(robot.angles[5]), -Math.sin(robot.angles[5]),
                        0, Math.sin(robot.angles[5]), Math.cos(robot.angles[5])));
                robot.tgJoint[2].setTransform(robot.transformJoint[2]);
            }
            robot.lastMove = 'j';
        }
        if (robot.key[KeyEvent.VK_L] && !robot.notAllow[11]) {
            robot.angles[5] += Math.PI / robot.robotSpeed;
            if(robot.angles[5]>Math.PI/4)robot.angles[5]=(float)(Math.PI/4);
            if(false){}
            else {
                robot.transformJoint[2].setRotation(new Matrix3d(1, 0, 0,
                        0, Math.cos(robot.angles[5]), -Math.sin(robot.angles[5]),
                        0, Math.sin(robot.angles[5]), Math.cos(robot.angles[5])));
                robot.tgJoint[2].setTransform(robot.transformJoint[2]);
            }
            robot.lastMove = 'l';
        }
    }

    public void playRecord(int n){
        robot.angles[0] = angles[n][0];
        robot.transformArm[0].setRotation(new Matrix3d(Math.cos(robot.angles[0]), 0, Math.sin(robot.angles[0]),
                                                            0, 1, 0,
                                                                -Math.sin(robot.angles[0]), 0, Math.cos(robot.angles[0])));
        robot.tgArm[0].setTransform(robot.transformArm[0]);

        robot.angles[1] = angles[n][1];
        robot.transformJoint[0].setRotation(new Matrix3d(Math.cos(robot.angles[1]), -Math.sin(robot.angles[1]), 0,
                                                         Math.sin(robot.angles[1]), Math.cos(robot.angles[1]), 0,
                                                         0, 0, 1));
        robot.tgJoint[0].setTransform(robot.transformJoint[0]);

        robot.angles[2] = angles[n][2];
        robot.transformJoint[1].setRotation(new Matrix3d(Math.cos(robot.angles[2]), -Math.sin(robot.angles[2]), 0,
                                                         Math.sin(robot.angles[2]), Math.cos(robot.angles[2]), 0,
                                                        0, 0, 1));
        robot.tgJoint[1].setTransform(robot.transformJoint[1]);

        robot.angles[3] = angles[n][3];
        robot.transformArm[3].setRotation(new Matrix3d(Math.cos(robot.angles[3]), 0, Math.sin(robot.angles[3]),
                                                                0, 1, 0,
                                                                -Math.sin(robot.angles[3]), 0, Math.cos(robot.angles[3])));
        robot.tgArm[3].setTransform(robot.transformArm[3]);

        robot.angles[4] = angles[n][4];
        robot.transformArm[4].setRotation(new Matrix3d(Math.cos(robot.angles[4]), -Math.sin(robot.angles[4]), 0,
                                                       Math.sin(robot.angles[4]), Math.cos(robot.angles[4]), 0,
                                                   0, 0, 1));
        robot.tgArm[4].setTransform(robot.transformArm[4]);

        robot.angles[5] = angles[n][5];
        robot.transformJoint[2].setRotation(new Matrix3d(1, 0, 0,
                                                         0, Math.cos(robot.angles[5]), -Math.sin(robot.angles[5]),
                                                         0, Math.sin(robot.angles[5]), Math.cos(robot.angles[5])));
        robot.tgJoint[2].setTransform(robot.transformJoint[2]);
    }
}
