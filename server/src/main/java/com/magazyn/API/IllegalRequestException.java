package com.magazyn.API;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalRequestException extends RuntimeException{

    /**
     *
     */
    private static final long serialVersionUID = 112790013003074550L;
    
}
