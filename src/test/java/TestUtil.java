import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestUtil {
    static List<Character> basicChars() {
        return Arrays.asList('a', 'b', 'c');
    }

    static List<Character> toCharArray(String str) {
        List<Character> list = new ArrayList<>();
        for (int i = 0; i < str.toCharArray().length; i++) {
            list.add(str.charAt(i));
        }
        return list;
    }
}
