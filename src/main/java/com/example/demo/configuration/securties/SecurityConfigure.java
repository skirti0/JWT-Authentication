package com.example.demo.configuration.securties;

import com.example.demo.service.UsersDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.HashMap;
import java.util.Map;

@Configurable
@EnableWebSecurity
public class SecurityConfigure extends WebSecurityConfigurerAdapter {
  @Autowired
  private UsersDetailsService usersDetailService;
  @Autowired
  private JwtRequestFilter jwtRequestFilter;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(usersDetailService);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
            .disable()
            .authorizeRequests()
            .antMatchers("/rest/v1/users").permitAll()
            .antMatchers("/rest/v1/users/_activate").permitAll()
            .antMatchers("/rest/v1/authenticate").permitAll()
            .antMatchers("/rest/v1/products").permitAll()
            .antMatchers("/rest/v1/users/active").authenticated()
            .anyRequest()
            .authenticated()
            .and()
            .logout().logoutUrl("/logout")
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
            .cors().configurationSource(corsConfigurationSource());
  }

  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
  @Bean
  public UrlBasedCorsConfigurationSource corsConfigurationSource()
  {
    UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
    Map<String, CorsConfiguration> mp = new HashMap<>();
    mp.put("/**",corsConfiguration());
    corsSource.setCorsConfigurations(mp);
    return corsSource;
  }

  @Bean
  public CorsConfiguration corsConfiguration()
  {
    CorsConfiguration corsCfg = new CorsConfiguration();
    corsCfg.setAllowCredentials(true);
    corsCfg.addAllowedOrigin("*");
    corsCfg.addAllowedHeader("*");
    corsCfg.addAllowedMethod("*");
    return corsCfg;
  }
}
