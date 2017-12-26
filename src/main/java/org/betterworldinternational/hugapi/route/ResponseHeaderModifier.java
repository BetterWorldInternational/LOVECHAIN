package org.betterworldinternational.hugapi.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ResponseHeaderModifier implements ResponseBodyAdvice<Object> {
    private static final String URI_WITH_HEADER = "/api/";

    private static final Logger logger = LoggerFactory.getLogger(ResponseHeaderModifier.class);

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
            if (servletServerHttpRequest.getServletRequest().getRequestURI().startsWith(URI_WITH_HEADER)) {
                logger.info("Adding headers to a response");

                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                response.getHeaders().set(HeaderConstants.SERVER_HEADER_NAME, HeaderConstants.SERVER_HEADER_VALUE);
            }
        }

        return body;
    }
}