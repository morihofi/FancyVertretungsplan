package de.industrieschule.vp.core.exceptions;

public class InsufficientPermissionException extends IllegalArgumentException{
    public InsufficientPermissionException(String s) {
        super(s);
    }
}
