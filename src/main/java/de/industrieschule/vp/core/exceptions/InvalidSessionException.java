package de.industrieschule.vp.exceptions;

public class InvalidSessionException extends IllegalArgumentException{
    public InvalidSessionException(String s) {
        super(s);
    }
}
