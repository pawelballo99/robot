import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;

import javax.media.j3d.*;
import javax.vecmath.Point3f;

public class MyShapes {

    public Shape3D makeCuboid(float length, float width, float height){
        Point3f a = new Point3f(-length/2, 0.0f, width/2);
        Point3f b = new Point3f(length/2, 0.0f, width/2);
        Point3f c = new Point3f(length/2, 0.0f, -width/2);
        Point3f d = new Point3f(-length/2, 0.0f, -width/2);

        Point3f e = new Point3f(-length/2, height, width/2);
        Point3f f = new Point3f(length/2, height, width/2);
        Point3f g = new Point3f(length/2, height, -width/2);
        Point3f h = new Point3f(-length/2, height, -width/2);

        TriangleArray obj = new TriangleArray(36, TriangleArray.COORDINATES);

        obj.setCoordinate(0, b);
        obj.setCoordinate(1, a);
        obj.setCoordinate(2, d);

        obj.setCoordinate(3, c);
        obj.setCoordinate(4, b);
        obj.setCoordinate(5, d);

        obj.setCoordinate(6, b);
        obj.setCoordinate(7, c);
        obj.setCoordinate(8, f);

        obj.setCoordinate(9, c);
        obj.setCoordinate(10, g);
        obj.setCoordinate(11, f);

        obj.setCoordinate(12, a);
        obj.setCoordinate(13, b);
        obj.setCoordinate(14, e);

        obj.setCoordinate(15, b);
        obj.setCoordinate(16, f);
        obj.setCoordinate(17, e);

        obj.setCoordinate(18, d);
        obj.setCoordinate(19, a);
        obj.setCoordinate(20, e);

        obj.setCoordinate(21, e);
        obj.setCoordinate(22, h);
        obj.setCoordinate(23, d);

        obj.setCoordinate(24, c);
        obj.setCoordinate(25, d);
        obj.setCoordinate(26, g);

        obj.setCoordinate(27, d);
        obj.setCoordinate(28, h);
        obj.setCoordinate(29, g);

        obj.setCoordinate(30, e);
        obj.setCoordinate(31, f);
        obj.setCoordinate(32, g);

        obj.setCoordinate(33, e);
        obj.setCoordinate(34, g);
        obj.setCoordinate(35, h);

        GeometryInfo geoInfo = new GeometryInfo(obj);
        NormalGenerator ng = new NormalGenerator();
        ng.generateNormals(geoInfo);

        GeometryArray result = geoInfo.getGeometryArray();

        return new Shape3D(result);
    }

    public Shape3D makeTriangularShape(float length, float width, float height, float zWidth){
        Point3f a = new Point3f(-length/2, height-height, zWidth/2);
        Point3f b = new Point3f(length/2, height-height, zWidth/2);
        Point3f c = new Point3f(length/2, height-width, zWidth/2);
        Point3f d = new Point3f((length/2-(width)), height, zWidth/2);
        Point3f e = new Point3f(-(length/2-(width)), height, zWidth/2);
        Point3f f = new Point3f(-length/2, height-width, zWidth/2);

        Point3f g = new Point3f(-length/2, height-height, -zWidth/2);
        Point3f h = new Point3f(length/2, height-height, -zWidth/2);
        Point3f i = new Point3f(length/2, height-width, -zWidth/2);
        Point3f j = new Point3f((length/2-(width)), height, -zWidth/2);
        Point3f k = new Point3f(-(length/2-(width)), height, -zWidth/2);
        Point3f l = new Point3f(-length/2, height-width, -zWidth/2);

        TriangleArray obj = new TriangleArray(60, TriangleArray.COORDINATES);

        obj.setCoordinate(0, a);
        obj.setCoordinate(1, e);
        obj.setCoordinate(2, f);

        obj.setCoordinate(3, a);
        obj.setCoordinate(4, d);
        obj.setCoordinate(5, e);

        obj.setCoordinate(6, a);
        obj.setCoordinate(7, c);
        obj.setCoordinate(8, d);

        obj.setCoordinate(9, a);
        obj.setCoordinate(10, b);
        obj.setCoordinate(11, c);

        //

        obj.setCoordinate(12, h);
        obj.setCoordinate(13, j);
        obj.setCoordinate(14, i);

        obj.setCoordinate(15, h);
        obj.setCoordinate(16, k);
        obj.setCoordinate(17, j);

        obj.setCoordinate(18, h);
        obj.setCoordinate(19, l);
        obj.setCoordinate(20, k);

        obj.setCoordinate(21, h);
        obj.setCoordinate(22, g);
        obj.setCoordinate(23, l);
        //
        obj.setCoordinate(24, a);
        obj.setCoordinate(25, g);
        obj.setCoordinate(26, b);

        obj.setCoordinate(27, h);
        obj.setCoordinate(28, b);
        obj.setCoordinate(29, g);
        //
        obj.setCoordinate(30, b);
        obj.setCoordinate(31, h);
        obj.setCoordinate(32, c);

        obj.setCoordinate(33, i);
        obj.setCoordinate(34, c);
        obj.setCoordinate(35, h);
//
        obj.setCoordinate(36, c);
        obj.setCoordinate(37, i);
        obj.setCoordinate(38, d);

        obj.setCoordinate(39, i);
        obj.setCoordinate(40, j);
        obj.setCoordinate(41, d);
//
        obj.setCoordinate(42, d);
        obj.setCoordinate(43, j);
        obj.setCoordinate(44, e);

        obj.setCoordinate(45, j);
        obj.setCoordinate(46, k);
        obj.setCoordinate(47, e);
//
        obj.setCoordinate(48, e);
        obj.setCoordinate(49, k);
        obj.setCoordinate(50, f);

        obj.setCoordinate(51, k);
        obj.setCoordinate(52, l);
        obj.setCoordinate(53, f);
//
        obj.setCoordinate(54, f);
        obj.setCoordinate(55, l);
        obj.setCoordinate(56, a);

        obj.setCoordinate(57, l);
        obj.setCoordinate(58, g);
        obj.setCoordinate(59, a);

        GeometryInfo geoInfo = new GeometryInfo(obj);
        NormalGenerator ng = new NormalGenerator();
        ng.generateNormals(geoInfo);

        GeometryArray result = geoInfo.getGeometryArray();

        return new Shape3D(result);
    }

}
