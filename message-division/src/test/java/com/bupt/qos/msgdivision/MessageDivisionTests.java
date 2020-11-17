package com.bupt.qos.msgdivision;

import com.bupt.qos.msgdivision.service.MessageDivisionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
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

                } else if(lineArr.length == 2){
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
}
