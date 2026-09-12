package com.rata.userService.errorHandling;

import com.rata.userService.records.ResponseResult;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.PropertyValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
class ControlAdvice extends ResponseEntityExceptionHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(ControlAdvice.class);
    private final Environment env;

    public ControlAdvice(Environment env) {
        this.env = env;
    }

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<?> handleAccessDeniedException(SQLIntegrityConstraintViolationException ex) {
       ex.printStackTrace();
        ResponseResult result = null;
        if (ex.getErrorCode() == 1062) {
            String filedName = StringUtils.substringBetween(ex.getMessage(), "'", "'");
            result = new ResponseResult(env.getProperty("error.sql_duplicate_entry"),filedName);
        }
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({PropertyValueException.class})
    public ResponseEntity<?> handlePropertyValueException(PropertyValueException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(new ResponseResult(env.getProperty("error.invalid_input_data"),null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({SQLException.class})
    public ResponseEntity<?> handleSqlExceptionHelper(SQLException ex) {
        ResponseResult ResponseResult = null;
        if (ex.getMessage().startsWith("Data truncation: Data too long for column")) {
            String filedName = StringUtils.substringBetween(ex.getMessage(), "'", "'");
            ResponseResult = new ResponseResult(env.getProperty("error.sql_data_too_long"), filedName);
        }
        return new ResponseEntity<>(ResponseResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NumberFormatException.class})
    public ResponseEntity<?> handleNumberFormatException(NumberFormatException ex) {
        ex.printStackTrace();
        LOGGER.error("Error ->" + ex.getMessage());
        return new ResponseEntity<>(new ResponseResult(env.getProperty("error.number_format"), null), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        ex.printStackTrace();
        LOGGER.error("Error ->" + ex.getMessage());
        return new ResponseEntity<>(new ResponseResult(env.getProperty(ex.getMessage()), null), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        LOGGER.error("Error ->" + ex.getMessage());
        return new ResponseEntity<>(new ResponseResult(env.getProperty("error.username_not_found"), null), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<?> handleNullPointerException(NullPointerException ex) {
        ex.printStackTrace();
        LOGGER.error("Error ->" + ex.getMessage());
        return new ResponseEntity<>(new ResponseResult(env.getProperty("error.invalid_input_data"),null), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({AuthenticationServiceException.class})
    public ResponseEntity<?> handleAuthenticationServiceException(AuthenticationServiceException ex) {
        ex.printStackTrace();
        LOGGER.error("Error ->" + ex.getMessage());
        return new ResponseEntity<>(new ResponseResult(env.getProperty(ex.getMessage()),null), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException ex) {
        ex.printStackTrace();
        LOGGER.error("Error ->" + ex.getMessage());
        return new ResponseEntity<>(new ResponseResult(env.getProperty("error.session_expire"),null), HttpStatus.FORBIDDEN);
    }
}
