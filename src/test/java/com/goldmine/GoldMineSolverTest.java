package com.goldmine;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class GoldMineSolverTest {
  
    private File resourcesDirectory = new File("src\\test\\resources");

    @Test
    public void calculateTreeMax_1x1() {

        String filePath = resourcesDirectory + "\\1x1.csv";

        int expectation = 6;

        String[] args = new String[1];
        args[0] = filePath;

        int outcome = GoldMineSolver.main(args);

        assertEquals(expectation, outcome);

    }

    @Test
    public void calculateTreeMax_1x8() {

        String filePath = resourcesDirectory + "\\1x8.csv";

        int expectation = 32;

        String[] args = new String[1];
        args[0] = filePath;

        int outcome = GoldMineSolver.main(args);

        assertEquals(expectation, outcome);

    }

    @Test
    public void calculateTreeMax_6x6() {

        String filePath = resourcesDirectory + "\\6x6.csv";

        int expectation = 24862;

        String[] args = new String[1];
        args[0] = filePath;

        int outcome = GoldMineSolver.main(args);

        assertEquals(expectation, outcome);

    }
  
}
