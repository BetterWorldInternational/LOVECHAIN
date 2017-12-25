package org.betterworldinternational.hugapi;

import org.betterworldinternational.hugapi.route.PageRoute;
import org.betterworldinternational.hugapi.route.UserRoute;
import spark.ResponseTransformer;

import static spark.Spark.get;
import static spark.Spark.post;

class Routes {
    public Routes(UserRoute userRoute, PageRoute pageRoute, ResponseTransformer responseTransformer) {
        post("/api/user/register/", userRoute::register, responseTransformer);
        post("/api/user/didit/", userRoute::didIt, responseTransformer);
        get("/api/user/map/", userRoute::map, responseTransformer);
        get("/api/user/effect/", userRoute::effect, responseTransformer);

        get("/challenge", pageRoute::challengePage); //to pageRoute.java, and rendering web page
    }
}