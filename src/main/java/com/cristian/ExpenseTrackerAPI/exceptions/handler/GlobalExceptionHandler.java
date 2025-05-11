package com.cristian.ExpenseTrackerAPI.exceptions.handler;

import com.cristian.ExpenseTrackerAPI.exceptions.ErrorMessage;
import com.cristian.ExpenseTrackerAPI.exceptions.ExpenseNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGenericException(Exception e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleExpenseNotFoundException(ExpenseNotFoundException e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String clientMessage = "VALIDATION ERROR: " + ex.getMostSpecificCause().getMessage();
        ErrorMessage errorMessage = new ErrorMessage(clientMessage, HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ErrorMessage> errorMessages = getValidationErrorList(ex.getBindingResult());
        return handleExceptionInternal(ex, errorMessages, headers, HttpStatus.BAD_REQUEST, request);
    }

    private List<ErrorMessage> getValidationErrorList(BindingResult bindingResult) {
        List<ErrorMessage> errorMessages = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String msg = "VALIDATION ERROR: " + fieldError.getDefaultMessage();
            errorMessages.add(new ErrorMessage(msg, 400));
        }
        return errorMessages;
    }
}

