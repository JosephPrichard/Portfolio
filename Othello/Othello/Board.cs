using System;
using System.Collections.Generic;

namespace Othello {
    class Board {
        public readonly int width;
        public readonly int height;

        public bool BlackTurn { get; private set; } = true;
        public int TurnCounter { get; private set; } = 0;
        private readonly Tile[,] currentTiles;
        private readonly List<Tile> avaliableTurns = new List<Tile>();
        private readonly List<List<Tile>> toFlipOnTurn = new List<List<Tile>>();
        private readonly List<Tile> whiteTiles = new List<Tile>();
        private readonly List<Tile> blackTiles = new List<Tile>();

        private readonly int[][] tileOffsets = new int[8][] {new int[2]{1, 1}, new int[2] { -1, 1},new int[2]{1, -1},new int[2]{-1, -1},
            new int[2] {1, 0},new int[2] {0, 1},new int[2] {-1, 0},new int[2] {0, -1}};

        public Board(int width, int height) {
            this.width = width;
            this.height = height;
            currentTiles = new Tile[width, height];
            InitialTiles(currentTiles);
            CalculateAvaliableTurns();
        }

        public IList<Tile> ToFlipForTurn(int index) {
            List<Tile> toFlip = new List<Tile>();
            foreach (Tile tile in toFlipOnTurn[index]) {
                toFlip.Add(tile);
            }
            return toFlip.AsReadOnly();
        }

        public Tile GetAvaliableTurn(int index) {
            return avaliableTurns[index];
        }

        public IList<Tile> AvaliableTurnsReadOnly() {
            return avaliableTurns.AsReadOnly();
        }

        public int BlackPoints() {
            return whiteTiles.Count;
        }

        public int WhitePoints() {
            return blackTiles.Count;
        }

        public bool WithinBounds(int row, int col) {
            return row >= 0 && col >= 0 && row < height && col < width;
        }

        private void CalculateAvaliableTurns() {
            avaliableTurns.Clear();
            toFlipOnTurn.Clear();
            List<Tile> mightMove = BlackTurn ? blackTiles : whiteTiles;
            foreach (Tile tile in mightMove) {
                foreach (int[] offset in tileOffsets) {
                    int row = tile.row;
                    int col = tile.col;
                    int r = offset[0];
                    int c = offset[1];
                    bool endIsEmpty = false;
                    bool atEnd = false;
                    Tile toCheck = currentTiles[row, col];
                    List<Tile> toFlip = new List<Tile>();
                    while (!atEnd) {
                        row += r;
                        col += c;
                        if (WithinBounds(row, col)) {
                            toCheck = currentTiles[row, col];
                            if (toCheck.HasOpponent(tile.Type)) {
                                toFlip.Add(toCheck);
                            }
                            else {
                                endIsEmpty = toCheck.HasEmpty();
                                atEnd = true;
                            }
                        }
                        else {
                            atEnd = true;
                        }
                    }
                    if (toFlip.Count > 0 && endIsEmpty) {
                        int loc = ListDuplicateLoc(avaliableTurns, toCheck);
                        if (loc == -1) {
                            avaliableTurns.Add(toCheck);
                            toFlipOnTurn.Add(toFlip);
                        }
                        else {
                            toFlipOnTurn[loc].InsertRange(0, toFlip);
                        }
                    }
                }
            }
        }

        private int ListDuplicateLoc(List<Tile> tiles, Tile tileIn) {
            return tiles.FindIndex(t => t == tileIn);
        }

        public bool DoTurnChecked(int index) {
            if (index >= 0 && index < avaliableTurns.Count) {
                DoTurn(index);
                return true;
            }
            return false;
        }

        public void DoTurn(int index) {
            DoTurn(GetAvaliableTurn(index), ToFlipForTurn(index));
        }

        public void DoTurn(Tile tileToMove, IList<Tile> toFlip) {
            TurnCounter++;
            AddDiscToTile(tileToMove);
            foreach (Tile tile in toFlip) {
                FlipDiscOnTile(tile);
            }
            BlackTurn = !BlackTurn;
            CalculateAvaliableTurns();
        }

        public void UndoTurn(Tile toRemove, IList<Tile> toFlip) {
            TurnCounter--;
            RemoveDiscFromTile(toRemove);
            foreach (Tile tile in toFlip) {
                FlipDiscOnTile(tile);
            }
            BlackTurn = !BlackTurn;
            CalculateAvaliableTurns();
        }

        private void EmptyTiles(Tile[,] tiles) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    tiles[i, j] = Tile.Empty(i, j);
                }
            }
        }

        private void InitialTiles(Tile[,] tiles) {
            EmptyTiles(tiles);
            int halfWidth = width / 2;
            int halfHeight = height / 2;
            AddNewWhiteTile(halfHeight, halfWidth);
            AddNewBlackTile(halfHeight, halfWidth - 1);
            AddNewBlackTile(halfHeight - 1, halfWidth);
            AddNewWhiteTile(halfHeight - 1, halfWidth - 1);
        }

        private void AddNewBlackTile(int row, int col) {
            currentTiles[row, col] = Tile.Black(row, col);
            blackTiles.Add(currentTiles[row, col]);
        }

        private void AddNewWhiteTile(int row, int col) {
            currentTiles[row, col] = Tile.White(row, col);
            whiteTiles.Add(currentTiles[row, col]);
        }

        private void AddDiscToTile(Tile tile) {
            tile.SetDisc(!BlackTurn, TurnCounter);
            List<Tile> addTo = BlackTurn ? blackTiles : whiteTiles;
            addTo.Add(tile);
        }

        private void RemoveDiscFromTile(Tile tile) {
            tile.SetEmpty();
            List<Tile> removeFrom = BlackTurn ? whiteTiles : blackTiles;
            removeFrom.Remove(tile);
        }

        private void FlipDiscOnTile(Tile tile) {
            bool isWhite = tile.HasWhite();
            if (isWhite) {
                whiteTiles.Remove(tile);
                blackTiles.Add(tile);
            }
            else {
                blackTiles.Remove(tile);
                whiteTiles.Add(tile);
            }
            tile.InvertType();
        }

        public String TilesToString() {
            String output = "  ";
            for (int j = 0; j < width; j++) {
                output += "  " + (j + 1) + "  ";
            }
            output += "\n";
            for (int i = 0; i < height; i++) {
                output += "  ";
                for (int j = 0; j < width; j++) {
                    output += "+---+";
                }
                output = output + "\n" + Tile.ROW_TO_LETTER[i] + " ";
                for (int j = 0; j < width; j++) {
                    output += "| " + currentTiles[i, j].GetChar() + " |";
                }
                output += "\n";
            }
            output += "  ";
            for (int j = 0; j < width; j++) {
                output += "+---+";
            }
            output += "\n";
            return output;
        }

    }
}
