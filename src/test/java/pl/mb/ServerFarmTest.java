package pl.mb;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.mb.Percent.hundred;
import static pl.mb.Percent.zero;
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
        MatcherAssert.assertThat(server, hasLoadPercentageOf(zero()));
    }

    @Test
    public void balancingOneServerWithOneSlotCapacity_andOneSlotVm_fillsTheServerWithTheVm() {
        Server server = new Server(1);
        Vm vm = new Vm(1);

        ServerFarm sf = new ServerFarm(servers(server));
        sf.addVms(vms(vm));

        assertThat(server.contains(vm)).isTrue();
        MatcherAssert.assertThat(server, hasLoadPercentageOf(hundred()));
    }

    @Test
    public void balancingOneServerWithTenSlotsCapacity_andOneSlotVm_fillTheServerWithTenPercent() {
        Server server = new Server(10);
        Vm vm = new Vm(1);

        ServerFarm sf = new ServerFarm(servers(server));
        sf.addVms(vms(vm));

        assertThat(server.contains(vm)).isTrue();
        MatcherAssert.assertThat(server, hasLoadPercentageOf(new Percent(10)));
    }


    @Test
    public void balancingAServerWithEnoughRoom_getsFilledWithAllVms(){
        Server server = new Server(10);
        Vm firstVm = new Vm(1);
        Vm secondVm = new Vm(2);

        ServerFarm sf = new ServerFarm(servers(server));
        sf.addVms(vms(firstVm, secondVm));

        assertThat(server.vmCount()).isEqualTo(2);
        assertThat(server.contains(firstVm)).isTrue();
        assertThat(server.contains(secondVm)).isTrue();
        MatcherAssert.assertThat(server, hasLoadPercentageOf(new Percent(30)));

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

        assertThat(lessLoadedServer.contains(vm)).isTrue();
        MatcherAssert.assertThat(lessLoadedServer,
                hasLoadPercentageOf(new Percent(63)));
    }

    @Test
    public void balanceAServerWithNotEnoughRoom_shouldNotBeFilledWithAVm(){
        Server server = new Server(10);
        server.addVm(new Vm(9));
        Vm vm = new Vm(2);

        ServerFarm sf = new ServerFarm(servers(server));
        sf.addVms(vms(vm));

        MatcherAssert.assertThat(server, hasLoadPercentageOf(new Percent(90)));

        assertThat(server.contains(vm)).isFalse();
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

        assertThat(s1.contains(vm1)).isTrue();
        assertThat(s1.contains(vm3)).isTrue();
        assertThat(s1.contains(vm2)).isFalse();

        assertThat(s2.contains(vm2)).isTrue();
        assertThat(s2.contains(vm1)).isFalse();
        assertThat(s2.contains(vm3)).isFalse();

        MatcherAssert.assertThat(s1, hasLoadPercentageOf(new Percent(75)));
        MatcherAssert.assertThat(s2, hasLoadPercentageOf(new Percent(67)));
    }
}
