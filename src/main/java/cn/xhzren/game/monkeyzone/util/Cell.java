package cn.xhzren.game.monkeyzone.util;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.math.Plane;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

import java.io.IOException;

public class Cell implements Savable {

    static final int VERT_A = 0;
    static final int VERT_B = 1;
    static final int VERT_C = 2;
    static final int SIDE_AB = 0;
    static final int SIDE_BC = 1;
    static final int SIDE_CA = 2;

    /**
     * 路径结果
     */
    enum PathResult {
        /**
         * The path does not cross this cell.
         * 路径未穿过此单元格.
         */
        NoRelationship,

        /**
         * The path ends in this cell.
         * 路径在此单元格结束.
         */
        EndingCell,

        /**
         *  The path exits this cell through side X.
         *路径通过X面离开此单元格.
         */
        ExitingCell;
    }

    /**
     * 归类结果
     */
    class ClassifyResult {
        PathResult result = PathResult.NoRelationship;
        int side = 0;
        Cell cell = null;
        //路口
        Vector2f intersection = new Vector2f();

        @Override
        public String toString() {
            return result.toString()+" " + cell;
        }
    }

    /**
     * A plane containing the cell triangle.
     * 包含单元三角形的平面.
     */
    private Plane cellPlane = new Plane();

    /**
     * pointers to the verticies of this triangle held in the
     * NavigationMesh's vertex pool.
     * 导航网格顶点池中保存的指向该三角形顶点的指针.
     */
    private Vector3f[] verticies = new Vector3f[3];

    /**
     * The center of the triangle.
     * 三角形的中心.
     */
    private Vector3f center = new Vector3f();

//    private Line

    @Override
    public void write(JmeExporter jmeExporter) throws IOException {

    }

    @Override
    public void read(JmeImporter jmeImporter) throws IOException {

    }
}
