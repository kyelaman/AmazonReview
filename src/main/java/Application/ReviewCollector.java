package Application;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import java.util.List;
import static java.util.Comparator.*;
import static java.util.stream.Collectors.toList;

public class ReviewCollector {

    private ISBNService isbnservice;

    public static List<ImmutablePair<String, Integer>> sortReviews(List<ImmutablePair<String, Integer>> records) {
        return records.stream()
                .sorted(comparing(ImmutablePair::getLeft))
                .collect(toList());
    }

    public static int countReviews(List<ImmutablePair<String,Integer>> records) {
        return records.stream()
                .mapToInt(bookrecord -> bookrecord.getRight())
                .sum();
    }

    public  ImmutablePair<ImmutablePair<String, Integer>, Exception> getInfoForISBN(String isbnNumber) {
        try{
            return ImmutablePair.of(isbnservice.parseInfoForISBN(isbnNumber), null);
        }
        catch(Exception ex){
            return ImmutablePair.of(null, ex);
        }
    }

    public ImmutableTriple<List<ImmutablePair<String, Integer>>, List<String>, Integer> getReviewsForISBNs(List<String> isbnList)
    {
         List<ImmutablePair<ImmutablePair<String, Integer>, Exception>> result =
           isbnList.stream()
                   .map(isbn -> getInfoForISBN(isbn))
                   .collect(toList());


         List<ImmutablePair<String, Integer>> validIsbnsBookReviews =
           result.stream()
                 .filter(info -> info.getLeft() != null)
                 .map(ImmutablePair::getLeft)
                 .collect(toList());

         List<String> exceptionMsgs =
           result.stream()
                 .filter(info -> info.getRight() != null)
                 .map(info -> info.getRight().getMessage())
                 .collect(toList());

        return ImmutableTriple.of(sortReviews(validIsbnsBookReviews), exceptionMsgs, countReviews(validIsbnsBookReviews));
    }

    public void setIsbnService(ISBNService service) {
        isbnservice = service;
    }
}