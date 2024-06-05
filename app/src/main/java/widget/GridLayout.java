package widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GridLayout extends View {
    public GridLayout(Context context) {
        this(context, null);
    }

    public GridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public int getChildCount() {
        return 0;
    }

    public Object getChildAt(int i) {
        return null;
    }

    public static class SudokuGenerator {

        public static int[][] generateSudoku6x6(Random random) {
            int[][] sudoku = new int[6][6];


            for (int i = 0; i < 6; i++) {
                // Losowa permutacja liczb od 1 do 6 dla pierwszego wiersza
                List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
                Collections.shuffle(numbers);
                for (int j = 0; j < 6; j++) {
                    sudoku[i][j] = numbers.get(j);
                }
            }

            // Generowanie kolejnych wierszy
            generateRows(sudoku, 1, random);

            return sudoku;
        }

        private static boolean generateRows(int[][] sudoku, int row, Random random) {
            if (row == 6) {
                return true; // Koniec generowania
            }

            for (int i = 0; i < 6; i++) {
                List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
                Collections.shuffle(numbers, random);

                for (int num : numbers) {
                    if (isSafe(sudoku, row, i, num)) {
                        sudoku[row][i] = num;

                        if (generateRows(sudoku, row + 1, random)) {
                            return true; // Jeśli udało się wygenerować resztę planszy
                        }

                        sudoku[row][i] = 0; // Backtracking
                    }
                }
            }

            return false;
        }

        private static boolean isSafe(int[][] sudoku, int row, int col, int num) {
            // Sprawdź, czy liczba już występuje w wierszu i kolumnie
            for (int i = 0; i < 6; i++) {
                if (sudoku[row][i] == num || sudoku[i][col] == num) {
                    return false;
                }
            }

            // Sprawdź, czy liczba nie występuje w tej podplanszy 2x3
            int startRow = row - row % 2;
            int startCol = col - col % 3;
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    if (sudoku[startRow + i][startCol + j] == num) {
                        return false;
                    }
                }
            }

            return true;
        }


    }
}
