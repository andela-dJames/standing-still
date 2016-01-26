package com.andela.standingstill.activity;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.andela.standingstill.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ActivityTests {

    @Rule
    public ActivityTestRule<AppLauncherActivity> mActivityRule = new ActivityTestRule(AppLauncherActivity.class);

    @Test
    public void listGoesOverTheFold() {
        onView(withId(R.id.parent_view));
    }

    @Test
    public void initialTimerState() {
        onView(withId(R.id.hourText)).check(matches(withText("00")));
        onView(withId(R.id.secondText)).check(matches(withText("00")));
        onView(withId(R.id.minuteText)).check(matches(withText("00")));
    }


}
