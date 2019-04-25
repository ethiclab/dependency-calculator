package it.ethiclab.depcal;

public class CircularReferenceException extends RuntimeException {
    public CircularReferenceException(String msg) {
        super(msg);
    }
}
