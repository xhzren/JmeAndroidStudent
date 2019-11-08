package cn.xhzren.game.localrpg.build;

import cn.xhzren.game.localrpg.entity.Life;
import com.jme3.environment.util.LightsDebugState;

import java.lang.reflect.Field;
import java.util.List;

public class SavableClassBuild {

    private static StringBuffer read = new StringBuffer();
    private static StringBuffer write = new StringBuffer();
    private static final String writeTemplate = "capsule.write(#fieldName#, #fieldStrName#, #value#);\n";
    private static final String readTemplate = "\t#fieldName# = capsule.read#type##array#(#fieldStrName#, #value#);\n";


    public static void createSavableClass(Class c) {
        Field[] fields = c.getDeclaredFields();

        getAllPack(fields);
    }

    public static List<String> getAllPack(Field[] fields) {
        StringBuffer importStr = new StringBuffer();
        for (Field field : fields) {
            String type = field.getGenericType().getTypeName();
            if(!type.contains("java.lang")) {
                importStr.append("import ").append(type).append(";\n");
            }

            if(type.contains("[]")) {
//                arrayType(field, type);
            }else {
//                aloneType(field, type);
            }

        }
        System.out.println(importStr.toString());

        return null;
    }

    public static String array2DType(Field field, String type) {
        String temp = aloneType(field, type);
        temp = readTemplate.replace("#value#", null);
        return temp;
    }

    public static String arrayType(Field field, String type) {
        String[] one = type.split("\\.");
        String[] two = one[one.length-1].split("\\[]");

        String writeTemp = writeTemplate.replace("#fieldName#", field.getName());
        writeTemp = readTemplate.replace("#fieldStrName#", "\"" + field.getName() + "\"");

        String readTemp = readTemplate.replace("#fieldName#", field.getName());
        readTemp = readTemplate.replace("#fieldStrName#", "\"" + field.getName() + "\"");

        if(type.contains("[]")) {
            readTemp = readTemplate.replace("#value#", "null");
            readTemp = readTemplate.replace("#array#", "Array");
            readTemp = readTemplate.replace("#type#", two[0]);
            writeTemp = readTemplate.replace("#value#", "null");

        }
        if(type.contains("[][]")) {
            readTemp = readTemplate.replace("#array#", "Array2D");
          }

        if(type.contains("String")) {
            writeTemp = readTemplate.replace("#value#", "\"\"");
        }else if(type.contains("")) {

        }

        return "";
    }

    public static String aloneType(Field field, String type) {
        String writeTemp = writeTemplate.replace("#fieldName#", field.getName());
        writeTemp = readTemplate.replace("#fieldStrName#", "\"" + field.getName() + "\"");

        String readTemp = readTemplate.replace("#fieldName#", field.getName());
        readTemp = readTemplate.replace("#fieldStrName#", "\"" + field.getName() + "\"");
        return writeTemp;
    }

    public static void main(String[] args) {
        createSavableClass(Life.class);
    }
}
