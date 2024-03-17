package algorithm.weekone;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * На шахматной доске стоят слоны и ладьи, необходимо посчитать, сколько клеток не бьется ни одной из фигур.
 *
 * Шахматная доска имеет размеры 8 на 8. Ладья бьет все клетки горизонтали и вертикали, проходящих через клетку, где она стоит, до первой встретившейся фигуры. Слон бьет все клетки обеих диагоналей, проходящих через клетку, где он стоит, до первой встретившейся фигуры.
 *
 * Формат ввода
 * В первых восьми строках ввода описывается шахматная доска. Первые восемь символов каждой из этих строк описывают состояние соответствующей горизонтали: символ B (заглавная латинская буква) означает, что в клетке стоит слон, символ R — ладья, символ * — что клетка пуста. После описания горизонтали в строке могут идти пробелы, однако длина каждой строки не превышает 250 символов. После описания доски в файле могут быть пустые строки.
 *
 * Формат вывода
 * Выведите количество пустых клеток, которые не бьются ни одной из фигур.
 */
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
