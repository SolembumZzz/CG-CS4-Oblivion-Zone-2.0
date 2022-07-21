package com.codegym.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController {

    @RequestMapping(value = "errors", method = RequestMethod.GET)
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {

        ModelAndView errorPage = new ModelAndView();
        errorPage.setViewName("error/errorPage");

        String errorMsg = "";
        String title = "";
        int httpErrorCode = getErrorCode(httpRequest);

        switch (httpErrorCode) {
            case 400: {
                title = "Bad Request";
                errorMsg = "Http Status Code: 400. Bad Request";
                break;
            }
            case 401: {
                title = "Unauthorized";
                errorMsg = "Http Status Code: 401. Unauthorized";
                break;
            }
            case 403: {
                title = "Forbidden";
                errorMsg = "Http Status Code: 403. Access Denied!";
                break;
            }
            case 404: {
                title = "Resource not found";
                errorMsg = "Http Status Code: 404. Resource not found";
                errorPage.setViewName("error/404");
                break;
            }
            case 405: {
                title = "Method Not Allowed";
                errorMsg = "Http Status Code: 405. Method Not Allowed";
                break;
            }
            case 409: {
                title = "Data Conflict";
                errorMsg = "Http Status Code: 409. Data Conflict";
                break;
            }
            case 500: {
                title = "Internal Server Error";
                errorMsg = "Http Status Code: 500. Internal Server Error";
                break;
            }
        }
        errorPage.addObject("title", title);
        errorPage.addObject("errorMsg", errorMsg);
        return errorPage;
    }

    @RequestMapping(value = "errors", method = RequestMethod.POST)
    public ModelAndView renderErrorPageMethod(HttpServletRequest httpRequest) {

        ModelAndView errorPage = new ModelAndView();
        errorPage.setViewName("error/errorPage");

        String errorMsg = "";
        String title = "";
        int httpErrorCode = getErrorCode(httpRequest);

        switch (httpErrorCode) {
            case 400: {
                title = "Bad Request";
                errorMsg = "Http Status Code: 400. Bad Request";
                break;
            }
            case 401: {
                title = "Unauthorized";
                errorMsg = "Http Status Code: 401. Unauthorized";
                break;
            }
            case 403: {
                title = "Forbidden";
                errorMsg = "Http Status Code: 403. Access Denied!";
                break;
            }
            case 404: {
                title = "Resource not found";
                errorMsg = "Http Status Code: 404. Resource not found";
                errorPage.setViewName("error/404");
                break;
            }
            case 405: {
                title = "Method Not Allowed";
                errorMsg = "Http Status Code: 405. Method Not Allowed";
                break;
            }
            case 409: {
                title = "Data Conflict";
                errorMsg = "Http Status Code: 409. Data Conflict";
                break;
            }
            case 500: {
                title = "Internal Server Error";
                errorMsg = "Http Status Code: 500. Internal Server Error";
                break;
            }
        }
        errorPage.addObject("title", title);
        errorPage.addObject("errorMsg", errorMsg);
        return errorPage;
    }

    @RequestMapping(value = "500Error", method = RequestMethod.GET)
    public void throwRuntimeException() {
        throw new NullPointerException("Throwing a null pointer exception");
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
    }
}