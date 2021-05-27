import com.buaa.texaspoker.client.gui.GameFrame;
import org.junit.Test;

public class GameFrameTest {

    @Test(timeout = 2000)
    public void test() throws InterruptedException {
        GameFrame gameFrame = new GameFrame("test frame", null);
        gameFrame.setVisible(true);
        Thread.sleep(1000);
    }
}
