package Hoseo.GraduationProject.TestController;

import Hoseo.GraduationProject.Exception.BusinessLogicException;
import Hoseo.GraduationProject.Security.ExceptionType.SecurityExceptionType;
import Hoseo.GraduationProject.Security.UserDetails.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AdminController {

    @GetMapping("/admin")
    public String admin(@AuthenticationPrincipal CustomUserDetails custom){
        return custom.getId();
    }

    @GetMapping("/student")
    public String student(@AuthenticationPrincipal CustomUserDetails custom){
        return custom.getId();
    }

    @GetMapping("/professor")
    public String professor(){
        return "professor";
    }

    @GetMapping("/api")
    public String all(@AuthenticationPrincipal CustomUserDetails custom){
        return custom.getId();
    }

}
