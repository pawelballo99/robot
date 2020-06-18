import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;
import java.awt.*;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class Ksztalty extends JFrame {

    Ksztalty(){
        super("Ksztalty");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3d = new Canvas3D(config);
        canvas3d.setPreferredSize(new Dimension(800, 600));

        add(canvas3d);
        pack();
        setVisible(true);

        SimpleUniverse simpleU = new SimpleUniverse(canvas3d);
        simpleU.getViewingPlatform().setNominalViewingTransform();
        simpleU.addBranchGraph(getScene());
    }

    public BranchGroup getScene(){
        BranchGroup scene = new BranchGroup();

        float smallerRadius = 0.1f;
        float height = 0.4f;
        float biggerRadius = 0.2f;
        int n = 200;

        // make points
        Point3f[] points = new Point3f[n];

        for(int i=0; i<n/2; i++){
            points[i] = new Point3f(new Vector3f((float)(biggerRadius * Math.cos(Math.PI * 2 * i / (n/2))),
                    0, (float)(biggerRadius * Math.sin(Math.PI * 2 * i / (n/2)))));
        }

        for(int i=n/2; i<n; i++){
            points[i] = new Point3f(new Vector3f((float)(smallerRadius * Math.cos(Math.PI * 2 * i / (n/2))),
                    height, (float)(smallerRadius * Math.sin(Math.PI * 2 * i / (n/2)))));
        }

        // set coordinates for these points ^
        // n/2 - 2: triangles for lower circle, n/2 - 2: triangles for upper circle and n: triangles for wall

        int coordinates = (n/2 - 2 + n/2 - 2 + n) * 3;
        TriangleArray obj = new TriangleArray(coordinates, TriangleArray.COORDINATES);

        // lower circle
        int count = 0;
        for(int i=0; i<(n/2-2)*3; i+=3){
            obj.setCoordinate(i, points[0]);
            obj.setCoordinate(i + 1, points[count+1]);
            obj.setCoordinate(i + 2, points[count+2]);
            count++;
        }

        // bigger circle
        count = n/2;
        for(int i=(n/2-2)*3; i<(n-4)*3; i+=3){
            obj.setCoordinate(i, points[n/2]);
            obj.setCoordinate(i + 1, points[count+2]);
            obj.setCoordinate(i + 2, points[count+1]);
            count++;
        }

        count = 0;
        for(int i=(n-4)*3; i<coordinates; i+=6){
            obj.setCoordinate(i, points[count]);
            if(count<(n/2)-1)
                obj.setCoordinate(i+1, points[count + (n/2) + 1]);
            else
                obj.setCoordinate(i+1, points[count + 1]);
            if(count<(n/2)-1)
                obj.setCoordinate(i+2, points[count + 1]);
            else
                obj.setCoordinate(i+2, points[0]);

            obj.setCoordinate(i+3, points[count]);
            obj.setCoordinate(i+4, points[count + (n/2)]);
            if(count<(n/2)-1)
                obj.setCoordinate(i+5, points[count + (n/2) + 1]);
            else
                obj.setCoordinate(i+5, points[n/2]);
            count++;
        }

        GeometryInfo geoInfo = new GeometryInfo(obj);
        NormalGenerator ng = new NormalGenerator();
        ng.generateNormals(geoInfo);

        GeometryArray result = geoInfo.getGeometryArray();


        Appearance appearance = new Appearance();
        Color3f color = new Color3f(Color.orange);
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

        Shape3D shape = new Shape3D(result, appearance);

        Transform3D tr = new Transform3D();
        tr.rotX(Math.PI*2);
        TransformGroup tg = new TransformGroup(tr);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.addChild(shape);
        scene.addChild(tg);

        Color3f light1Color = new Color3f(1.0f, 1.0f, 1.0f);
        Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);
        light1.setInfluencingBounds(bounds);
        scene.addChild(light1);

        Color3f ambientColor = new Color3f(.0f, .0f, .0f);
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(bounds);
        scene.addChild(ambientLightNode);

        MouseRotate myMouseRotate = new MouseRotate();
        myMouseRotate.setTransformGroup(tg);
        myMouseRotate.setSchedulingBounds(new BoundingSphere());
        scene.addChild(myMouseRotate);

        return scene;
    }

    public static void main(String[] args) {
        new Ksztalty();
    }
}
