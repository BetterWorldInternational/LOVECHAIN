package org.betterworldinternational.hugapi.route;

import org.betterworldinternational.hugapi.service.UserService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class PageRoute {
    private final UserService userService;

    public PageRoute(UserService userService) {
        this.userService = userService;
    }

    public String challengePage(Request request, Response response) {
        Map<String, Object> model = new HashMap<>();

        String invite = request.queryParams("invite");
        int hugs = 0;
        int activates = 0;

        int allEffect = userService.getAllEffect();
        int allActivates = userService.getAllActivates();

        if (invite == null) {
            invite = "Keep it empty";
        } else {
            hugs = userService.getEffectByInviteCode(invite);
            activates = userService.getActivatesByInviteCode(invite);
        }

        model.put("url", "http://hug.tgcstories.com/challenge?invite=" + invite);
        model.put("invite", invite);
        model.put("hugs", hugs);
        model.put("activates", activates);

        model.put("allEffect", allEffect);
        model.put("allActivates", allActivates);

        return new ThymeleafTemplateEngine().render(new ModelAndView(model, "challenge"));
    }
}