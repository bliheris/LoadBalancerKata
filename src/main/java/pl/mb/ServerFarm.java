package pl.mb;

import java.util.*;

public class ServerFarm {

    private List<Server> servers;

    public ServerFarm(Server[] s) {
        this.servers = Arrays.asList(s);
    }

    public void addVms(Vm[] v){
        ArrayList<Vm> vms = new ArrayList<Vm>();
        vms.addAll(Arrays.asList(v));

        sortServersByLoadAscending();
        for (Server server : servers) {
            addVmsToServer(server, vms);
        }
    }

    private void addVmsToServer(Server server, ArrayList<Vm> vms) {
        for(Iterator<Vm> it = vms.iterator();it.hasNext();){
            Vm vm = it.next();
            if(server.cannotTake(vm)){
                continue;
            }
            server.addVm(vm);
            it.remove();
        }
    }

    private void sortServersByLoadAscending() {
        Collections.sort(servers, new Comparator<Server>() {
            @Override
            public int compare(Server o1, Server o2) {
                return o1.load().compareTo(o2.load());
            }
        });
    }
}
