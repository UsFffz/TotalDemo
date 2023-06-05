package com.example.totaldemo.controller;

import com.example.totaldemo.aop.anotation.UserLog;
import com.example.totaldemo.service.CasualService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
@RequestMapping("/api/casualController")
@Slf4j
@Api(tags = "书籍处")
public class CasualController {
    @Autowired
    private CasualService casualService;

    @GetMapping("/selectAllBook")
    @UserLog("查看所有图书")
    public Map<String,Object> selectAllBook(HttpServletRequest httpServletRequest){
        return casualService.bookList();
    }

    @GetMapping("/buyBook")
    @UserLog("购买图书")
    @ApiOperation(value = "购买图书")
    public Map<String,Object> buyBook(Integer bookId, HttpServletRequest httpServletRequest){
        return casualService.buyBook(bookId);
    }

}
