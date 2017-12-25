package org.betterworldinternational.hugapi;

import com.google.gson.Gson;
import org.betterworldinternational.hugapi.exception.HugException;
import org.betterworldinternational.hugapi.route.response.MessageResponse;

import static spark.Spark.after;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

public class Main {
    public static void main(String[] args) {
        port(80);

        Gson gson = new GsonFactory().create();
        JsonResponseTransformer responseTransformer = new JsonResponseTransformer(gson);
        new RoutesFactory(responseTransformer, gson).create();

        after("/api/*", (req, res) -> {
            res.type("application/json");
            res.header("Server", "BWI Server");
        });

        exception(HugException.class, (e, request, response) -> {
            response.type("application/json");
            response.header("Server", "BWI Server");
            response.status(400);
            MessageResponse messageResponse = new MessageResponse(false, e.getMessage());
            messageResponse.setErrors(e.getErrors());
            response.body(responseTransformer.render(messageResponse));
        });

        exception(RuntimeException.class, (e, request, response) -> {
            e.printStackTrace();
            response.type("application/json");
            response.header("Server", "BWI Server");
            response.status(500);
            MessageResponse messageResponse = new MessageResponse(false, "Something went wrong");
            response.body(responseTransformer.render(messageResponse));
        });

        get("*", (req, res) -> {
            res.status(404);
            return new MessageResponse(false, "Resource not found");
        }, responseTransformer);

        post("*", (req, res) -> {
            res.status(404);
            return new MessageResponse(false, "Resource not found");
        }, responseTransformer);
    }
}