package Hoseo.GraduationProject.Member.Domain;

import Hoseo.GraduationProject.Chat.Domain.UserChat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor
public class Member {

    @Id
    @Column(name = "id")
    private String id;

    @NotBlank
    @Column(name = "password")
    private String password;

    @NotBlank
    @Column(name = "name")
    private String name;

    @Email
    @NotBlank
    @Column(name = "email", unique = true)
    private String email;

    @NotBlank
    @Column(name = "role")
    private String role;

    @NotBlank
    @Column(name = "major")
    private String major;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<UserChat> userChats = new ArrayList<>();

    public void updatePassword(String password){
        this.password = password;
    }

    @Builder
    Member(String id, String password, String name,
           String email, String role, String major){
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.major = major;
    }

    @Override
    public String toString(){
        return "Member{id:"+id+"}";
    }
}
