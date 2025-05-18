package com.ticketsystem.ticketsystem.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String index(){
        return "index.html";
    }

    @RequestMapping("/test")
    public String test(){
        return "test";
    }
}
