# SudokuVCC

## Opis

Projekt "SudokuVCC" to aplikacja umożliwiająca rozwiązywanie i generowanie łamigłówek Sudoku. Główne cele projektu to dostarczenie funkcjonalnego narzędzia dla miłośników Sudoku oraz stworzenie algorytmów do efektywnego rozwiązywania i generowania plansz o różnym stopniu trudności.

## Funkcjonalności

- Rozwiązywanie plansz Sudoku z podanych danych wejściowych.
- Generowanie plansz Sudoku z różnym poziomem trudności.
- Walidacja poprawności plansz Sudoku.
- Możliwość zapisywania i ładowania plansz z plików.
- Interfejs graficzny ułatwiający użytkowanie aplikacji.

## Wymagania

- Java 11+
- Maven (do zarządzania zależnościami)
- Opcjonalnie środowisko IDE obsługujące projekty Java (np. IntelliJ IDEA, Eclipse)

## Instalacja

1. Sklonuj repozytorium:
   ```bash
   git clone https://github.com/Konume/SudokuVCC.git
   cd SudokuVCC
   ```

2. Zbuduj projekt za pomocą Mavena:
   ```bash
   mvn clean install
   ```

3. Uruchom aplikację:
   ```bash
   mvn exec:java -Dexec.mainClass="com.sudoku.Main"
   ```

## Użycie

1. Po uruchomieniu aplikacji wybierz jedną z dostępnych opcji:
   - Wprowadzenie własnej planszy Sudoku do rozwiązania.
   - Generowanie nowej planszy Sudoku.
2. Użyj interfejsu graficznego, aby uzupełniać planszę lub wybrać preferencje.
3. Zapisz swoje postępy lub rozwiązane plansze do pliku.

## Dokumentacja

Więcej informacji na temat algorytmów użytych w aplikacji, struktury projektu oraz instrukcji rozszerzania funkcjonalności znajduje się w katalogu `docs` w repozytorium.

## Wkład

Zapraszamy do zgłaszania błędów, sugestii oraz nowych pomysłów poprzez system zgłoszeń (Issues). Pull requesty są mile widziane, a wszelki wkład do projektu zostanie doceniony!

## Licencja

Projekt "SudokuVCC" jest licencjonowany na warunkach licencji MIT. Szczegóły znajdują się w pliku [LICENSE](LICENSE).

---

Dziękujemy za zainteresowanie projektem i zachęcamy do wspólnego rozwiązywania Sudoku!
