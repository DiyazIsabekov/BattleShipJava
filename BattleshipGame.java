import java.util.Random;
import java.util.Scanner;

public class BattleshipGame {
    private static final int FIELD_SIZE = 8;
    private char[][] field;
    private long startTime;

    public BattleshipGame() {
        field = new char[FIELD_SIZE][FIELD_SIZE];
        initializeField();
    }

    private void initializeField() {
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                field[i][j] = '~';
            }
        }
    }

    public void placeShips() {
        Random random = new Random();

        // Размещение однопалубного корабля
        int row = random.nextInt(FIELD_SIZE);
        int col = random.nextInt(FIELD_SIZE);
        field[row][col] = 'O';

        // Размещение первого двухпалубного корабля
        boolean placed = false;
        while (!placed) {
            row = random.nextInt(FIELD_SIZE);
            col = random.nextInt(FIELD_SIZE - 1);
            if (field[row][col] == '~' && field[row][col + 1] == '~') {
                field[row][col] = 'O';
                field[row][col + 1] = 'O';
                placed = true;
            }
        }

        // Размещение второго двухпалубного корабля
        placed = false;
        while (!placed) {
            row = random.nextInt(FIELD_SIZE);
            col = random.nextInt(FIELD_SIZE - 1);
            if (field[row][col] == '~' && field[row][col + 1] == '~') {
                field[row][col] = 'O';
                field[row][col + 1] = 'O';
                placed = true;
            }
        }
    }

    public void startGame() {
        try (Scanner scanner = new Scanner(System.in)) {
            startTime = System.currentTimeMillis();

            while (!isGameOver()) {
                System.out.print("Куда стреляем? (например, A2): ");
                String target = scanner.nextLine().toUpperCase();
                shoot(target);

                printField();
            }

            System.out.println("Поздравляем! Вы потопили все корабли.");
            System.out.println("Время игры: " + getGameTimeInSeconds() + " сек.");
        }
    }

    public boolean isGameOver() {
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                if (field[i][j] == 'O') {
                    return false;
                }
            }
        }
        return true;
    }

    public void printField() {
        System.out.println("   1 2 3 4 5 6 7 8");

        for (int i = 0; i < FIELD_SIZE; i++) {
            System.out.print((char) ('A' + i) + " ");
            for (int j = 0; j < FIELD_SIZE; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void shoot(String target) {
        int row = target.charAt(0) - 'A';
        int col = target.charAt(1) - '1';

        if (row < 0 || row >= FIELD_SIZE || col < 0 || col >= FIELD_SIZE) {
            System.out.println("Недопустимая ячейка! Попробуйте снова.");
            return;
        }

        if (field[row][col] == 'O') {
            System.out.println("Попадание!");
            field[row][col] = 'U';

            if (checkSunkShip(row, col)) {
                System.out.println("Корабль потоплен!");
                markSunkShip(row, col);
                markSurroundingCells(row, col);
            }
        } else if (field[row][col] == '~') {
            System.out.println("Промах!");
            field[row][col] = 'o';
        } else {
            System.out.println("Вы уже стреляли в эту ячейку! Попробуйте снова.");
        }

    }

    private boolean checkSunkShip(int row, int col) {
        char shipSymbol = field[row][col];

        // Check horizontally if the ship is sunk
        if (col > 0 && field[row][col - 1] == shipSymbol) {
            for (int i = col + 1; i < FIELD_SIZE; i++) {
                if (field[row][i] == shipSymbol) {
                    return false;
                }
            }
        } else if (col < FIELD_SIZE - 1 && field[row][col + 1] == shipSymbol) {
            for (int i = col - 1; i >= 0; i--) {
                if (field[row][i] == shipSymbol) {
                    return false;
                }
            }
        }

        // Check vertically if the ship is sunk
        if (row > 0 && field[row - 1][col] == shipSymbol) {
            for (int i = row + 1; i < FIELD_SIZE; i++) {
                if (field[i][col] == shipSymbol) {
                    return false;
                }
            }
        } else if (row < FIELD_SIZE - 1 && field[row + 1][col] == shipSymbol) {
            for (int i = row - 1; i >= 0; i--) {
                if (field[i][col] == shipSymbol) {
                    return false;
                }
            }
        }

        return true;
    }

    private void markSunkShip(int row, int col) {
        char shipSymbol = field[row][col];

        // Mark horizontally if the ship is sunk
        if (col > 0 && field[row][col - 1] == shipSymbol) {
            for (int i = col - 1; i >= 0 && field[row][i] == shipSymbol; i--) {
                field[row][i] = 'X';
            }
        } else if (col < FIELD_SIZE - 1 && field[row][col + 1] == shipSymbol) {
            for (int i = col + 1; i < FIELD_SIZE && field[row][i] == shipSymbol; i++) {
                field[row][i] = 'X';
            }
        }

        // Mark vertically if the ship is sunk
        if (row > 0 && field[row - 1][col] == shipSymbol) {
            for (int i = row - 1; i >= 0 && field[i][col] == shipSymbol; i--) {
                field[i][col] = 'X';
            }
        } else if (row < FIELD_SIZE - 1 && field[row + 1][col] == shipSymbol) {
            for (int i = row + 1; i < FIELD_SIZE && field[i][col] == shipSymbol; i++) {
                field[i][col] = 'X';
            }
        }
    }

    private void markSurroundingCells(int row, int col) {
        markCellIfValid(row - 1, col);
        markCellIfValid(row + 1, col);
        markCellIfValid(row, col - 1);
        markCellIfValid(row, col + 1);
    }

    private void markCellIfValid(int row, int col) {
        if (row >= 0 && row < FIELD_SIZE && col >= 0 && col < FIELD_SIZE && field[row][col] == '~') {
            field[row][col] = 'o';
        }
    }

    public long getGameTimeInSeconds() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - startTime) / 1000;
    }
}
