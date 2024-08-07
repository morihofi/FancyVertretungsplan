package de.industrieschule.vp.responseTempates;

public class SimpleStatusResponse {
    private String message;

    public SimpleStatusResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

