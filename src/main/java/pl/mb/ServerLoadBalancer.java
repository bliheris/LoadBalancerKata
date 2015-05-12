package pl.mb;

public class ServerLoadBalancer {

    private Server server;
    private Vm[] vms;

    public ServerLoadBalancer(Server server, Vm[] vms) {
        this.server = server;
        this.vms = vms;
    }

    public void balance() {
        for (Vm vm : vms) {
            server.addVm(vm);
        }
    }
}
