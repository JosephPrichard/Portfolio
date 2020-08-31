using System;
using System.Collections.Generic;

namespace Othello {
    class Game {
        private readonly Board board;
        private readonly List<Tile> turnsMade = new List<Tile>();
        private readonly List<IList<Tile>> flippedOnTurn = new List<IList<Tile>>();

        public Game(int width, int height) {
            board = new Board(width, height);
        }

        public bool BlackTurn() {
            return board.BlackTurn;
        }

        public int BlackPoints() {
            return board.BlackPoints();
        }

        public int WhitePoints() {
            return board.WhitePoints();
        }

        public IList<Tile> AvaliableTurnsReadOnly() {
            return board.AvaliableTurnsReadOnly();
        }

        public String TilesToString() {
            return board.TilesToString();
        }

        public int NumOfTurns() {
            return board.TurnCounter;
        }

        public void DoTurn(int index) {
            CutTurnsFromGame(board.TurnCounter);
            turnsMade.Add(board.GetAvaliableTurn(index));
            flippedOnTurn.Add(board.ToFlipForTurn(index));
            board.DoTurn(index);
        }

        public bool DoTurnChecked(int index) {
            if (index >= 0 && index < board.AvaliableTurnsReadOnly().Count) {
                DoTurn(index);
                return true;
            }
            return false;
        }

        public void UndoTurn() {
            board.UndoTurn(turnsMade[board.TurnCounter - 1], flippedOnTurn[board.TurnCounter - 1]);
        }

        public bool UndoTurnChecked() {
            if (board.TurnCounter > 0) {
                UndoTurn();
                return true;
            }
            return false;
        }

        public void GoForward() {
            board.DoTurn(turnsMade[board.TurnCounter], flippedOnTurn[board.TurnCounter]);
        }

        public bool GoForwardChecked() {
            if (!(board.TurnCounter == turnsMade.Count)) {
                GoForward();
                return true;
            }
            return false;
        }

        public void GoTo(int turn) {
            int turns = turn - board.TurnCounter;
            int count = Math.Abs(turns);
            if (turns < 0) {
                for (int i = 0; i < count; i++) {
                    UndoTurn();
                }
            }
            else {
                for (int i = 0; i < count; i++) {
                    GoForward();
                }
            }

        }

        public bool GoToChecked(int turn) {
            if (turn <= turnsMade.Count && turn >= 0) {
                GoTo(turn);
                return true;
            }
            return false;
        }

        private void CutTurnsFromGame(int index) {
            int count = turnsMade.Count;
            for (int i = index; i < count; i++) {
                turnsMade.RemoveAt(index);
                flippedOnTurn.RemoveAt(index);
            }
        }

    }
}
