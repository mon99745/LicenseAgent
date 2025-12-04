package com.licenseweb.filter;

import com.licenseweb.model.entity.Token;
import com.licenseweb.service.TokenService;
import com.licenseweb.util.JwtTokenProvider;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Token - Filter
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final JwtTokenProvider jwtTokenProvider;
	private final TokenService tokenService;

	/**
	 * Filter Request 단위로 시행
	 *
	 * @param request  The request to process
	 * @param response The response associated with the request
	 * @param chain    Provides access to the next filter in the chain for this filter to pass the request and response
	 *                 to for further processing
	 * @throws IOException
	 * @throws ServletException
	 * @throws java.io.IOException
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException, java.io.IOException {

		// 1. Request Header 에서 JWT 토큰 추출
		Token token = resolveToken((HttpServletRequest) request);
		String accessToken = token.getAccessToken();
		String refreshToken = token.getRefreshToken();

		// 2. Token 유효성 검사
		if (accessToken != null) {
			// 2-1 Access Token 검사
			if (jwtTokenProvider.validateToken(accessToken)) {
				setAuthenticationInContext(accessToken);
			} else if (refreshToken != null && tokenService.existsRefreshToken(refreshToken)) {
				// 2-2 Refresh Token 검사
				if (jwtTokenProvider.validateToken(refreshToken));
				// Access Token 재발급
				Authentication authentication = jwtTokenProvider.getAuthentication(null, refreshToken);
				String newAccessToken = jwtTokenProvider.regenerateToken(authentication, token.getRefreshToken())
						.getAccessToken();
				// Header 에 Access Token 추가해서 반환
				jwtTokenProvider.setHeaderAccessToken((HttpServletResponse) response, newAccessToken);

				// SecurityContext 에 저장
				this.setAuthenticationInContext(newAccessToken);
			} else {
				throw new RuntimeException("토큰 정보가 정확하지 않습니다 Access Token :" + accessToken
						+ "Refresh Token: " + refreshToken);
			}
		}
		chain.doFilter(request, response);
	}

	/**
	 * 토큰의 Authentication Object -> SecurityContext 저장
	 *
	 * @param accessToken
	 */
	private void setAuthenticationInContext(String accessToken) {
		Authentication authentication = jwtTokenProvider.getAuthentication(accessToken, null);
		SecurityContextHolder
				.getContext()
				.setAuthentication(authentication);
	}

	/**
	 * Request Header 에서 토큰 추출
	 *
	 * @param request
	 * @return
	 */
	private Token resolveToken(HttpServletRequest request) {
		String accessToken = null;
		String refreshToken = null;

		String accessBearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(accessBearerToken) && accessBearerToken.startsWith("Bearer")) {
			accessToken = accessBearerToken.substring(7);
		}

		String refreshBearerToken = request.getHeader("X-Refresh-Token");
		if (StringUtils.hasText(refreshBearerToken) && refreshBearerToken.startsWith("Bearer")) {
			refreshToken = refreshBearerToken.substring(7);
		}
		Token token = Token.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();

		return token;
	}
}