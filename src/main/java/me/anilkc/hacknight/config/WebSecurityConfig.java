package me.anilkc.hacknight.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import me.anilkc.hacknight.config.handler.CustomAccessDeniedHandler;
import me.anilkc.hacknight.config.handler.CustomAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


  @Autowired
  private CustomAccessDeniedHandler customAccessDeniedHandler;

  @Autowired
  private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
  // @formatter:off
    auth.inMemoryAuthentication()
                .withUser("user").password("password").roles("USER")
                .and()
                .withUser("admin").password("password").roles("ADMIN");
  // @formatter:on

  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

  // @formatter:off
      http
      .csrf().disable()
          .httpBasic()  
          .and()
          .authorizeRequests().antMatchers("/alexaHackNight")
          .permitAll()
          .and()
          .authorizeRequests()
          .anyRequest().authenticated()
          .and()
          .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler)
          .authenticationEntryPoint(customAuthenticationEntryPoint);
  // @formatter:on
  }

}
