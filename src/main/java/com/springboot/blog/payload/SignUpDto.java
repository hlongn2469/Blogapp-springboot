package com.springboot.blog.payload;

import lombok.Data;

@Data
public class SignUpDto {
    private String name;
    private String user_name;
    private String email;
    private String password;
}
