package model;

public class Word implements Comparable<Word> {
    private String word;
    private Long count;

    public Word() {
    }

    public Word(String word) {
        this.word = word;
        count = 0L;
    }

    public Word(String word, Long count) {
        this.word = word;
        this.count = count;
    }


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }


    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public void increaseCount() {
        count++;
    }

    public boolean isWordContains(String word) {
        return this.word.equals(word);
    }

    @Override
    public int compareTo(Word o) {
        return this.count.compareTo(o.count);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word1 = (Word) o;

        return (word.equals(word1.word));

    }

    @Override
    public int hashCode() {
        int result = word.hashCode();
        result = 31 * result + count.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", count, word);
    }
}
