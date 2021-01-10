package com.bupt.qos.msgdivision.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

    //场景一：车辆节点数量在变化
    public Map<String, Object> analyzeMsgDivWithMultiNodes(Integer startNodesNum, Integer endNodesNum, Integer interval) {
        clearGeneratedFiles();
        generateMsgDivTraceFileAndAWKFilesWithMultiNodes(startNodesNum, endNodesNum, interval);

        log.info("开始分析awk生成文件");

        Map<String, Object> allMap = new HashMap<>();

        //分析时延
        Map<String, Object> delayMap = new HashMap<>();     //存储时延分析所有的信息
        List<Double> averageDelayList = new ArrayList<>();      //存储场景一的所有平均时延
        try {
            //遍历所有的时延awk文件
            for (int i = 0; i < nodesNumList.size(); i++) {
                BufferedReader br = new BufferedReader(new FileReader(AWK_DIRECTORY_PATH +VANET_ROUTING_COMPARE_DELAY_SCENE1_FILENAME + i));
                String line = "";
                while ((line = br.readLine()) != null) {
                    String[] lineArr = line.split(" ");
                    if (lineArr.length == 1) {
                        averageDelayList.add(Double.parseDouble(lineArr[0]));
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        delayMap.put("nodesNumList", nodesNumList);
        delayMap.put("averageDelayList", averageDelayList);


        //分析吞吐率
        JSONArray throughputArray = new JSONArray();
        DecimalFormat dfl = new DecimalFormat("0.000"); //保留三位小数用

        for (int i = 0; i < nodesNumList.size(); i++) {
            JSONObject jo = new JSONObject();   //用来存储单个文件的所有结果
            //统计总的吞吐率
            double totalThroughputRate = 0;
            double totalSimulationTime = 0;
            //统计间隔的吞吐率
            double intervalTimeOffset = 0.25;
            double intervalTime = 0.5;
            List<Double> intervalThroughputRates = new ArrayList<>();   //画图的纵坐标
            List<Double> intervalThroughputRatesKB = new ArrayList<>();   //KB格式
            List<Double> intervalXAxis = new ArrayList<>();    //画图的横坐标
            try {
                BufferedReader br = new BufferedReader(new FileReader(AWK_DIRECTORY_PATH + VANET_ROUTING_COMPARE_THROUGHPUT_SCENE1_FILENAME + i));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] lineArr = line.split(" ");
                    if (lineArr.length == 3) {
                        double oneRate = Double.parseDouble(lineArr[2]);
                        intervalThroughputRates.add(oneRate);   //将一个间隔的吞吐率(B/s)加入intervalThroughputRates
                        intervalThroughputRatesKB.add(oneRate / 1024);  //将一个间隔的吞吐率(KB/s)加入intervalThroughputRatesKB
                        intervalXAxis.add(intervalTimeOffset);  //将当前间隔的时间坐标加入intervalXAxis
                        intervalTimeOffset += intervalTime;

                    } else if(lineArr.length == 2){
                        totalThroughputRate = Double.parseDouble(dfl.format(Double.parseDouble(lineArr[0])));
                        totalSimulationTime = Double.parseDouble(dfl.format(Double.parseDouble(lineArr[1])));
                    }

                }
                jo.put("intervalThroughputRates", intervalThroughputRates);
                jo.put("intervalThroughputRatesKB", intervalThroughputRatesKB);
                jo.put("intervalXAxis", intervalXAxis);
                jo.put("totalThroughputRate", totalThroughputRate);
                jo.put("totalSimulationTime", totalSimulationTime);
                throughputArray.add(jo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        allMap.put("delayMap", delayMap);
        allMap.put("throughputMap", throughputArray);

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
        return allMap;

    }

    public void clearGeneratedFiles() {
        log.info("开始准备清除历史记录");

    }

    public void generateMsgDivTraceFileAndAWKFilesWithMultiNodes(Integer startNodesNum, Integer endNodesNum, Integer interval) {
        //统计需要生成几个trace文件
        for (int i = startNodesNum; i < endNodesNum; i += interval) {
            nodesNumList.add(i);
        }
        nodesNumList.add(endNodesNum);

        File file = new File(VANET_ROUTING_COMPARE_TRACE_PATH);
        log.info(">>>>>>>>>>vanet-routing-compare.tr文件不存在，需要重新生成<<<<<<<<<<");
        try {
            //因为无法生成多个trace文件，所以
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < nodesNumList.size(); i++) {
                sb.append("cd " + NS3_PATH + " && ./waf --run \"scratch/mythird --asciiTrace=1 --nodes=").append(nodesNumList.get(i)).append(" --totaltime=10\"")
                        .append(" && cd " + AWK_DIRECTORY_PATH + " && gawk -f " + VANET_ROUTING_COMPARE_DELAY_AWK_FILENAME + " " + VANET_ROUTING_COMPARE_TRACE_PATH + " > " + VANET_ROUTING_COMPARE_DELAY_SCENE1_FILENAME).append(i)
                        .append(" && cd " + AWK_DIRECTORY_PATH + " && gawk -f " + VANET_ROUTING_COMPARE_THROUGHPUT_AWK_FILENAME + " " + VANET_ROUTING_COMPARE_TRACE_PATH + " > " + VANET_ROUTING_COMPARE_THROUGHPUT_SCENE1_FILENAME).append(i);
                if (i < nodesNumList.size() - 1) {
                    sb.append(" && ");
                }
            }
            String shellCommand = sb.toString();
            System.out.println(sb.toString());
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public static void main(String[] args) {
//        MessageDivisionWithParamService m = new MessageDivisionWithParamService();
//        m.generateMsgDivTraceFileAndAWKFilesWithMultiNodes(40, 80, 20);
//        System.out.println(m.getNodesNumList());
//    }

}
