package be.anees.paesync.exception;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Spring;

import org.springframework.data.mongodb.core.aggregation.BooleanOperators.And;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

                     // This tells Spring, “Hey, this class will watch over all controllers and handle their exceptions in one place.”
@RestControllerAdvice // Think of it as a global safety net for your REST API — any unhandled exception in your controllers that matches one of your @ExceptionHandler methods will be caught here.
public class GlobalExceptionHandler {
    
                                                            // This method is triggered whenever Spring throws a MethodArgumentNotValidException, which happens when:
    @ExceptionHandler(MethodArgumentNotValidException.class)  // You send a request to an endpoint that uses a @Valid object (like your Course class), And one or more validation rules fail (e.g. a blank title or an out-of-range number).
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
// The method returns a ResponseEntity — a Spring wrapper for both a response body and a status code.
// Inside, you’re building a simple JSON object (a Map<String, String>) where:

// the key is the invalid field name (sigle, title, ects, etc.),

// the value is the validation message (like "Le sigle ne peut pas être vide.").
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
