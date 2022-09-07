package ua.com.api.security;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ua.com.api.exception.model.ExceptionResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class AuthorizationExceptionHandler implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ExceptionResponse e = new ExceptionResponse(
                new SimpleDateFormat("HH:mm:ss.SSS dd-MM-yyyy").format(new Date()),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Invalid credentials or not authorized!"
        );
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(new Gson().newBuilder().setPrettyPrinting().create().toJson(e));
    }
}