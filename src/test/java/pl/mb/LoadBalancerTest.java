package pl.mb;

import org.assertj.core.api.Assertions;
import org.junit.Test;

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

/*    @Test
    public void balancingOneServerWithOneSlotCapacity_andOneSlotVm_fillsTheServerWithTheVm() {
        Server server = new Server(1);
        Vm vm = new Vm(1);

        assertThat(server.load()).isEqualTo(Percent.hundred());
    }*/
}
