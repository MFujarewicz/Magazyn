package com.magazyn.API.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class WrongPlaceException extends RuntimeException {
    /**
     *
     */
}
