package common;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import static org.junit.Assert.*;

/**
 * JUnit tests for all {@link Utils} methods.
 */
public class UtilsTest {
    @Test
    public void getRandomLineInNonExistentFile() {
        String randomLine = Utils.getRandomLineInFile("");
        assertNull(randomLine);
    }

    @Test
    public void getRandomLineInFileWithNoLines() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("src/test/resources/testFile");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.close();

        for (int i = 0; i < 10000; i++) {
            String randomLine = Utils.getRandomLineInFile("src/test/resources/testFile");
            assertEquals("", randomLine);
        }
    }

    @Test
    public void getRandomLineInFileWithOneLine() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("src/test/resources/testFile");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.println("The first line");
        writer.close();

        for (int i = 0; i < 10000; i++) {
            String randomLine = Utils.getRandomLineInFile("src/test/resources/testFile");
            assertEquals("The first line", randomLine);
        }
    }

    @Test
    public void getRandomLineInFileWithTwoLines() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("testFile");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.println("The first line");
        writer.println("The second line");
        writer.close();

        for (int i = 0; i < 10000; i++) {
            String randomLine = Utils.getRandomLineInFile("testFile");
            assertTrue(randomLine.equals("The first line") || randomLine.equals("The second line"));
        }
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void getRandomLongSizeWithNegativeUpperBound() {
        long size = Utils.getRandomSize(-1);
    }

    @Test
    public void getRandomLongSizeWithUpperBoundZero() {
        for (int i = 0; i < 10000; i++){
            long size = Utils.getRandomSize(0);
            assertEquals(0, size);
        }
    }

    @Test
    public void getRandomLongSizeWithUpperBoundOne() {
        for (int i = 0; i < 10000; i++) {
            long size = Utils.getRandomSize(1);
            assertEquals(0, size);
        }
    }

    @Test
    public void getRandomLongSizeWithUpperBoundTwo() {
        for (int i = 0; i < 10000; i++) {
            long size = Utils.getRandomSize(2);
            assertTrue(size == 0 || size == 1);
        }
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void getRandomDoubleSizeWithNegativeUpperBound() {
        double size = Utils.getRandomSize(-1.0);
    }

    @Test
    public void getRandomDoubleSizeWithUpperBoundZero() {
        for (int i = 0; i < 10000; i++) {
            double size = Utils.getRandomSize(0.0);
            assertEquals(0.0, size, 0.0);
        }
    }

    @Test
    public void getRandomDoubleSizeWithPositiveUpperBound() {
        for (int i = 0; i < 10000; i++) {
            double size = Utils.getRandomSize(1.0);
            assertTrue(size >= 0.0 && size < 1.0);
        }
    }

    @Test
    public void getRandomBracketWithZeroValue() {
        double value = 0;
        double bracket = 0.10;
        for (int i = 0; i < 10000; i++) {
            double result = Utils.getRandomBracket(value, bracket);
            assertEquals(0.0, result, 0.0);
        }
    }

    @Test
    public void getRandomBracketWithZeroBracket() {
        double value = 10.0;
        double bracket = 0.0;
        for (int i = 0; i < 10000; i++) {
            double result = Utils.getRandomBracket(value, bracket);
            assertEquals(10.0, result, 0.0);
        }
    }

    @Test
    public void getRandomBracketWithNegativeValue() {
        double value = -10.0;
        double bracket = 0.10;
        for (int i = 0; i < 10000; i++) {
            double result = Utils.getRandomBracket(value, bracket);
            assertTrue(result >= -11.0 && result < -9.0);
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void getRandomBracketWithNegativeBracket() {
        double result = Utils.getRandomBracket(10.0, -0.10);
    }

    @Test (expected = IllegalArgumentException.class)
    public void getRandomBracketWithBracketGreaterThanOne() {
        double result = Utils.getRandomBracket(10.0, 2.0);
    }

    @Test
    public void getRandomBracketWithPositiveValueAndBracket() {
        double value = 15.0;
        double bracket = 0.05;
        for (int i = 0; i < 10000; i++) {
            double result = Utils.getRandomBracket(value, bracket);
            assertTrue(result >= 14.25 && result < 15.75);
        }
    }
}
