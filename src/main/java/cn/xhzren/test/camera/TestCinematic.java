package cn.xhzren.test.camera;

import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.Cinematic;
import netscape.security.PrivilegeTable;
import org.apache.ibatis.javassist.bytecode.analysis.Executor;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class TestCinematic extends SimpleApplication {


    @Override
    public void simpleInitApp()
    {
    }

    public static void main(String[] args) throws Exception {
        Callable<Object> c1 = Executors.callable(()-> {
            System.out.println("Executors.callable");
        });
        c1.call();

        Callable<Integer> c2 = Executors.callable(()-> {}, 33);
        c2.call();

        Callable<Object> c3 = Executors.callable(new PrivilegedAction<String>() {
            @Override
            public String run() {
                return "Str";
            }
        });
        c3.call();
    }
}
