/**
 * Controller to redirect users to the JavaDoc documentation.
 * <p>
 * This controller provides a convenient endpoint that redirects users to the
 * generated JavaDoc documentation for the Job Portal application. When users
 * access the /docs endpoint, they are automatically redirected to the JavaDoc
 * index page.
 * </p>
 * <p>
 * The JavaDoc documentation provides comprehensive API documentation for developers
 * working with the Job Portal system, including information about all controllers,
 * services, repositories, models, and utility classes.
 * </p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-17
 */
package me.josephsf.jobportaljosephsfeirtest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JavaDocController {

    /**
     * Redirects requests from /docs endpoint to the JavaDoc index page.
     * <p>
     * This method handles GET requests to the /docs endpoint and redirects the user
     * to the main index.html page of the generated JavaDoc documentation. The JavaDoc
     * documentation is served from the /javadoc directory as configured in the
     * application.properties file.
     * </p>
     *
     * @return A string containing the redirect URL for the JavaDoc index page
     */
    @GetMapping("/docs")
    public String redirectToJavaDocs() {
        return "redirect:/javadoc/index.html";
    }
}