package org.gateway.filter;

import lombok.RequiredArgsConstructor;
import org.gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>{


	private RouteValidator validator;
	private JwtUtil jwtUtil;
	@Autowired
	public AuthenticationFilter(RouteValidator validator, JwtUtil jwtUtil) {
		super(Config.class);
		this.validator = validator;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public GatewayFilter apply(Config config){

		return ((exchange, chain) -> {
			if (validator.isSecured.test(exchange.getRequest())) {
				//header contains token or not
				if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
					throw new RuntimeException("missing authorization header");
				}

				String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
				if (authHeader != null && authHeader.startsWith("Bearer ")) {
					authHeader = authHeader.substring(7);
				}
				try {

					jwtUtil.validateToken(authHeader);

				} catch (Exception e) {
					System.out.println("invalid access...!");
					throw new RuntimeException("un authorized access to application");
				}
			}
			return chain.filter(exchange);
		});
	}

	public static class Config{

	}
}