package org.conor10.web3j.protocol.jsonrpc20;


import java.util.List;

public class Request {
    private String jsonRpc = "2.0";
    private String method;
    private List<String> params;
    private long id;

    public Request() { }

    public Request(String method, List<String> params, long id) {
        this.method = method;
        this.params = params;
        this.id = id;
    }

    public String getJsonRpc() {
        return jsonRpc;
    }

    public void setJsonRpc(String jsonRpc) {
        this.jsonRpc = jsonRpc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
