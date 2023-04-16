package sda.ejemplo.auth.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import sda.ejemplo.auth.jwt.JwtEntryPoint;
import sda.ejemplo.auth.jwt.JwtTokenFilter;
import sda.ejemplo.model.service.UsuarioServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ConfigSecurity {

	@Autowired
	UsuarioServiceImpl userDetailsUuarioServiceImpl;
	
	@Autowired
	JwtEntryPoint jwtEntryPoint;
	
	@Autowired
	JwtTokenFilter jwtTokenFilter;
	
	public AuthenticationManager authenticationManager;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder builder= http.getSharedObject(AuthenticationManagerBuilder.class);
		builder.userDetailsService(userDetailsUuarioServiceImpl).passwordEncoder(bCryptPasswordEncoder());
		authenticationManager= builder.build();
		http.authenticationManager(authenticationManager);
		
		http.csrf().disable();
		http.cors();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.authorizeHttpRequests().requestMatchers("/auth/login").permitAll().anyRequest()
		.authenticated();
		
		http.exceptionHandling().authenticationEntryPoint(jwtEntryPoint);
		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
		
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS","PUT","DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization","enctype", "Authorization"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

	
	
}
