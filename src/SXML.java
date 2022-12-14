import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class SXML {
    public static void main(String[] args) throws IOException {
        System.out.println(parse(String.join("", Files.readAllLines(new File("tag_files\\sample1.html").toPath()))));
    }

    public static String parse(String input) {
        return recursivelyParse(new StringBuilder(input)).toString();
    }

    private static StringBuilder recursivelyParse(StringBuilder input) {
        StringBuilder output = new StringBuilder();
        int tagsFound = 0;
        String startingTag = "";
        StringBuilder tag = new StringBuilder();
        StringBuilder inner = new StringBuilder();

        boolean readingTag = false;
        for (char c : input.toString().toCharArray()) {
            if (c == '>') {
                readingTag = false;
                tag.append(c);
                if (startingTag.isEmpty())
                    startingTag = tag.toString();
            } else if (c == '<') {
                tag = new StringBuilder();
                if (startingTag.isEmpty())
                    tagsFound++;
                readingTag = true;
            }
            if (tagsFound == 0)
                output.append(c);
            else {
                inner.append(c);

                if (readingTag) {
                    tag.append(c);
                } else if (startingTag.replace("<", "</").equals(tag.toString())) {
                    tagsFound -= 1;
                    inner.delete(inner.indexOf(startingTag), inner.indexOf(startingTag) + startingTag.length()).delete(inner.length() - startingTag.length() - 1, inner.length());
                    output.append("\n").append(recursivelyParse(inner));
                    startingTag = "";
                    inner = new StringBuilder();
                }

            }
        }
        if (tagsFound != 0)
            System.err.println("InvalidFile");
        return output;
    }
}
