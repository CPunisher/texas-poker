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
            new Poker(9, PokerType.ACE),
            new Poker(3, PokerType.CLUB),
            new Poker(14, PokerType.HEART),
            new Poker(10, PokerType.HEART),
            new Poker(8, PokerType.CLUB),
            new Poker(8, PokerType.HEART),
            new Poker(8, PokerType.DIAMOND)
        );

        List<Poker> list2 = List.of(
                new Poker(9, PokerType.ACE),
                new Poker(3, PokerType.CLUB),
                new Poker(14, PokerType.HEART),
                new Poker(10, PokerType.HEART),
                new Poker(8, PokerType.CLUB),
                new Poker(6, PokerType.HEART),
                new Poker(7, PokerType.DIAMOND)
        );

        assertEquals(-1, comparator.compare(list1, list2));
    }
}
