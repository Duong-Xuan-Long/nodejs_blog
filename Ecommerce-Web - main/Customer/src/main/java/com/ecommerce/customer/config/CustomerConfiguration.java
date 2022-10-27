package com.ecommerce.customer.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class CustomerConfiguration extends WebSecurityConfigurerAdapter {
    private final AuthorizationFilterCustom authorizationFilterCustom;

    private final AuthenticationEntryPointCustom authenticationEntryPointCustom;
    private final AccessDeniedHandlerCustom accessDeniedHandlerCustom;

    private final CustomerServiceConfig customerServiceConfig;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customerServiceConfig);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    //Cung cấp provider cho Authentication manager
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/", "/shop/login", "/shop/do-login").permitAll()
                .antMatchers("/customer/**").hasRole("CUSTOMER")
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointCustom)
                .accessDeniedHandler(accessDeniedHandlerCustom)
                //.and()
//                .logout()
//                .invalidateHttpSession(true)
                //.clearAuthentication(true)
                //.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .deleteCookies("JWT_COOKIE", "JSESSIONID")
                //.logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)))
//                .logoutSuccessUrl("/login?logout")
                //.permitAll().
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(authorizationFilterCustom, UsernamePasswordAuthenticationFilter.class);
    }
}

