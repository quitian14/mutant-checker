package org.quitian.mutantchecker.exceptions;

import org.quitian.mutantchecker.model.api.responses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(IsMutantException.class)
    public ErrorResponse handleMutantException(IsMutantException exception) {
        exception.printStackTrace();
        return new ErrorResponse("IS_HUMAN", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(BadDNASequenceException.class)
    public ErrorResponse handleBadDNAException(BadDNASequenceException exception) {
        exception.printStackTrace();
        return new ErrorResponse("BAD_DNA", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception exception) {
        exception.printStackTrace();
        return new ErrorResponse("500", exception.getMessage());
    }
}