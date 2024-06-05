package pl.edu.ug.sudoku;



import android.widget.ImageView;
import java.util.Random;
import pl.edu.ug.sudoku.MainActivity.PlayerSymbol;
import java.security.SecureRandom;

public class SudokuBoard {
    public static final int BOARD_SIZE = 6;

    public int[][] gameState = new int[BOARD_SIZE][BOARD_SIZE];
    private int[][] sudokuGrid = new int[BOARD_SIZE][BOARD_SIZE];
    private ImageView[][] cellViews;


    public SudokuBoard(ImageView[][] cellViews) {
    }

    // Metoda zwracająca wartość w danej komórce planszy
    public int getCellValue(int row, int col) {
        return gameState[row][col];
    }


    // Metoda zwracająca planszę Sudoku bez truskawek i hotdogów
    public int[][] getSudokuGrid() {
        int[][] simplifiedGrid = new int[BOARD_SIZE][BOARD_SIZE];

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                // Jeśli komórka nie zawiera truskawki (STRAWBERRY) ani hotdoga (HOTDOG),
                // przypisz jej wartość do uproszczonej planszy
                if (sudokuGrid[i][j] != PlayerSymbol.STRAWBERRY.getValue() &&
                        sudokuGrid[i][j] != PlayerSymbol.HOTDOG.getValue()) {
                    simplifiedGrid[i][j] = sudokuGrid[i][j];
                } else {
                    // Jeśli komórka zawiera truskawkę lub hotdoga, przypisz 0 (pusta komórka)
                    simplifiedGrid[i][j] = 0;
                }
            }
        }

        return simplifiedGrid;
    }

    public void setCellValue(int row, int col, int symbol) {
        gameState[row][col] = symbol;
    }

    // Konstruktor z parametrami, inicjalizujący planszę danymi wejściowymi
    public SudokuBoard(ImageView[][] cellViews, int[][] initialGameState, int[][] initialSudokuGrid) {


        // Skopiuj stan początkowy gry, eliminując truskawki (STRAWBERRY) i hotdogy (HOTDOG)
        for (int i = 0; i < initialGameState.length; i++) {
            for (int j = 0; j < initialGameState[i].length; j++) {
                if (initialGameState[i][j] == PlayerSymbol.STRAWBERRY.getValue() ||
                        initialGameState[i][j] == PlayerSymbol.HOTDOG.getValue()) {
                    // Zamień truskawki i hotdogi na puste pola (0)
                    initialGameState[i][j] = 0;
                }
                // Skopiuj stan początkowy gry
                gameState[i][j] = initialGameState[i][j];
            }
            this.cellViews = cellViews;
            this.sudokuGrid = initialSudokuGrid;
            initializeBoard();
        }
    }


    public void updateGameState(int row, int col, int symbol) {
        // Sprawdź, czy dane pole jest puste
        if (gameState[row][col] == 0) {
            // Dodaj logikę sprawdzającą czy aktualizacja jest dozwolona
            if (isUpdateAllowed(row, col, symbol)) {
                // Aktualizuj stan gry
                gameState[row][col] = symbol;

                // Dodaj dodatkową logikę lub akcje po aktualizacji stanu gry
                // np. wywołanie metody do sprawdzenia zwycięzcy, aktualizacja interfejsu użytkownika, itp.
                // ...

                // Przykładowe wywołanie metody do sprawdzenia zwycięzcy

            } else {
                // Dodaj obsługę przypadku, gdy aktualizacja nie jest dozwolona
                System.out.println("Aktualizacja niedozwolona dla pola (" + row + ", " + col + ")");
            }
        } else {
            // Dodaj obsługę przypadku, gdy pole jest już zajęte
            System.out.println("Pole (" + row + ", " + col + ") jest już zajęte");
        }
    }

    // Przykładowa dodatkowa logika sprawdzająca, czy aktualizacja jest dozwolona
    private boolean isUpdateAllowed(int row, int col, int symbol) {
        // Tutaj możesz dodać własne reguły, które muszą być spełnione
        // aby aktualizacja była dozwolona
        // Na przykład, sprawdzenie czy symbol nie jest taki sam jak w innych rzędach, kolumnach, itp.
        // Możesz dostosować tę logikę według potrzeb gry.

        // Przykładowa logika - sprawdzenie czy symbol nie występuje już w danym wierszu
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (gameState[row][i] == symbol) {
                return false;
            }
        }

        // Przykładowa logika - sprawdzenie czy symbol nie występuje już w danej kolumnie
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (gameState[i][col] == symbol) {
                return false;
            }
        }

        // Przykładowa logika - sprawdzenie czy symbol nie występuje już w danym obszarze (np. blok 3x3)
        int boxStartRow = row - row % 2;
        int boxStartCol = col - col % 2;

        for (int i = boxStartRow; i < boxStartRow + 2; i++) {
            for (int j = boxStartCol; j < boxStartCol + 2; j++) {
                if (gameState[i][j] == symbol) {
                    return false;
                }
            }
        }

        // Jeśli żadna z powyższych reguł nie została naruszona, zwróć true
        return true;
    }


    public void resetGame() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                gameState[i][j] = 0;
                sudokuGrid[i][j] = 0;
            }
        }
    }

    public void clearBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (cellViews != null && cellViews.length > i && cellViews[i].length > j) {
                    cellViews[i][j].setImageDrawable(null);
                }
            }
        }
    }


    public void initializeBoard() {
        // Krok 1: Zainicjuj planszę losowymi wartościami, pomijając 1 i 4
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                do {
                    gameState[i][j] = getRandomCellValueExcluding(1, 4);
                } while (gameState[i][j] == 1 || gameState[i][j] == 4);
                sudokuGrid[i][j] = 0;
            }
        }
    }

    // Metoda generująca losową wartość, pomijając podane wartości
    private int getRandomCellValueExcluding(int... excludedValues) {
        SecureRandom secureRandom = new SecureRandom();
        int value;

        do {
            value = secureRandom.nextInt(BOARD_SIZE) + 1;  // Dostosuj, jeśli potrzebujesz innego zakresu
        } while (contains(excludedValues, value));
        return value;
    }


    // Metoda sprawdzająca, czy dana wartość jest zawarta w tablicy
    private boolean contains(int[] array, int value) {
        for (int element : array) {
            if (element == value) {
                return true;
            }
        }
        return false;
    }

    // Metoda zwracająca stan gry
    public int[][] getGameState() {
        return gameState;
    }

    // Metoda zwracająca tablicę komórek ImageView
    public ImageView[][] getCellViews() {
        return cellViews;
    }


}

