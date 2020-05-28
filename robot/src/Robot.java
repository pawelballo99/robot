import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.media.j3d.Transform3D;
import javax.swing.*;
import javax.vecmath.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

public class Robot extends JFrame {

    private Timer clock;
    private double angles[];
    private BoundingSphere bounds;

    private Appearance create_appearance(Color3f color){
        Appearance ap=new Appearance();
        Material mat=new Material();
        mat.setDiffuseColor(color);
        ap.setMaterial(mat);
        return ap;
    }

    Robot(){
        super("ROBOT");
        bounds=new BoundingSphere(new Point3d(0.0f,0.0f,0.0f),100.0d);

        angles= new double[6];
        angles[0]=0.0*Math.PI/180;
        angles[1]=30*Math.PI/180;
        angles[2]=80.0*Math.PI/180;
        angles[3]=60.0*Math.PI/180;
        angles[4]=30.0*Math.PI/180;
        angles[5]=45.0*Math.PI/180;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        GraphicsConfiguration config =
                SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        canvas3D.setPreferredSize(new Dimension(800,600));
        add(canvas3D);
        pack();
        setVisible(true);
        BranchGroup scene = CreateScene();
        scene.compile();
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);
        Transform3D obserwator = new Transform3D();
        obserwator.set(new Vector3f(0.0f,0.7f,13.0f));
        simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(obserwator);
        simpleU.addBranchGraph(scene);


    }

    BranchGroup CreateScene(){
        Alpha alpha= new Alpha(-1,6000);
        BranchGroup node_scene=new BranchGroup();

        TransformGroup robot = new TransformGroup();
        robot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        RotationInterpolator rot = new RotationInterpolator(alpha,robot);
        rot.setMinimumAngle(0);
        rot.setMaximumAngle(6.28f);
        rot.setSchedulingBounds(bounds);
        robot.addChild(rot);

        TransformGroup arm = new TransformGroup();
        Transform3D arm_t = new Transform3D();
        arm_t.setScale(1.5);
        arm_t.setRotation(new Matrix3d(Math.cos(angles[0]),0,Math.sin(angles[0]),0,1,0,-Math.sin(angles[0]),0,Math.cos(angles[0])));
        arm.setTransform(arm_t);

        TransformGroup shoulder = new TransformGroup();
        shoulder.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D shoulder_t = new Transform3D();
        shoulder_t.set(new Vector3f(0.0f,1.0f,0.0f));
        shoulder_t.setRotation(new Matrix3d(Math.cos(angles[1]),-Math.sin(angles[1]),0,Math.sin(angles[1]),Math.cos(angles[1]),0,0,0,1));
        shoulder.setTransform(shoulder_t);

        TransformGroup elbow = new TransformGroup();
        elbow.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D elbow_t = new Transform3D();
        elbow_t.set(new Vector3f(0.0f,1.0f,0.0f));
        elbow_t.setRotation(new Matrix3d(Math.cos(angles[2]),-Math.sin(angles[2]),0,Math.sin(angles[2]),Math.cos(angles[2]),0,0,0,1));
        elbow.setTransform(elbow_t);

        TransformGroup pitch = new TransformGroup();
        pitch.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D pitch_t = new Transform3D();
        pitch_t.set(new Vector3f(0.0f,1.0f,0.0f));
        pitch_t.setRotation(new Matrix3d(Math.cos(angles[3]),-Math.sin(angles[3]),0,Math.sin(angles[3]),Math.cos(angles[3]),0,0,0,1));
        pitch.setTransform(pitch_t);

        TransformGroup yaw= new TransformGroup();
        yaw.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D yaw_t = new Transform3D();
        yaw_t.set(new Vector3f(0.0f,0.1f,0.0f));
        yaw_t.setRotation(new Matrix3d(1,0,0,0,Math.cos(angles[4]),-Math.sin(angles[4]),0,Math.sin(angles[4]),Math.cos(angles[4])));
        yaw.setTransform(yaw_t);

        TransformGroup roll=new TransformGroup();
        roll.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D roll_t = new Transform3D();
        roll_t.set(new Vector3f(0.0f,0.07f,0.0f));
        roll_t.setRotation(new Matrix3d(Math.cos(angles[5]),0,Math.sin(angles[5]),0,1,0,-Math.sin(angles[5]),0,Math.cos(angles[5])));
        roll.setTransform(roll_t);


        yaw.addChild(roll);
        pitch.addChild(yaw);
        elbow.addChild(pitch);
        shoulder.addChild(elbow);
        arm.addChild(shoulder);
        robot.addChild(arm);
        node_scene.addChild(robot);

        //ARM
        Cylinder cylinder1=new Cylinder(0.1f,1.0f,create_appearance(new Color3f(0.08f,0.14f,0.93f)));
        TransformGroup arm_cylinder=new TransformGroup();
        Transform3D arm_cylinder_t= new Transform3D();
        arm_cylinder_t.set(new Vector3f(0.0f,0.5f,0.0f));
        arm_cylinder.setTransform(arm_cylinder_t);
        arm_cylinder.addChild(cylinder1);

        arm.addChild(arm_cylinder);

        //SHOULDER
        Cylinder cylinder2=new Cylinder(0.1f,1.0f,create_appearance(new Color3f(0.93f,0.08f,0.14f)));
        TransformGroup shoulder_cylinder=new TransformGroup();
        Transform3D shoulder_cylinder_t= new Transform3D();
        shoulder_cylinder_t.set(new Vector3f(0.0f,0.5f,0.0f));
        shoulder_cylinder.setTransform(shoulder_cylinder_t);
        shoulder_cylinder.addChild(cylinder2);

        shoulder.addChild(shoulder_cylinder);

        //ELBOW
        Cylinder cylinder3=new Cylinder(0.1f,1.0f,create_appearance(new Color3f(0.07f,0.66f,0.07f)));
        TransformGroup elbow_cylinder=new TransformGroup();
        Transform3D elbow_cylinder_t= new Transform3D();
        elbow_cylinder_t.set(new Vector3f(0.0f,0.5f,0.0f));
        elbow_cylinder.setTransform(elbow_cylinder_t);
        elbow_cylinder.addChild(cylinder3);

        elbow.addChild(elbow_cylinder);

        //PITCH
        Sphere sphere1 = new Sphere(0.13f,create_appearance(new Color3f(0.22f,0.47f,0.53f)));
        TransformGroup pitch_sphere=new TransformGroup();
        Transform3D pitch_sphere_t= new Transform3D();
        pitch_sphere_t.set(new Vector3f(0.0f,0.0f,0.0f));
        pitch_sphere.setTransform(pitch_sphere_t);
        pitch_sphere.addChild(sphere1);

        pitch.addChild(pitch_sphere);

        //YAW
        Sphere sphere2 = new Sphere(0.13f,create_appearance(new Color3f(0.94f,0.57f,0.1f)));
        TransformGroup yaw_sphere=new TransformGroup();
        Transform3D yaw_sphere_t= new Transform3D();
        yaw_sphere_t.set(new Vector3f(0.0f,0.0f,0.0f));
        yaw_sphere.setTransform(yaw_sphere_t);
        yaw_sphere.addChild(sphere2);

        yaw.addChild(yaw_sphere);

        //ROLL
        Cylinder finger1=new Cylinder(0.02f,0.19f,create_appearance(new Color3f(0.93f,0.08f,0.14f)));
        Cylinder finger2=new Cylinder(0.02f,0.19f,create_appearance(new Color3f(0.93f,0.08f,0.14f)));

        TransformGroup roll_finger1=new TransformGroup();
        TransformGroup roll_finger2=new TransformGroup();
        Transform3D roll_finger1_t= new Transform3D();
        Transform3D roll_finger2_t= new Transform3D();
        roll_finger1_t.set(new Vector3f(0.085f,0.095f,0.0f));
        roll_finger2_t.set(new Vector3f(-0.085f,0.095f,0.0f));
        roll_finger1.setTransform(roll_finger1_t);
        roll_finger2.setTransform(roll_finger2_t);
        roll_finger1.addChild(finger1);
        roll_finger2.addChild(finger2);

        roll.addChild(roll_finger1);
        roll.addChild(roll_finger2);

        //JOINTS
        Sphere shoulder_joint = new Sphere(0.14f,create_appearance(new Color3f(0.94f,0.97f,0.01f)));
        Sphere elbow_joint = new Sphere(0.14f,create_appearance(new Color3f(0.94f,0.97f,0.01f)));
        shoulder.addChild(shoulder_joint);
        elbow.addChild(elbow_joint);

        //LIGHTS
        DirectionalLight light=new DirectionalLight();
        light.setInfluencingBounds(bounds);
        node_scene.addChild(light);

        return node_scene;
    }

    public static void main(String args[]){
        new Robot();
    }

}
