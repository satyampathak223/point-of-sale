package com.increff.pos.controller;

import com.increff.pos.model.InfoData;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UiController {

    @Value("${app.baseUrl}")
    private String baseUrl;

    @ApiOperation(value = "Serving a basic html page")
    @RequestMapping(path = "")
    public ModelAndView index() {
        return mav("brands.html");
    }

    @ApiOperation(value = "Serving a basic html page")
    @RequestMapping(path = "/products")
    public ModelAndView products() {
        return mav("products.html");
    }

    private ModelAndView mav(String page) {
        ModelAndView mav = new ModelAndView(page);
        mav.addObject("info", new InfoData());
        mav.addObject("baseUrl", baseUrl);
        return mav;
    }
}
