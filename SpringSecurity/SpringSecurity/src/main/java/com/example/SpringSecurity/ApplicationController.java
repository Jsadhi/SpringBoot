package com.example.SpringSecurity;

import com.example.SpringSecurity.JWT.JwtUtils;
import com.example.SpringSecurity.JWT.LoginRequest;
import com.example.SpringSecurity.JWT.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ApplicationController {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private final JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    public ApplicationController(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }
    @GetMapping("get")
    public ResponseEntity<String> get() {
        return new ResponseEntity<>("Hello World", HttpStatus.OK);
    }
    @PostMapping("CreateUser")
    public ResponseEntity<String> createUser(@RequestParam String username,
                                             @RequestParam String password,
                                             @RequestParam String role) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        if(userDetailsManager.userExists(username)) {
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }
        UserDetails user= User.withUsername(username)
                .password(passwordEncoder.encode(password))
                .roles(role).build();
        userDetailsManager.createUser(user);
        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }
    @PostMapping("signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try
        {
            authentication=authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        }
        catch (AuthenticationException exception)
        {
            Map<String,Object> map=new HashMap<>();
            map.put("error","Invalid username or password");
            map.put("Status",false);
            return new ResponseEntity<Object>(map,HttpStatus.NOT_FOUND);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String jwtToken=jwtUtils.generateTokenFromUsername(user);
        List<String> roles=user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        LoginResponse loginResponse=new LoginResponse(jwtToken,user.getUsername(),roles);
        return ResponseEntity.ok(loginResponse);
    }

}
