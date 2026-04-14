package com.github.kurbatov.kappi_port.config;

import com.github.kurbatov.kappi_port.repos.UserRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;


@Configuration
@EnableWebSecurity
class WebSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/greeting", "/greeting/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin((form) -> form
                        .loginPage("/login").successHandler(new SimpleUrlAuthenticationSuccessHandler("/greeting"))
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(UserRepo userRepo) {
        return username -> {
            com.github.kurbatov.kappi_port.domain.User userData = userRepo.findByUsername(username);

            if (userData == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }

            return User.withUsername(userData.getUsername())
                    .password(userData.getPassword())
                    .roles("ADMIN")
                    .build();
        };
    }
}
