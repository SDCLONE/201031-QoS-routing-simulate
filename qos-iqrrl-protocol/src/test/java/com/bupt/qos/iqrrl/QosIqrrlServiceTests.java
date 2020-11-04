package com.bupt.qos.iqrrl;


import com.bupt.qos.iqrrl.service.QosIqrrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class QosIqrrlServiceTests {

    @Autowired
    private QosIqrrlService qosIqrrlService;

    @Test
    void generateIqrrlAwkFilesTest() {
        qosIqrrlService.generateIqrrlAwkFiles();
    }


}
