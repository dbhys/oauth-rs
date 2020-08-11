package com.dbhys.oauth;


import java.net.URI;

/**
 * Parse exception.
 */
public class ParseException extends Exception{


    /**
     * Creates a new parse exception.
     *
     * @param message The exception message. May be {@code null}.
     */
    public ParseException(final String message) {

        this(message, null);
    }
    /**
     * Creates a new parse exception.
     *
     * @param message The exception message. May be {@code null}.
     * @param cause   The associated error, {@code null} if not specified.
     */
    public ParseException(final String message, final Throwable cause) {

        super(message, cause);
    }


}
