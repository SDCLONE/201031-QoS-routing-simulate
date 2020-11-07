package com.bupt.qos.iqrrl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class QosIqrrlService {
    /**
     * Linux调用逻辑
     * 1.首先
     */

    private final String IQRRL_DIRECTORY_PATH = "/home/baobao/ns-allinone-2.35/ns-2.35/iqrrl/";
    private final String WIRELESS_IQRRL_TCL_FILENAME = "wireless-iqrrl.tcl";
    private final String IQRRLTRACE_FILENAME = "iqrrltrace.tr";

    private final String WIRELESS_IQRRL_TCL_PATH = IQRRL_DIRECTORY_PATH + WIRELESS_IQRRL_TCL_FILENAME;
    private final String IQRRLTRACE_PATH = IQRRL_DIRECTORY_PATH + IQRRLTRACE_FILENAME;


    //todo 需要和学姐调试好怎么生成ns2  iqrrl的awk分析文件
    private final String AWK_DIRECTORY_PATH = "/home/baobao/Desktop/2020.09.09-differation-service-data-analysis/";
    private final String IQRRL_DELAY_AWK_FILENAME = "iqrrl-delay.awk";
    private final String IQRRL_THROUGHPUT_AWK_FILENAME = "iqrrl-throughput.awk";
    private final String IQRRL_DELAY_FILENAME = "iqrrl-delay";
    private final String IQRRL_THROUGHPUT_FILENAME = "iqrrl-throughput";

    private final String IQRRL_DELAY_AWK_PATH = AWK_DIRECTORY_PATH + IQRRL_DELAY_AWK_FILENAME;
    private final String IQRRL_THROUGHPUT_AWK_PATH = AWK_DIRECTORY_PATH + IQRRL_THROUGHPUT_AWK_FILENAME;
    private final String IQRRL_DELAY_PATH = AWK_DIRECTORY_PATH + IQRRL_DELAY_FILENAME;
    private final String IQRRL_THROUGHPUT_PATH = AWK_DIRECTORY_PATH + IQRRL_THROUGHPUT_FILENAME;




    public void analyzeIqrrlTrace() {
        //首先生成iqrrltrace.tr文件
        generateIqrrlTraceFile();
        //再生成awk分析文件
        generateIqrrlAwkFiles();
        //最后进行分析
        log.info("-------------------开始进行iqrrl文件的分析------------------------");
    }

    public void generateIqrrlAwkFiles() {
        File iqrrlDelayFile = new File(IQRRL_DELAY_PATH);
        File iqrrlThroughputFile = new File(IQRRL_THROUGHPUT_PATH);
        List<String[]> awkPathList = new ArrayList<>();
        //不存在就加入list
        if (!iqrrlDelayFile.exists()) {
            awkPathList.add(new String[]{IQRRL_DELAY_AWK_FILENAME, IQRRL_DELAY_FILENAME});
        }
        if (!iqrrlThroughputFile.exists()) {
            awkPathList.add(new String[]{IQRRL_THROUGHPUT_AWK_FILENAME, IQRRL_THROUGHPUT_FILENAME});
        }
        //根据list生成linux指令
        StringBuilder shellCommand = new StringBuilder();

        if (awkPathList.size() == 0) {  //如果没有命令直接结束
            log.info(">>>>>>>>>>iqrrl: delay和throughput都存在<<<<<<<<<<");
            return;
        } else {    //如果有就生成命令
            log.info(">>>>>>>>>>iqrrl: delay和throughput有不存在的<<<<<<<<<<");
            shellCommand.append("cd ").append(AWK_DIRECTORY_PATH);
            for (String[] strings : awkPathList) {
                shellCommand.append(" && gawk -f ").append(strings[0]).append(" ").append(IQRRLTRACE_PATH).append(" > ").append(strings[1]);
            }
        }
        System.out.println(shellCommand.toString());

        //调用命令行生成awk分析文件
        try {
            String[] arg1 = new String[]{"/bin/sh", "-c", shellCommand.toString()};
            Process pr = Runtime.getRuntime().exec(arg1);

            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream(), "gbk"));
            String line;
            //读取python中print的值
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            int r2 = pr.waitFor();
            System.out.println("ending" + r2);
            log.info("生成awk分析文件完毕");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void generateIqrrlTraceFile() {
        File file = new File(IQRRLTRACE_PATH);
        if (!file.exists()) {
            log.info(">>>>>>>>>>iqrrltrace文件不存在，需要重新生成<<<<<<<<<<");
            try {
                String[] arg1 = new String[]{"/bin/sh", "-c", "cd " + IQRRL_DIRECTORY_PATH + " && " + "ns " + WIRELESS_IQRRL_TCL_FILENAME};
                Process pr = Runtime.getRuntime().exec(arg1);
                BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream(), "gbk"));
                String line;
                //读取python中print的值
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
                in.close();
                int r2 = pr.waitFor();
                System.out.println("ending" + r2);
                log.info("生成iqrrltrace.tr文件完毕");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            log.info(">>>>>>>>>>iqrrltrace已经存在，无需生成<<<<<<<<<<");
        }

    }

    public void clearGeneratedFiles() {
        String shellCommand =
                "rm -f " + IQRRLTRACE_PATH +
                        " && rm -f " + IQRRL_DELAY_PATH +
                        " && rm -f " + IQRRL_THROUGHPUT_PATH;
//        System.out.println(shellCommand);
        //调用命令行清除所有生成文件
        try {
            String[] arg1 = new String[]{"/bin/sh", "-c", shellCommand};
            Process pr = Runtime.getRuntime().exec(arg1);

            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream(), "gbk"));
            String line;
            //读取python中print的值
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            int r2 = pr.waitFor();
            System.out.println("ending" + r2);
            log.info("清除生成文件完毕");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
