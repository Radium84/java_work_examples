package algorithm.weekthree;

import java.util.*;

/**
 * Вам даны три списка чисел. Найдите все числа, которые встречаются хотя бы в двух из трёх списков.
 *
 * Формат ввода
 * Во входных данных описывается три списка чисел. Первая строка каждого описания списка состоит из длины списка n (1 ≤ n ≤ 1000).
 * Вторая строка описания содержит список натуральных чисел, записанных через пробел. Числа не превосходят 109.
 *
 * Формат вывода
 * Выведите все числа, которые содержатся хотя бы в двух списках из трёх, в порядке возрастания.
 * Обратите внимание, что каждое число необходимо выводить только один раз.
 */
public class E {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Чтение трех списков чисел как множества для устранения повторов внутри каждого списка
        Set<Integer> set1 = readSet(scanner);
        Set<Integer> set2 = readSet(scanner);
        Set<Integer> set3 = readSet(scanner);

        // HashMap для подсчета количества появлений каждого уникального числа в списках
        HashMap<Integer, Integer> countMap = new HashMap<>();

        // Обновление подсчета для каждого списка
        updateCountMap(countMap, set1);
        updateCountMap(countMap, set2);
        updateCountMap(countMap, set3);

        // TreeSet для уникальности и сортировки результатов
        TreeSet<Integer> resultSet = new TreeSet<>();
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() >= 2) {
                resultSet.add(entry.getKey());
            }
        }

        // Вывод результата
        for (int number : resultSet) {
            System.out.print(number + " ");
        }
    }

    private static void updateCountMap(HashMap<Integer, Integer> countMap, Set<Integer> set) {
        for (int number : set) {
            countMap.put(number, countMap.getOrDefault(number, 0) + 1);
        }
    }

    private static Set<Integer> readSet(Scanner scanner) {
        int n = scanner.nextInt();
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < n; i++) {
            set.add(scanner.nextInt());
        }
        return set;
    }
}
