import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Robot implements KeyListener {

    // transformgroup całego robota
    private TransformGroup group;
    // kąty każdego ramienia
    public float angles[];
//    private float angles[];
    // transformgroupy każdych części
    public TransformGroup[] tgArm;
//    private TransformGroup[] tgArm;
    public TransformGroup[] tgJoint;
//    private TransformGroup[] tgJoint;
    public Transform3D[] transformArm;
//    private Transform3D[] transformArm;
    public Transform3D[] transformJoint;
//    private Transform3D[] transformJoint;
    // obiekty z których składa się robot
    private Sphere[] joint;
    private Shape3D[] arms;
    private Shape3D[] yawShapes;
    private Shape3D[] pitchShapes;
    private Shape3D[] rollShapes;
    private Cylinder[] yawCylinder;
    private Cylinder[] pitchCylinder;
    private Cylinder[] rollCylinder;
    // pomocna zmienna do zmiany transformacji
    public Transform3D tmp;
//    private Transform3D tmp;
    // predkosc z jaka się obraca ramię - 1/180
    public float robotSpeed = 180;
//    private float robotSpeed = 180;
    private BoundingSphere bounds;
    private World world;

    public char lastMove;
    public Boolean[] key;
    public Boolean[] notAllow;

    Robot(World world){
        group = makeRobot();
        this.world = world;
        key = new Boolean[256];
        notAllow = new Boolean[12];
        for(int i=0; i<256; i++){
            key[i] = false;
        }
        for(int i=0; i<12; i++){
            notAllow[i] = false;
        }
    }

    private TransformGroup makeRobot(){
        TransformGroup bg = new TransformGroup();

        angles = new float[6];
        tgArm = new TransformGroup[6];
        tgJoint = new TransformGroup[3];
        transformArm = new Transform3D[6];
        transformJoint = new Transform3D[3];
        arms = new Shape3D[3];
        yawShapes = new Shape3D[2];
        pitchShapes = new Shape3D[2];
        rollShapes = new Shape3D[2];
        yawCylinder = new Cylinder[2];
        pitchCylinder = new Cylinder[1];
        rollCylinder = new Cylinder[1];
        joint = new Sphere[3];
        tmp = new Transform3D();
        bounds = new BoundingSphere(new Point3d(), 10000.0);

        // podstawa
        Shape3D podst = new MyShapes().makeCustomCylinder(0.4f, 0.35f, 0.15f, 100);
        podst.setAppearance(createAppearance(new Color3f(Color.GRAY)));
        podst.setUserData(new String("podst"));
        Transform3D transformPodst = new Transform3D();
        TransformGroup tgPodst = new TransformGroup(transformPodst);
        tgPodst.addChild(podst);
        bg.addChild(tgPodst);

        // ramię
        // kształty
        arms[0] = new MyShapes().makeCustomCylinder(0.35f, 0.35f, 0.05f, 100);
        arms[0].setAppearance(createAppearance(new Color3f(Color.ORANGE)));
        arms[0].setUserData(new String("arms[0]"));

        Shape3D arm0part0 = new MyShapes().makeCustomCylinder(0.1f, 0.1f, 0.05f, 100);
        arm0part0.setAppearance(createAppearance(new Color3f(Color.ORANGE)));
        arm0part0.setUserData(new String("arm0part0"));

        // ustawienie kształtów aby utworzyły dany obiekt
        Transform3D transformPart0 = new Transform3D();
        transformPart0.setTranslation(new Vector3f(0.0f, -0.05f, 0.0f));
        TransformGroup tgPart0 = new TransformGroup(transformPart0);
        tgPart0.addChild(arm0part0);

        Shape3D arm0part1 = new MyShapes().makeTriangularShape(0.3f, 0.1f, 0.3f, 0.03f);
        arm0part1.setAppearance(createAppearance(new Color3f(Color.ORANGE)));
        arm0part1.setUserData(new String("arm0part1"));
        Shape3D arm0part2 = new MyShapes().makeTriangularShape(0.3f, 0.1f, 0.3f, 0.03f);
        arm0part2.setAppearance(createAppearance(new Color3f(Color.ORANGE)));
        arm0part2.setUserData(new String("arm0part2"));

        Transform3D transformPart1 = new Transform3D();
        transformPart1.setTranslation(new Vector3f(0.0f, 0.05f, 0.25f));
        TransformGroup tgPart1 = new TransformGroup(transformPart1);
        tgPart1.addChild(arm0part1);

        Transform3D transformPart2 = new Transform3D();
        transformPart2.setTranslation(new Vector3f(0.0f, 0.05f, -0.25f));
        TransformGroup tgPart2 = new TransformGroup(transformPart2);
        tgPart2.addChild(arm0part2);

        // początkowy kąt ramienia
        angles[0] = 0;

        transformArm[0] = new Transform3D();
        transformArm[0].setTranslation(new Vector3f(0.0f, 0.2f, 0.0f));
        tgArm[0] = new TransformGroup(transformArm[0]);
        tgArm[0].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgArm[0].addChild(arms[0]);
        tgArm[0].addChild(tgPart0);
        tgArm[0].addChild(tgPart1);
        tgArm[0].addChild(tgPart2);
        bg.addChild(tgArm[0]);

        // połączenie ramienia i barku
        joint[0] = new Sphere(0.25f);
        joint[0].setAppearance(createAppearance(new Color3f(Color.ORANGE)));
        joint[0].setUserData(new String("joint[0]"));

        // ustawienia położenia kształtów
        Shape3D joint0Part0 = new MyShapes().makeCustomCylinder(0.1f, 0.1f, 0.6f, 100);
        joint0Part0.setAppearance(createAppearance(new Color3f(Color.ORANGE)));
        joint0Part0.setUserData(new String("joint0part0"));
        Transform3D rotationJoint0Part0 = new Transform3D();
        rotationJoint0Part0.rotX(Math.PI/2);
        TransformGroup tgRotJoint0Part0 = new TransformGroup(rotationJoint0Part0);
        tgRotJoint0Part0.addChild(joint0Part0);

        Transform3D transformJoint0Part0 = new Transform3D();
        transformJoint0Part0.setTranslation(new Vector3f(0.0f, 0.0f, -0.3f));
        TransformGroup tgJoint0Part0 = new TransformGroup(transformJoint0Part0);
        tgJoint0Part0.addChild(tgRotJoint0Part0);

        // początkowy kąt barku
        angles[1] = (float)(Math.PI/4);

        transformJoint[0] = new Transform3D();
        transformJoint[0].setTranslation(new Vector3f(0.0f, 0.25f, 0.0f));
        tmp.rotZ(-Math.PI/4);
        transformJoint[0].mul(tmp);
        tgJoint[0] = new TransformGroup(transformJoint[0]);
        tgJoint[0].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgJoint[0].addChild(joint[0]);
        tgJoint[0].addChild(tgJoint0Part0);
        tgArm[0].addChild(tgJoint[0]);

        // bark
        arms[1] = new MyShapes().makeCustomCylinder(0.1f, 0.08f, 0.5f, 100);
        arms[1].setAppearance(createAppearance(new Color3f(Color.ORANGE)));
        arms[1].setUserData(new String("arms[1]"));
        transformArm[1] = new Transform3D();
        transformArm[1].setTranslation(new Vector3f(0.0f, 0.2f, 0.0f));
        tgArm[1] = new TransformGroup(transformArm[1]);
        tgArm[1].addChild(arms[1]);
        tgJoint[0].addChild(tgArm[1]);

        // połączenie między barkiem i łokciem ramienia
        joint[1] = new Sphere(0.15f);
        joint[1].setAppearance(createAppearance(new Color3f(Color.ORANGE)));
        joint[1].setUserData(new String("joint[1]"));
        angles[2] = (float)(Math.PI/4);
        transformJoint[1] = new Transform3D();
        transformJoint[1].setTranslation(new Vector3f(0.0f, 0.55f, 0.0f));
        tmp.rotZ(-Math.PI/4);
        transformJoint[1].mul(tmp);
        tgJoint[1] = new TransformGroup(transformJoint[1]);
        tgJoint[1].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgJoint[1].addChild(joint[1]);
        tgArm[1].addChild(tgJoint[1]);

        // łokieć
        arms[2] = new MyShapes().makeCustomCylinder(0.08f, 0.07f, 0.5f, 100);
        arms[2].setAppearance(createAppearance(new Color3f(Color.WHITE)));
        arms[2].setUserData(new String("arms[2]"));
        transformArm[2] = new Transform3D();
        transformArm[2].setTranslation(new Vector3f(0.0f, 0.05f, 0.0f));
        tgArm[2] = new TransformGroup(transformArm[2]);
        tgArm[2].addChild(arms[2]);
        tgJoint[1].addChild(tgArm[2]);

        // roll
        yawCylinder[0] = new Cylinder(0.1f, 0.2f);
        angles[3] = 0;
        yawCylinder[0].setAppearance(createAppearance(new Color3f(Color.ORANGE)));
        yawCylinder[0].setUserData(new String("yawCylinder[0]"));
        yawCylinder[1] = new Cylinder(0.1f, 0.2f);
        yawCylinder[1].setAppearance(createAppearance(new Color3f(Color.ORANGE)));
        yawCylinder[1].setUserData(new String("yawCylinder[1]"));

        yawShapes[0] = new MyShapes().makeTriangularShape(0.15f, 0.05f, 0.2f, 0.01f);
        yawShapes[0].setAppearance(createAppearance(new Color3f(Color.ORANGE)));
        yawShapes[0].setUserData(new String("yawShapes[0]"));
        yawShapes[1] = new MyShapes().makeTriangularShape(0.15f, 0.05f, 0.2f, 0.01f);
        yawShapes[1].setAppearance(createAppearance(new Color3f(Color.ORANGE)));
        yawShapes[1].setUserData(new String("yawShapes[1]"));

        // ustawienia położenia kształtów
        Transform3D yawRotation = new Transform3D();
        yawRotation.rotX(Math.PI/2);
        TransformGroup yawRotTg1 = new TransformGroup(yawRotation);
        yawRotTg1.addChild(yawCylinder[0]);
        Transform3D yawTransform1 = new Transform3D();
        yawTransform1.setTranslation(new Vector3f(0.0f, 0.1f, 0.0f));
        TransformGroup yawTg1 = new TransformGroup(yawTransform1);
        yawTg1.addChild(yawRotTg1);

        Transform3D yawTransform2 = new Transform3D();
        yawTransform2.setTranslation(new Vector3f(0.0f, 0.1f, 0.1f));
        TransformGroup yawTg2 = new TransformGroup(yawTransform2);
        yawTg2.addChild(yawShapes[0]);

        Transform3D yawTransform3 = new Transform3D();
        yawTransform3.setTranslation(new Vector3f(0.0f, 0.1f, -0.1f));
        TransformGroup yawTg3 = new TransformGroup(yawTransform3);
        yawTg3.addChild(yawShapes[1]);

        Transform3D yawTransform4 = new Transform3D();
        yawTransform4.setTranslation(new Vector3f(0.0f, 0.f, 0.0f));
        TransformGroup yawTg4 = new TransformGroup(yawTransform4);
        yawTg4.addChild(yawCylinder[1]);

        transformArm[3] = new Transform3D();
        transformArm[3].setTranslation(new Vector3f(0.0f, 0.6f, 0.0f));
        tgArm[3] = new TransformGroup(transformArm[3]);
        tgArm[3].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgArm[3].addChild(yawTg1);
        tgArm[3].addChild(yawTg2);
        tgArm[3].addChild(yawTg3);
        tgArm[3].addChild(yawTg4);
        tgArm[2].addChild(tgArm[3]);

        // pitch
        angles[4] = 0;
        pitchShapes[0] = new MyShapes().makeCuboid(0.2f, 0.025f, 0.15f);
        pitchShapes[0].setAppearance(createAppearance(new Color3f(Color.ORANGE)));
        pitchShapes[0].setUserData(new String("pitchShapes[0]"));
        pitchShapes[1] = new MyShapes().makeCuboid(0.1f, 0.05f, 0.1f);
        pitchShapes[1].setAppearance(createAppearance(new Color3f(Color.ORANGE)));
        pitchShapes[1].setUserData(new String("pitchShapes[1]"));
        pitchCylinder[0] = new Cylinder(0.05f, 0.2f);
        pitchCylinder[0].setAppearance(createAppearance(new Color3f(Color.ORANGE)));
        pitchCylinder[0].setUserData(new String("pitchCyinder[0]"));

        // ustawienia położenia kształtów
        Transform3D pitchTransform1 = new Transform3D();
        pitchTransform1.rotX(Math.PI/2);
        TransformGroup pitchTg1 = new TransformGroup(pitchTransform1);
        pitchTg1.addChild(pitchCylinder[0]);

        Transform3D pitchTransform2 = new Transform3D();
        pitchTransform2.rotY(Math.PI/2);
        TransformGroup pitchTg2 = new TransformGroup(pitchTransform2);
        pitchTg2.addChild(pitchShapes[0]);

        Transform3D pitchRotation3 = new Transform3D();
        pitchRotation3.rotX(Math.PI/2);
        tmp.rotZ(Math.PI/2);
        pitchRotation3.mul(tmp);
        TransformGroup pitchRotTg = new TransformGroup(pitchRotation3);
        pitchRotTg.addChild(pitchShapes[1]);
        Transform3D pitchTransform3 = new Transform3D();
        pitchTransform3.setTranslation(new Vector3f(0.05f, 0.125f, 0.0f));
        TransformGroup pitchTg3 = new TransformGroup(pitchTransform3);
        pitchTg3.addChild(pitchRotTg);

        transformArm[4] = new Transform3D();
        transformArm[4].setTranslation(new Vector3f(0.0f, 0.25f, 0.0f));
        tgArm[4] = new TransformGroup(transformArm[4]);
        tgArm[4].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgArm[4].addChild(pitchTg1);
        tgArm[4].addChild(pitchTg2);
        tgArm[4].addChild(pitchTg3);
        tgArm[3].addChild(tgArm[4]);

        // yaw
        angles[5] = 0;
        joint[2] = new Sphere(0.06f);
        joint[2].setAppearance(createAppearance(new Color3f(Color.ORANGE)));
        joint[2].setUserData(new String("joint[2]"));
        joint[2].getShape().setUserData("joint[2]");
        transformJoint[2] = new Transform3D();
        transformJoint[2].setTranslation(new Vector3f(0.0f, 0.15f, 0.0f));
        tgJoint[2] = new TransformGroup(transformJoint[2]);
        tgJoint[2].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgJoint[2].addChild(joint[2]);
        tgArm[4].addChild(tgJoint[2]);

        rollCylinder[0] = new Cylinder(0.05f, 0.1f);
        rollCylinder[0].setAppearance(createAppearance(new Color3f(Color.ORANGE)));
        rollCylinder[0].setUserData(new String("rollCylinder[0]"));
        rollCylinder[0].getShape(Cylinder.TOP).setUserData("rollCylinder[0]");
        rollShapes[0] = new MyShapes().makeTriangularShape(0.05f, 0.02f, 0.05f, 0.01f);
        rollShapes[0].setAppearance(createAppearance(new Color3f(Color.ORANGE)));
        rollShapes[0].setUserData(new String("rollShapes[0]"));
        rollShapes[1] = new MyShapes().makeTriangularShape(0.05f, 0.02f, 0.05f, 0.01f);
        rollShapes[1].setAppearance(createAppearance(new Color3f(Color.ORANGE)));
        rollShapes[1].setUserData(new String("rollShapes[1]"));

        // ustawienia położenia kształtów
        Transform3D rollTransform1 = new Transform3D();
        rollTransform1.setTranslation(new Vector3f(0.0f, 0.05f, 0.02f));
        TransformGroup rollTg1 = new TransformGroup(rollTransform1);
        rollTg1.addChild(rollShapes[0]);

        Transform3D rollTransform2 = new Transform3D();
        rollTransform2.setTranslation(new Vector3f(0.0f, 0.05f, -0.02f));
        TransformGroup rollTg2 = new TransformGroup(rollTransform2);
        rollTg2.addChild(rollShapes[1]);

        transformArm[5] = new Transform3D();
        transformArm[5].setTranslation(new Vector3f(0.0f, 0.1f, 0.0f));
        tgArm[5] = new TransformGroup(transformArm[5]);
        tgArm[5].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgArm[5].addChild(rollCylinder[0]);
        tgArm[5].addChild(rollTg1);
        tgArm[5].addChild(rollTg2);

        tgJoint[2].addChild(tgArm[5]);

        return bg;
    }

    // key manager
    @Override
    public void keyPressed(KeyEvent e) {
        // obsługa sterowania robotem
        // sterowanie obrotem ramienia
//        if (e.getKeyChar() == 'a') {
//            angles[0] -= Math.PI / robotSpeed;
//            tmp.rotY(Math.PI / robotSpeed);
//            transformArm[0].mul(tmp);
//            tgArm[0].setTransform(transformArm[0]);
//            this.lastMove = 'a';
//        }
//        if (e.getKeyChar() == 'd') {
//            angles[0] += Math.PI / robotSpeed;
//            tmp.rotY(-Math.PI / robotSpeed);
//            transformArm[0].mul(tmp);
//            tgArm[0].setTransform(transformArm[0]);
//            this.lastMove = 'd';
//        }
//        // sterowanie położeniem barku
//        if (e.getKeyChar() == 'w') {
//            angles[1] -= Math.PI / robotSpeed;
//            if (angles[1] < -Math.PI / 6) angles[1] = (float) (-30 * Math.PI / 180);
//            else{
//                tmp.rotZ(Math.PI / robotSpeed);
//                transformJoint[0].mul(tmp);
//                tgJoint[0].setTransform(transformJoint[0]);
//            }
//            this.lastMove = 'w';
//        }
//        if (e.getKeyChar() == 's') {
//            angles[1] += Math.PI / robotSpeed;
//            if (angles[1] > Math.PI / 2) angles[1] = (float) (Math.PI / 2);
//            else {
//                tmp.rotZ(-Math.PI / robotSpeed);
//                transformJoint[0].mul(tmp);
//                tgJoint[0].setTransform(transformJoint[0]);
//            }
//            this.lastMove = 's';
//        }
//        // sterowanie położeniem łokcia
//        if (e.getKeyChar() == 'q') {
//            angles[2] -= Math.PI / robotSpeed;
//            if(angles[2]<0) angles[2]=0;
//            else {
//                tmp.rotZ(Math.PI / robotSpeed);
//                transformJoint[1].mul(tmp);
//                tgJoint[1].setTransform(transformJoint[1]);
//            }
//            this.lastMove = 'q';
//        }
//        if (e.getKeyChar() == 'e') {
//            angles[2] += Math.PI / robotSpeed;
//            if(angles[2]>105*Math.PI/180) angles[2]=(float)(105*Math.PI/180);
//            else {
//                tmp.rotZ(-Math.PI / robotSpeed);
//                transformJoint[1].mul(tmp);
//                tgJoint[1].setTransform(transformJoint[1]);
//            }
//            this.lastMove = 'e';
//        }
//        // sterowaniem obrotem roll
//        if (e.getKeyChar() == 'u') {
//            angles[3] -= Math.PI / robotSpeed;
//            tmp.rotY(Math.PI / robotSpeed);
//            transformArm[3].mul(tmp);
//            tgArm[3].setTransform(transformArm[3]);
//            this.lastMove = 'u';
//        }
//        if (e.getKeyChar() == 'o') {
//            angles[3] += Math.PI / robotSpeed;
//            tmp.rotY(-Math.PI / robotSpeed);
//            transformArm[3].mul(tmp);
//            tgArm[3].setTransform(transformArm[3]);
//            this.lastMove = 'o';
//        }
//        // sterowanie pitch
//        if (e.getKeyChar() == 'i') {
//            angles[4] -= Math.PI / robotSpeed;
//            if(angles[4]<-Math.PI/2) angles[4]=(float)(-Math.PI/2);
//            else {
//                tmp.rotZ(Math.PI / robotSpeed);
//                transformArm[4].mul(tmp);
//                tgArm[4].setTransform(transformArm[4]);
//            }
//            this.lastMove = 'i';
//        }
//        if (e.getKeyChar() == 'k') {
//            angles[4] += Math.PI / robotSpeed;
//            if(angles[4]>Math.PI/2) angles[4]=(float)(Math.PI/2);
//            else {
//                tmp.rotZ(-Math.PI / robotSpeed);
//                transformArm[4].mul(tmp);
//                tgArm[4].setTransform(transformArm[4]);
//            }
//            this.lastMove = 'k';
//        }
//        // sterowanie yaw
//        if (e.getKeyChar() == 'j') {
//            angles[5] -= Math.PI / robotSpeed;
//            if(angles[5]<-Math.PI/4)angles[5]=(float)(-Math.PI/4);
//            else {
//                tmp.rotX(Math.PI / robotSpeed);
//                transformJoint[2].mul(tmp);
//                tgJoint[2].setTransform(transformJoint[2]);
//            }
//            this.lastMove = 'j';
//        }
//        if (e.getKeyChar() == 'l') {
//            angles[5] += Math.PI / robotSpeed;
//            if(angles[5]>Math.PI/4)angles[5]=(float)(Math.PI/4);
//            else {
//                tmp.rotX(-Math.PI / robotSpeed);
//                transformJoint[2].mul(tmp);
//                tgJoint[2].setTransform(transformJoint[2]);
//            }
//            this.lastMove = 'l';
//        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // invoke when key has been released
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // invoke when key has been only typed

    }

    public Appearance createAppearance(Color3f color){
        // funkcja która zwraca wygląd z kolorem który podamy
        Appearance appearance = new Appearance();
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
        Texture texture = new Texture2D();
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        texture.setBoundaryModeS(Texture.WRAP);
        texture.setBoundaryModeT(Texture.WRAP);
        texture.setBoundaryColor(new Color4f(1.0f, 1.0f, 0.0f, 0.0f));
        Material mat = new Material(color, black, color, white, 70f);
        appearance.setTextureAttributes(texAttr);
        appearance.setMaterial(mat);
        appearance.setTexture(texture);

        return appearance;
    }

    public TransformGroup getGroup(){
        return group;
    }
}
