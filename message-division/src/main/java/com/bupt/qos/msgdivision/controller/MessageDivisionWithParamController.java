package com.bupt.qos.msgdivision.controller;

import com.bupt.qos.msgdivision.service.MessageDivisionService;
import com.bupt.qos.msgdivision.service.MessageDivisionWithParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/msgDivWithParam")
@CrossOrigin
public class MessageDivisionWithParamController {
    @Autowired
    private MessageDivisionWithParamService messageDivisionWithParamService;

    @RequestMapping("/generateMsgDivTraceFileAndAWKFilesWithMultiNodes")
    public void generateMsgDivTraceFileAndAWKFilesWithMultiNodes(Integer startNodesNum, Integer endNodesNum, Integer interval) {
        messageDivisionWithParamService.generateMsgDivTraceFileAndAWKFilesWithMultiNodes(startNodesNum, endNodesNum, interval);
    }

    @RequestMapping("/analyzeMsgDivWithMultiNodes")
    public Map<String, Object> analyzeMsgDivWithMultiNodes(Integer startNodesNum, Integer endNodesNum, Integer interval) {
        return messageDivisionWithParamService.analyzeMsgDivWithMultiNodes(startNodesNum, endNodesNum, interval);
    }
}
