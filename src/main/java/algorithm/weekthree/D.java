package algorithm.weekthree;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Вам дана последовательность измерений некоторой величины. Требуется определить, повторялась ли какое-либо число, причём расстояние между повторами не превосходило k.
 *
 * Формат ввода
 * В первой строке задаются два числа n и k (1 ≤ n, k ≤ 105).
 *
 * Во второй строке задаются n чисел, по модулю не превосходящих 109.
 *
 * Формат вывода
 * Выведите YES, если найдется повторяющееся число и расстояние между повторами не превосходит k и NO в противном случае.
 */
public class D {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(); // Количество чисел
        int k = scanner.nextInt(); // Максимальное расстояние между повторами

        int[] numbers = new int[n];
        for (int i = 0; i < n; i++) {
            numbers[i] = scanner.nextInt();
        }

        String result = checkForRepeatsWithinDistanceK(numbers, k) ? "YES" : "NO";
        System.out.println(result);
    }

    private static boolean checkForRepeatsWithinDistanceK(int[] numbers, int k) {
        Set<Integer> seen = new HashSet<>();
        for (int i = 0; i < numbers.length; i++) {
            if(seen.contains(numbers[i])) {
                return true; // Найден повтор
            }
            seen.add(numbers[i]);
            if(i >= k) {
                seen.remove(numbers[i-k]); // Удалить элемент, который находится на расстоянии > k
            }
        }
        return false;
    }
}
