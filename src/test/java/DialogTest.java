import com.buaa.texaspoker.client.gui.BettingDialog;
import com.buaa.texaspoker.network.play.SPacketRequestBetting;
import org.junit.Test;

import javax.swing.*;

public class DialogTest {

    @Test
    public void test() throws InterruptedException {
        JFrame frame = new JFrame();
        frame.setSize(1280, 720);
        frame.setVisible(true);

        BettingDialog dialog = new BettingDialog(frame, new SPacketRequestBetting(null, false, 0, 20),true);
        dialog.setVisible(true);
        dialog.dispose();
        System.out.println(dialog.getValue());
    }
}
