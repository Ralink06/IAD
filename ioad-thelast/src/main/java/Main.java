import kmeans.*;
import kmeans.Point;
import model.MLP;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;


public class Main {

    private static double LEARNING_RATE = 0.1;
    private static int MAX_ITERATIONS = 1000;
    private static double MIN_ERROR = 0.01;
    private static int centroidsNumber = 10;


    static File numbers = new File(Main.class.getClassLoader().getResource("numbers.txt").getFile());

    public static void main(String[] args) throws FileNotFoundException {

        Queue<KMeans> fiveAttempts = new PriorityQueue<>((o1, o2) -> Double.compare(o1.getEndError(), o2.getEndError()));

        for (int i = 0; i < 5; i++)
            fiveAttempts.add(new KMeans(readTrainingData(numbers), centroidsNumber));

        MLP mlp = new MLP(readTrainingData(numbers),fiveAttempts.poll(),centroidsNumber,LEARNING_RATE,MIN_ERROR,MAX_ITERATIONS);
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