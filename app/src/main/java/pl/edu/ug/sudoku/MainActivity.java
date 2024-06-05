package pl.edu.ug.sudoku;

import static pl.edu.ug.sudoku.SudokuBoard.BOARD_SIZE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    SudokuBoard sudokuBoard;

    public enum PlayerSymbol {
        Nothing(0),
        HOTDOG(1),
        HAWTHORN(2),
        HAMBURGER(3),
        STRAWBERRY(4),
        APPLE(5),
        PIZZA(6);

        private final int value;

        PlayerSymbol(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    PlayerSymbol activePlayer = PlayerSymbol.HOTDOG;  // Domyślnie hotdog
    PlayerSymbol player2Symbol = PlayerSymbol.STRAWBERRY;
    PlayerSymbol player1Symbol = PlayerSymbol.HOTDOG;



    boolean gameActive = true;

    Button playAgainButton;
    TextView winnerTextView;

    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;
    ImageView[][] cellViews;
    int[][] gameState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else
        {
            textView.setText(user.getEmail());
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        playAgainButton = findViewById(R.id.playAgainButton);
        winnerTextView = findViewById(R.id.winnerTextView);
        // Zainicjuj tablicę 2D obiektów typu ImageView
        cellViews = new ImageView[6][6];
        initializeCellViews();

        sudokuBoard = new SudokuBoard(cellViews);
        gameState = new int[6][6];
        initializeGame();


    }

    private void initializeCellViews() {
        // Wypełnij tablicę cellViews odwołaniami do obiektów typu ImageView
        // Musisz zamienić R.id.your_image_view_id na rzeczywiste identyfikatory z twojego układu XML
        int[][] cellIds = {
                {R.id.R1C1, R.id.R1C2, R.id.R1C3, R.id.R1C4, R.id.R1C5, R.id.R1C6},
                {R.id.R2C1, R.id.R2C2, R.id.R2C3, R.id.R2C4, R.id.R2C5, R.id.R2C6},
                {R.id.R3C1, R.id.R3C2, R.id.R3C3, R.id.R3C4, R.id.R3C5, R.id.R3C6},
                {R.id.R4C1, R.id.R4C2, R.id.R4C3, R.id.R4C4, R.id.R4C5, R.id.R4C6},
                {R.id.R5C1, R.id.R5C2, R.id.R5C3, R.id.R5C4, R.id.R5C5, R.id.R5C6},
                {R.id.R6C1, R.id.R6C2, R.id.R6C3, R.id.R6C4, R.id.R6C5, R.id.R6C6}
        };


        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                cellViews[i][j] = findViewById(cellIds[i][j]);

            }
        }
    }

    private void initializeGame() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                gameState[i][j] = 0;
            }
        }
        sudokuBoard.clearBoard();
        displayMessageForPlayer(activePlayer);
        updateUI();
    }

    @SuppressLint("SetTextI18n")
    private void displayMessageForPlayer(PlayerSymbol activePlayer) {
        if (activePlayer == player1Symbol) {
            winnerTextView.setText("Hotdog, to twój ruch!");
        } else {
            winnerTextView.setText("Truskawka, to twój ruch!");
        }
        winnerTextView.setVisibility(View.VISIBLE);
        playAgainButton.setVisibility(View.INVISIBLE);
    }



    private void updateUI() {

        sudokuBoard.clearBoard();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                int symbol = gameState[i][j];
                if (symbol != 0) {
                    ImageView cell = sudokuBoard.getCellViews()[i][j];
                    updateCellImage(cell, PlayerSymbol.values()[symbol-1]);
                    sudokuBoard.getSudokuGrid()[i][j] = symbol;
                }
            }
        }
    }

    public void clickCounter(View view) {
        if (gameActive) {
            if (view instanceof ImageView) {
                ImageView clickedView = (ImageView) view;

                int row, col;

                // Dodaj logi, aby sprawdzić wartość clickedView.getTag()
                Log.d("ClickCounter", "Clicked View Tag: " + clickedView.getTag());

                // Sprawdź, czy kliknięty widok to licznik czy komórka w siatce
                if (clickedView.getTag() != null && clickedView.getTag().toString().length() >= 2) {
                    // Kliknięty licznik
                    int tempRow = Integer.parseInt(clickedView.getTag().toString().substring(0, 1));
                    int tempCol = Integer.parseInt(clickedView.getTag().toString().substring(1, 2));

                    // Dodaj logi, aby sprawdzić wartości tempRow i tempCol
                    Log.d("ClickCounter", "Temp Row: " + tempRow + ", Temp Col: " + tempCol);

                    // Sprawdź, czy indeksy są w prawidłowym zakresie
                    if (tempRow >= 0 && tempRow < BOARD_SIZE && tempCol >= 0 && tempCol < BOARD_SIZE) {
                        row = tempRow;
                        col = tempCol;
                    } else {
                        // Obsługa błędu - może wyświetlić komunikat lub podjąć inne działania
                        // Na przykład, możesz użyć wartości domyślnych lub zakończyć funkcję
                        Log.e("ClickCounter", "Invalid indices");
                        return;
                    }
                } else {
                    // Kliknięta komórka w siatce
                    row = Integer.parseInt(clickedView.getTag().toString());
                    col = Integer.parseInt(clickedView.getTag().toString());
                }

                // Dodaj logi, aby sprawdzić wartości row i col
                Log.d("ClickCounter", "Final Row: " + row + ", Final Col: " + col);

                if (gameState[row][col] == 0 && gameActive) {
                    PlayerSymbol symbol = (activePlayer == PlayerSymbol.HOTDOG) ? player1Symbol : player2Symbol;
                    if (isCellOccupied(row, col)) {
                        // Komórka już zajęta, wyświetl komunikat o błędzie
                        // Możesz dostosować to działanie do swoich preferencji
                        Log.d("ClickCounter", "Cell already occupied!");
                        return;
                    }
                    gameState[row][col] = symbol.getValue();

                    clickedView.setTranslationY(-1000);

                    updateCellImage(clickedView, symbol);

                    activePlayer = (activePlayer == PlayerSymbol.HOTDOG) ? PlayerSymbol.STRAWBERRY : PlayerSymbol.HOTDOG;

                    clickedView.animate().translationYBy(1000).rotation(3600).setDuration(1000);

                    checkForWinner();
                    CheckForIdiots();
                    if(gameActive)
                        displayMessageForPlayer(activePlayer);
                }
            }
        }
    }

    private boolean isCellOccupied(int row, int col) { return gameState[row][col]!=0;
    }


    private void updateCellImage(ImageView cell, PlayerSymbol values) {
        switch (values) {
            case HOTDOG:
                cell.setImageResource(R.drawable.hotdog);
                break;
            case HAWTHORN:
                cell.setImageResource(R.drawable.hawthorn);
                break;

            case HAMBURGER:
                cell.setImageResource(R.drawable.hamburger);
                break;
            case STRAWBERRY:
                cell.setImageResource(R.drawable.strawberry);
                break;
            case APPLE:
                cell.setImageResource(R.drawable.apple);
                break;
            case PIZZA:
                cell.setImageResource(R.drawable.pizza);
                break;
            default:
                cell.setImageResource(0);
                break;
        }
    }

    private void CheckForIdiots(){
        Log.d("CheckForIdiots", "Checking for idiots");
        if (isIdiot(player1Symbol)) {
            displayWinner("Hotdog, ty niedopieczony psie to sudoku, nie kółko i krzyżyk, nie wstawiaj symboli w tą samą kolumnę,czy wiersz!");
        } else if (isIdiot(player2Symbol)) {
            displayWinner("Strawberry, ty jagodo, którą ktoś zerwał i jest cała zielona. Zasady Sudoku: Jeden symbol na jedną kolumnę i jeden wiersz!");
        } else if (isBoardFull()) {
            displayWinner("Brawo, cymbały graliście w kółko i krzyżyk, w wersji food wars!");
        }
    }


    private boolean isIdiot(PlayerSymbol symbol) {
        return checkExcessiveRows(symbol) || checkExcessiveColumns(symbol);
    }

    private boolean checkExcessiveRows(PlayerSymbol symbol) {
        for (int i = 0; i < 6; i++) {
            int occurrences = 0;

            for (int j = 0; j < 6; j++) {
                if (gameState[i][j] == symbol.getValue()) {
                    occurrences++;
                }
            }

            if (occurrences > 1) {
                // Występuje nadmiarowe wystąpienie w danym rzędzie
                return true;
            }
        }

        return false;
    }

    private boolean checkExcessiveColumns(PlayerSymbol symbol) {
        for (int i = 0; i < 6; i++) {
            int occurrences = 0;

            for (int j = 0; j < 6; j++) {
                if (gameState[j][i] == symbol.getValue()) {
                    occurrences++;
                }
            }

            if (occurrences > 1) {
                // Występuje nadmiarowe wystąpienie w danej kolumnie
                return true;
            }
        }

        return false;
    }


    private void checkForWinner() {
        Log.d("CheckForWinner", "Checking for winner...");
        if (isWinner(player1Symbol)) {
            displayWinner("Hotdog Wygrywa!");
        } else if (isWinner(player2Symbol)) {
            displayWinner("Gracz 2 Strawberry Wygrywa!");
        } else if (isBoardFull()) {
            displayWinner("Remis!");
        }
    }

    private boolean isWinner(PlayerSymbol symbol) {
        //return checkRows(symbol) || checkColumns(symbol) ;
        return checkRows(symbol) && checkColumns(symbol) ;

    }

    private boolean checkRows(PlayerSymbol symbol) {
        int n = 0, c = 0;

        for (int i = 0; i < 6; i++) {
/*
            if (sudokuBoard.getSudokuGrid()[i][0] == symbol.getValue() &&
                    sudokuBoard.getSudokuGrid()[i][1] == symbol.getValue() &&
                    sudokuBoard.getSudokuGrid()[i][2] == symbol.getValue() &&
                    sudokuBoard.getSudokuGrid()[i][3] == symbol.getValue() &&
                    sudokuBoard.getSudokuGrid()[i][4] == symbol.getValue() &&
                    sudokuBoard.getSudokuGrid()[i][5] == symbol.getValue()) {
                return true;
            }
 */
            n = 0;
            for (int j = 0; j < 6; j++) {
                if(gameState[i][j] == symbol.getValue()) n++;
            }
            c += n;
        }
        return (c == 6);
    }

    private boolean checkColumns(PlayerSymbol symbol) {
        int n = 0, c = 0;

        for (int i = 0; i < 6; i++) {
/*
            if (sudokuBoard.getSudokuGrid()[0][i] == symbol.getValue() &&
                    sudokuBoard.getSudokuGrid()[1][i] == symbol.getValue() &&
                    sudokuBoard.getSudokuGrid()[2][i] == symbol.getValue() &&
                    sudokuBoard.getSudokuGrid()[3][i] == symbol.getValue() &&
                    sudokuBoard.getSudokuGrid()[4][i] == symbol.getValue() &&
                    sudokuBoard.getSudokuGrid()[5][i] == symbol.getValue()) {
                return true;
            }
*/
            n = 0;
            for (int j = 0; j < 6; j++) {
                if(gameState[j][i] == symbol.getValue()) n++;
            }
            c += n;
        }
        return (c == 6);
    }



    private boolean isBoardFull() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
               // if (sudokuBoard.getSudokuGrid()[i][j] == 0) {
                    if (sudokuBoard.getCellValue(i, j) == 0) {

                        return false;
                }
            }
        }
        Log.d("IsBoardFull", "Board is full!");
        return true;
    }

    private void displayWinner(String winnerMessage) {
        winnerTextView.setText(winnerMessage);
        winnerTextView.setVisibility(View.VISIBLE);
        winnerTextView.invalidate();
        playAgainButton.setVisibility(View.VISIBLE);
        winnerTextView.invalidate();
        gameActive = false;
        Log.d("DisplayWinner", "Winner displayed!");
    }

    public void playAgain(View view) {

        playAgainButton.setVisibility(View.INVISIBLE);
        winnerTextView.setVisibility(View.INVISIBLE);



        GridLayout gridLayout = findViewById(R.id.gridLayout);
        for(int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView child = (ImageView) gridLayout.getChildAt(i);
            child.setImageDrawable(null); //czyszczę obrazek
            setContentView(R.layout.activity_main);
        }

        winnerTextView = findViewById(R.id.winnerTextView);
        winnerTextView.setVisibility(View.INVISIBLE);
        winnerTextView.setText("");
        playAgainButton = findViewById(R.id.playAgainButton);
        playAgainButton.setVisibility(View.INVISIBLE);
        
        gameActive = true;

        initializeGame();
        displayMessageForPlayer(activePlayer);
    }
}

