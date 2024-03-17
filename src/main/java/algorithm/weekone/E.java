package algorithm.weekone;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Scanner;

/**
 * k
 *  друзей организовали стартап по производству укулеле для кошек. На сегодняшний день прибыль составила
 * n
 *  рублей. Вы, как главный бухгалтер компании, хотите в каждый из ближайших
 * d
 *  дней приписывать по одной цифре в конец числа, выражающего прибыль. При этом в каждый из дней прибыль должна делиться на
 * k
 * .
 */
public class E {
    void method() {
        // Замена System.in на чтение из файла для соответствия предыдущему запросу
        try {
            File file = new File("input.txt");
            Scanner scanner = new Scanner(file);

            BigInteger n = scanner.nextBigInteger();  // Изначальная прибыль
            BigInteger k = scanner.nextBigInteger();  // Количество учредителей
            int d = scanner.nextInt();  // Количество дней
            scanner.close();

            int day = 0;
            while (day < d) {
                BigInteger[] result = addDigit(n, k, d - day);
                n = result[0];
                if (n.equals(BigInteger.valueOf(-1))) {
                    System.out.println(-1);
                    return;
                }
                if (result[1].compareTo(BigInteger.ZERO) > 0) {
                    // Если есть оставшиеся итерации, выполним сразу добавление всех нулей
                    BigInteger remainingDays = result[1];
                    n = n.multiply(BigInteger.TEN.pow(remainingDays.intValue()));
                    break;
                }
                day++;
            }

            System.out.println(n);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
        private static BigInteger[] addDigit(BigInteger n, BigInteger k, int remainingDays) {
            for (int digit = 0; digit <= 9; digit++) {
                BigInteger newNumber = n.multiply(BigInteger.TEN).add(BigInteger.valueOf(digit));
                if (newNumber.mod(k).equals(BigInteger.ZERO)) {
                    // Если условие выполнено при digit == 0
                    if (digit == 0) {
                        // Возвращаем также оставшееся количество итераций
                        return new BigInteger[] {newNumber, BigInteger.valueOf(remainingDays - 1)};
                    }
                    return new BigInteger[] {newNumber, BigInteger.valueOf(-1)};
                }
            }
            return new BigInteger[] {BigInteger.valueOf(-1), BigInteger.valueOf(-1)};
        }
}
