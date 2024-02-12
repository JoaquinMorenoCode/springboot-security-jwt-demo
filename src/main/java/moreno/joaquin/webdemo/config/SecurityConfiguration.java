package moreno.joaquin.webdemo.config;


import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws  Exception{

            //Set FilterChain authorized request
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(rq -> {
                        rq.requestMatchers("/api/user/**").permitAll();
                        //rq.requestMatchers("/home/**").permitAll();
                        rq.requestMatchers("/home/**").authenticated();
                    });
            // Set Seesion Manager
            http.sessionManagement(sessionAuthStrategy -> sessionAuthStrategy.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
            //Set Authentication Provider
            http.authenticationProvider(authenticationProvider);
            //Add Filter before the username one
            http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();


    }

}
