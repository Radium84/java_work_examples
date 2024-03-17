package algorithm.weekthree;
import java.util.Scanner;

/**
 * Задано две строки, нужно проверить, является ли одна анаграммой другой. Анаграммой называется строка, полученная из другой перестановкой букв.
 *
 * Формат ввода
 * Строки состоят из строчных латинских букв, их длина не превосходит 100000. Каждая записана в отдельной строке.
 *
 * Формат вывода
 * Выведите "YES" если одна из строк является анаграммой другой и "NO" в противном случае.
 */
public class B {
    public void method(){
        Scanner scanner = new Scanner(System.in);

        // Чтение строк
        String first = scanner.nextLine();
        String second = scanner.nextLine();

        System.out.println(areAnagrams(first, second) ? "YES" : "NO");
    }

    public static boolean areAnagrams(String first, String second) {
        if (first.length() != second.length()) {
            return false; // Если длины строк разные, они точно не являются анаграммами
        }

        int[] charCounts = new int[26]; // Предположим, что в строке только строчные латинские буквы

        // Увеличиваем счетчик для каждого символа первой строки
        for (char c : first.toCharArray()) {
            charCounts[c - 'a']++;
        }

        // Уменьшаем счетчик для каждого символа второй строки
        for (char c : second.toCharArray()) {
            charCounts[c - 'a']--;
        }

        // Если все счетчики равны нулю, строки являются анаграммами
        for (int count : charCounts) {
            if (count != 0) {
                return false;
            }
        }

        return true;
    }
}
