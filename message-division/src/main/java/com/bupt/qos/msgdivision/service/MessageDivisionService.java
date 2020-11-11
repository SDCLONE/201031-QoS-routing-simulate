package com.bupt.qos.msgdivision.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Object> analyzeMsgDiv() {
        generateMsgDivTraceFile();
        generateMsgDivAwkFiles();
        log.info("-----------------开始分析vanet-routing-compare-----------------------");

//        String filePath = "D:\\Study\\ProgrammingProject\\IDEAProject\\WangGuan\\201031-QoS-routing-simulate\\message-division\\src\\test\\java\\com\\bupt\\qos\\msgdivision\\vanet-routing-compare-delay";
//        String filePath2 = "D:\\Study\\ProgrammingProject\\IDEAProject\\WangGuan\\201031-QoS-routing-simulate\\message-division\\src\\test\\java\\com\\bupt\\qos\\msgdivision\\vanet-routing-compare-throughput";
        Map<String, Object> allMap = new HashMap<>();
        Map<String, Object> delayMap = new HashMap<>();
        Map<String, Object> throughputMap = new HashMap<>();

        //分析时延
        double averageDelay = 0;
        int lowDelayCount = 0;      // <=0.001
        int middleDelayCount = 0;   // <=3   >0.001
        int highDelayCount = 0;     // >3
        try {
            BufferedReader br = new BufferedReader(new FileReader(VANET_ROUTING_COMPARE_DELAY_PATH));
//            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] lineArr = line.split(" ");
                if (lineArr.length > 1) {   //不是最后一行
//                    System.out.println(lineArr[4]);
                    //todo 是否要进行统计
                    double delay = Double.parseDouble(lineArr[4]);
                    if (delay <= 0.001) {
                        lowDelayCount++;
                    } else if (delay > 0.001 && delay <= 3) {
                        middleDelayCount++;
                    } else {
                        highDelayCount++;
                    }
                } else {    //是最后一行
//                    System.out.println("avg: " + lineArr[0]);
                    averageDelay = Double.parseDouble(lineArr[0]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //分析吞吐率
        double averageThroughput = 0;
        int lowThroughputCount = 0;  // <=5
        int highThroughputCount = 0;   // > 5

        try {
            BufferedReader br = new BufferedReader(new FileReader(VANET_ROUTING_COMPARE_THROUGHPUT_PATH));
//            BufferedReader br = new BufferedReader(new FileReader(filePath2));
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] lineArr = line.split(" ");
                if (lineArr.length > 1) {   //不是最后一行
//                    System.out.println(lineArr[1]);
                    //todo 是否要进行统计
                    double throughput = Double.parseDouble(lineArr[1]);
                    if (throughput <= 5) {
                        lowThroughputCount++;
                    } else {
                        highThroughputCount++;
                    }
                } else {    //是最后一行
//                    System.out.println("avg: " + lineArr[0]);
                    averageThroughput = Double.parseDouble(lineArr[0]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将delay信息加入map
        delayMap.put("averageDelay", averageDelay);
        delayMap.put("lowDelayCount", lowDelayCount);
        delayMap.put("middleDelayCount", middleDelayCount);
        delayMap.put("highDelayCount", highDelayCount);
        delayMap.put("allDelayCount", lowDelayCount + middleDelayCount + highDelayCount);

        //将throughput加入map
        throughputMap.put("averageThroughput", averageThroughput);
        throughputMap.put("lowThroughputCount", lowThroughputCount);
        throughputMap.put("highThroughputCount", highThroughputCount);
        throughputMap.put("allThroughputCount", lowThroughputCount + highThroughputCount);

        //将整个map打包
        allMap.put("msgDivDelay", delayMap);
        allMap.put("msgDivThroughput", throughputMap);

        return allMap;


    }

    public void clearGeneratedFiles() {
        String shellCommand =
                "rm -f " + VANET_ROUTING_COMPARE_TRACE_PATH +
                        " && rm -f " + VANET_ROUTING_COMPARE_DELAY_PATH +
                        " && rm -f " + VANET_ROUTING_COMPARE_THROUGHPUT_PATH;
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
        File vanetRoutingCompareDelayFile = new File(VANET_ROUTING_COMPARE_DELAY_PATH);
        File vanetRoutingCompareThroughputFile = new File(VANET_ROUTING_COMPARE_THROUGHPUT_PATH);
        List<String[]> awkPathList = new ArrayList<>();
        //不存在就加入list
        if (!vanetRoutingCompareDelayFile.exists()) {
            awkPathList.add(new String[]{VANET_ROUTING_COMPARE_DELAY_AWK_FILENAME, VANET_ROUTING_COMPARE_DELAY_FILENAME});
        }
        if (!vanetRoutingCompareThroughputFile.exists()) {
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
