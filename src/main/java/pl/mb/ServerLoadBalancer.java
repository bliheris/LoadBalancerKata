package pl.mb;

import java.util.*;

public class ServerLoadBalancer {

    private List<Server> servers;
    private Vm[] vms;

    public ServerLoadBalancer(Server[] s, Vm[] vms) {
        this.servers = Arrays.asList(s);
        Collections.sort(servers, new Comparator<Server>() {
            @Override
            public int compare(Server o1, Server o2) {
                return o1.load().compareTo(o2.load());
            }
        });
        this.vms = vms;
    }

    public void balance() {
        for (Server server : servers) {
            for (Vm vm : vms) {
                server.addVm(vm);
            }
        }
    }
}
