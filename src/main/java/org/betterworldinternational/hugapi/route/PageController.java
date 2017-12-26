package org.betterworldinternational.hugapi.route;

import org.betterworldinternational.hugapi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {
    private static final Logger logger = LoggerFactory.getLogger(PageController.class);

    private final UserService userService;

    @Autowired
    public PageController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/challenge", method = RequestMethod.GET)
    public String challengePage(@RequestParam(value = "invite", required = false) String invite, Model model) {
        logger.info("Showing challenge page. Invite = {}", invite);

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

        model.addAttribute("url", "http://hug.tgcstories.com/challenge?invite=" + invite);
        model.addAttribute("invite", invite);
        model.addAttribute("hugs", hugs);
        model.addAttribute("activates", activates);

        model.addAttribute("allEffect", allEffect);
        model.addAttribute("allActivates", allActivates);

        return "challenge";
    }
}