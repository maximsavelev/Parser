package parser;

import model.Word;

import java.util.List;

public interface WebPageParser {

    int getUniqueWordsCount();

    int getCountOfWords();

    List<Word> getWords();
}