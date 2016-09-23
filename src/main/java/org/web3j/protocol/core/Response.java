package org.web3j.protocol.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> {
    private long id;
    private String jsonrpc;
    private T result;
    private Error error;

    public Response() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public boolean hasError() {
        return error != null;
    }

    public static class Error {
        private int code;
        private String message;
        private String data;

        public Error() {
        }

        public Error(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Error error = (Error) o;

            if (code != error.code) return false;
            return message != null ? message.equals(error.message) : error.message == null;

        }

        @Override
        public int hashCode() {
            int result = code;
            result = 31 * result + (message != null ? message.hashCode() : 0);
            return result;
        }
    }
}
