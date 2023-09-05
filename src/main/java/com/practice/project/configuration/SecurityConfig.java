package com.practice.project.configuration;

import com.practice.project.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler;
    @Autowired
    CustomUserDetailService customUserDetailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(requests -> requests
            .requestMatchers(
                new AntPathRequestMatcher("/h2-console/**"),
                new AntPathRequestMatcher("/"),
                new AntPathRequestMatcher("/shop/**"),
                new AntPathRequestMatcher("/register"),
                new AntPathRequestMatcher("/resources/**"),
                new AntPathRequestMatcher("/static/**"),
                new AntPathRequestMatcher("/css/**"),
                new AntPathRequestMatcher("/images/**"),
                new AntPathRequestMatcher("/productImages/**"),
                new AntPathRequestMatcher("/js/**")
            ).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")
            .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
                .failureUrl("/login?error=true")
                .successHandler(customAuthenticationSuccessHandler()) // Use custom success handler
                .passwordParameter("password")
                .usernameParameter("email")
            )
            .oauth2Login(oauth -> oauth
                .loginPage("/login")
                .successHandler(googleOAuth2SuccessHandler)
                .failureUrl("/login?error=true")
                .successHandler(customAuthenticationSuccessHandler()) // Use custom success handler
            )
            .exceptionHandling(configurer -> configurer
                .accessDeniedHandler((request, response, accessDeniedException) -> response.sendRedirect("/access-denied"))
                .accessDeniedPage("/access-denied")
            )
            .logout(logout -> logout
                .permitAll()
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            )
            .csrf(httpSecurityCsrfConfigurer ->
                httpSecurityCsrfConfigurer.disable())
            .headers(headers ->
                headers.disable()
            );

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            if (authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
                response.sendRedirect("/admin");
            } else {
                response.sendRedirect("/");
            }
        };
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(customUserDetailService);
    }
}
