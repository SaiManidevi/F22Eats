package com.example.android.f22eats;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.f22eats.activities.CartActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class CartActivityScreenTest {
    private static final String COUPON_CODE = "F22LABS";
    private static final String SNACKBAR_TEXT = "Yay! Coupon applied";

    @Rule
    public ActivityTestRule<CartActivity> cartActivityActivityTestRule = new ActivityTestRule<>(CartActivity.class);

    @Test
    public void checkSnackbar_whenCorrectCoupon() {
        onView(withId(R.id.et_coupon)).perform(clearText(),
                typeText(COUPON_CODE), closeSoftKeyboard());

        onView(withId(R.id.btn_apply_coupon)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(SNACKBAR_TEXT)))
                .check(matches(isDisplayed()));
    }
}
