/*
 * package com.ogive.oheo.config;
 * 
 * import org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.security.config.annotation.web.builders.HttpSecurity;
 * import org.springframework.security.config.annotation.web.configuration.
 * WebSecurityConfigurerAdapter; import
 * org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import
 * org.springframework.security.crypto.password.PasswordEncoder;
 * 
 * @Configuration public class ApplicationSecurityConfig extends
 * WebSecurityConfigurerAdapter {
 * 
 * 
 * @Override protected void configure(HttpSecurity http) throws Exception {
 * http.csrf().disable(); http.authorizeRequests()
 * .antMatchers("/products/**").authenticated()
 * .antMatchers("/login").permitAll()
 * //.antMatchers("/getStudentRoles").hasAuthority("ROLE_WRITE") .and()
 * .httpBasic(); }
 * 
 * @Bean public PasswordEncoder passwordEncoder() { return new
 * BCryptPasswordEncoder(); }
 * 
 * }
 */