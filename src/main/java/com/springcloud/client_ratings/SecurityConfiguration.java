package com.springcloud.client_ratings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal1(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.inMemoryAuthentication();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/ratings").permitAll()
                .antMatchers(HttpMethod.GET, "/ratings/*").permitAll()
                .antMatchers(HttpMethod.POST, "/ratings").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/ratings/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/ratings/*").hasRole("ADMIN")
                .antMatchers("/encrypt/**").permitAll()
                .antMatchers("/decrypt/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf()
                .disable();
    }
}
