package common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * A collection of static utility methods.
 *
 * @author Tristan Batchler
 */
public final class Utils {
    /**
     * A random number generator to use in static utility methods.
     */
    public static Random rng = new Random();

    /**
     * Returns a random line in a file given as a filename. Prints the stack trace if an IOException exception occurs.
     *
     * Returns the empty string if the given file is empty and null if an IOException occurred.
     * @param filename The filename for the file to get the random line from.
     * @return A random line in the file given by the filename, the empty string if the file contains no lines, or null
     *         if an IOException occurred.
     */
    public static String getRandomLineInFile(String filename) {
        ArrayList<String> names = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                names.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        String returnLine;
        try {
            returnLine = names.get((int) (Math.random() * names.size()));
        } catch (IndexOutOfBoundsException e) {
            returnLine = "";
        }

        return returnLine;
    }

    /**
     * Returns a random long in the range [0, upperBound) where upperBound is given to be a non-negative long.
     * @param upperBound A non-negative long to serve as the upper bound for the random size generation.
     * @return A random long in the range [0, upperBound) where upperBound is the given positive long.
     * @throws IndexOutOfBoundsException If the given upper bound is negative.
     */
    public static long getRandomSize(long upperBound) throws IndexOutOfBoundsException {
        if (upperBound < 0) {
            throw new IndexOutOfBoundsException();
        }
        return (long)(rng.nextDouble() * upperBound);
    }

    /**
     * Returns a random double in the range [0, upperBound) where upperBound is given to be a non-negative double.
     * @param upperBound A non-negative double to serve as the upper bound for the random size generation.
     * @return A random double in the range [0, upperBound) where upperBound is the given positive double.
     * @throws IndexOutOfBoundsException If the given upper bound is negative.
     */
    public static double getRandomSize(double upperBound) throws IndexOutOfBoundsException {
        if (upperBound < 0) {
            throw new IndexOutOfBoundsException();
        }
        return rng.nextDouble() * upperBound;
    }

    /**
     * Returns a random double within a certain give-or-take percentage of a given value. For example,
     * getRandomBracket(10.00, 0.10) will return random double in the range [9, 11).
     * @param value The value.
     * @param giveOrTakePercentage The give-or-take percentage.
     * @return A random double within a certain give-or-take percentage of the given value.
     * @throws IllegalArgumentException If the given give-or-take percentage is not within the range [0, 1].
     */
    public static double getRandomBracket(double value, double giveOrTakePercentage) throws IllegalArgumentException {
        if (giveOrTakePercentage < 0 || giveOrTakePercentage > 1) {
            throw new IllegalArgumentException("give-or-take argument must be a percentage in the range [0, 1]");
        }

        double min = value - value * giveOrTakePercentage;
        double max = value + value * giveOrTakePercentage;
        return min + (max - min) * rng.nextDouble();
    }
}
