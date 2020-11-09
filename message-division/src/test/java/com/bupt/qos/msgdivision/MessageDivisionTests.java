package com.bupt.qos.msgdivision;

import com.bupt.qos.msgdivision.service.MessageDivisionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.FileReader;

@SpringBootTest
public class MessageDivisionTests {

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
}
