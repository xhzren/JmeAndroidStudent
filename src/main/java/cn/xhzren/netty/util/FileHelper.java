package cn.xhzren.netty.util;

import com.sun.javafx.tk.FileChooserType;

import java.io.File;
import java.time.LocalDateTime;

public class FileHelper {

    private static String root = "D:\\upload\\ATMP\\";

    public static File makeTmpFile(String ext) {
        File file = new File(root+ System.currentTimeMillis() + "." + ext);
        try {
            file.createNewFile();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void getKeyMapping() {
         }

    public static void main(String[] args) throws Exception {

    }
}
