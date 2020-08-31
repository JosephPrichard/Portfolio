using System;

namespace Othello {
    class Tile {
        public static readonly char[] ROW_TO_LETTER = new char[8] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };
        public readonly int row;
        public readonly int col;
        public int Order { get; set; } = -1;
        public TileType Type { get; set; }

        public Tile(int row, int col, TileType type) {
            this.row = row;
            this.col = col;
            this.Type = type;
        }

        public static Tile Empty(int row, int col) {
            return new Tile(row, col, TileType.EMPTY);
        }

        public static Tile Black(int row, int col) {
            return new Tile(row, col, TileType.BLACK);
        }

        public static Tile White(int row, int col) {
            return new Tile(row, col, TileType.WHITE);
        }

        public void InvertType() {
            Type = HasWhite() ? TileType.BLACK : TileType.WHITE;
        }

        public bool HasWhite() {
            return Type == TileType.WHITE;
        }

        public bool HasEmpty() {
            return Type == TileType.EMPTY;
        }

        public bool HasOpponent(TileType opposingType) {
            return Type == TileType.WHITE && opposingType == TileType.BLACK
                || Type == TileType.BLACK && opposingType == TileType.WHITE;
        }

        public void SetDisc(bool white, int order) {
            this.Order = order;
            Type = white ? TileType.WHITE : TileType.BLACK;
        }

        public void SetEmpty() {
            Order = -1;
            Type = TileType.EMPTY;
        }

        public char GetChar() {
            if (Type == TileType.WHITE) {
                return 'O';
            }
            else if (Type == TileType.BLACK) {
                return 'X';
            }
            else {
                return ' ';
            }
        }

        public String GetCoordAsString() {
            return ROW_TO_LETTER[row] + (col + 1).ToString();
        }

    }

    enum TileType {
        EMPTY, WHITE, BLACK
    }
}
