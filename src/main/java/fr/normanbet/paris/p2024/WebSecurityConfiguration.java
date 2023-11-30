package fr.normanbet.paris.p2024;

import fr.normanbet.paris.p2024.services.DbUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;


@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        (req)->req.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"),
                                        AntPathRequestMatcher.antMatcher("/confirmation"),
                                        AntPathRequestMatcher.antMatcher("/resendValidationEmail"),
                                        AntPathRequestMatcher.antMatcher("/mailvalidation"),
                                        AntPathRequestMatcher.antMatcher("/badUser"),
                                        AntPathRequestMatcher.antMatcher("/resendBadValidationEmail"),
                                        AntPathRequestMatcher.antMatcher("/regitrationConfirm"),
                                        AntPathRequestMatcher.antMatcher("/register"),
                                        AntPathRequestMatcher.antMatcher("/css/**"),
                                        AntPathRequestMatcher.antMatcher("/"),
                                        AntPathRequestMatcher.antMatcher("/images/**"),
                                        AntPathRequestMatcher.antMatcher("/js/**"))
                                .permitAll()
                                .anyRequest()
                                .authenticated()

                )
                .csrf(csrf->csrf.ignoringRequestMatchers(toH2Console()))
                .formLogin(
                        (form)-> form.loginPage("/login")
                                .defaultSuccessUrl("/")
                                .failureUrl("/login")
                                .permitAll())
                .logout((logout) -> logout.logoutSuccessUrl("/login"))
                .headers(
                        (headers)->headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );
        return http.build();
    }




    @Bean
    public UserDetailsService getUserDetailsService() {
        return new DbUserService();
    }
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
