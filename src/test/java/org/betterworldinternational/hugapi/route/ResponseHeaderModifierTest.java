package org.betterworldinternational.hugapi.route;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseHeaderModifierTest {
    private final static Object BODY = new Object();
    private final static MediaType DEFAULT_CONTENT_TYPE = MediaType.APPLICATION_XML;
    private final static String URI_WITH_HEADER = "/api/test";
    private final static String URI_WITHOUT_HEADER = "/apiWithoutHeader";

    @Test
    public void supportReturnsAlwaysTheSameValue() {
        assertTrue(new ResponseHeaderModifier().supports(null, null));
    }

    @Test
    public void nonServletRequest() {
        checkIfHeadersAreModified(mock(ServerHttpRequest.class), false);
    }

    @Test
    public void servletRequestWithoutHeader() {
        checkIfHeadersAreModified(createRequest(URI_WITHOUT_HEADER), false);
    }

    @Test
    public void servletRequestWithHeader() {
        checkIfHeadersAreModified(createRequest(URI_WITH_HEADER), true);
    }

    private static void checkIfHeadersAreModified(ServerHttpRequest request, boolean areHeadersModified) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(DEFAULT_CONTENT_TYPE);
        Object body = new ResponseHeaderModifier().beforeBodyWrite(
                BODY,
                null,
                null,
                null,
                request,
                createResponse(headers));
        assertThat(body, is(sameInstance(BODY)));

        MediaType contentType = areHeadersModified ? MediaType.APPLICATION_JSON : DEFAULT_CONTENT_TYPE;
        assertThat(headers.getContentType(), is(equalTo(contentType)));

        String serverHeaderValue = areHeadersModified ? HeaderConstants.SERVER_HEADER_VALUE : null;
        assertThat(headers.getFirst(HeaderConstants.SERVER_HEADER_NAME), is(equalTo(serverHeaderValue)));
    }

    private static ServletServerHttpRequest createRequest(String uri) {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getRequestURI()).thenReturn(uri);

        ServletServerHttpRequest request = mock(ServletServerHttpRequest.class);
        when(request.getServletRequest()).thenReturn(servletRequest);
        return request;
    }

    private static ServerHttpResponse createResponse(HttpHeaders headers) {
        ServerHttpResponse response = mock(ServerHttpResponse.class);
        when(response.getHeaders()).thenReturn(headers);
        return response;
    }
}