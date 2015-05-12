package pl.mb;

public class ServerLoadBalancer {

    private Server server;
    private Vm vm;

    public ServerLoadBalancer(Server server, Vm vm) {
        this.server = server;
        this.vm = vm;
    }

    public void balance() {
        server.addVm(vm);
    }
}
