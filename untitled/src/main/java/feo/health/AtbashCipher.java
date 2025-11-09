package feo.health;

public class AtbashCipher {
    private static final char A = 'А';
    private static final char Ya = 'Я';
    private static final char a = 'а';
    private static final char ya = 'я';

    public static String encrypt(String plainText) {
        return transform(plainText);
    }

    public static String decrypt(String cipherText) {
        return transform(cipherText);
    }

    private static String transform(String text) {
        StringBuilder result = new StringBuilder();

        for (char ch : text.toCharArray())
            if (Character.isLetter(ch)) {
                if (Character.isUpperCase(ch))
                    result.append((char) (Ya - (ch - A)));
                else
                    result.append((char) (ya - (ch - a)));
            } else
                result.append(ch);

        return result.toString();
    }
}
