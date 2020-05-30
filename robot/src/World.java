import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.util.concurrent.Flow;

public class World extends JFrame {

    private Robot robot;
    private SimpleUniverse simpleU;
    private Canvas3D canvas3d;

    World(){
        super("Projekt");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        robot = new Robot();

        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

        canvas3d = new Canvas3D(config);
        canvas3d.setPreferredSize(new Dimension(800, 600));
        canvas3d.addKeyListener(robot);

        add("Center", canvas3d);
        pack();
        setVisible(true);

        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(130, 200));
        p.setLayout(new FlowLayout());

        p.add(new Label("Instrukcja:"));
        p.add(new Label("a, d - Arm sweep"));
        p.add(new Label("w, s - Shoulder swivel"));
        p.add(new Label("q, e - Elbow"));
        p.add(new Label("j, l - Yaw"));
        p.add(new Label("i, k - Pitch"));
        p.add(new Label("u, o - Roll"));

        add("East", p);

        simpleU = new SimpleUniverse(canvas3d);

        Transform3D przesuniecie_obserwatora = new Transform3D();
        przesuniecie_obserwatora.set(new Vector3f(0.0f,0.5f,5.0f));
        simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);
        simpleU.getViewer().getView().setMinimumFrameCycleTime(20);

        simpleU.addBranchGraph(getScene());
    }

    private BranchGroup getScene(){
        BranchGroup scene = new BranchGroup();
        TransformGroup world = new TransformGroup();
        world.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        TransformGroup robotTg;

        robotTg = robot.getGroup();
        robotTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        world.addChild(robotTg);

        TransformGroup ground = new TransformGroup();
        Shape3D gr = new MyShapes().makeCuboid(10.0f, 10.0f, 0.0f);
        gr.setAppearance(robot.createAppearance(new Color3f(Color.WHITE)));

        ground.addChild(gr);
        world.addChild(ground);

        Color3f light1Color = new Color3f(1.0f, 1.0f, 1.0f);
        Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        scene.addChild(light1);

        MouseRotate myMouseRotate = new MouseRotate();
        myMouseRotate.setTransformGroup(world);
        myMouseRotate.setSchedulingBounds(bounds);
        myMouseRotate.setFactor(0.01d,0.01d);
        world.addChild(myMouseRotate);

        MouseWheelZoom myMouseWheelZoom = new MouseWheelZoom();
        myMouseWheelZoom.setTransformGroup(world);
        myMouseWheelZoom.setSchedulingBounds(bounds);
        world.addChild(myMouseWheelZoom);

        scene.addChild(world);

        return scene;
    }

    private Texture createTexture(String sciezka) {
        // Załadowanie tekstury
        TextureLoader loader = new TextureLoader(sciezka, null);
        ImageComponent2D image = loader.getImage();

        if (image == null) {
            System.out.println("Nie udało się załadować tekstury: " + sciezka);
        }

        // can't use parameterless constuctor
        Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight());
        texture.setMagFilter(Texture.NICEST);
        texture.setMinFilter(Texture.NICEST);
        texture.setImage(0, image);

        return texture;
    }

}
