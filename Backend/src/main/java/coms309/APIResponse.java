package coms309;

public class APIResponse<T> {
    private T payload;
    private String errorMessage;

    public APIResponse(T payload, String errorMessage) {
        this.payload = payload;
        this.errorMessage = errorMessage;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getPayload() {
        return payload;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
