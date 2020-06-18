import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
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
    private Boolean collisionWithGround;
    private Boolean collisionWithObject;

    public float objX = 1.5f;
    public float objY = 1f;
    public float objZ = 0.0f;
    public Transform3D objectTransform;
    public TransformGroup objectTg;

    World(){
        // tworzenie okienka
        super("Projekt");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        robot = new Robot(this);

        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

        // ustawianie zmiennych
        collisionWithGround = false;
        collisionWithObject = false;

        // tworzenie płótna
        canvas3d = new Canvas3D(config);
        canvas3d.setPreferredSize(new Dimension(800, 600));
        canvas3d.addKeyListener(robot);

        add("Center", canvas3d);
        pack();
        setVisible(true);

        // panel z instrukcją
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

        // obsługa myszy, żeby można było obracać świat
        OrbitBehavior orbit = new OrbitBehavior(canvas3d, OrbitBehavior.REVERSE_ROTATE);
        orbit.setSchedulingBounds(new BoundingSphere());
        simpleU.getViewingPlatform().setViewPlatformBehavior(orbit);

        simpleU.addBranchGraph(getScene());
    }

    private BranchGroup getScene(){
        // branch grooup calej sceny
        BranchGroup scene = new BranchGroup();
        // transformgroup zawierający robota oraz podłoże
        TransformGroup world = new TransformGroup();
        world.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        TransformGroup robotTg;

        // nasz robot pobrany z klasy Robot
        robotTg = robot.getGroup();
        robotTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        world.addChild(robotTg);

        // tworzenie podłoża
        TransformGroup ground = new TransformGroup();
        Shape3D gr = new MyShapes().makeGround();
        gr.setUserData(new String("ground"));
        Appearance appGround = new Appearance();
        appGround.setTexture(createTexture("ground.jpg"));
        gr.setAppearance(appGround);
        CollisionDetector collisionGround = new CollisionDetector(gr, new BoundingSphere(), this, robot);

        ground.addChild(gr);
        world.addChild(collisionGround);
        world.addChild(ground);

        // światła
        Color3f light1Color = new Color3f(1.0f, 1.0f, 1.0f);
        Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        scene.addChild(light1);

        // obiekt do podnoszenia
        Sphere object = new Sphere(0.15f, robot.createAppearance(new Color3f(Color.ORANGE)));
        object.getShape().setUserData(new String("object"));
        objectTransform = new Transform3D();
//        Transform3D objectTransform = new Transform3D();
        objectTransform.setTranslation(new Vector3f(objX, objY, objZ));
        objectTg = new TransformGroup(objectTransform);
        objectTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
//        TransformGroup objectTg = new TransformGroup(objectTransform);
        objectTg.addChild(object);
        world.addChild(objectTg);

        Moving moving = new Moving(robot);
        moving.setSchedulingBounds(bounds);
        world.addChild(moving);

        GameLoop loop = new GameLoop(this);
        loop.setSchedulingBounds(bounds);
        world.addChild(loop);

        CollisionDetector collisionObject = new CollisionDetector(object.getShape(), new BoundingSphere(new Point3d(), 0.15), this, robot);
        world.addChild(collisionObject);

        scene.addChild(world);

        return scene;
    }

    private Texture createTexture(String path) {
        // Załadowanie tekstury
        TextureLoader loader = new TextureLoader(path, null);
        ImageComponent2D image = loader.getImage();

        if (image == null) {
            System.out.println("Nie udało się załadować tekstury: " + path);
        }

        Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight());

//        texture.setMagFilter(Texture.NICEST);
//        texture.setMinFilter(Texture.NICEST);
        texture.setImage(0, image);
        texture.setBoundaryModeS(Texture.WRAP);
        texture.setBoundaryModeT(Texture.WRAP);

        return texture;
    }

    public void setCollisionWithGround(Boolean arg){
        this.collisionWithGround = arg;
    }

    public Boolean getCollisionWithGround(){
        return collisionWithGround;
    }
}
