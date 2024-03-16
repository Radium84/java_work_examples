package algotrian;

import java.util.Scanner;

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
