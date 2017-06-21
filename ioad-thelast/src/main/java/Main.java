import kmeans.KMeans;
import kmeans.Point;
import model.MLP;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Main {

    private static double LEARNING_RATE = 0.1;
    private static int MAX_ITERATIONS = 1000;
    private static double MIN_ERROR = 0.05;
    private static int centroidsNumber = 20;

    private static File numbers = new File(Main.class.getClassLoader().getResource("numbers.txt").getFile());

    public static void main(String[] args) throws FileNotFoundException {

        Queue<KMeans> fiveAttempts = new PriorityQueue<>(Comparator.comparingDouble(KMeans::getEndError));

        for (int i = 0; i < 5; i++) {
            fiveAttempts.add(new KMeans(readTrainingData(numbers), centroidsNumber));
        }

        MLP mlp = new MLP(readTrainingData(numbers), fiveAttempts.poll(), centroidsNumber, LEARNING_RATE, MIN_ERROR, MAX_ITERATIONS);
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