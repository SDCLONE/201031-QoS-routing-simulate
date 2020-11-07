package com.bupt.qos.msgdivision;

import com.bupt.qos.msgdivision.service.MessageDivisionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MessageDivisionTests {

    @Autowired
    MessageDivisionService messageDivisionService;

    @Test
    void clearGeneratedFileTest() {
        messageDivisionService.clearGeneratedFiles();
    }
}
