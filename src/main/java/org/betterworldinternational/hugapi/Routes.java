package org.betterworldinternational.hugapi;

import org.betterworldinternational.hugapi.route.PageRoute;
import org.betterworldinternational.hugapi.route.UserRoute;
import org.betterworldinternational.hugapi.util.JsonUtil;

import static spark.Spark.get;
import static spark.Spark.post;

public class Routes {

    public Routes() {
        prepareUserRoutes();
    }

    private static void prepareUserRoutes() {
        UserRoute userRoute = new UserRoute();
        PageRoute pageRoute = new PageRoute();

        post("/api/user/register/", userRoute::register, JsonUtil.json());
        post("/api/user/didit/", userRoute::didIt, JsonUtil.json());
        get("/api/user/map/", userRoute::map, JsonUtil.json());
        get("/api/user/effect/", userRoute::effect, JsonUtil.json());

        //get("/", pageRoute::challengePage);
        get("/challenge", pageRoute::challengePage); //to pageRoute.java, and rendering web page
    }
}
