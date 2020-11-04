package com.bupt.qos.msgdivision.service;

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
public class MessageDivisionService {

    //ns3目录下的文件
    private final String NS3_PATH = "/home/baobao/workspace/ns326/";
    private final String VANET_ROUTING_COMPARE_TRACE_FILENAME = "vanet-routing-compare.tr";

    private final String VANET_ROUTING_COMPARE_TRACE_PATH = NS3_PATH + VANET_ROUTING_COMPARE_TRACE_FILENAME;

    //awk目录下的文件
    private final String AWK_DIRECTORY_PATH = "/home/baobao/Desktop/2020.09.09-differation-service-data-analysis/";
    private final String VANET_ROUTING_COMPARE_DELAY_AWK_FILENAME = "vanet-routing-compare-delay.awk";
    private final String VANET_ROUTING_COMPARE_THROUGHPUT_AWK_FILENAME = "vanet-routing-compare-throughput.awk";
    private final String VANET_ROUTING_COMPARE_DELAY_FILENAME = "vanet-routing-compare-delay";
    private final String VANET_ROUTING_COMPARE_THROUGHPUT_FILENAME = "vanet-routing-compare-throughput";

    private final String VANET_ROUTING_COMPARE_DELAY_AWK_PATH = AWK_DIRECTORY_PATH + VANET_ROUTING_COMPARE_DELAY_AWK_FILENAME;
    private final String VANET_ROUTING_COMPARE_THROUGHPUT_AWK_PATH = AWK_DIRECTORY_PATH + VANET_ROUTING_COMPARE_THROUGHPUT_AWK_FILENAME;
    private final String VANET_ROUTING_COMPARE_DELAY_PATH = AWK_DIRECTORY_PATH + VANET_ROUTING_COMPARE_DELAY_FILENAME;
    private final String VANET_ROUTING_COMPARE_THROUGHPUT_PATH = AWK_DIRECTORY_PATH + VANET_ROUTING_COMPARE_THROUGHPUT_FILENAME;


    public void mkdirTest() {
        try {
            String path = "D:\\Study\\ProgrammingProject\\IDEAProject\\WangGuan\\201031-QoS-routing-simulate\\message-division\\src\\main\\java\\com\\bupt\\qos\\msgdivision\\service";
            String[] arg1 = new String[]{"cmd", "/c", "mkdir", path + "\\testingdir"};
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
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void mkdirTestLinux() {
        try {
            String[] arg1 = new String[]{"/bin/sh", "-c", "mkdir /home/baobao/Desktop/yzb-java-linux/testDir"};
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
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void analyzeMsgDiv() {
        generateMsgDivTraceFile();
        generateMsgDivAwkFiles();
        log.info("-----------------开始分析vanet-routing-compare-----------------------");
    }

    public void generateMsgDivTraceFile() {
        File file = new File(VANET_ROUTING_COMPARE_TRACE_PATH);
        //如果不存在文件，就生成
        if (!file.exists()) {
            log.info(">>>>>>>>>>vanet-routing-compare.tr文件不存在，需要重新生成<<<<<<<<<<");
            try {
                String shellCommand = "cd " + NS3_PATH + " && ./waf --run \"scratch/mythird --asciiTrace=1\"";

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
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            log.info(">>>>>>>>>>vanet-routing-compare.tr文件已经存在<<<<<<<<<<");
        }
    }

    public void generateMsgDivAwkFiles() {
        File iqrrlDelayFile = new File(VANET_ROUTING_COMPARE_DELAY_PATH);
        File iqrrlThroughputFile = new File(VANET_ROUTING_COMPARE_THROUGHPUT_PATH);
        List<String[]> awkPathList = new ArrayList<>();
        //不存在就加入list
        if (!iqrrlDelayFile.exists()) {
            awkPathList.add(new String[]{VANET_ROUTING_COMPARE_DELAY_AWK_FILENAME, VANET_ROUTING_COMPARE_DELAY_FILENAME});
        }
        if (!iqrrlThroughputFile.exists()) {
            awkPathList.add(new String[]{VANET_ROUTING_COMPARE_THROUGHPUT_AWK_FILENAME, VANET_ROUTING_COMPARE_THROUGHPUT_FILENAME});
        }
        //根据list生成linux指令
        StringBuilder shellCommand = new StringBuilder();

        if (awkPathList.size() == 0) {  //如果没有命令直接结束
            log.info(">>>>>>>>>>vanet-routing-compare: delay和throughput都存在<<<<<<<<<<");
            return;
        } else {    //如果有就生成命令
            log.info(">>>>>>>>>>vanet-routing-compare: delay和throughput有不存在的<<<<<<<<<<");
            shellCommand.append("cd ").append(AWK_DIRECTORY_PATH);
            for (String[] strings : awkPathList) {
                shellCommand.append(" && gawk -f ").append(strings[0]).append(" ").append(VANET_ROUTING_COMPARE_TRACE_PATH).append(" > ").append(strings[1]);
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
}
