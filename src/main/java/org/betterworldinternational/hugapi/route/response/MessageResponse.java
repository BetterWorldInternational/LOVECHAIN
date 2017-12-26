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

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Field field = (Field) o;

            if (key != null ? !key.equals(field.key) : field.key != null) {
                return false;
            }

            return message != null ? message.equals(field.message) : field.message == null;
        }

        @Override
        public int hashCode() {
            int result = key != null ? key.hashCode() : 0;
            result = 31 * result + (message != null ? message.hashCode() : 0);
            return result;
        }
    }
}