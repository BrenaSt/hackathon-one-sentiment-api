package com.sentimentapi.exception;

/**
 * Exceção lançada quando há erro na comunicação com o DS Service.
 */
public class DsServiceException extends RuntimeException {

    public DsServiceException(String message) {
        super(message);
    }

    public DsServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
