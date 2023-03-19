package com.example.springboot.pojo.dto;

import com.example.springboot.pojo.Menu;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin {
   private String username;
   private String password;
   private String nickname;
   private String avatarUrl;
   private String token;
   private String role;
   private List<Menu> menus;
}
