package pl.mb;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ServerLoadMatcher extends TypeSafeMatcher<Server> {

    private Percent expectedLoad;

    public static ServerLoadMatcher hasLoadPercentageOf(
            Percent expectedLoad) {
        return new ServerLoadMatcher(expectedLoad);
    }

    public ServerLoadMatcher(Percent expectedLoad) {
        this.expectedLoad = expectedLoad;
    }

    public void describeTo(Description description) {
        description.appendText("a server with load of ")
                .appendValue(expectedLoad);
    }

    @Override
    protected void describeMismatchSafely(Server item, Description description) {
        description.appendText("a server with load of ")
                .appendValue(item.load());
    }

    @Override
    protected boolean matchesSafely(Server server) {
        return server.load().equals(expectedLoad);
    }
}
