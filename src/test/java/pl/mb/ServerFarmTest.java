package pl.mb;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static pl.mb.Percent.hundred;
import static pl.mb.Percent.zero;
import static pl.mb.ServerContentMatcher.contains;
import static pl.mb.ServerContentMatcher.containsOnly;
import static pl.mb.ServerLoadMatcher.hasLoadPercentageOf;

public class ServerFarmTest {

    private Vm[] vms(Vm... vms) {
        return vms;
    }
    private Server[] servers(Server... servers) {
        return servers;
    }

    @Test
    public void balancing_oneServer_noVms_serverStaysEmpty() {
        Server server = new Server(1);
        assertThat(server, hasLoadPercentageOf(zero()));
    }

    @Test
    public void balancingOneServerWithOneSlotCapacity_andOneSlotVm_fillsTheServerWithTheVm() {
        Server server = new Server(1);
        Vm vm = new Vm(1);

        ServerFarm sf = new ServerFarm(servers(server));
        sf.addVms(vms(vm));

        assertThat(server, contains(vm));
        assertThat(server, hasLoadPercentageOf(hundred()));
    }

    @Test
    public void balancingOneServerWithTenSlotsCapacity_andOneSlotVm_fillTheServerWithTenPercent() {
        Server server = new Server(10);
        Vm vm = new Vm(1);

        ServerFarm sf = new ServerFarm(servers(server));
        sf.addVms(vms(vm));

        assertThat(server, contains(vm));
        assertThat(server, hasLoadPercentageOf(new Percent(10)));
    }

    @Test
    public void balancingAServerWithEnoughRoom_getsFilledWithAllVms(){
        Server server = new Server(10);
        Vm firstVm = new Vm(1);
        Vm secondVm = new Vm(2);

        ServerFarm sf = new ServerFarm(servers(server));
        sf.addVms(vms(firstVm, secondVm));

        assertThat(server.vmCount(), is(2));
        assertThat(server, contains(firstVm, secondVm));
        assertThat(server, hasLoadPercentageOf(new Percent(30)));
    }

    @Test
    public void aVm_shouldBeBalanced_onLessLoadedServerFirst(){
        Server moreLoadedServer = new Server(100);
        moreLoadedServer.addVm(new Vm(55));
        Server lessLoadedServer = new Server(100);
        lessLoadedServer.addVm(new Vm(40));
        Vm vm = new Vm(23);

        ServerFarm sf = new ServerFarm(servers(moreLoadedServer, lessLoadedServer));
        sf.addVms(vms(vm));

        assertThat(lessLoadedServer, contains(vm));
        assertThat(lessLoadedServer, hasLoadPercentageOf(new Percent(63)));
    }

    @Test
    public void balanceAServerWithNotEnoughRoom_shouldNotBeFilledWithAVm(){
        Server server = new Server(10);
        Vm oldVm = new Vm(9);
        server.addVm(oldVm);

        ServerFarm sf = new ServerFarm(servers(server));
        Vm newVm = new Vm(2);
        sf.addVms(vms(newVm));

        assertThat(server, containsOnly(oldVm));
        assertThat(server, hasLoadPercentageOf(new Percent(90)));
    }

    @Test
    public void balance_serversAndVms() {
        Server s1 = new Server(4);
        Server s2 = new Server(6);

        Vm vm1 = new Vm(1);
        Vm vm2 = new Vm(4);
        Vm vm3 = new Vm(2);

        ServerFarm sf = new ServerFarm(servers(s1, s2));
        sf.addVms(vms(vm1, vm2, vm3));

        assertThat(s1, containsOnly(vm1, vm3));
        assertThat(s1, hasLoadPercentageOf(new Percent(75)));

        assertThat(s2, containsOnly(vm2));
        assertThat(s2, hasLoadPercentageOf(new Percent(67)));
    }
}
