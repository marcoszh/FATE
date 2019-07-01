package com.webank.ai.fate.board.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.*;

public class ReadJson {
    private ReadJson() {
    }

    public static String readJsonFile(String filename) {
        //定义接收可变字符串和需要读取的文件
        StringBuilder strFile = new StringBuilder();
        File jsonFile = new File(filename);

        //读取文件信息
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileInputStream = new FileInputStream(jsonFile);
            inputStreamReader = new InputStreamReader(fileInputStream, "GBK");
            bufferedReader = new BufferedReader(inputStreamReader);
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                strFile.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return strFile.toString();
    }
}
