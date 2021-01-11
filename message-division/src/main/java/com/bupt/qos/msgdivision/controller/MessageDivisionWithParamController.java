package com.bupt.qos.msgdivision.controller;

import com.bupt.qos.msgdivision.service.MessageDivisionService;
import com.bupt.qos.msgdivision.service.MessageDivisionWithParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/msgDivWithParam")
@CrossOrigin
public class MessageDivisionWithParamController {
    @Autowired
    private MessageDivisionWithParamService messageDivisionWithParamService;

    @GetMapping("/generateMsgDivTraceFileAndAWKFilesWithMultiNodes")
    public void generateMsgDivTraceFileAndAWKFilesWithMultiNodes(
            @RequestParam Integer startNodesNum, @RequestParam Integer endNodesNum, @RequestParam Integer interval) {
        messageDivisionWithParamService.generateMsgDivTraceFileAndAWKFilesWithMultiNodes(startNodesNum, endNodesNum, interval);
    }

    @GetMapping("/analyzeMsgDivWithMultiNodes")
    public Map<String, Object> analyzeMsgDivWithMultiNodes(
            @RequestParam Integer startNodesNum, @RequestParam Integer endNodesNum, @RequestParam Integer interval) {
        return messageDivisionWithParamService.analyzeMsgDivWithMultiNodes(startNodesNum, endNodesNum, interval);
    }

    @GetMapping("/analyzeMsgDivWithTime")
    public Map<String, Object> analyzeMsgDivWithTime(
            @RequestParam Integer nodesNum, @RequestParam Integer simulationTime) {
        return messageDivisionWithParamService.analyzeMsgDivWithTime(nodesNum, simulationTime);
    }

    @GetMapping("/generateMsgDivTraceFileAndAWKFilesWithTime")
    public void generateMsgDivTraceFileAndAWKFilesWithTime(
            @RequestParam Integer nodesNum, @RequestParam Integer simulationTime) {
        messageDivisionWithParamService.generateMsgDivTraceFileAndAWKFilesWithTime(nodesNum, simulationTime);
    }

    @GetMapping("/clearGeneratedFiles")
    public void clearGeneratedFiles(@RequestParam String deleteMode) {
        messageDivisionWithParamService.clearGeneratedFiles(deleteMode);
    }

}
