package Application;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ISBNServiceClassTest {
    private ISBNServiceClass isbnservice = new ISBNServiceClass();
    ImmutablePair<String, Integer> twoStates = ImmutablePair.of("2 States: The Story of My Marriage", 120);

    @Test
    public void testParseInfoForISBNForValidISBN(){
        assertEquals(twoStates, isbnservice.parseInfoForISBN("8129115301"));
    }

    @Test
    public void testParseInfoForISBNForInValidISBN() {
        try {
            isbnservice.parseInfoForISBN("8129155301");
        } catch (Exception e) {
            assertEquals("Unable to connect to Amazon.com for 8129155301", e.getMessage());
        }
    }

    @Test
    public void testgetTitleOfTheBookForValidISBN(){
        String html = "<span id=\"productTitle\" class=\"a-size-large\">2 States: The Story of My Marriage</span></div>";
        Document doc = Jsoup.parse(html);
        assertEquals("2 States: The Story of My Marriage", isbnservice.getTitleOfTheBook(doc));
    }

    @Test
    public void testgetNumberOfReviewsForValidISBN(){
        String html = " <span id=\"acrCustomerReviewText\" class=\"a-size-base\">120 customer reviews</span>";
        Document doc = Jsoup.parse(html);
        assertEquals(120, isbnservice.getNumberOfReviews(doc));
    }
}
