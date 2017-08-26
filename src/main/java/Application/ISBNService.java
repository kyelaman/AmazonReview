package Application;

import org.apache.commons.lang3.tuple.ImmutablePair;

public interface ISBNService {

    ImmutablePair<String,Integer> parseInfoForISBN(String ISBNNumber);
}

