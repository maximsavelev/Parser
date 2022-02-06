import model.Word;
import org.junit.jupiter.api.Test;
import parser.Parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTestClass {


    public boolean isWordExist(List<Word> words, String word) {
        return words.stream()
                .anyMatch(o -> o.getWord().equals(word));
    }


    @Test
    void testCountOfNumberIsCorrect() throws IOException {
        String filename = "src/test/resources/test.html";
        Parser parser = new Parser(filename);
        List<Word> words = parser.getWords();
        assertEquals(8, words.size());
        assertTrue(isWordExist(words, "название"));
        assertTrue(isWordExist(words, "страницы"));
        assertTrue(isWordExist(words, "эта"));
        assertTrue(isWordExist(words, "страница"));
        assertTrue(isWordExist(words, "содержит"));
        assertTrue(isWordExist(words, "целых"));
        assertTrue(isWordExist(words, "восемь"));
        assertTrue(isWordExist(words, "слов"));
    }

    @Test
    void excludeWrongSymbols() throws IOException {
        String filename = "src/test/resources/test2.html";
        Parser parser = new Parser(filename);
        List<Word> words = parser.getWords();
        assertEquals(6, words.size());
        assertTrue(isWordExist(words, "название"));
        assertTrue(isWordExist(words, "страницы"));
        assertTrue(isWordExist(words, "здесь"));
        assertTrue(isWordExist(words, "много"));
        assertTrue(isWordExist(words, "лишних"));
        assertTrue(isWordExist(words, "знаков"));
    }

    @Test
    void throwExceptionOnInvalidURL() {
        assertThrows(IOException.class, () -> {
            Parser parser = new Parser("https/invalidURL.ru");
        });
    }

    @Test
    void throwExceptionOnWrongPath() {
        assertThrows(FileNotFoundException.class, () -> {
            Parser parser = new Parser("src/main/resources/notExistedFile.html");
        });
    }

}
