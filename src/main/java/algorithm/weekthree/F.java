package algorithm.weekthree;

import java.util.*;

/**
 * было принято решение укоротить некоторые слова в тексте. Для этого был составлен словарь слов, до которых можно сокращать более длинные слова. Слово из текста можно сократить, если в словаре найдется слово, являющееся началом слова из текста. Например, если в списке есть слово "лом", то слова из текста "ломбард", "ломоносов" и другие слова, начинающиеся на "лом", можно сократить до "лом".
 *
 * Если слово из текста можно сократить до нескольких слов из словаря, то следует сокращать его до самого короткого слова.
 *
 * Формат ввода
 *
 * В первой строке через пробел вводятся слова из словаря, слова состоят из маленьких латинских букв.
 * Гарантируется, что словарь не пуст и количество слов в словаре не превышет 1000, а длина слов — 100 символов.
 *
 * Во второй строке через пробел вводятся слова текста (они также состоят только из маленьких латинских букв).
 * Количество слов в тексте не превосходит 10 в 5, а суммарное количество букв в них — 10 в 6.
 *
 *
 *
 * Формат вывода
 * Выведите текст, в котором осуществлены замены.
 */
public class F {
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEndOfWord;

        // Метод для добавления слова в дерево
        void addWord(String word) {
            TrieNode current = this;
            for (char ch : word.toCharArray()) {
                current = current.children.computeIfAbsent(ch, k -> new TrieNode());
            }
            current.isEndOfWord = true;
        }

        // Метод для поиска наибольшего префикса в дереве
        String findShortestPrefix(String word) {
            TrieNode current = this;
            StringBuilder prefixBuilder = new StringBuilder();

            for (char ch : word.toCharArray()) {
                prefixBuilder.append(ch);
                TrieNode next = current.children.get(ch);
                if (next == null) break;  // Если нет продолжения, прекращаем
                if (next.isEndOfWord) return prefixBuilder.toString(); // Нашли префикс
                current = next;
            }

            return word; // Если не нашли подходящего префикса, возвращаем исходное слово
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Создаем префиксное дерево и добавляем туда слова из словаря
        TrieNode trie = new TrieNode();
        String[] dictionaryWords = scanner.nextLine().split(" ");
        for (String word : dictionaryWords) {
            trie.addWord(word);
        }

        // Читаем слова из текста
        String[] textWords = scanner.nextLine().split(" ");

        // Выполняем сокращение слов текста
        for (int i = 0; i < textWords.length; i++) {
            textWords[i] = trie.findShortestPrefix(textWords[i]);
        }

        // Выводим результат
        System.out.println(String.join(" ", textWords));

    }
}