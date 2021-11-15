package com.springboot.blog.controller;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.payload.JWTAuthResponse;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.SignUpDto;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository user_repo;

    @Autowired
    private RoleRepository role_repo;

    @Autowired
    private PasswordEncoder pw_encoder;

    @Autowired
    private JwtTokenProvider token_provider;

    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto login_dto){
        Authentication auth = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(login_dto.getUsername_or_email(), login_dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);

        //get token from tokenprovider
        String token = token_provider.generateToken(auth);
        return ResponseEntity.ok(new JWTAuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signup_dto){
        if(user_repo.existsByUsername(signup_dto.getUser_name())){
            return new ResponseEntity<>("username already taken", HttpStatus.BAD_REQUEST);
        }

        if(user_repo.existsByEmail(signup_dto.getEmail())){
            return new ResponseEntity<>("email already taken", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setName(signup_dto.getName());
        user.setUsername(signup_dto.getUser_name());
        user.setEmail(signup_dto.getEmail());
        user.setPassword(pw_encoder.encode(signup_dto.getPassword()));

        Role roles = role_repo.findByName("ROLE_ADMIN").get();
        user.setRoles(Collections.singleton(roles));
        user_repo.save(user);
        return new ResponseEntity<>("User registered succesfully", HttpStatus.OK);
    }
}
