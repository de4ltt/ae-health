package feo.health;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class KamasutraCipher {
    private final Map<Character, Character> map = new HashMap<>();

    public KamasutraCipher(String alphabet) {
        String key = generateKey(alphabet);
        for (int i = 0; i < key.length() - 1; i += 2)
            System.out.println(key.charAt(i) + " <-> " + key.charAt(i + 1));

        assert key.length() % 2 == 0;
        for (int i = 0; i < key.length() - 1; i += 2) {
            char a = key.charAt(i), b = key.charAt(i + 1);
            map.put(a, b);
            map.put(b, a);
        }
    }

    private String generateKey(String alphabet) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(alphabet.length());
        while (!alphabet.isEmpty()) {
            int charIndex = random.nextInt(alphabet.length());
            sb.append(alphabet.charAt(charIndex));
            alphabet = alphabet.replace(String.valueOf(alphabet.charAt(charIndex)), "");
        }
        return sb.toString();
    }

    public String encrypt(String plainText) {
        return transform(plainText);
    }

    public String decrypt(String cipherText) {
        return transform(cipherText);
    }

    private String transform(String text) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            boolean up = Character.isUpperCase(c);
            char low = Character.toLowerCase(c);
            char res = map.getOrDefault(low, low);
            sb.append(up ? Character.toUpperCase(res) : res);
        }
        return sb.toString();
    }
}
