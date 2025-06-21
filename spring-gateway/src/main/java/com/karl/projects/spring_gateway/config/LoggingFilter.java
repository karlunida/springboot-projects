package com.karl.projects.spring_gateway.config;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.Set;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.cloud.gateway.support.ipresolver.XForwardedRemoteAddressResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter implements GlobalFilter{

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		Set<URI> uris = exchange.getAttributeOrDefault(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR, Collections.emptySet());
		String originalUri = uris.isEmpty() ? exchange.getRequest().getURI().toString() : uris.iterator().next().toString();
		String beforeMutatetUri = exchange.getAttributeOrDefault(DefinedConstants.GatewayConstants.URI_BEFORE_MUTATE, null);
		String sourceUri = beforeMutatetUri != null ? beforeMutatetUri : originalUri;
		Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
		URI routeUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
		//extracting source ip address
		XForwardedRemoteAddressResolver resolver = XForwardedRemoteAddressResolver.maxTrustedIndex(1);
		InetSocketAddress inetSocketAddress = resolver.resolve(exchange);
		String sourceIp = inetSocketAddress.getAddress().getHostAddress();
		log.info("Incomimg request [ {} ] is routed to id: [ {} ], uri: [ {} ], Source IP: [ {} ]", sourceUri, route.getId(), routeUri, sourceIp);
		return chain.filter(exchange);
	}

}
