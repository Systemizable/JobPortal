package me.josephsf.jobportaljosephsfeir.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JavaDocController {

    @GetMapping("/docs")
    public String redirectToJavaDocs() {
        return "redirect:/javadoc/index.html";
    }
}
