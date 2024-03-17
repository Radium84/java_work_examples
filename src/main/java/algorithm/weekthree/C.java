package algorithm.weekthree;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;


/**
 * Дан массив a из n чисел. Найдите минимальное количество чисел, после удаления которых попарная разность
 * оставшихся чисел по модулю не будет превышать 1, то есть после удаления ни одно число не должно отличаться
 * от какого-либо другого более чем на 1.
 */
public class C {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Чтение общего количества элементов
        int n = Integer.parseInt(reader.readLine());

        // Чтение элементов массива
        String[] inputLine = reader.readLine().split(" ");

        int[] a = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(inputLine[i]);
        }

        // Вывод результата
        System.out.println(minRemovals(a));

        reader.close();
    }

    public static int minRemovals(int[] a) {
        HashMap<Integer, Integer> countMap = new HashMap<>();

         // Максимально встречающееся число

        // Подсчет количества встреч каждого числа в массиве
        for (int num : a) {
            int currentCount = countMap.getOrDefault(num, 0) + 1;
            countMap.put(num, currentCount);

        }

        int maxPair = 0; // Максимальное количество "соседних" чисел

        for (int key : countMap.keySet()) {
            int keyCount = countMap.get(key);
            if (countMap.containsKey(key + 1)) {
                maxPair = Math.max(maxPair, keyCount + countMap.get(key + 1));
            }
            if(countMap.containsKey(key - 1)){
                maxPair = Math.max(maxPair, keyCount + countMap.get(key - 1));
            }
            // Дополнительно проверяем, выгодно ли накапливать текущее число само по себе
            maxPair = Math.max(maxPair, keyCount);
        }

        // Минимальное количество чисел для удаления
        return a.length - maxPair;
    }
}
