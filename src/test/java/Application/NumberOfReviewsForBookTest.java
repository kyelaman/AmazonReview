package Application;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.mockito.Mockito;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NumberOfReviewsForBookTest {

    private ReviewCollector reviewCollector;
    private ReviewCollector reviewCollectorObject;

    @Before
    public void init()
    {
        reviewCollectorObject = new ReviewCollector();
    }

    ImmutablePair<String, Integer> mehula = ImmutablePair.of("Meluha", 6000);
    ImmutablePair<String, Integer> fivepoint = ImmutablePair.of("Five Point SomeOne", 1000);
    ImmutablePair<String, Integer> deadwake = ImmutablePair.of("Dead Wake", 5000);
    ImmutablePair<String, Integer> mehulaOtherEdition = ImmutablePair.of("Meluha", 650);

    List<ImmutablePair<String, Integer>> list = new ArrayList<ImmutablePair<String, Integer>>();
    List<ImmutablePair<String, Integer>> expected = new ArrayList<ImmutablePair<String, Integer>>();

    @Test
    public void testForSortingEmptyList() {
        assertEquals(expected, reviewCollector.sortReviews(list));
    }

    @Test
    public void testForSortingListWithThreeItems() {

        list = Arrays.asList(mehula, fivepoint, deadwake);
        expected = Arrays.asList(deadwake, fivepoint, mehula);

        assertEquals(expected, reviewCollector.sortReviews(list));
    }

    @Test
    public void testForSortingListWhichContainsSameTitle() {
        list = Arrays.asList(mehula, fivepoint, mehulaOtherEdition);
        expected = Arrays.asList(fivepoint, mehula, mehulaOtherEdition);

        assertEquals( expected, reviewCollector.sortReviews(list));
    }

    @Test
    public void testNumberOfReviewsForEmptyList(){
        assertEquals(0, reviewCollector.countReviews(list));
    }

    @Test
    public void testNumberOfReviewsForListOfFourElements(){
        list = Arrays.asList(mehula, fivepoint, deadwake);

        assertEquals(12000, reviewCollector.countReviews(list));
    }

    @Test
    public void testgetDetailsWithISBNNumber(){
        ISBNService isbnService = mock(ISBNService.class);
        when(isbnService.parseInfoForISBN("6789590")).thenReturn(mehula);

        reviewCollectorObject.setIsbnService(isbnService);
        ImmutablePair<ImmutablePair<String, Integer>, Exception> record = reviewCollectorObject.getInfoForISBN("6789590");
        ImmutablePair<ImmutablePair<String, Integer>, Exception> expectedrecord = ImmutablePair.of(mehula, null);

        assertEquals(record, expectedrecord);
    }

    @Test
    public void testgetDetailsWithISBNNumberException() {
        ISBNService isbnService = mock(ISBNService.class);
        when(isbnService.parseInfoForISBN("123456")).thenThrow(new RuntimeException("Unable to connect to Amazon.com for 123456"));

        reviewCollectorObject.setIsbnService(isbnService);
        ImmutablePair<ImmutablePair<String, Integer>, Exception> record = reviewCollectorObject.getInfoForISBN("123456");
        ImmutablePair<ImmutablePair<String, Integer>, Exception> expectedrecord =
                ImmutablePair.of(null, new RuntimeException("Unable to connect to Amazon.com for 123456"));

        assertEquals(expectedrecord.getRight().getMessage(), record.getRight().getMessage());
    }

    @Test
    public void testGetReviewForISBNWithThreeValidISBN(){
        ISBNService isbnService = mock(ISBNService.class);
        when(isbnService.parseInfoForISBN("6789590")).thenReturn(mehula);
        when(isbnService.parseInfoForISBN("6780590")).thenReturn(fivepoint);
        when(isbnService.parseInfoForISBN("9780590")).thenReturn(deadwake);

        List<String> isbnlist = Arrays.asList("6789590", "6780590", "9780590");
        reviewCollectorObject.setIsbnService(isbnService);
        ImmutableTriple<List<ImmutablePair<String, Integer>>, List<String>, Integer> record =
                reviewCollectorObject.getReviewsForISBNs(isbnlist);
        ImmutableTriple<List<ImmutablePair<String, Integer>>, List<String>, Integer> expected =
                ImmutableTriple.of(Arrays.asList(deadwake, fivepoint, mehula), new ArrayList<String>(), 12000);

        assertEquals(record, expected);
    }

    @Test
    public void testGetReviewForISBNWithTwoValidOneErrorISBN() {
        ISBNService isbnService = mock(ISBNService.class);
        when(isbnService.parseInfoForISBN("6789590")).thenReturn(mehula);
        when(isbnService.parseInfoForISBN("6780590")).thenReturn(fivepoint);
        when(isbnService.parseInfoForISBN("9780590")).thenThrow(new RuntimeException("Unable to connect to Amazon.com for 9780590"));

        List<String> isbnlist = Arrays.asList("6789590", "6780590", "9780590");
        reviewCollectorObject.setIsbnService(isbnService);
        List<String> expectionMessages = Arrays.asList("Unable to connect to Amazon.com for 9780590");
        ImmutableTriple<List<ImmutablePair<String, Integer>>, List<String>, Integer> record =
                reviewCollectorObject.getReviewsForISBNs(isbnlist);
        ImmutableTriple<List<ImmutablePair<String, Integer>>, List<String>, Integer> expected =
                ImmutableTriple.of(Arrays.asList(fivepoint, mehula), expectionMessages, 7000);

        assertEquals(record, expected);
    }

}