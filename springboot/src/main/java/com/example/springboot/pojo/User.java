package com.example.springboot.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")
@ToString
public class User implements Serializable {


  @TableId(value = "id", type = IdType.AUTO)
  private Long id;


  private String  username;

  @JsonIgnore
  private String password;


  private String nickname;


  private String email;


  private String phone;


  private String address;


  @TableField("create_time")
  private Date createTime;


  @TableField("avatar_url")
  private String avatarUrl;

  private String role;


  public User(String username, String password, String nickname, String email, String phone, String address) {
    this.username = username;
    this.password = password;
    this.nickname = nickname;
    this.email = email;
    this.phone = phone;
    this.address = address;
  }
}
