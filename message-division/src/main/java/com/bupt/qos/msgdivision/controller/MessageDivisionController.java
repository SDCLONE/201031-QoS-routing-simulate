package com.bupt.qos.msgdivision.controller;

import com.bupt.qos.msgdivision.service.MessageDivisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/msgDiv")
public class MessageDivisionController {

    @Autowired
    private MessageDivisionService messageDivisionService;

    @RequestMapping("/helloTest")
    public String helloTest() {
        return "hello test";
    }

    //测试调用命令行在windows生成一个文件夹
    @GetMapping("/mkdirTest")
    public void mkdirTest() {
        messageDivisionService.mkdirTest();
    }

    //测试调用命令行在linux生成一个文件夹
    @GetMapping("/mkdirTestLinux")
    public void mkdirTestLinux() {
        messageDivisionService.mkdirTestLinux();
    }

    //生成trace文件
    @GetMapping("/generateMsgDivTraceFile")
    public void generateMsgDivTraceFile() {
        messageDivisionService.generateMsgDivTraceFile();
    }

    //生成awk分析文件
    @GetMapping("/generateMsgDivAwkFiles")
    public void generateMsgDivAwkFiles() {
        messageDivisionService.generateMsgDivAwkFiles();
    }
}
