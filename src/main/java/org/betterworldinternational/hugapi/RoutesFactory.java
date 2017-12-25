package org.betterworldinternational.hugapi;

import com.google.gson.Gson;
import org.betterworldinternational.hugapi.repository.UserRepository;
import org.betterworldinternational.hugapi.route.PageRoute;
import org.betterworldinternational.hugapi.route.UserRoute;
import org.betterworldinternational.hugapi.service.UserService;
import org.betterworldinternational.hugapi.validation.UserValidation;
import spark.ResponseTransformer;

class RoutesFactory {
    private final ResponseTransformer responseTransformer;
    private final Gson gson;

    public RoutesFactory(ResponseTransformer responseTransformer, Gson gson) {
        this.responseTransformer = responseTransformer;
        this.gson = gson;
    }

    public Routes create() {
        UserService userService = new UserService(new UserRepository(new Sql2oFactory().create()));
        return new Routes(
                new UserRoute(userService, new UserValidation(new ValidatorFactory().create()), gson),
                new PageRoute(userService),
                responseTransformer);
    }
}