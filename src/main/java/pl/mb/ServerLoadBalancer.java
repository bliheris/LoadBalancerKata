package pl.mb;

import java.util.*;

public class ServerLoadBalancer {

    private List<Server> servers;
    private Vm[] vms;

    public ServerLoadBalancer(Server[] s, Vm[] vms) {
        this.vms = vms;
        this.servers = Arrays.asList(s);
        sortServersByLoadAscending();
    }

    private void sortServersByLoadAscending() {
        Collections.sort(servers, new Comparator<Server>() {
            @Override
            public int compare(Server o1, Server o2) {
                return o1.load().compareTo(o2.load());
            }
        });
    }

    public void balance() {
        for (Server server : servers) {
            for (Vm vm : vms) {
                if(server.cannotTake(vm)){
                    continue;
                }
                server.addVm(vm);
            }
        }
    }
}
