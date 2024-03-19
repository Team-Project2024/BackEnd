package Hoseo.GraduationProject.TestController;

import Hoseo.GraduationProject.Member.Domain.Member;
import Hoseo.GraduationProject.Security.UserDetails.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Iterator;

@RestController
public class AdminController {

    @GetMapping("/api/admin")
    public String admin(@AuthenticationPrincipal CustomUserDetails custom){
        System.out.println(custom.getId());
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
