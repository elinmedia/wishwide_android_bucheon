import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        System.out.println(Pattern.matches("^(010)(\\d{3,4})(\\d{4})", "#01012354545"));

    }
}
