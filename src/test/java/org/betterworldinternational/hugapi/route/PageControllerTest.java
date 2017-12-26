package org.betterworldinternational.hugapi.route;

import org.betterworldinternational.hugapi.service.UserService;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PageControllerTest {
    private static final int HUGS_COUNT = 1;
    private static final int ACTIVATES_COUNT = 2;
    private static final int ALL_EFFECT_COUNT = 3;
    private static final int ALL_ACTIVATES_COUNT = 4;
    private static final String INVITE_CODE = "123ds3";
    private static final String DEFAULT_INVITE = "Keep it empty";

    @Test
    public void challengePageWithoutInvite() {
        PageController pageController = new PageController(createUserService());
        ExtendedModelMap model = new ExtendedModelMap();
        String page = pageController.challengePage(null, model);
        assertThat(page, is(equalTo("challenge")));
        assertThat(model.get("url"), is(equalTo("http://hug.tgcstories.com/challenge?invite=" + DEFAULT_INVITE)));
        assertThat(model.get("invite"), is(equalTo(DEFAULT_INVITE)));
        assertThat(model.get("hugs"), is(equalTo(0)));
        assertThat(model.get("activates"), is(equalTo(0)));
        assertThat(model.get("allEffect"), is(equalTo(ALL_EFFECT_COUNT)));
        assertThat(model.get("allActivates"), is(equalTo(ALL_ACTIVATES_COUNT)));
    }

    @Test
    public void challengePageWithInvite() {
        PageController pageController = new PageController(createUserService());
        ExtendedModelMap model = new ExtendedModelMap();
        String page = pageController.challengePage(INVITE_CODE, model);
        assertThat(page, is(equalTo("challenge")));
        assertThat(model.get("url"), is(equalTo("http://hug.tgcstories.com/challenge?invite=" + INVITE_CODE)));
        assertThat(model.get("invite"), is(equalTo(INVITE_CODE)));
        assertThat(model.get("hugs"), is(equalTo(HUGS_COUNT)));
        assertThat(model.get("activates"), is(equalTo(ACTIVATES_COUNT)));
        assertThat(model.get("allEffect"), is(equalTo(ALL_EFFECT_COUNT)));
        assertThat(model.get("allActivates"), is(equalTo(ALL_ACTIVATES_COUNT)));
    }

    private static UserService createUserService() {
        UserService userService = mock(UserService.class);
        when(userService.getAllEffect()).thenReturn(ALL_EFFECT_COUNT);
        when(userService.getAllActivates()).thenReturn(ALL_ACTIVATES_COUNT);
        when(userService.getEffectByInviteCode(INVITE_CODE)).thenReturn(HUGS_COUNT);
        when(userService.getActivatesByInviteCode(INVITE_CODE)).thenReturn(ACTIVATES_COUNT);
        return userService;
    }
}