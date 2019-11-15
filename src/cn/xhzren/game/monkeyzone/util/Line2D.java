package cn.xhzren.game.monkeyzone.util;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import groovyjarjarantlr.collections.impl.Vector;

import java.io.IOException;
/**
 *
 * Line2D represents a line in 2D space. Line data is held as a line segment having two
 * endpoints and as a fictional 3D plane extending verticaly. The Plane is then used for
 * spanning and point clasification tests. A Normal vector is used internally to represent
 * the fictional plane.
 *
 * Portions Copyright (C) Greg Snook, 2000
 * @author TR
 *
 * Line2D代表2D空间中的一条线. 线的数据被保存为具有两个端点的线段
 * 和垂直延伸的虚拟3D平面.然后将平面用于跨度测试和点分类测试.
 * 内部使用法线向量表示虚构平面.
 */
public class Line2D implements Savable {

    enum PointSide {

        /**
         * The point is on, or very near, the line.
         * 该点在直线上或附近.
         */
        OnLine,

        /**
         *looking from endpoint A to B, the test point is on the left.
         * 从端点A到B, 测试点在左侧.
         */
        Left,

        /**
         *looking from endpoint A to B, the test point is on the right.
         * 从端点A到B, 测试点在右侧.
         */
        Right;
    }

    enum LineIntersect {

        /**
         * both lines are parallel and overlap each other.
         * 两条线平行且彼此重叠.
         */
        CoLinear,

        /**
         * lines intersect, but their segments do not.
         * 线相交, 但线段不相交.
         */
        LineIntersect,

        /**
         * both line segments bisect each other.
         * 两个线段彼此平分.
         */
        SegmentsIntersect,

        /**
         * line segment B is crossed by line A.
         * 线段B与线段A相交
         */
        ABisectsB,

        /**
         * line segment A is crossed by line B.
         * 线段A与线段B相交
         */
        BBisectsA,

        /**
         * the lines are paralell.
         * 线是平行的.
         */
        Parallel;

    }

    /**
     * Endpoint A of our line segment.
     * 我们线段的端点A.
     */
    private Vector2f pointA;

    /**
     * Endpoint B of our line segment.
     * 我们线段的端点B.
     */
    private Vector2f pointB;

    /**
     * 'normal' of the ray.
     * a vector pointing to the right-hand side of the line
     * when viewed from PointA towards PointB.
     * 射线的"正常"状态.
     * 从端点A向端点B看时, 指向该线右侧的向量.
     */
    private Vector2f normal;

    public Line2D(Vector2f pointA, Vector2f pointB) {
        this.pointA = pointA;
        this.pointB = pointB;
        normal = null;
    }

    public void setPointA(Vector2f pointA) {
        this.pointA = pointA;
    }

    public void setPointB(Vector2f pointB) {
        this.pointB = pointB;
    }

    public void setPoint(Vector2f pointA, Vector2f pointB) {
        this.pointA = pointA;
        this.pointB = pointB;
        normal = null;
    }

    public Vector2f getNormal() {
        if(normal == null) {
            computeNormal();
        }
        return normal;
    }

    public void setPoints(float pointAX,float pointAY,float pointBX,float pointBY) {
        this.pointA.x = pointAX;
        this.pointA.y = pointAY;
        this.pointB.x = pointBX;
        this.pointB.y = pointBY;
        normal = null;
    }

    public Vector2f getPointA() {
        return pointA;
    }

    public Vector2f getPointB() {
        return pointB;
    }

    public float length() {
        //x距离
        float xdist = pointB.x - pointA.x;
        //y距离
        float ydist = pointB.y = pointA.y;

        //乘于自身
        xdist *= xdist;
        ydist *= ydist;

        //求出两者相加的平方根
        return (float)Math.sqrt(xdist + ydist);
    }

    /**
     * /获取从A到B的归一化方向(向量)
     * @return
     */
    public Vector2f getDirection() {
        return pointB.subtract(pointA).normalizeLocal();
    }

    /**
     * 计算法线
     */
    private void computeNormal() {
        //Get Normailized direction from A to B.
        //获取从A到B的归一化方向
        normal = getDirection();

        //Rotate by -90 degrees to get normal of line.
        //旋转-90度以获得法线.
        float oldY = normal.y;
        normal.y = -normal.y;
        normal.x = oldY;
    }

    /**
     *  Determines the signed distance from a point to this line. Consider the line as
     *  if you were standing on PointA of the line looking towards PointB. Posative distances
     *  are to the right of the line, negative distances are to the left.
     *  确定从点到该线的有符号距离.
     *  考虑这条线, 好像您正站在指向端点B的那条线的端点A上一样.
     *  正距离在直线的右边, 负距离在左边.
     * @param point
     * @return
     */
    public float signedDistance(Vector2f point) {
        if(normal == null) {
            computeNormal();
        }
        return point.subtract(pointA).dot(normal);
    }

    /**
     * Determines where a point lies in relation to this line. Consider the line as
     * if you were standing on PointA of the line looking towards PointB. The incomming
     * point is then classified as being on the Left, Right or Centered on the line.
     * 确定从点相对于此线的位置.
     * 考虑这条线, 好像您正站在指向端点B的那条线的端点A上一样.
     * 然后, 将入局点分类为在线上的左, 右或居中.
     * @param point
     * @param epsilon
     * @return
     */
    public PointSide getSide(Vector2f point, float epsilon) {
        PointSide result = PointSide.OnLine;
        float distance = signedDistance(point);

        if(distance > epsilon) {
            result = PointSide.Right;
        }else if(distance < -epsilon) {
            result = PointSide.Left;
        }
        return result;
    }

    public LineIntersect intersect(Line2D other, Vector2f intersectionPoint) {
        float denom = (other.pointB.y - other.pointA.y) * (this.pointB.x - this.pointA.x)
                - (other.pointB.x - other.pointA.x) * (this.pointB.y - this.pointA.y);

        return null;
    }






    @Override
    public void write(JmeExporter jmeExporter) throws IOException {

    }

    @Override
    public void read(JmeImporter jmeImporter) throws IOException {

    }
}
