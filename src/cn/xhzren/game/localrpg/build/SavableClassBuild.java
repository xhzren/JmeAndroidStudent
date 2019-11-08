package cn.xhzren.game.localrpg.build;

import cn.xhzren.game.localrpg.entity.Life;
import cn.xhzren.game.localrpg.entity.LifeCopy;
import com.jme3.environment.util.LightsDebugState;
import com.jme3.export.Savable;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

public class SavableClassBuild {

    private static StringBuffer read = new StringBuffer();
    private static StringBuffer write = new StringBuffer();
    private static StringBuffer fieldBuffer = new StringBuffer();
    private static final String savalbleClassTemplatePath = "/src/cn/xhzren/game/localrpg/template/SavableTemplate.dt";
    private static final String writeTemplate = "capsule.write(#fieldName#, #fieldStrName#, #value#);\n";
    private static final String readTemplate = "#fieldName# = capsule.read#type##array#(#fieldStrName#, #value#);\n";


    public static void createSavableClass(Class c) throws Exception {
        Field[] fields = c.getDeclaredFields();

        getAllPack(fields);

        File templateFile = new File("");
        String filePath = templateFile.getCanonicalPath();
        templateFile = new File(filePath + savalbleClassTemplatePath);
        File outFile = new File(filePath + "\\src\\" + c.getPackage().getName().replace(".", "\\") + "/" + c.getSimpleName() + "Savable.java");

        BufferedReader bufferedReader = new BufferedReader(new FileReader(templateFile));
        StringBuilder outStr = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中

        String s = "";
        while ((s = bufferedReader.readLine()) != null) {
            outStr.append(s).append("\n");
        }
        bufferedReader.close();
        System.out.println(c.getSimpleName());

        String fileText = outStr.toString().replace("#className#", c.getSimpleName() + "Savable");
        fileText = fileText.replace("#readCapsule#", read.toString());
        fileText = fileText.replace("#writeCapsule#", write.toString());
        fileText = fileText.replace("#fieldBuffer#", fieldBuffer.toString());

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outFile));
        bufferedWriter.write(fileText);

        bufferedWriter.close();

        System.out.println("-------------fileText:");
        System.out.println(fileText);
    }

    public static List<String> getAllPack(Field[] fields) throws Exception {
        StringBuffer importStr = new StringBuffer();
        for (Field field : fields) {
            String type = field.getGenericType().getTypeName();
            String modifier = Modifier.toString(field.getModifiers());
            String simpleType = getType(type);
            if (!type.contains("java.lang")) {
                importStr.append("import ").append(type).append(";\n");
            }
            fieldBuffer.append("\n").append(modifier).append(" ").append(simpleType)
                    .append(" ").append(field.getName()).append(";\n");
            arrayType(field, type);
        }

        System.out.println("-------------importStr:");
        System.out.println(importStr.toString());
        System.out.println("-------------write:");
        System.out.println(write);
        System.out.println("--------------read:");
        System.out.println(read);

        return null;
    }


    public static String arrayType(Field field, String type) {
        String[] one = type.split("\\.");
        String[] two = one[one.length - 1].split("\\[]");

        String writeTemp = writeTemplate.replace("#fieldName#", field.getName());
        writeTemp = writeTemp.replace("#fieldStrName#", "\"" + field.getName() + "\"");

        String readTemp = readTemplate.replace("#fieldName#", field.getName());
        readTemp = readTemp.replace("#fieldStrName#", "\"" + field.getName() + "\"");

        if (type.contains("[]")) {
            readTemp = readTemp.replace("#value#", "null");
            if (!type.contains("[][]")) {
                readTemp = readTemp.replace("#array#", "Array");
            } else {
                readTemp = readTemp.replace("#array#", "Array2D");
            }
            readTemp = readTemp.replace("#type#", unPackType(two[0]));
            writeTemp = writeTemp.replace("#value#", "null");

        }


        if (!type.contains("[]")) {
            readTemp = readTemp.replace("#array#", "");
            readTemp = readTemp.replace("#type#", unPackType(two[0]));
        }

        if (type.contains("String")) {
            writeTemp = writeTemp.replace("#value#", "\"\"");
            readTemp = readTemp.replace("#value#", "\"\"");
        } else if (type.contains("byte") || type.contains("Byte") ||
                type.contains("int") || type.contains("Integer") ||
                type.contains("float") || type.contains("Float") ||
                type.contains("double") || type.contains("Double") ||
                type.contains("long") || type.contains("Long")) {
            writeTemp = writeTemp.replace("#value#", "0");
            readTemp = readTemp.replace("#value#", "0");
        }

        write.append(writeTemp);
        read.append(readTemp);

        return "";
    }

    public static String getType(String type) {
        String[] one = type.split("\\.");
        return one[one.length - 1];
    }

    public static String unPackType(String str) {
        if (str != null && str != "") {
            str = str.substring(0, 1).toUpperCase() + str.substring(1);
        }
        return str;
    }

//    public static String aloneType(Field field, String type) {
//        String writeTemp = writeTemplate.replace("#fieldName#", field.getName());
//        writeTemp = readTemplate.replace("#fieldStrName#", "\"" + field.getName() + "\"");
//
//        String readTemp = readTemplate.replace("#fieldName#", field.getName());
//        readTemp = readTemplate.replace("#fieldStrName#", "\"" + field.getName() + "\"");
//        return writeTemp;
//    }

    public static void main(String[] args) throws Exception {
        createSavableClass(LifeCopy.class);

    }
}
