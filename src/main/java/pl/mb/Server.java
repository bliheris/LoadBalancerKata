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
        return Percent.make(takenCapacity(), capacity);
    }

    public void addVm(Vm vm) {
        vms.add(vm);
    }

    public boolean contains(Vm vm) {
        return vms.contains(vm);
    }

    public int vmCount() {
        return vms.size();
    }

    public boolean cannotTake(Vm vm) {
        return takenCapacity() + vm.size() > capacity;
    }

    private int takenCapacity(){
        int res = 0;
        for (Vm vm : vms) {
            res += vm.size();
        }
        return res;
    }
}
