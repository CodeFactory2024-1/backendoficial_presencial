package co.udea.airline.api.utils.common;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import co.udea.airline.api.utils.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Aspect
@Component
public class AuthValidationAspect {
    @Value("${airline-api.security.api-url}")
    private String airlineApiSecurityApiUrl;

    @Autowired
    private HttpServletRequest request;

    @Before("@annotation(AuthRequired)")
    public void verifyAuth(JoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        if (method.isAnnotationPresent(AuthRequired.class)) {
            String token = request.getHeader("Authorization");
            if (token == null || !isValidToken(token)) {
                throw new UnauthorizedException(
                        "You are not authorized or your session has expired. Please log in again.");
            }
        }
    }

    private boolean isValidToken(String token) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(airlineApiSecurityApiUrl)).header("Authorization", token)
                .GET()
                .build();
        try {
            client.send(request, BodyHandlers.ofString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // TODO: Add method to validate the role and permissions of the user
}
