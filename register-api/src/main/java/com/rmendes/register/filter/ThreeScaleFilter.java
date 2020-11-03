package com.rmendes.register.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Provider
public class ThreeScaleFilter implements ContainerRequestFilter{
	static final String API_GATEWAY_HEADER="X-3scale-proxy-secret-token";

	@ConfigProperty(name = "api.gateway.token")
	String tokenValue;

	@ConfigProperty(name= "api.gateway.enabled")
	boolean apiGatewayEnabled;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if(apiGatewayEnabled) {
			if(!requestContext.getHeaders().containsKey(API_GATEWAY_HEADER)) {
				requestContext.abortWith(Response.status(Status.BAD_GATEWAY).entity("Api Gateway Header not Set").build());
			}else if(!requestContext.getHeaderString(API_GATEWAY_HEADER).equals(tokenValue)) {
				requestContext.abortWith(Response.status(Status.BAD_GATEWAY).entity("Api Gateway Header Incorrect").build());
			}
		}

	}

}
