package com.licenseweb.config;

import com.licenseweb.filter.JwtAuthenticationFilter;
import com.licenseweb.service.OAuth2Service;
import com.licenseweb.service.TokenService;
import com.licenseweb.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring security config
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtTokenProvider jwtTokenProvider;
	private final TokenService tokenService;
	private final OAuth2Service oAuth2Service;

	@Value("${spring.security.oauth2-enabled:false}")
	private boolean oauth2Enabled;

	private static final String[] PERMIT_URL = {
			"/",
			"/auth/**",
			"/error/**",
			"/h2-console/**"
	};

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		/**
		 * 인가 설정
		 */
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers(PERMIT_URL).permitAll()
				.requestMatchers("/index-test-case1").authenticated()
				.requestMatchers("/index-test-case2").hasRole("USER")
				.requestMatchers("/index-test-case3").hasRole("ADMIN")
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
				.anyRequest().authenticated()
		);

		/**
		 * 인증 설정
		 */
		http.formLogin(auth -> auth
				.loginPage("/auth/login")
				.defaultSuccessUrl("/")
				.failureUrl("/auth/login")
				.usernameParameter("username")
				.passwordParameter("password")
				.permitAll()
		);

		configureJwt(http);

		if (oauth2Enabled) {
			configureOAuth2(http);
		}

		return http.build();
	}

	private void configureJwt(HttpSecurity http) {
		http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, tokenService),
						UsernamePasswordAuthenticationFilter.class);
	}

	private void configureOAuth2(HttpSecurity http) throws Exception {
		http.oauth2Login(oauth2 -> oauth2
				.loginPage("/auth/login")
				.defaultSuccessUrl("/auth/login-reconfirm", true)
				.userInfoEndpoint(userInfo ->
						userInfo.userService(oAuth2Service)
				)
		);
	}
}
