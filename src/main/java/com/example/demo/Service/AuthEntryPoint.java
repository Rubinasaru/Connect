package com.example.demo.Service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.demo.DTO.response.ResponseObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthEntryPoint.class);

    /**
     * Handles an unauthorized access attempt by providing a custom JSON error response.
     *
     * @param request       the HttpServletRequest object that triggered the exception
     * @param response      the HttpServletResponse object for returning the error resposne
     * @param authException the exception thrown when authentication fails
     * @throws IOException      if an input or output error occurs
     * @throws ServletException if a servlet-specific error occurs
     */

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
        // Log the unauthorized access attempt with the exception message
        logger.error("Unauthorized error: {}", authException.getMessage());

        // Set the content type to JSON and the response status to 401 (Unauthorized)
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Prepare a custom error response body
        ResponseObject responseObject = ResponseObject.failure("Authentication required! Please log in!");

        // Serialize the error response to JSON and write it to the response output stream
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), responseObject);
    }
}
