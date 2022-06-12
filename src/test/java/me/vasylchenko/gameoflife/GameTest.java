package me.vasylchenko.gameoflife;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

public class GameTest {

    private CellState[][] getGliderField() {
        final int size = 25;
        CellState[][] gliderField = new CellState[size][size];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                gliderField[y][x] = CellState.DEAD;
            }
        }
        int center = size / 2;
        gliderField[center - 1][center] = CellState.ALIVE;
        gliderField[center][center + 1] = CellState.ALIVE;
        gliderField[center + 1][center - 1] = CellState.ALIVE;
        gliderField[center + 1][center] = CellState.ALIVE;
        gliderField[center + 1][center + 1] = CellState.ALIVE;
        return gliderField;
    }

    private boolean validate(CellState[][] field) {
        int x = -1;
        int y = -1;
        for (int j = 0; j < field.length; j++) {
            for (int i = 0; i < field[j].length; i++) {
                if (field[j][i] == CellState.ALIVE) {
                    y = j;
                    x = i;
                    break;
                }
            }
            if (x >= 0 || y >= 0) {
                break;
            }
        }
        boolean check1 = false;
        boolean check2 = false;

        // Better check might be implemented but should be enough at this stage
        if (y + 2 >= field.length || x + 2 >= field.length || x - 1 < 0) {
            return true;
        }
//        y + 1 < field.length && x + 1 < field.length
//        y + 2 < field.length && x + 1 < field.length
//        y + 2 < field.length
//        y + 2 < field.length && x - 1 >= 0

//        x + 2 < field.length
//        y + 1 < field.length && x + 1 < field.length
//        y + 1 < field.length && x + 2 < field.length
//        y + 2 < field.length && x + 1 < field.length

        if (field[y + 1][x + 1] != CellState.ALIVE) {
            check1 = true;
        }
        if (field[y + 2][x + 1] != CellState.ALIVE) {
            check1 = true;
        }
        if (field[y + 2][x] != CellState.ALIVE) {
            check1 = true;
        }
        if (field[y + 2][x - 1] != CellState.ALIVE) {
            check1 = true;
        }

        if (field[y][x + 2] != CellState.ALIVE) {
            check2 = true;
        }
        if (field[y + 1][x + 1] != CellState.ALIVE) {
            check2 = true;
        }
        if (field[y + 1][x + 2] != CellState.ALIVE) {
            check2 = true;
        }
        if (field[y + 2][x + 1] != CellState.ALIVE) {
            check2 = true;
        }
        return check1 || check2;
    }

    @Test
    public void statesCountTest() {
        Flux<CellState[][]> flux = new Game().game(getGliderField()).take(5);
        StepVerifier.create(flux)
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void statesTest() {
        List<CellState[][]> states = new ArrayList<>();
        new Game().game(getGliderField())
                .take(100)
                .map(states::add)
                .subscribe();
        for (CellState[][] state : states) {
            Assertions.assertTrue(validate(state));
        }
    }

    @Test
    public void printerTest() {
        new Game().game(getGliderField())
                .take(5)
                .doOnNext(UI::printState).subscribe();
    }

    @Test
    public void nullFieldTest() {
        Assertions.assertThrows(GameException.class, () -> new Game().game(null)
                .take(5)
                .doOnNext(UI::printState).subscribe());
    }

    @Test
    public void rectangleFieldTest() {
        Assertions.assertThrows(GameException.class, () -> new Game().game(new CellState[5][10])
                .take(5)
                .doOnNext(UI::printState).subscribe());
    }

    @Test
    public void notInitFieldTest() {
        Assertions.assertThrows(GameException.class, () -> new Game().game(new CellState[5][5])
                .take(5)
                .doOnNext(UI::printState).subscribe());
    }
}
