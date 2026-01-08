package com.example.springsecuritypact2.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfiguartion {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http.csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth->
                    auth.requestMatchers("/adminPage").hasRole("admin")
                            .requestMatchers("/page1").hasRole("user")
                        .requestMatchers("/home","/users","/admins").permitAll()
                )
                .formLogin(auth->{})
                .httpBasic(auth->{})
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails user = User.withUsername("user").password(passwordEncoder().encode("root")).roles("user").build();
//        UserDetails admin = User.withUsername("admin").password(passwordEncoder().encode("root")).roles("admin").build();
//        return new InMemoryUserDetailsManager(user,admin);
//    }
}
