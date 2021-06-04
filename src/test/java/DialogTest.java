import com.buaa.texaspoker.client.gui.BettingDialog;
import org.junit.Test;

import javax.swing.*;

public class DialogTest {

    @Test
    public void test() throws InterruptedException {
        JFrame frame = new JFrame();
        frame.setSize(1280, 720);
        frame.setVisible(true);

        BettingDialog dialog = new BettingDialog(frame, 0,0, 20, 1560, true);
        dialog.setVisible(true);
        dialog.dispose();
        System.out.println(dialog.getValue());
    }
}
