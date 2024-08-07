package de.industrieschule.vp.core.exceptions;

public class InvalidSessionException extends IllegalArgumentException{
    public InvalidSessionException(String s) {
        super(s);
    }
}
