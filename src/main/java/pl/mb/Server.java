package pl.mb;

import java.util.ArrayList;
import java.util.List;

public class Server {

    private int capacity;
    private List<Vm> vms;

    public Server(int capacity) {
        this.capacity = capacity;
        this.vms = new ArrayList<Vm>();
    }

    public Percent load() {
        if(vms.isEmpty()){
            return Percent.zero();
        }

        int totalSize = 0;
        for (Vm vm : vms) {
            totalSize += vm.size();
        }

        return new Percent((totalSize / capacity) * 100);
    }

    public void addVm(Vm vm) {
        vms.add(vm);
    }

    public boolean contains(Vm vm) {
        return vms.contains(vm);
    }
}
