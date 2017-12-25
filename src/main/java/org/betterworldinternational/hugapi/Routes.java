package org.betterworldinternational.hugapi;

import org.betterworldinternational.hugapi.route.PageController;
import org.betterworldinternational.hugapi.route.UserRoute;
import spark.ResponseTransformer;

import static spark.Spark.get;
import static spark.Spark.post;

class Routes {
    public Routes(UserRoute userRoute, PageController pageController, ResponseTransformer responseTransformer) {
        post("/api/user/register/", userRoute::register, responseTransformer);
        post("/api/user/didit/", userRoute::didIt, responseTransformer);
        get("/api/user/map/", userRoute::map, responseTransformer);
        get("/api/user/effect/", userRoute::effect, responseTransformer);
    }
}