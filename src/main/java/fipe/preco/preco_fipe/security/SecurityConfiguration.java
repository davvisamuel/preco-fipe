package fipe.preco.preco_fipe.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AuthenticationFilter authenticationFilter;
    private static final String[] WHITE_LIST = {"/v1/api/**", "/v1/auth/**", "/v1/user/register"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST).permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/v1/user").hasAuthority("USER")
                        .requestMatchers(HttpMethod.PUT, "/v1/user").hasAuthority("USER")
                        .requestMatchers(HttpMethod.POST, "/v1/favorite").hasAuthority("USER")
                        .requestMatchers(HttpMethod.DELETE, "/v1/favorite/**").hasAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "/v1/favorite").hasAuthority("USER")
                        .requestMatchers(HttpMethod.POST, "/v1/comparison").hasAuthority("USER")
                        .requestMatchers(HttpMethod.DELETE, "/v1/comparison/**").hasAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "/v1/comparison").hasAuthority("USER")
                        .requestMatchers(HttpMethod.POST, "/v1/comparisonConsultation").hasAuthority("USER")
                        .requestMatchers(HttpMethod.DELETE, "/v1/comparisonConsultation/**").hasAuthority("USER")
                        .requestMatchers(HttpMethod.GET).hasAuthority("ADMIN")
                )
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
