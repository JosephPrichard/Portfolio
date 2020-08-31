using System;

namespace Othello {
    class Program {
        public static int WIDTH = 8;
        public static int HEIGHT = 8;

        static void Main() {
            Game game = new Game(WIDTH, HEIGHT);
            Console.WriteLine("Welcome to Othello Console. Press a number each turn to make a turn.\n");
            PromptMove(game);
        }

        public static void OutputBoard(Game game) {
            Console.WriteLine(game.TilesToString());
        }

        public static void PromptMove(Game game) {
            OutputBoard(game);
            int count = 1;
            if (game.AvaliableTurnsReadOnly().Count == 0) {
                String victory = game.BlackPoints() > game.WhitePoints() ? "Black Wins\n" : "White Wins\n";
                Console.WriteLine(victory);
                return;
            }
            foreach (Tile tile in game.AvaliableTurnsReadOnly()) {
                Console.WriteLine(count + ". " + tile.GetCoordAsString());
                count++;
            }
            RecieveInput(game);
            PromptMove(game);
        }

        public static void RecieveInput(Game game) {
            String prompt = "Current turn: " + (game.NumOfTurns() + 1) + ". ";
            prompt += game.BlackTurn() ? "Black's turn: " : "White's turn: ";
            Console.WriteLine(prompt);
            String input = Console.ReadLine();
            if (input == "<") {
                if (!game.UndoTurnChecked()) {
                    Console.Write("Cannot Take Back. ");
                    RecieveInput(game);
                }
                else {
                    Console.Write("Took Back.\n");
                }
            }
            else if (input == ">") {
                if (!game.GoForwardChecked()) {
                    Console.Write("Cannot Go Forward. ");
                    RecieveInput(game);
                }
                else {
                    Console.Write("Went forward.\n");
                }
            }
            else if (input != "" && input[0] == '/') {
                try {
                    int num = Int16.Parse(input.Remove(0, 1));
                    if (!game.GoToChecked(num - 1)) {
                        Console.Write("Cannnot Go To. ");
                        RecieveInput(game);
                    }
                    else {
                        Console.Write("Went To.\n");
                    }
                }
                catch (FormatException) {
                    Console.Write("Invalid. ");
                    RecieveInput(game);
                }
            }
            else {
                try {
                    int num = Int16.Parse(input);
                    if (!game.DoTurnChecked(num - 1)) {
                        Console.Write("Invalid. ");
                        RecieveInput(game);
                    }
                    else {
                        Console.Write("Turn made.\n");
                    }
                }
                catch (FormatException) {
                    Console.Write("Invalid. ");
                    RecieveInput(game);
                }
            }
        }
    }
}
