package com.bupt.qos.msgdivision.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MessageDivisionWithParamService {

    //ns3目录下的文件
    private final String NS3_PATH = "/home/baobao/workspace/ns326/";
    private final String VANET_ROUTING_COMPARE_TRACE_FILENAME = "vanet-routing-compare.tr";

    private final String VANET_ROUTING_COMPARE_TRACE_PATH = NS3_PATH + VANET_ROUTING_COMPARE_TRACE_FILENAME;

    //awk目录下的文件
    private final String AWK_DIRECTORY_PATH = "/home/baobao/Desktop/2020.09.09-differation-service-data-analysis/";
    private final String VANET_ROUTING_COMPARE_DELAY_AWK_FILENAME = "vanet-routing-compare-delay.awk";
    private final String VANET_ROUTING_COMPARE_THROUGHPUT_AWK_FILENAME = "vanet-routing-compare-throughput.awk";
    //场景一生成的
    private final String VANET_ROUTING_COMPARE_DELAY_SCENE1_FILENAME = "vanet-routing-compare-delay-scene1-";
    private final String VANET_ROUTING_COMPARE_THROUGHPUT_SCENE1_FILENAME = "vanet-routing-compare-throughput-scene1-";
    private final String VANET_ROUTING_COMPARE_DELAY_SCENE2_FILENAME = "vanet-routing-compare-delay-scene2";
    private final String VANET_ROUTING_COMPARE_THROUGHPUT_SCENE2_FILENAME = "vanet-routing-compare-throughput-scene2";


    private final String VANET_ROUTING_COMPARE_DELAY_AWK_PATH = AWK_DIRECTORY_PATH + VANET_ROUTING_COMPARE_DELAY_AWK_FILENAME;
    private final String VANET_ROUTING_COMPARE_THROUGHPUT_AWK_PATH = AWK_DIRECTORY_PATH + VANET_ROUTING_COMPARE_THROUGHPUT_AWK_FILENAME;
//    private final String VANET_ROUTING_COMPARE_DELAY_PATH = AWK_DIRECTORY_PATH + VANET_ROUTING_COMPARE_DELAY_FILENAME;
//    private final String VANET_ROUTING_COMPARE_THROUGHPUT_PATH = AWK_DIRECTORY_PATH + VANET_ROUTING_COMPARE_THROUGHPUT_FILENAME;

    //用于记录随车辆节点数变化需要生成几个awk分析文件
    //注意：tr文件只能生成一个
    private List<Integer> nodesNumList = new ArrayList<>();     //存储多个trace文件的参数信息

    public List<Integer> getNodesNumList() {
        return nodesNumList;
    }

    public Map<String, Object> analyzeMsgDiv() {
//        generateMsgDivTraceFile();
//        generateMsgDivAwkFiles();
//        log.info("-----------------开始分析vanet-routing-compare-----------------------");
//
////        String filePath = "D:\\Study\\ProgrammingProject\\IDEAProject\\WangGuan\\201031-QoS-routing-simulate\\message-division\\src\\test\\java\\com\\bupt\\qos\\msgdivision\\vanet-routing-compare-delay";
////        String filePath2 = "D:\\Study\\ProgrammingProject\\IDEAProject\\WangGuan\\201031-QoS-routing-simulate\\message-division\\src\\test\\java\\com\\bupt\\qos\\msgdivision\\vanet-routing-compare-throughput";
//        Map<String, Object> allMap = new HashMap<>();
//        Map<String, Object> delayMap = new HashMap<>();
//        Map<String, Object> throughputMap = new HashMap<>();
//
//        //分析时延
//        double averageDelay = 0;
//        int lowDelayCount = 0;      // <=0.001
//        int middleDelayCount = 0;   // <=3   >0.001
//        int highDelayCount = 0;     // >3
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(VANET_ROUTING_COMPARE_DELAY_PATH));
////            BufferedReader br = new BufferedReader(new FileReader(filePath));
//            String line = "";
//            while ((line = br.readLine()) != null) {
//                String[] lineArr = line.split(" ");
//                if (lineArr.length > 1) {   //不是最后一行
////                    System.out.println(lineArr[4]);
//                    //todo 是否要进行统计
//                    double delay = Double.parseDouble(lineArr[4]);
//                    if (delay <= 0.001) {
//                        lowDelayCount++;
//                    } else if (delay > 0.001 && delay <= 3) {
//                        middleDelayCount++;
//                    } else {
//                        highDelayCount++;
//                    }
//                } else {    //是最后一行
////                    System.out.println("avg: " + lineArr[0]);
//                    averageDelay = Double.parseDouble(lineArr[0]);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //分析吞吐率
//        DecimalFormat dfl = new DecimalFormat("0.000"); //保留三位小数用
//
//        //统计总的吞吐率
//        double totalThroughputRate = 0;
//        double totalSimulationTime = 0;
//        //统计间隔的吞吐率
//        double intervalTimeOffset = 0.25;
//        double intervalTime = 0.5;
//        List<Double> intervalThroughputRates = new ArrayList<>();   //画图的纵坐标
//        List<Double> intervalThroughputRatesKB = new ArrayList<>();   //KB格式
//        List<Double> intervalXAxis = new ArrayList<>();    //画图的横坐标
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(VANET_ROUTING_COMPARE_THROUGHPUT_PATH));
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] lineArr = line.split(" ");
//                if (lineArr.length == 3) {
//                    double oneRate = Double.parseDouble(lineArr[2]);
//                    intervalThroughputRates.add(oneRate);   //将一个间隔的吞吐率(B/s)加入intervalThroughputRates
//                    intervalThroughputRatesKB.add(oneRate / 1024);  //将一个间隔的吞吐率(KB/s)加入intervalThroughputRatesKB
//                    intervalXAxis.add(intervalTimeOffset);  //将当前间隔的时间坐标加入intervalXAxis
//                    intervalTimeOffset += intervalTime;
//
//                } else if(lineArr.length == 2){
//                    totalThroughputRate = Double.parseDouble(dfl.format(Double.parseDouble(lineArr[0])));
//                    totalSimulationTime = Double.parseDouble(dfl.format(Double.parseDouble(lineArr[1])));
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//        //将delay信息加入map
//        delayMap.put("averageDelay", averageDelay);
//        delayMap.put("lowDelayCount", lowDelayCount);
//        delayMap.put("middleDelayCount", middleDelayCount);
//        delayMap.put("highDelayCount", highDelayCount);
//        delayMap.put("allDelayCount", lowDelayCount + middleDelayCount + highDelayCount);
//
//        //将throughput加入map
//        throughputMap.put("averageThroughputRate", totalThroughputRate);
//        throughputMap.put("totalSimulationTime", totalSimulationTime);
//        throughputMap.put("intervalThroughputRatesArr", intervalThroughputRates.toArray());
//        throughputMap.put("intervalThroughputRatesKBArr", intervalThroughputRatesKB.toArray());
//        throughputMap.put("intervalXAxisArr", intervalXAxis.toArray());
//
//        //将整个map打包
//        allMap.put("msgDivDelay", delayMap);
//        allMap.put("msgDivThroughput", throughputMap);
//
//        return allMap;
        return null;

    }

    public void clearGeneratedFiles() {
//        String shellCommand =
//                "rm -f " + VANET_ROUTING_COMPARE_TRACE_PATH +
//                        " && rm -f " + VANET_ROUTING_COMPARE_DELAY_PATH +
//                        " && rm -f " + VANET_ROUTING_COMPARE_THROUGHPUT_PATH;
////        System.out.println(shellCommand);
//        //调用命令行清除所有生成文件
//        try {
//            String[] arg1 = new String[]{"/bin/sh", "-c", shellCommand};
//            Process pr = Runtime.getRuntime().exec(arg1);
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream(), "gbk"));
//            String line;
//            //读取python中print的值
//            while ((line = in.readLine()) != null) {
//                System.out.println(line);
//            }
//            in.close();
//            int r2 = pr.waitFor();
//            System.out.println("ending" + r2);
//            log.info("清除生成文件完毕");
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public void generateMsgDivTraceFileAndAWKFilesWithMultiNodes(Integer startNodesCount, Integer endNodesCount, Integer interval) {
        //统计需要生成几个trace文件
        for (int i = startNodesCount; i < endNodesCount; i += interval) {
            nodesNumList.add(i);
        }
        nodesNumList.add(endNodesCount);

        File file = new File(VANET_ROUTING_COMPARE_TRACE_PATH);
        log.info(">>>>>>>>>>vanet-routing-compare.tr文件不存在，需要重新生成<<<<<<<<<<");
        try {
            //因为无法生成多个trace文件，所以
            for (int i = 0; i < nodesNumList.size(); i++) {
                String shellCommand =
                        "cd " + NS3_PATH + " && ./waf --run \"scratch/mythird --asciiTrace=1 --nodes=" + nodesNumList.get(i) + " --totaltime=10\"" +
                        " && cd " + AWK_DIRECTORY_PATH + " && gawk -f " + VANET_ROUTING_COMPARE_DELAY_AWK_FILENAME + " " + VANET_ROUTING_COMPARE_TRACE_PATH + " > " + VANET_ROUTING_COMPARE_DELAY_SCENE1_FILENAME + i;
                System.out.println(shellCommand);
//                String[] arg1 = new String[]{"/bin/sh", "-c", shellCommand};
//                Process pr = Runtime.getRuntime().exec(arg1);
//                BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream(), "gbk"));
//                String line;
//                //读取python中print的值
//                while ((line = in.readLine()) != null) {
//                    System.out.println(line);
//                }
//                in.close();
//                int r2 = pr.waitFor();
//                System.out.println("ending" + r2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        File file = new File(VANET_ROUTING_COMPARE_TRACE_PATH);
//        //如果不存在文件，就生成
//        if (!file.exists()) {
//            log.info(">>>>>>>>>>vanet-routing-compare.tr文件不存在，需要重新生成<<<<<<<<<<");
//            try {
//                String shellCommand = "cd " + NS3_PATH + " && ./waf --run \"scratch/mythird --asciiTrace=1\"";
//
//                String[] arg1 = new String[]{"/bin/sh", "-c", shellCommand};
//                Process pr = Runtime.getRuntime().exec(arg1);
//                BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream(), "gbk"));
//                String line;
//                //读取python中print的值
//                while ((line = in.readLine()) != null) {
//                    System.out.println(line);
//                }
//                in.close();
//                int r2 = pr.waitFor();
//                System.out.println("ending" + r2);
//            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
//            }
//        } else {
//            log.info(">>>>>>>>>>vanet-routing-compare.tr文件已经存在<<<<<<<<<<");
//        }
    }

    public void generateMsgDivAwkFiles() {
//        File vanetRoutingCompareDelayFile = new File(VANET_ROUTING_COMPARE_DELAY_PATH);
//        File vanetRoutingCompareThroughputFile = new File(VANET_ROUTING_COMPARE_THROUGHPUT_PATH);
//        List<String[]> awkPathList = new ArrayList<>();
//        //不存在就加入list
//        if (!vanetRoutingCompareDelayFile.exists()) {
//            awkPathList.add(new String[]{VANET_ROUTING_COMPARE_DELAY_AWK_FILENAME, VANET_ROUTING_COMPARE_DELAY_FILENAME});
//        }
//        if (!vanetRoutingCompareThroughputFile.exists()) {
//            awkPathList.add(new String[]{VANET_ROUTING_COMPARE_THROUGHPUT_AWK_FILENAME, VANET_ROUTING_COMPARE_THROUGHPUT_FILENAME});
//        }
//        //根据list生成linux指令
//        StringBuilder shellCommand = new StringBuilder();
//
//        if (awkPathList.size() == 0) {  //如果没有命令直接结束
//            log.info(">>>>>>>>>>vanet-routing-compare: delay和throughput都存在<<<<<<<<<<");
//            return;
//        } else {    //如果有就生成命令
//            log.info(">>>>>>>>>>vanet-routing-compare: delay和throughput有不存在的<<<<<<<<<<");
//            shellCommand.append("cd ").append(AWK_DIRECTORY_PATH);
//            for (String[] strings : awkPathList) {
//                shellCommand.append(" && gawk -f ").append(strings[0]).append(" ").append(VANET_ROUTING_COMPARE_TRACE_PATH).append(" > ").append(strings[1]);
//            }
//        }
//        System.out.println(shellCommand.toString());
//
//        //调用命令行生成awk分析文件
//        try {
//            String[] arg1 = new String[]{"/bin/sh", "-c", shellCommand.toString()};
//            Process pr = Runtime.getRuntime().exec(arg1);
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream(), "gbk"));
//            String line;
//            //读取python中print的值
//            while ((line = in.readLine()) != null) {
//                System.out.println(line);
//            }
//            in.close();
//            int r2 = pr.waitFor();
//            System.out.println("ending" + r2);
//            log.info("生成awk分析文件完毕");
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public static void main(String[] args) {
        MessageDivisionWithParamService m = new MessageDivisionWithParamService();
        m.generateMsgDivTraceFileAndAWKFilesWithMultiNodes(40, 80, 60);
        System.out.println(m.getNodesNumList());
    }

}
