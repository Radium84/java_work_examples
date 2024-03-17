package algorithm.weekone;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Петя - начинающий программист. Сегодня он написал код из
 * n
 *  строк.
 * К сожалению оказалось, что этот код трудно читать. Петя решил исправить это, добавив в различные места пробелы. А точнее, для
 * i
 * -й строки ему нужно добавить ровно
 * a
 * i
 *  пробелов.
 * Для добавления пробелов Петя выделяет строку и нажимает на одну из трёх клавиш: Space, Tab, и Backspace. При нажатии на Space в строку добавляется один пробел. При нажатии на Tab в строку добавляются четыре пробела. При нажатии на Backspace в строке удаляется один пробел.
 * Ему хочется узнать, какое наименьшее количество клавиш придётся нажать, чтобы добавить необходимое количество пробелов в каждую строку. Помогите ему!
 *Формат вывода
 * Выведите одно число – минимальное количество нажатий, чтобы добавить в каждой строке необходимое количество пробелов.
 */
public class C {
    void resolve(){
        File file = new File("input.txt");

        try {
            // Создание объекта Scanner для чтения из файла
            Scanner scanner = new Scanner(file);

            // Ввод количества чисел
            int n = scanner.nextInt();

            // Создание массива для хранения всех чисел
            int[] numbers = new int[n];

            // Считывание чисел в массив
            for (int i = 0; i < n; i++) {
                numbers[i] = scanner.nextInt();
            }

            // Счетчик использованных чисел из множества
            long counter = 0;

            // Перебор всех чисел в массиве
            for (int currentNumber : numbers) {
                // Добавляем количество использований "4"
                counter += currentNumber / 4;
                // Теперь работаем с остатком
                currentNumber %= 4;

                if (currentNumber == 1) {
                    counter++;
                } else if (currentNumber == 2) {
                    counter += 2;
                } else if (currentNumber == 3) {
                    counter += 2; // "1" и один "-1"
                }
                // Нет необходимости что-либо делать для "0"
            }

            // Вывод суммы использований
            System.out.println(counter);
            // long correctResponse = 13024153355L;
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
            e.printStackTrace();
        }
    }
}
