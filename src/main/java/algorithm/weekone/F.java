package algorithm.weekone;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 * Миша сидел на занятиях математики в Высшей школе экономики и решал следующую задачу: дано
 * n
 *  целых чисел и нужно расставить между ними знаки
 * +
 *  и
 * ×
 *  так, чтобы результат полученного арифметического выражения был нечётным (например, между числами
 * 5
 * ,
 * 7
 * ,
 * 2
 * , можно расставить арифметические знаки следующим образом:
 * 5
 * ×
 * 7
 * +
 * 2
 * =
 * 3
 * 7
 * ). Так как примеры становились все больше и больше, а Миша срочно убегает в гости, от вас требуется написать программу решающую данную задачу.
 */
public class F {
    public void method(){
        try {
            File file = new File("input.txt");
            Scanner scanner = new Scanner(file);

            int n = scanner.nextInt();  // Чтение размера последовательности
            int[] numbers = new int[n];

            int oddCount = 0;  // Счётчик нечетных чисел
            for (int i = 0; i < n; i++) {
                numbers[i] = scanner.nextInt();  // Чтение числа
                if (numbers[i] % 2 != 0) {
                    oddCount++;  // Увеличиваем счетчик, если число нечетное
                }
            }

            StringBuilder result = new StringBuilder();
            if (oddCount % 2 != 0) {  // Нечетное количество нечетных чисел
                result.append("+".repeat(n - 1));  // Все операции "+"
            } else {
                boolean oddFound = false;
                for (int i = 0; i < n - 1; i++) {
                    if (numbers[i] % 2 != 0 && !oddFound) {
                        result.append("x");  // Первое нечётное число -> операция "x"
                        oddFound = true;
                    } else {
                        result.append("+");  // Для всех остальных - "+"
                    }
                }
            }
            System.out.println(result.toString());

            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}
