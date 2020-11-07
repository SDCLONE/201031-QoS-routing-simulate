package com.bupt.qos.iqrrl.controller;


import com.bupt.qos.iqrrl.service.QosIqrrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/iqrrl")
public class QosIqrrlController {

    @Autowired
    private QosIqrrlService qosIqrrlService;

    @GetMapping("/testingHello")
    public String testingHello() {
        return "testing hello!!!";
    }

    @GetMapping("/analyzeIqrrlTrace")
    public void analyzeIqrrlTrace() {
        qosIqrrlService.analyzeIqrrlTrace();
    }

    @GetMapping("/generateIqrrlTraceFile")
    public void generateIqrrlTraceFile() {
        qosIqrrlService.generateIqrrlTraceFile();
    }

    @GetMapping("/generateIqrrlAwkFiles")
    public void generateIqrrlAwkFiles() {
        qosIqrrlService.generateIqrrlAwkFiles();
    }

    @GetMapping("/clearGeneratedFiles")
    public void clearGeneratedFiles() {
        qosIqrrlService.clearGeneratedFiles();
    }
}
