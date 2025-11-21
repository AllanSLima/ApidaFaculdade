package br.com.faculdade.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SecurityConfig {

    // Bean para o BCryptPasswordEncoder
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuração do filtro de segurança, incluindo configuração de CORS
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors().and()            // habilita CORS com configurationSource abaixo
            .csrf().disable()        // desabilita CSRF para APIs REST (opcional conforme necessidade)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/alunos/**", "/cobrancas/**", "/auth/**").permitAll() // ajuste conforme rotas abertas
                .anyRequest().authenticated()
            );

        return http.build();
    }

    // Configuração de CORS para permitir as origens e métodos necessários
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:8081", "http://localhost:8083", "https://front-para-a-faculdade.onrender.com", "https://front-para-os-alunos.onrender.com"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
