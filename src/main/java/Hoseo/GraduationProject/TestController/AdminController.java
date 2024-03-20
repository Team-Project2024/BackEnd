package Hoseo.GraduationProject.TestController;

import Hoseo.GraduationProject.Security.UserDetails.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AdminController {

    @GetMapping("/api/admin")
    public String admin(@AuthenticationPrincipal CustomUserDetails custom){
        return "admin";
    }

    @GetMapping("/api/student")
    public String student(){
        return "student";
    }

    @GetMapping("/api/professor")
    public String professor(){
        return "professor";
    }

}
