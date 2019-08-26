package com.example.moxin.future.margerequest;

import java.util.concurrent.CompletableFuture;

public class Request {
    private String id;
    private String name;
    public CompletableFuture completableFuture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CompletableFuture getCompletableFuture() {
        return completableFuture;
    }

    public void setCompletableFuture(CompletableFuture completableFuture) {
        this.completableFuture = completableFuture;
    }
}
