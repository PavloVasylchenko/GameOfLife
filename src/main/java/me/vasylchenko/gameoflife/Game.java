package me.vasylchenko.gameoflife;

import reactor.core.publisher.Flux;

public class Game {

    /**
     * Generate next field based on current field
     * @param field current field
     * @return next field
     */
    private CellState[][] iterate(CellState[][] field) {
        CellState[][] nextField = new CellState[field.length][field[0].length];
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[0].length; x++) {
                CellState cellState = applyRules(x, y, field);
                nextField[y][x] = cellState;
            }
        }
        return nextField;
    }

    /**
     * Game Flux generator. Generates sequence of states.
     * Each state based on previous state.
     *
     * @param initialField initial field
     * @return next fields sequence
     */
    public Flux<CellState[][]> game(CellState[][] initialField) {
        validate(initialField);
        return Flux.generate(() -> initialField, (state, sink) -> {
            sink.next(state);
            return iterate(state);
        });
    }

    /**
     * Validating initial setup.
     * Field size should be more than 3 to put glider into it
     * Field need to be initialized with values
     * Filed need to be square
     *
     * @param initialField initial field
     */
    private void validate(CellState[][] initialField) {
        if (initialField == null) {
            throw new GameException("Field is not initialized properly");
        }
        if (initialField.length < 3) {
            throw new GameException("Size less than 3 is not supported");
        }
        if (initialField.length != initialField[0].length) {
            throw new GameException("Filed should be square");
        }
        for (int j = 0; j < initialField.length; j++) {
            for (int i = 0; i < initialField[j].length; i++) {
                if (initialField[j][i] == null) {
                    throw new GameException("Field is not initialized properly");
                }
            }
        }
    }

    /**
     * Apply game rules to current cell.
     *
     * @param x     x coordinate of cell that we are going to check
     * @param y     y coordinate of cell that we are going to check
     * @param field field array
     * @return new state of cell
     */
    private CellState applyRules(int x, int y, CellState[][] field) {
        int neighborsCount = getNeighborsCount(x, y, field);
//        if (neighborsCount < 2 && field[y][x] == State.ALIVE) {
//            return State.DEAD;
//        }
        if (neighborsCount >= 2 && neighborsCount <= 3 && field[y][x] == CellState.ALIVE) {
            return CellState.ALIVE;
        }
//        if (neighborsCount > 3 && field[y][x] == State.DEAD) {
//            return State.DEAD;
//        }
        if (neighborsCount == 3) {
            return CellState.ALIVE;
        }
        return CellState.DEAD;
    }

    /**
     * Getting amount of neighbors
     *
     * @param x     x coordinate of cell that we are going to check
     * @param y     y coordinate of cell that we are going to check
     * @param field field array
     * @return amount of neighbors
     */
    private int getNeighborsCount(int x, int y, CellState[][] field) {
        int result = 0;
        for (int j = y - 1; j <= y + 1; j++) {
            for (int i = x - 1; i <= x + 1; i++) {
                if (j >= 0 && j < field.length && i >= 0 && i < field[0].length && !(i == x && j == y)) {
                    if (field[j][i] == CellState.ALIVE) {
                        result++;
                    }
                }
            }
        }
        return result;
    }
}
