package algorithm.weekone;

import java.util.Scanner;

/**
 * Раунд плей-офф между двумя командами состоит из двух матчей. Каждая команда проводит по одному матчу «дома» и «в гостях». Выигрывает команда, забившая большее число мячей. Если же число забитых мячей совпадает, выигрывает команда, забившая больше мячей «в гостях». Если и это число мячей совпадает, матч переходит в дополнительный тайм или серию пенальти.
 *
 * Вам дан счёт первого матча, а также счёт текущей игры (которая ещё не завершилась). Помогите комментатору сообщить, сколько голов необходимо забить первой команде, чтобы победить, не переводя игру в дополнительное время.
 *
 * Формат ввода
 * В первой строке записан счёт первого мачта в формате G1:G2, где G1 — число мячей, забитых первой командой, а G2 — число мячей, забитых второй командой.
 *
 * Во второй строке записан счёт второго (текущего) матча в аналогичном формате. Все числа в записи счёта не превышают 5.
 *
 * В третьей строке записано число 1, если первую игру первая команда провела «дома», или 2, если «в гостях».
 *
 * Формат вывода
 * Выведите единственное целое число "— необходимое количество мячей.
 */
public class B {
    void method() {
        Scanner scanner = new Scanner(System.in);

        // Считываем счёт первой игры
        String[] firstMatchScore = scanner.next().split(":");
        int firstGTeam1 = Integer.parseInt(firstMatchScore[0]);
        int firstGTeam2 = Integer.parseInt(firstMatchScore[1]);

        // Считываем счёт второй (текущей) игры
        String[] secondMatchScore = scanner.next().split(":");
        int secondGTeam1 = Integer.parseInt(secondMatchScore[0]);
        int secondGTeam2 = Integer.parseInt(secondMatchScore[1]);

        // Считываем, где первая команда играла первый матч (дома или в гостях)
        int firstGameLocation = scanner.nextInt();
        int totalTeam1 = firstGTeam1 + secondGTeam1;
        int totalTeam2 = firstGTeam2 + secondGTeam2;
        if (firstGameLocation == 1) {
            if (totalTeam1 > totalTeam2) {
                System.out.println(0);
            } else if (totalTeam1 == totalTeam2) {
                if (secondGTeam1 <= firstGTeam2) {
                    System.out.println(1);
                } else {
                    System.out.println(0);
                }
            } else {
                int delta = 0;
                while (firstGTeam1 + secondGTeam1 + delta < firstGTeam2 + secondGTeam2) {
                    delta++;
                }
                if (secondGTeam1 + delta <= firstGTeam2) {
                    delta++;
                }
                System.out.println(delta);
            }
        } else {
            if (totalTeam1 > totalTeam2) {
                System.out.println(0);
            } else if (totalTeam1 == totalTeam2) {
                if (firstGTeam1 <= secondGTeam2) {
                    System.out.println(1);
                } else {
                    System.out.println(0);
                }
            } else {
                int delta = 0;
                while (firstGTeam1 + secondGTeam1 + delta < firstGTeam2 + secondGTeam2) {
                    delta++;
                }
                if (firstGTeam1 <= secondGTeam2) {
                    delta++;
                }
                System.out.println(delta);
            }
        }
        scanner.close();
    }
}
