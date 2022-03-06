package com.example.jwtcleanspring.aspect;

import com.example.jwtcleanspring.annotation.Auth;
import com.example.jwtcleanspring.exception.exceptions.NoPermissionException;
import com.example.jwtcleanspring.exception.exceptions.UnauthorizedException;
import com.example.jwtcleanspring.model.JWT;
import com.example.jwtcleanspring.model.User;
import com.example.jwtcleanspring.model.enums.Role;
import com.example.jwtcleanspring.repository.UserRepository;
import com.example.jwtcleanspring.service.JWTProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class AuthHandler {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private final JWTProvider jwtProvider;
    private final UserRepository userRepository;

    @Autowired
    public AuthHandler(JWTProvider jwtProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    @Around("@annotation(com.example.jwtcleanspring.annotation.Auth)")
    public Object checkToken(ProceedingJoinPoint javaPoint) throws Throwable {
        Role[] roles = ((MethodSignature) javaPoint.getSignature()).getMethod().getAnnotation(Auth.class).roles();
        HttpServletRequest request = (HttpServletRequest) Arrays.stream(javaPoint.getArgs())
                .filter(arg -> arg instanceof HttpServletRequest)
                .findFirst().orElseThrow(() -> new IllegalArgumentException("@Auth not found HttpServlet in methods args"));
        String header = request.getHeader(AUTHORIZATION);

        if (header == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        if (header.startsWith(BEARER)) {
            String token = header.substring(BEARER.length());
            JWT jwt = jwtProvider.encodeToken(token);
            User user = userRepository.findByUsername(jwt.getPayload().getUsername());
            if (!jwtProvider.verifyToken(user, token)) {
                throw new UnauthorizedException("You token invalid");
            }
            if(hasAccess(roles, jwt.getPayload().getRole())) {
                return javaPoint.proceed();
            }
            throw new NoPermissionException("Not enough permission, check you role");
        }

        throw new UnauthorizedException("You auth type not supported");
    }

    private boolean hasAccess(Role[] roles, Role userRole) {
        if (roles.length == 0) {
            return true;
        }
        if(Arrays.stream(roles).anyMatch(role -> role.getPriority() >= userRole.getPriority())) {
            return true;
        }

        return false;
    }

}
