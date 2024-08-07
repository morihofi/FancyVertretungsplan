package de.industrieschule.vp.exceptions;

public class InsufficientPermissionException extends IllegalArgumentException{
    public InsufficientPermissionException(String s) {
        super(s);
    }
}
