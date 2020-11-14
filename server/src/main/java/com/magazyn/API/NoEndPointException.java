package com.magazyn.API;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
public class NoEndPointException extends RuntimeException{

    /**
     *
     */
    private static final long serialVersionUID = 7710234606118823470L;
    
}
