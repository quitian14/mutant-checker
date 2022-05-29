package org.quitian.mutantchecker.exceptions;

/**
 * Exception to notify the mis formatted DNA sequence.
 */
public class BadDNASequenceException extends RuntimeException {
    public BadDNASequenceException(String message) {
        super(message);
    }
}
