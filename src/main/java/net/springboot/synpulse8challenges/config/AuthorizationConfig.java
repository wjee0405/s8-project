package net.springboot.synpulse8challenges.config;

import lombok.extern.log4j.Log4j2;
import net.springboot.synpulse8challenges.utilities.ResponseUtility;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Configuration
@Log4j2
@Aspect
public class AuthorizationConfig {

    @Pointcut("execution(* net.springboot.synpulse8challenges.controllers.*.*(..))")
    public void controller() {

    }

    @Around("controller()")
    public Object authenticateUser(ProceedingJoinPoint jointPoint) {
        Object result = null;
        String authorizeJWT = StringUtils.EMPTY;
        try {
            log.info("Enter here");
            if (!ObjectUtils.isEmpty(RequestContextHolder.getRequestAttributes())) {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                authorizeJWT = request.getHeader("jwtToken");
                if ("admin".equals(authorizeJWT)) {
                    result = jointPoint.proceed();
                } else {
                    result = ResponseUtility.buildResponse(
                            Collections.singletonList("Unauthorized to use API"), HttpStatus.FORBIDDEN, null
                    );
                }
            } else {
                result = ResponseUtility.buildResponse(
                        Collections.singletonList("Unauthorized to use API"), HttpStatus.FORBIDDEN, null
                );
            }
        } catch (Exception ex) {
            log.error("Error in authorizing user.Denied access.", ex);
            result = ResponseUtility.buildResponse(
                    Collections.singletonList("Unauthorized to use API"), HttpStatus.FORBIDDEN, null
            );
        } catch (Throwable ex) {
            log.error("Error in authorizing user.Denied access.", ex);
            result = ResponseUtility.buildResponse(
                    Collections.singletonList("Unauthorized to use API"), HttpStatus.FORBIDDEN, null
            );
        }
        return result;
    }
}
