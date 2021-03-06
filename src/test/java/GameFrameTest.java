import com.buaa.texaspoker.client.GameClient;
import com.buaa.texaspoker.client.gui.GameFrame;
import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.PokerType;
import com.buaa.texaspoker.entity.player.ClientPlayer;
import com.buaa.texaspoker.entity.Room;
import com.buaa.texaspoker.entity.player.Player;
import com.buaa.texaspoker.util.message.TextMessage;
import org.junit.Test;

import javax.swing.*;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class GameFrameTest {

    @Test(timeout = 10000)
    public void test() throws InterruptedException {
        GameClient client = mock(GameClient.class);

        Room room = new Room();
        ClientPlayer player = new ClientPlayer(UUID.randomUUID(), "CPunisher", client);
        player.getData().getPokers().add(new Poker(11, PokerType.ACE));
        for (int i = 0; i < 10; i++) {
            room.getPlayerList().add(new ClientPlayer(UUID.randomUUID(), "test" + i, client));
        }
        room.getPublicPokers().add(new Poker(2, PokerType.ACE));
        room.getPublicPokers().add(new Poker(4, PokerType.HEART));
        room.getPublicPokers().add(new Poker(14, PokerType.DIAMOND));

        when(client.getPlayer()).thenReturn(player);
        when(client.getRoom()).thenReturn(room);
        GameFrame gameFrame = new GameFrame("test frame", client);
        when(client.getGui()).thenReturn(gameFrame);
        gameFrame.setVisible(true);
        for (int i = 0; i < 50; i++) {
            client.getGui().getMessagePanel().printMessage(new TextMessage("Test Msg"));
        }

//        Thread.sleep(5000);
    }
}
