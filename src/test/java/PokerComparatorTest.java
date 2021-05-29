import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.PokerType;
import com.buaa.texaspoker.util.PokerComparator;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PokerComparatorTest {

    @Test
    public void test() {
        PokerComparator comparator = new PokerComparator();
        List<Poker> list1 = List.of(
            new Poker(14, PokerType.ACE),
            new Poker(13, PokerType.ACE),
            new Poker(12, PokerType.ACE),
            new Poker(11, PokerType.ACE),
            new Poker(10, PokerType.ACE),
            new Poker(9, PokerType.ACE),
            new Poker(8, PokerType.ACE)
        );

        List<Poker> list2 = List.of(
                new Poker(14, PokerType.ACE),
                new Poker(13, PokerType.ACE),
                new Poker(12, PokerType.ACE),
                new Poker(11, PokerType.ACE),
                new Poker(10, PokerType.ACE),
                new Poker(9, PokerType.ACE),
                new Poker(8, PokerType.ACE)
        );

        assertEquals(0, comparator.compare(list1, list2));
    }
}
