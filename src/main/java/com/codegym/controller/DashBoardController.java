package com.codegym.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/dashboard")
public class DashBoardController {
    @GetMapping({"", "/", "/product", "/products"})
    public ModelAndView showDashboardProduct() {
        return new ModelAndView("/dashboard/products");
    }

    @GetMapping({"user", "/users"})
    public ModelAndView showDashboardUser() {
        return new ModelAndView("/dashboard/users");
    }
}
