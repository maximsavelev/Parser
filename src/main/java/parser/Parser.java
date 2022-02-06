package parser;

import model.Word;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Parser implements WebPageParser {

    private final String webPageRegex =
            "(http|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?";
    private static final Logger LOGGER = LogManager.getLogger(Parser.class);

    private List<Word> words;
    private String url;
    private Document document;
    private int countOfWords;


    public Parser(String url) throws IOException {
        validateUrl(url);
        this.url = url;
        getPageWords();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void printStatistics() {
        for (Word word : words) {
            System.out.println(word);
        }
        System.out.printf("Всего слов: %s \nУникальных слов: %s\nСтатистика была собрана по адресу: %s",
                getCountOfWords(), getUniqueWordsCount(), url);
    }

    @Override
    public int getUniqueWordsCount() {
        return words.size();
    }

    @Override
    public int getCountOfWords() {
        return countOfWords;
    }

    @Override
    public List<Word> getWords() {
        return words;
    }

    private void getPageWords() {
        String[] strings = parseDocument(document);
        words = ConvertStringArrayToWordList(strings);
        countOfWords = strings.length;
        LOGGER.info("List of words received from " + url + " unique words: " + getUniqueWordsCount());
    }

    private void checkConnection(String url) throws IOException {
        try {
            document = Jsoup.parse(new URL(url), 20000);
            this.url = url;
            LOGGER.info("Connection successful to " + url);
        } catch (IOException e) {
            throw new IOException("Failed to connect to '" + url);
        }
    }

    //search local html page
    private void searchLocalPage(String path) throws IOException {
        File page = new File(path);
        if (isPageExist(page)) {
            document = Jsoup.parse(page, "UTF-8", "https://example.com/");
            LOGGER.info("Parsing file from " + path);
        } else {
            throw new FileNotFoundException("Local file not found.Wrong path or url : " + path);
        }
    }

    private void validateUrl(String url) throws IOException {
        if (url.matches(webPageRegex)) {
            checkConnection(url);
        } else {
            searchLocalPage(url);
        }
    }

    private boolean isPageExist(File file) {
        return file.exists() && (file.getPath().endsWith("html") || file.getPath().endsWith(".txt"));
    }

    private List<Word> ConvertStringArrayToWordList(String[] words) {
        Map<String, Long> map = Arrays.stream(words)
                .collect(groupingBy(Function.identity(), counting()));
        return map.entrySet()
                .stream()
                .map(e -> new Word(e.getKey(), e.getValue()))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    private String[] parseDocument(Document document) {
        return document.text()
                .replaceAll("[[^\\u0027|]&&[^\\p{L}]]", " ")
                .replaceAll("[\\s|\\u00A0]+", " ")
                .toLowerCase()
                .split(" ");
    }

}

