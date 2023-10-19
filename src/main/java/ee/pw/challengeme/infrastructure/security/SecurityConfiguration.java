package ee.pw.challengeme.infrastructure.security;

import ee.pw.challengeme.infrastructure.security.oauth2.OAuthFailureHandler;
import ee.pw.challengeme.infrastructure.security.oauth2.OAuthSuccessHandler;
import ee.pw.challengeme.infrastructure.security.oauth2.OAuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

	private final TokenAuthenticationFilter tokenAuthenticationFilter;
	private final OAuthSuccessHandler oAuthSuccessHandler;
	private final OAuthFailureHandler oAuthFailureHandler;
	private final OAuthUserService oAuthUserService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
		throws Exception {
		httpSecurity.cors(Customizer.withDefaults());
		httpSecurity.csrf(AbstractHttpConfigurer::disable);

		httpSecurity.sessionManagement(session ->
			session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		);

		httpSecurity.addFilterBefore(
			tokenAuthenticationFilter,
			UsernamePasswordAuthenticationFilter.class
		);

		httpSecurity.authorizeHttpRequests(request -> {
			request.requestMatchers("/api/login").permitAll();
			request.requestMatchers("/api/create-user").permitAll();
			request.requestMatchers("/api/main-page").permitAll();
			request.anyRequest().authenticated();
		});

		httpSecurity.logout(httpSecurityLogoutConfigurer -> {
			httpSecurityLogoutConfigurer.logoutUrl("/api/logout");
			httpSecurityLogoutConfigurer.addLogoutHandler(
				((request, response, authentication) -> response.setStatus(200))
			);
			httpSecurityLogoutConfigurer.clearAuthentication(true);
			httpSecurityLogoutConfigurer.deleteCookies(
				"short-access-token",
				"refresh-access-token"
			);
			httpSecurityLogoutConfigurer.logoutSuccessUrl("/");
		});

//		httpSecurity.oauth2Login(httpSecurityOAuth2LoginConfigurer -> {
//			httpSecurityOAuth2LoginConfigurer.loginPage("/oauth-login");
//			httpSecurityOAuth2LoginConfigurer.userInfoEndpoint(userInfoEndpointConfig ->
//				userInfoEndpointConfig.userService(oAuthUserService)
//			);
//			httpSecurityOAuth2LoginConfigurer.successHandler(oAuthSuccessHandler);
//			httpSecurityOAuth2LoginConfigurer.failureHandler(oAuthFailureHandler);
//		});

		return httpSecurity.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);

		return source;
	}
}
