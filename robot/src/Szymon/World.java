import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import java.awt.*;

public class World extends JFrame {

    private Robot robot;
    private SimpleUniverse simpleU;
    private Canvas3D canvas3d;

    World(){
        super("Projekt");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        robot = new Robot();

        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

        canvas3d = new Canvas3D(config);
        canvas3d.setPreferredSize(new Dimension(800, 600));
        canvas3d.addKeyListener(robot);

        add(canvas3d);
        pack();
        setVisible(true);

        simpleU = new SimpleUniverse(canvas3d);
        Transform3D przesuniecie_obserwatora = new Transform3D();
        przesuniecie_obserwatora.set(new Vector3f(0.0f,0.5f,5.0f));
        simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);
        simpleU.getViewer().getView().setMinimumFrameCycleTime(20);

        simpleU.addBranchGraph(getScene());
    }

    private BranchGroup getScene(){
        BranchGroup scene = new BranchGroup();
        MouseRotate myMouseRotate = new MouseRotate();
        MouseWheelZoom myMouseWheelZoom = new MouseWheelZoom();
        TransformGroup scene_T= new TransformGroup();
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

        myMouseWheelZoom.setTransformGroup(scene_T);
        myMouseRotate.setTransformGroup(scene_T);
        myMouseRotate.setSchedulingBounds(bounds);
        myMouseWheelZoom.setSchedulingBounds(bounds);
        scene_T.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        myMouseRotate.setFactor(0.01d,0.01d);
        scene_T.addChild(myMouseRotate);
        scene_T.addChild(myMouseWheelZoom);
        scene.addChild(scene_T);
        scene_T.addChild(robot.getGroup());

        Color3f light1Color = new Color3f(1.0f, 0.0f, 0.0f);
        Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);

        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        scene.addChild(light1);

        return scene;
    }

}
