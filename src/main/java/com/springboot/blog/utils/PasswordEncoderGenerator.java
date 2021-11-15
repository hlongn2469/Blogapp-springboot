package com.springboot.blog.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderGenerator {
    public static void main(String[] args){
        PasswordEncoder pw_encoder = new BCryptPasswordEncoder();
        System.out.println(pw_encoder.encode("admin"));
    }
}
