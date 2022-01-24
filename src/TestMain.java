import java.nio.charset.StandardCharsets;

public class TestMain {

    public static void main(String[] args) {
        String keyword = "해리";
        StringBuffer encodedKeyword = new StringBuffer();
        for (int i = 0; i < keyword.length(); i++) {
            encodedKeyword.append("%");
            System.out.println(String.valueOf(keyword.charAt(i)).getBytes(StandardCharsets.US_ASCII)[i]);
            int ASCIIKeyword = (int)keyword.charAt(i);
            System.out.println(ASCIIKeyword);
            String hexKeyword = Integer.toHexString(ASCIIKeyword);
            encodedKeyword.append(hexKeyword);
        }
        System.out.println(encodedKeyword.toString());
    }
}
