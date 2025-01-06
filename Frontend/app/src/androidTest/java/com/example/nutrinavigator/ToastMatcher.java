/*import android.view.View;
import android.widget.Toast;

import androidx.test.espresso.matcher.BoundedMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Custom matcher to check Toast messages
 */
/*
public class ToastMatcher {

    public static Matcher<View> withText(final String expectedText) {
        return new BoundedMatcher<View, Toast>(Toast.class) {
            @Override
            protected boolean matchesSafely(Toast toast) {
                // Get the Toast's message using android.R.id.message
                View toastView = toast.getView();
                CharSequence message = (CharSequence) toastView.findViewById(android.R.id.message).getText();
                return message != null && message.toString().contains(expectedText);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with toast text: ").appendValue(expectedText);
            }
        };
    }
}

 */

