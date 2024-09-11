package com.scm.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NotHandledExceptionHandler
//        implements ErrorController
{
//    private static final Logger log = LoggerFactory.getLogger(NotHandledExceptionHandler.class);
//
//    @RequestMapping("/error")
//    public String handleError(HttpServletRequest request, Model model) {
//        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
//        if(statusCode == 404){
//            model.addAttribute("message", "Page not found");
//            log.info("Page not found , {}",statusCode);
//            return "error-404";
//        }else if (statusCode == 500) {
//            model.addAttribute("message", "Internal server error");
//            log.info("Internal server error , {}",statusCode);
//            return "error-500";
//        }
//        model.addAttribute("message", "Unexpected error");
//        return "error";
//    }
}
