package com.dataart.softwarestore.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    private final static String SQL_GET_USERS = "SELECT username,password,enabled FROM users WHERE username=?";
    private final static String SQL_GET_ROLES = "SELECT username,authority FROM users WHERE username=?";

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(SQL_GET_USERS)
                .authoritiesByUsernameQuery(SQL_GET_ROLES).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/submit/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVELOPER')")
                .antMatchers("/rating/add/**").authenticated()
                .antMatchers("/**").permitAll()
                .and().formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .usernameParameter("username").passwordParameter("password")
                .and().exceptionHandling().accessDeniedPage("/")
                .and().csrf().disable();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }
}