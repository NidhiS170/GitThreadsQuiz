import java.io.*;
import java.util.concurrent.*;

public class GitQuiz {
    // Nidhi and Arya 12/4

    public static void main(String[] args) {
        String book1 = "warAndPeace.txt";
        String book2 = "theCountOfMonteCristo.txt";

        String output1 = "warAndPeace_doubled.txt";
        String output2 = "theCountOfMonteCristo_doubled.txt";

        // ExecutorService with 2 threads
        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            long startTotal = System.nanoTime();

            // Submit tasks for each file
			Future<Long> future1 = executor.submit(() -> {
				try {
					return doubleFile(book1, output1);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});

			Future<Long> future2 = executor.submit(() -> {
				try {
					return doubleFile(book2, output2);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});

            // Wait for both tasks to finish and get their times
            long time1 = future1.get();
            long time2 = future2.get();

            System.out.printf("Finished %s in %.3f seconds%n",
                    book1, time1 / 1_000_000_000.0);
            System.out.printf("Finished %s in %.3f seconds%n",
                    book2, time2 / 1_000_000_000.0);

            long endTotal = System.nanoTime();
            System.out.printf("%nTotal time: %.3f seconds%n",
                    (endTotal - startTotal) / 1_000_000_000.0);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    // Returns how long it took, in nanoseconds
    public static long doubleFile(String input, String output) throws IOException {
        long start = System.nanoTime();

        try (
            BufferedReader reader = new BufferedReader(new FileReader(input));
            BufferedWriter writer = new BufferedWriter(new FileWriter(output))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {

                String repeated = "";  // must use String, not StringBuilder

                for (char c : line.toCharArray()) {
                    // Append c 20 times
                    for (int i = 0; i < 20; i++) {
                        repeated = repeated + c;
                    }
                }

                writer.write(repeated);
                writer.newLine();
            }
        }

        long end = System.nanoTime();
        return end - start;
    }
}
