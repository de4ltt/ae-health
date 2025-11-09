package feo.health;

public class CaesarCipher {
    private static final int ALPHABET_SIZE = 32;

    public static String encrypt(String plainText, int shift) {
        return transform(plainText, shift);
    }

    public static String decrypt(String cipherText, int shift) {
        return transform(cipherText, -shift);
    }

    private static String transform(String text, int shift) {
        StringBuilder result = new StringBuilder();

        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                char base = Character.isUpperCase(ch) ? 'А' : 'а';

                int offset = (ch - base + shift) % ALPHABET_SIZE;

                if (offset < 0)
                    offset += ALPHABET_SIZE;

                result.append((char) (base + offset));
            } else
                result.append(ch);
        }

        return result.toString();
    }
}
