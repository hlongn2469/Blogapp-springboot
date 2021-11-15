package com.springboot.blog.payload;

import lombok.Data;

@Data
public class LoginDto {
    private String username_or_email;
    private String password;
}
