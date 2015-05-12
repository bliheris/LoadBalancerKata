package pl.mb;

import java.util.*;

public class ServerLoadBalancer {

    private ArrayList<Server> servers;
    private ArrayList<Vm> vms;

    public ServerLoadBalancer(Server[] s, Vm[] vms) {
        this.vms = new ArrayList<Vm>();
        this.vms.addAll(Arrays.asList(vms));
        this.servers = new ArrayList<Server>();
        this.servers.addAll(Arrays.asList(s));
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
            for(Iterator<Vm> it = vms.iterator();it.hasNext();){
                Vm vm = it.next();
                if(server.cannotTake(vm)){
                    continue;
                }
                server.addVm(vm);
                it.remove();
            }
        }
    }
}
