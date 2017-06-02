package com.example.raymond.barbro;

/**
 * Created by raymond on 6/1/17.
 */
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    /** Launches {@link MainActivity} for every test */
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Test that clicking on a Navigation Drawer Item will open the correct fragment.
     * Espresso: openDrawer, onView, withText, perform, click, matches, check, isDisplayed
     */


    @Test
    public void testAllDrinksRecyclerView() {

        onView(withId(R.id.recyclerview_drinks)).perform(

                // First position the recycler view. Necessary to allow the layout
                // manager perform the scroll operation
                RecyclerViewActions.scrollToPosition(3),

                // Click the item to trigger navigation to detail view
                RecyclerViewActions.actionOnItemAtPosition(3, click())
        );
        onView(withId(R.id.header)).check(matches(withText("76")));
    }

    /**
     * Test opening the Navigation Drawer and pressing the back button.
     * Espresso: openDrawer, pressBack
     */
    /*@Test
    public void testNavigationDrawerBackButton() {
        openDrawer(R.id.drawer_layout);
        pressBack();
    }*/




}



