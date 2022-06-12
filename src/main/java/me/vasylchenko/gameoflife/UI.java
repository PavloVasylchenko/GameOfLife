package me.vasylchenko.gameoflife;

public class UI {

    /**
     * State printer
     *
     * @param cellState field array
     */
    public static void printState(CellState[][] cellState) {
        for (int y = 0; y < cellState.length; y++) {
            for (int x = 0; x < cellState[y].length; x++) {
                switch (cellState[y][x]) {
                    case DEAD -> System.out.print("|_");
                    case ALIVE -> System.out.print("|A");
                }
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }
}
