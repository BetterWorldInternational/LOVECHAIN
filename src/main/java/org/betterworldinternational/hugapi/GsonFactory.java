package org.betterworldinternational.hugapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class GsonFactory {
    public Gson create() {
        return new GsonBuilder().create();
    }
}