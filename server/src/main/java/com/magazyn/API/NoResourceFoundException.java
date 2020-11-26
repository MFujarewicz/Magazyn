package com.magazyn.API;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoResourceFoundException extends RuntimeException{

    /**
     *
     */
    private static final long serialVersionUID = 2864335521671590080L;
    
}
