import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static AtomicInteger counter1 = new AtomicInteger();
    private static AtomicInteger counter2 = new AtomicInteger();
    private static AtomicInteger counter3 = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        //сгенерированное слово является палиндромом, т. е. читается одинаково как слева направо, так и справа налево, например, abba;
        Thread palindrome = new Thread(() -> {
            for (String text : texts) {
                if (isPalindrome(text) && !isSameLetter(text)) {
                    incremenentCountrer(text.length());
                }
            }
        });
        palindrome.start();

        //сгенерированное слово состоит из одной и той же буквы, например, aaa;
        Thread sameLetter = new Thread(() -> {
            for (String text : texts) {
                if (isSameLetter(text)) {
                    incremenentCountrer(text.length());
                }
            }
        });
        sameLetter.start();

        //буквы в слове идут по возрастанию: сначала все a (при наличии), затем все b (при наличии), затем все c и т. д. Например, aaccc.
        Thread lettersAscendingOrder = new Thread(() -> {
            for (String text : texts) {
                if (!isSameLetter(text) && isLettersAscendingOrder(text)) {
                    incremenentCountrer(text.length());
                }
            }
        });
        lettersAscendingOrder.start();

        palindrome.join();
        sameLetter.join();
        lettersAscendingOrder.join();

        System.out.println("Красивых слов с длиной 3: " + counter1 + " шт.");
        System.out.println("Красивых слов с длиной 4: " + counter2 + " шт.");
        System.out.println("Красивых слов с длиной 5: " + counter3 + " шт.");
    }

    private static boolean isLettersAscendingOrder(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != text.charAt(i - 1))
                return false;
        }
        return true;
    }

    private static boolean isSameLetter(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != text.charAt(i - 1))
                return false;
        }
        return true;
    }

    private static boolean isPalindrome(String text) {
        return text.equals(new StringBuilder(text).reverse().toString());
    }

    private static void incremenentCountrer(int length) {
        switch (length) {
            case 3:
                counter1.incrementAndGet();
                break;
            case 4:
                counter2.incrementAndGet();
                break;
            case 5:
                counter3.incrementAndGet();
                break;
        }
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}