package pl.mb;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class LoadBalancerTest {

    @Test
    public void itCompiles() {
        assertThat(true).isEqualTo(true);
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

        new ServerLoadBalancer(server, new Vm[]{vm}).balance();

        assertThat(server.contains(vm)).isTrue();
        assertThat(server.load()).isEqualTo(Percent.hundred());
    }

    @Test
    public void balancingOneServerWithTenSlotsCapacity_andOneSlotVm_fillTheServerWithTenPercent() {
        Server server = new Server(10);
        Vm vm = new Vm(1);

        new ServerLoadBalancer(server, new Vm[]{vm}).balance();

        assertThat(server.contains(vm)).isTrue();
        assertThat(server.load()).isEqualTo(new Percent(10));
    }

    @Test
    public void balancingAServerWithEnoughRoom_getsFilledWithAllVms(){
        Server server = new Server(10);
        Vm firstVm = new Vm(1);
        Vm secondVm = new Vm(2);

        new ServerLoadBalancer(server, new Vm[]{firstVm, secondVm}).balance();

        assertThat(server.vmCount()).isEqualTo(2);
        assertThat(server.contains(firstVm));
        assertThat(server.contains(secondVm));
        assertThat(server.load()).isEqualTo(new Percent(30));
    }
}
