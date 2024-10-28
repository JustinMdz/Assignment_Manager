package org.una.programmingIII.Assignment_Manager.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.una.programmingIII.Assignment_Manager.Service.JWTService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> {
//                    auth.requestMatchers("/auth/login").permitAll();
//                    auth.requestMatchers("/auth/refreshToken").permitAll();
//                    auth.requestMatchers("/api/users/create").permitAll(); //preguntar
//                    auth.requestMatchers("/hello").permitAll();
//                    auth.anyRequest().authenticated();
//                })
//
//                .addFilterBefore(new JWTAuthorizationFilter(jwtService), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }

        @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)  // Deshabilitar CSRF si no es necesario
                .authorizeHttpRequests(auth -> {
                    auth.anyRequest().permitAll();  // Permitir todas las solicitudes sin autenticación
                });
        return http.build();
    }


}
