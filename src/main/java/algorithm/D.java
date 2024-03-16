package algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class D {
    // Функция для маркировки битых клеток
    private static void markAttacked(boolean[][] attacked, int x, int y, char[][] board, int dx, int dy) {
        // Начинаем с текущей позиции фигуры
        int curX = x + dx;
        int curY = y + dy;
        // Пока мы в пределах доски
        while (curX >= 0 && curX < 8 && curY >= 0 && curY < 8) {
            attacked[curX][curY] = true;
            // Если нашли другую фигуру, останавливаем маркировку
            if (board[curX][curY] != '*') {
                break;
            }
            // Двигаемся дальше
            curX += dx;
            curY += dy;
        }
    }
    void method() {


        char[][] board = new char[8][8];
        try {
            File file = new File("input.txt");
            Scanner scanner = new Scanner(file);
            for (int i = 0; i < 8; i++) {
                String line = scanner.nextLine();
                for (int j = 0; j < 8; j++) {
                    board[i][j] = line.charAt(j);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return;
        }

        // Используем матрицу для обработки битых клеток
        boolean[][] attacked = new boolean[8][8];

        // Маркируем клетки, которые бьются слонами и ладьями
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 'R') {
                    markAttacked(attacked, i, j, board, 0, 1); // Горизонталь для ладьи
                    markAttacked(attacked, i, j, board, 1, 0); // Вертикаль для ладьи
                    markAttacked(attacked, i, j, board, 0, -1); // Горизонталь для ладьи
                    markAttacked(attacked, i, j, board, -1, 0); // Вертикаль для ладьи

                } else if (board[i][j] == 'B') {
                    markAttacked(attacked, i, j, board, 1, 1); // Диагональ для слона
                    markAttacked(attacked, i, j, board, -1, -1); // Диагональ для слона
                    markAttacked(attacked, i, j, board, 1, -1); // Диагональ для слона
                    markAttacked(attacked, i, j, board, -1, 1); // Диагональ для слона
                }
            }
        }

        // Подсчитываем небитые клетки
        int safeCells = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!attacked[i][j] && board[i][j] == '*') {
                    safeCells++;
                }
            }
        }

        System.out.println(safeCells);
    }
}
