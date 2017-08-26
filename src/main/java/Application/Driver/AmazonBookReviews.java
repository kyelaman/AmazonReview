package Application.Driver;

import Application.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class AmazonBookReviews {

    public  static void displaySummary( ImmutableTriple<List<ImmutablePair<String, Integer>>, List<String>, Integer> summaryOfAuthorsBook)
    {
        System.out.println("List of the books and Number of Reviews:");
        for(ImmutablePair<String, Integer> record : summaryOfAuthorsBook.getLeft())
            System.out.println("Book Title: "+record.getLeft()+" Number of Reviews:"+record.getRight());
        System.out.println("\nTotal Number of Reviews: "+summaryOfAuthorsBook.getRight() );
        System.out.println("\nException Messages\n");
        for(String msg : summaryOfAuthorsBook.getMiddle())
            System.out.println(msg);
        return;
    }

    public static void main(String args[]) {

        try {
            ReviewCollector reviewCollector = new ReviewCollector();
            ISBNServiceClass isbnServiceClass = new ISBNServiceClass();
            List<String> listOfIsbns = Files.lines(Paths.get("..\\assign2\\src\\inputFile.txt")).collect(Collectors.toList());
            reviewCollector.setIsbnService(isbnServiceClass);
            displaySummary(reviewCollector.getReviewsForISBNs(listOfIsbns));
        }
        catch (IOException ex){
            throw new RuntimeException("Unable to open the file");
        }
    }
}
