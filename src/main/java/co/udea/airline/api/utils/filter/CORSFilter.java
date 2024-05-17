package co.udea.airline.api.utils.filter;

import jakarta.servlet.Filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter for Cross-Origin Resource Sharing.
 *
 */
@Component
public class CORSFilter implements Filter {

    @Value("${airline-api.cors.allow-origins}")
    private String allowedOrigins;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", allowedOrigins);
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, origin, content-type, accept");
        response.setHeader("Access-Control-Max-Age", "1800");
        chain.doFilter(req, res);
    }

}