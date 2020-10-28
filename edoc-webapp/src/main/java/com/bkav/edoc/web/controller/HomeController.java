package com.bkav.edoc.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/")
@Controller
public class HomeController {

    @GetMapping
    public String index(HttpServletRequest request) {
        return "homePage";
    }



    @GetMapping(value = "/upload")
    public String uploadFile(){
        return "uploadFilePage";
    }



}
