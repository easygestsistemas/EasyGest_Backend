package com.easygest.springboot.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Codifica a senha com BCrypt. A senha informada em EASYGEST_SECURITY_PASSWORD
     * nunca fica gravada em texto puro no banco nem no codigo.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Usuario administrativo em memoria (sem JWT, sem sessao persistente).
     * As credenciais devem ser injetadas por variavel de ambiente:
     *   EASYGEST_SECURITY_USERNAME
     *   EASYGEST_SECURITY_PASSWORD
     */
    @Bean
    public UserDetailsService userDetailsService(Environment env, PasswordEncoder passwordEncoder) {
        String username = env.getProperty("EASYGEST_SECURITY_USERNAME");
        String password = env.getProperty("EASYGEST_SECURITY_PASSWORD");

        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {
            throw new IllegalStateException(
                    "Seguranca nao configurada. Defina as variaveis de ambiente "
                    + "EASYGEST_SECURITY_USERNAME e EASYGEST_SECURITY_PASSWORD antes de iniciar a aplicacao.");
        }

        String senhaCodificada = passwordEncoder.encode(password);

        UserDetails usuario = User.builder()
                .username(username)
                .password(senhaCodificada)
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(usuario);
    }

    /**
     * Politica CORS que permite o preflight OPTIONS e chamadas cross-origin.
     * Complementa o CorsConfig ja existente, mantendo compatibilidade com
     * @CrossOrigin e com as origens configuradas por EASYGEST_CORS_ALLOWED_ORIGINS.
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuracao = new CorsConfiguration();
        configuracao.setAllowedOrigins(List.of("https://easygesterp.vercel.app"));
        configuracao.setAllowedMethods(
                List.of(HttpMethod.GET.name(), HttpMethod.POST.name(),
                        HttpMethod.PUT.name(), HttpMethod.DELETE.name(),
                        HttpMethod.OPTIONS.name(), HttpMethod.PATCH.name()));
        configuracao.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuracao.setAllowCredentials(true);
        configuracao.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource fonte = new UrlBasedCorsConfigurationSource();
        fonte.registerCorsConfiguration("/**", configuracao);
        return fonte;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()) // stateless: nao e necessario CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS).permitAll() // preflight CORS
                        .requestMatchers("/", "/health").permitAll() // health check do Render
                        .anyRequest().authenticated() // todas as demais rotas exigem autenticacao
                )
                .httpBasic(httpBasic -> {}) // autenticacao HTTP Basic (sem form de login, sem JWT)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));

        return http.build();
    }
}
