package com.isansc.desafioconcrete;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.isansc.desafioconcrete.assertions.RecyclerViewItemCountAssertion;
import com.isansc.desafioconcrete.view.RepositoryListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class EspressoReloadRequestsTest {

    @Rule
    public ActivityTestRule<RepositoryListActivity> mActivityRule =
            new ActivityTestRule<>(RepositoryListActivity.class);

    @Test
    public void ensureReloading() {
        // before click
        onView(withId(R.id.rcv_repositories)).check(new RecyclerViewItemCountAssertion(30));

        // clicking
        onView(withId(R.id.btn_action_refresh)).perform(click());

        // after click (count must be resetted and the final amount must be the same first 30)
        onView(withId(R.id.rcv_repositories)).check(new RecyclerViewItemCountAssertion(30));
    }
}