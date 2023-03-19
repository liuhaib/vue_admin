package com.example.springboot.uaits;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.springboot.pojo.User;
import com.example.springboot.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
@Component
public class TokenUaits {
      private static UserService userInService;

      @Resource
      private  UserService userService;

      @PostConstruct
      public void setUserService(){
         userInService=userService;
      }

    /**
     * 签发 token
     * @param userID
     * @param sign
     * @return
     */
        public static String GetToken(String userID,String sign){
            String token = null;
            try {
                token = JWT.create().withAudience(userID) //用户id作为凭证
                        .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) //token 过期时间 2小时
                        .sign(Algorithm.HMAC256(sign));//用户密码 作为 hash加密
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return token;
        }

    /**
     * 通过 token 获取 用户
     * @return
     */
    public static User GetTokenUser(){
            try {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                String token = request.getHeader("token");
                if (StrUtil.isNotBlank(token)) {
                    String userId = JWT.decode(token).getAudience().get(0);
                    return userInService.getById(Integer.valueOf(userId));
                }
            } catch (Exception e) {
                return null;
            }
            return null;
        }
}
