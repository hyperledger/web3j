package org.conor10.web3j.protocol.jsonrpc20;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> {
    private long id;
    private String jsonrpc;
    private T result;

    public Response() { }

    public Response(long id, String jsonrpc, T result) {
        this.id = id;
        this.jsonrpc = jsonrpc;
        this.result = result;
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

    @Override
    public String toString() {
        return "SimpleResult{" +
                "id=" + id +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
