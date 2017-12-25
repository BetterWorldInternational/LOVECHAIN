package org.betterworldinternational.hugapi.route.response;

import java.util.List;

public class MessageResponse {
    private boolean success;
    private String message;
    private List<Field> errors;

    public MessageResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public MessageResponse(boolean success, String message, List<Field> errors) {
        this.success = success;
        this.message = message;
        this.errors = errors;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Field> getErrors() {
        return errors;
    }

    public void setErrors(List<Field> errors) {
        this.errors = errors;
    }

    public static class Field {
        private String key;
        private String message;

        public Field(String key, String message) {
            this.key = key;
            this.message = message;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}