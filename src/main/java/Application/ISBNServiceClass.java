package Application;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;


public class ISBNServiceClass implements ISBNService{

    public ImmutablePair<String,Integer> parseInfoForISBN(String ISBNNumber)
    {
        try {
            Document htmlcontent = Jsoup.connect("http://www.amazon.com/exec/obidos/ASIN/" + ISBNNumber).get();
            return ImmutablePair.of(getTitleOfTheBook(htmlcontent.getElementById("productTitle")), getNumberOfReviews(htmlcontent.getElementById("acrCustomerReviewText")));
        }
        catch (IOException ex) {
            throw new RuntimeException("Unable to connect to Amazon.com for " + ISBNNumber); 
        }
    }

    public String getTitleOfTheBook(Element htmlcontent){
        Elements titleTags = htmlcontent.getElementsByTag("span");
        return titleTags.select("span").first().text();
    }

    public int getNumberOfReviews(Element htmlContent){
        Elements noOfReviewsHtmlTags = htmlContent.getElementsByTag("span");
        String noOfReviews[] = noOfReviewsHtmlTags.select("span").first().text().split("\\s");
        return Integer.parseInt(noOfReviews[0]);
    }
}
