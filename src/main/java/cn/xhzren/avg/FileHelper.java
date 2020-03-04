package cn.xhzren.avg;

import cn.xhzren.avg.entity.ArchiveRecords;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class FileHelper {

    public static void writeRecords(ArchiveRecords archiveRecords,int type) {
        List<ArchiveRecords> recordData = JSONArray.parseArray(readJsonData(Constant.recordPath), ArchiveRecords.class);
        if(recordData.size() == Constant.recordCount) {
            return;
        }

        if(type == 1) {
            if(recordData.size() == 0) {
                archiveRecords.setRecordIndex(0);
            }else {
                archiveRecords.setRecordIndex(recordData.size()+1);
            }
        }
        recordData.add(archiveRecords);
        writeJsonData(JSON.toJSONString(recordData), Constant.recordPath);
    }

    public static String readJsonData(String chapterName) {
        BufferedReader reader = null;
        StringBuilder jsonStr = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(chapterName));
            reader.lines().forEach((e)-> {
                jsonStr.append(e);
            });
        }catch (IOException e) {
            e.printStackTrace();
        }
        return jsonStr.toString();
    }

    public static void writeJsonData(String data, String recordPath) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(recordPath));
            writer.write(data);
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getArchiveCover() {
        return sortByDate(Constant.screenShot);
    }


    // Sort by date.
    private static String sortByDate(String dir) {
        File file = new File(dir);
        File[] fs = file.listFiles();
        if(!(fs.length > 0)) {
            return "";
        }
        Arrays.sort(fs, (File f1, File f2) -> {
            long diff = f1.lastModified() - f2.lastModified();
            if (diff > 0) {
                return -1;
            } else if (diff == 0) {
                return 0;
            } else {
                return 1;
            }
        });
        return "Textures/Avg/save/"+fs[0].getName();
    }
}
