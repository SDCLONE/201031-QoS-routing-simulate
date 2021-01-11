package com.bupt.qos.msgdivision;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bupt.qos.msgdivision.service.MessageDivisionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class MessageDivisionTests {

    public static void main(String[] args) throws ParseException {
//        DecimalFormat dfl = new DecimalFormat("0.000");
//        String s = "1.3456";
//        double d = 1.3456;
//        System.out.println(dfl.format(d));
//        System.out.println(String.format("%.2f", s));
        double d = 1.3456;
        BigDecimal bigDecimal = new BigDecimal(d);
        d = bigDecimal.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(d);
    }

    @Autowired
    MessageDivisionService messageDivisionService;

    @Test
    void clearGeneratedFileTest() {
        messageDivisionService.clearGeneratedFiles();
    }

    @Test
    void readFileTest() {
        String filePath = "D:\\Study\\ProgrammingProject\\IDEAProject\\WangGuan\\201031-QoS-routing-simulate\\message-division\\src\\test\\java\\com\\bupt\\qos\\msgdivision\\vanet-routing-compare-delay";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            while (br.readLine() != null) {
                String line = br.readLine();
                String[] lineArr = line.split(" ");
                if (lineArr.length > 1) {
                    System.out.println(lineArr[4]);
                } else {
                    System.out.println("avg: " + lineArr[0]);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void readVanetRoutingTest() {
        String filePath = "D:\\Study\\ProgrammingProject\\IDEAProject\\WangGuan\\201031-QoS-routing-simulate\\message-division\\src\\test\\java\\com\\bupt\\qos\\msgdivision\\vanet-routing-compare-throughput";
        BufferedReader br = null;

        DecimalFormat dfl = new DecimalFormat("0.000");


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
            br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineArr = line.split(" ");
                if (lineArr.length == 3) {
                    double oneRate = Double.parseDouble(lineArr[2]);
                    intervalThroughputRates.add(oneRate);   //将一个间隔的吞吐率加入intervalThroughputRates
                    intervalThroughputRatesKB.add(oneRate / 1024);
                    intervalXAxis.add(intervalTimeOffset);  //将当前间隔的时间坐标加入intervalXAxis
                    intervalTimeOffset += intervalTime;

                } else if (lineArr.length == 2) {
                    totalThroughputRate = Double.parseDouble(dfl.format(Double.parseDouble(lineArr[0])));
                    totalSimulationTime = Double.parseDouble(dfl.format(Double.parseDouble(lineArr[1])));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(totalThroughputRate + "  " + totalSimulationTime);
        System.out.println(Arrays.toString(intervalThroughputRates.toArray()));
        System.out.println(Arrays.toString(intervalThroughputRatesKB.toArray()));
        System.out.println(Arrays.toString(intervalXAxis.toArray()));
    }

    @Test
    void analyzeMsgDivWithMultiNodesTests() {
//分析时延
        List<Double> averageDelayList = new ArrayList<>();
        try {
            //遍历所有的时延awk文件
            for (int i = 0; i < 3; i++) {
                //D:\Study\ProgrammingProject\IDEAProject\WangGuan\201031-QoS-routing-simulate\message-division\src\test\java\com\bupt\qos\msgdivision\vanet-routing-compare-delay-scene1-0
                BufferedReader br = new BufferedReader(new FileReader("D:\\Study\\ProgrammingProject\\IDEAProject\\WangGuan\\201031-QoS-routing-simulate\\message-division\\src\\test\\java\\com\\bupt\\qos\\msgdivision\\vanet-routing-compare-delay-scene1-" + i));
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
        System.out.println(averageDelayList);
    }

    @Test
    void fastJsonTests() {
//        List<Integer> list = new ArrayList<>();
//        list.add(1);
//        list.add(2);
//        list.add(3);
//        System.out.println(JSON.toJSONString(list));
//        System.out.println(JSON.toJSONString(list.toArray()));
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 4; i++) {
            JSONObject jo = new JSONObject();
            jo.put("hello", i);
            jo.put("world", i + 1);
            float[] floats = {1.2f, 2.3f, 4f};
            jo.put("floats", floats);
            jsonArray.add(jo);
        }

        System.out.println(JSON.toJSONString(jsonArray));
    }

    @Test
    void deleteFileTests() {
        //进入文件夹
        File file = new File("D:\\Study\\ProgrammingProject\\IDEAProject\\WangGuan\\201031-QoS-routing-simulate\\message-division\\src\\test\\java\\com\\bupt\\qos\\msgdivision");

        List<String> needDeleteFileNames = new ArrayList<>();
        String[] fileNameArr = file.list();
        System.out.println(Arrays.toString(fileNameArr));
        assert fileNameArr != null;
        for (String fileName : fileNameArr) {
            if (fileName.matches(".*(scene1).*")) {
                needDeleteFileNames.add(fileName);
            }
//            System.out.println(fileName.indexOf("scene1"));
        }

        StringBuilder sb = new StringBuilder();
//        sb.append("cd ").append()
        System.out.println(needDeleteFileNames);
    }
}
