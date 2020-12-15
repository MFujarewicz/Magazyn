package com.magazyn.API.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyDoneException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 8647002923442639568L;
    
}
