import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Robot implements KeyListener {

    private TransformGroup group;
    private TransformGroup[] tgArm;
    private TransformGroup[] tgJoint;
    private Transform3D[] transformArm;
    private Transform3D[] transformJoint;
    private Sphere[] joint;
    private Cylinder[] arm;
    private Transform3D tmp;
    private double angles[];
    private float robotSpeed = 180;

    Robot(){
        group = makeRobot();
    }

    private TransformGroup makeRobot(){
        TransformGroup bg = new TransformGroup();

        // define variables
        angles=new double[6];
        tgArm = new TransformGroup[3];
        tgJoint = new TransformGroup[2];
        transformArm = new Transform3D[3];
        transformJoint = new Transform3D[2];
        arm = new Cylinder[3];
        joint = new Sphere[2];
        tmp = new Transform3D();

        // base
        Cylinder podst = new Cylinder(0.3f, 0.1f);
        Transform3D transformPodst = new Transform3D();
        transformPodst.setTranslation(new Vector3f(0.0f, 0.05f, 0.0f));
        TransformGroup tgPodst = new TransformGroup(transformPodst);
        tgPodst.addChild(podst);
        bg.addChild(tgPodst);

        // arm
        arm[0] = new Cylinder(0.1f, 0.5f);
        transformArm[0] = new Transform3D();
        transformArm[0].setTranslation(new Vector3f(0.0f, 0.25f, 0.0f));
        tgArm[0] = new TransformGroup(transformArm[0]);
        tgArm[0].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgArm[0].addChild(arm[0]);
        bg.addChild(tgArm[0]);

        // joint between arm and shoulder
        joint[0] = new Sphere(0.15f);
        transformJoint[0] = new Transform3D();
        transformJoint[0].setTranslation(new Vector3f(0.0f, 0.35f, 0.0f));
        tmp.rotZ(-Math.PI/4);
        transformJoint[0].mul(tmp);
        tgJoint[0] = new TransformGroup(transformJoint[0]);
        tgJoint[0].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgJoint[0].addChild(joint[0]);
        tgArm[0].addChild(tgJoint[0]);

        // shoulder
        arm[1] = new Cylinder(0.1f, 0.5f);
        transformArm[1] = new Transform3D();
        transformArm[1].setTranslation(new Vector3f(0.0f, 0.35f, 0.0f));
        tgArm[1] = new TransformGroup(transformArm[1]);
        tgArm[1].addChild(arm[1]);
        tgJoint[0].addChild(tgArm[1]);

        // joint between shoulder and elbow
        joint[1] = new Sphere(0.15f);
        transformJoint[1] = new Transform3D();
        transformJoint[1].setTranslation(new Vector3f(0.0f, 0.35f, 0.0f));
        tmp.rotZ(-Math.PI/4);
        transformJoint[1].mul(tmp);
        tgJoint[1] = new TransformGroup(transformJoint[1]);
        tgJoint[1].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgJoint[1].addChild(joint[1]);
        tgArm[1].addChild(tgJoint[1]);

        // elbow
        arm[2] = new Cylinder(0.1f, 0.5f);
        transformArm[2] = new Transform3D();
        transformArm[2].setTranslation(new Vector3f(0.0f, 0.35f, 0.0f));
        tgArm[2] = new TransformGroup(transformArm[2]);
        tgArm[2].addChild(arm[2]);
        tgJoint[1].addChild(tgArm[2]);

        return bg;
    }

    // key manager
    @Override
    public void keyPressed(KeyEvent e) {
        // arm rotation
        if (e.getKeyChar() == 'a') {
            angles[0]+=Math.toDegrees(Math.PI / robotSpeed);
            tmp.rotY(Math.PI / robotSpeed);
            transformArm[0].mul(tmp);
            tgArm[0].setTransform(transformArm[0]);
        }
        if (e.getKeyChar() == 'd') {
            angles[0]-=Math.toDegrees(Math.PI / robotSpeed);
            tmp.rotY(-Math.PI / robotSpeed);
            transformArm[0].mul(tmp);
            tgArm[0].setTransform(transformArm[0]);
        }
        // shoulder rotation
        if (e.getKeyChar() == 'w') {
            angles[1]+=Math.toDegrees(Math.PI / robotSpeed);
            tmp.rotZ(Math.PI / robotSpeed);
            transformJoint[0].mul(tmp);
            tgJoint[0].setTransform(transformJoint[0]);
        }
        if (e.getKeyChar() == 's') {
            angles[1]-=Math.toDegrees(Math.PI / robotSpeed);
            tmp.rotZ(-Math.PI / robotSpeed);
            transformJoint[0].mul(tmp);
            tgJoint[0].setTransform(transformJoint[0]);
        }
        // elbow rotation
        if (e.getKeyChar() == 'q') {
            angles[2]+=Math.toDegrees(Math.PI / robotSpeed);
            tmp.rotZ(Math.PI / robotSpeed);
            transformJoint[1].mul(tmp);
            tgJoint[1].setTransform(transformJoint[1]);
        }
        if (e.getKeyChar() == 'e') {
            angles[2]-=Math.toDegrees(Math.PI / robotSpeed);
            tmp.rotZ(-Math.PI / robotSpeed);
            transformJoint[1].mul(tmp);
            tgJoint[1].setTransform(transformJoint[1]);
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // invoke when key has been released
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // invoke when key has been only typed

    }

    public TransformGroup getGroup(){
        return group;
    }
}
