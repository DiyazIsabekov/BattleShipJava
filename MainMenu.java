import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class MainMenu {
    private List<Long> gameTimes;

    public MainMenu() {
        gameTimes = new ArrayList<>();
    }

    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean gameOver = false;

        while (!gameOver) {
            System.out.println("Главное меню");
            System.out.println("1. Новая игра");
            System.out.println("2. Результаты");
            System.out.println("3. Выход");
            System.out.print("Выберите действие: ");

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) {
                    case 1:
                        BattleshipGame game = new BattleshipGame();
                        game.placeShips();
                        game.startGame();
                        gameTimes.add(game.getGameTimeInSeconds());
                        System.out.println("Поздравляем! Вы потопили все корабли.");
                        System.out.println("Время игры: " + game.getGameTimeInSeconds() + " сек.");
                        System.out.println(); 
                        gameOver = true;
                        break;
                    case 2:
                        showResults();
                        break;
                    case 3:
                        System.out.println("До свидания!");
                        gameOver = true;
                        break;
                    default:
                        System.out.println("Недопустимый выбор. Попробуйте снова.");
                }
            } else {
                System.out.println("Недопустимый выбор. Попробуйте снова.");
                scanner.nextLine(); 
                 
            }
        }

        scanner.close();
    }

    private void showResults() {
        System.out.println("Топ 3 самых быстрых игр:");

        if (gameTimes.isEmpty()) {
            System.out.println("Нет сохраненных результатов.");
            return;
        }

        Collections.sort(gameTimes);

        int count = Math.min(3, gameTimes.size());
        for (int i = 0; i < count; i++) {
            System.out.println((i + 1) + ". " + gameTimes.get(i) + " сек.");
        }
    }
}
