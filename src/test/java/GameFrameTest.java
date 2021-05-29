import com.buaa.texaspoker.client.GameClient;
import com.buaa.texaspoker.client.gui.GameFrame;
import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.PokerType;
import com.buaa.texaspoker.entity.player.ClientPlayer;
import com.buaa.texaspoker.entity.Room;
import org.junit.Test;

import javax.swing.*;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class GameFrameTest {

    @Test(timeout = 10000)
    public void test() throws InterruptedException {
        GameClient client = mock(GameClient.class);

        Room room = new Room();
        for (int i = 0; i < 10; i++) {
            room.getPlayerList().add(new ClientPlayer(UUID.randomUUID(), "test" + i, client));
        }
        room.getPublicPokers().add(new Poker(2, PokerType.ACE));
        room.getPublicPokers().add(new Poker(4, PokerType.HEART));
        room.getPublicPokers().add(new Poker(14, PokerType.DIAMOND));

        when(client.getRoom()).thenReturn(room);
        GameFrame gameFrame = new GameFrame("test frame", client);
        gameFrame.setVisible(true);
        Thread.sleep(5000);
    }
}
