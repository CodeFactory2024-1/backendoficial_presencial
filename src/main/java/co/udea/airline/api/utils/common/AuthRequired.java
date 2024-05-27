package co.udea.airline.api.utils.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@SecurityRequirement(name = "JWT")
public @interface AuthRequired {
}