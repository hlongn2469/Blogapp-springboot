package com.springboot.blog.exception;

import com.springboot.blog.payload.ErrorDetails;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ResourceNotFountException.class)
    // custom
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFountException exception,
                                                                         WebRequest web_request){
        ErrorDetails details = new ErrorDetails(new Date(), exception.getMessage(), web_request.getDescription(false));
        return new ResponseEntity<>(details, HttpStatus.NOT_FOUND);
    }

    // custom
    @ExceptionHandler(BlogAPIException.class)
    public ResponseEntity<ErrorDetails> handleBlogAPIException(BlogAPIException exception,
                                                                        WebRequest web_request){
        ErrorDetails details = new ErrorDetails(new Date(), exception.getMessage(), web_request.getDescription(false));
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

    // global
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception,
                                                               WebRequest web_request){
        ErrorDetails details = new ErrorDetails(new Date(), exception.getMessage(), web_request.getDescription(false));
        return new ResponseEntity<>(details, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error)->{
            String field_name = ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            errors.put(field_name, message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    // custom
//    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
//                                                                        WebRequest web_request){
//        Map<String, String> errors = new HashMap<>();
//        exception.getBindingResult().getAllErrors().forEach((error)->{
//            String field_name = ((FieldError)error).getField();
//            String message = error.getDefaultMessage();
//            errors.put(field_name, message);
//        });
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }
}
