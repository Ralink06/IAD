import kmeans.KMeans;
import kmeans.Point;
import model.MLP;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Main {

    private static double LEARNING_RATE = 0.2;
    private static int MAX_ITERATIONS = 1000;
    private static double MIN_ERROR = 0.05;
    private static int centroidsNumber = 8;
    private static boolean bias = true;

    private static File numbers = new File(Main.class.getClassLoader().getResource("numbers.txt").getFile());

    public static void main(String[] args) throws FileNotFoundException {

        MLP mlp = new MLP(readTrainingData(numbers), null, centroidsNumber, LEARNING_RATE, MIN_ERROR, MAX_ITERATIONS,bias);
        mlp.train();

    }

    private static List<Point> readTrainingData(File file) throws FileNotFoundException {
        List<Point> trainingData = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            Point point = new Point(Double.valueOf(scanner.next()), Double.valueOf(scanner.next()));
            trainingData.add(point);
        }

        return trainingData;
    }


}