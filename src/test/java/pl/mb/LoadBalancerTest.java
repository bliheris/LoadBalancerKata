package pl.mb;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class LoadBalancerTest {

    private Vm[] vms(Vm... vms) {
        return vms;
    }
    private Server[] servers(Server... servers) {
        return servers;
    }

    @Test
    public void balancing_oneServer_noVms_serverStaysEmpty() {
        Server server = new Server(1);
        assertThat(server.load()).isEqualTo(Percent.zero());
    }

    @Test
    public void balancingOneServerWithOneSlotCapacity_andOneSlotVm_fillsTheServerWithTheVm() {
        Server server = new Server(1);
        Vm vm = new Vm(1);

        new ServerLoadBalancer(new Server[]{server}, new Vm[]{vm}).balance();

        assertThat(server.contains(vm)).isTrue();
        assertThat(server.load()).isEqualTo(Percent.hundred());
    }

    @Test
    public void balancingOneServerWithTenSlotsCapacity_andOneSlotVm_fillTheServerWithTenPercent() {
        Server server = new Server(10);
        Vm vm = new Vm(1);

        new ServerLoadBalancer(new Server[]{server}, new Vm[]{vm}).balance();

        assertThat(server.contains(vm)).isTrue();
        assertThat(server.load()).isEqualTo(new Percent(10));
    }

    @Test
    public void balancingAServerWithEnoughRoom_getsFilledWithAllVms(){
        Server server = new Server(10);
        Vm firstVm = new Vm(1);
        Vm secondVm = new Vm(2);

        new ServerLoadBalancer(new Server[]{server}, new Vm[]{firstVm, secondVm}).balance();

        assertThat(server.vmCount()).isEqualTo(2);
        assertThat(server.contains(firstVm)).isTrue();
        assertThat(server.contains(secondVm)).isTrue();
        assertThat(server.load()).isEqualTo(new Percent(30));
    }

    @Test
    public void aVm_shouldBeBalanced_onLessLoadedServerFirst(){
        Server moreLoadedServer = new Server(100);
        moreLoadedServer.addVm(new Vm(55));
        Server lessLoadedServer = new Server(100);
        lessLoadedServer.addVm(new Vm(40));
        Vm vm = new Vm(23);

        new ServerLoadBalancer(servers(moreLoadedServer, lessLoadedServer),
                vms(vm)).balance();

        assertThat(lessLoadedServer.contains(vm)).isTrue();
        assertThat(lessLoadedServer.load()).isEqualTo(new Percent(63));
    }

    @Test
    public void balanceAServerWithNotEnoughRoom_shouldNotBeFilledWithAVm(){
        Server server = new Server(10);
        server.addVm(new Vm(9));
        Vm vm = new Vm(2);

        new ServerLoadBalancer(servers(server), vms(vm)).balance();
        assertThat(server.load()).isEqualTo(new Percent(90));
        assertThat(server.contains(vm)).isFalse();
    }

    @Test
    public void balance_serversAndVms() {
        Server s1 = new Server(4);
        Server s2 = new Server(6);

        Vm vm1 = new Vm(1);
        Vm vm2 = new Vm(4);
        Vm vm3 = new Vm(2);

        new ServerLoadBalancer(
                servers(s1, s2), vms(vm1, vm2, vm3)).balance();

        assertThat(s1.contains(vm1)).isTrue();
        assertThat(s1.contains(vm3)).isTrue();
        assertThat(s1.contains(vm2)).isFalse();

        assertThat(s2.contains(vm2)).isTrue();
        assertThat(s2.contains(vm1)).isFalse();
        assertThat(s2.contains(vm3)).isFalse();

        assertThat(s1.load()).isEqualTo(new Percent(75));
        assertThat(s2.load()).isEqualTo(new Percent(67));
    }
}
