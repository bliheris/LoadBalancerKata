package pl.mb;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ServerContentMatcher extends TypeSafeMatcher<Server> {

    private Vm[] expectedVms;
    private String errorMsg = "";
    private boolean containsOnly;

    public static ServerContentMatcher contains(Vm... vms) {
        return new ServerContentMatcher(false, vms);
    }

    public static ServerContentMatcher containsOnly(Vm... vms) {
        return new ServerContentMatcher(true, vms);
    }

    public ServerContentMatcher(boolean containsOnly, Vm... vms) {
        this.containsOnly = containsOnly;
        this.expectedVms = vms;
    }

    public void describeTo(Description description) {
        description.appendText("a server with Vms ")
                .appendValue(expectedVms);
    }

    @Override
    protected void describeMismatchSafely(Server item, Description description) {
        description.appendText("a server ")
                .appendValue(errorMsg);
    }

    @Override
    protected boolean matchesSafely(Server server) {
        if(containsOnly){
            if(server.vmCount() != expectedVms.length){
                errorMsg += "with different numbers of Vms";
                return false;
            }
        }

        for (Vm vm : expectedVms) {
            if(!server.contains(vm)){
                errorMsg += "does not contain Vm: " + vm;
                return false;
            }
        }
        return true;
    }
}