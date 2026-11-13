package app.benchmark;

import app.core.AppConfig;
import app.core.AppLogger;

import java.io.IOException;
import java.util.Arrays;

public class Main {

    private static final String CSV_PATH = "results/benchmark_results.csv";
    private static final String DEFAULT_SIZES = "10,100,1000,10000,100000,1000000";

    public static void main(String[] args) {
        AppConfig config = AppConfig.getInstance();
        AppLogger  logger = AppLogger.getInstance();

        // Defaults are safe for any machine (~1 min). Larger sizes can be set in .env.
        int[] sizes       = parseSizes(config.get("BENCHMARK_SIZES", DEFAULT_SIZES));
        int   samples     = Math.max(1, config.getInt("BENCHMARK_SAMPLES", 15));

        logger.info("Benchmark starting — sizes: " + Arrays.toString(sizes)
                    + ", samples: " + samples);

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      DATA STRUCTURES BENCHMARK       ║");
        System.out.println("╚══════════════════════════════════════╝");

        // ── Timing (nothing else runs inside these calls) ─────────────────────
        System.out.println("\n\n━━━  LINKED LISTS  ━━━");
        ListBenchmark.run(sizes, samples);

        System.out.println("\n\n━━━  STACK  ━━━");
        StackBenchmark.run(sizes, samples);

        System.out.println("\n\n━━━  QUEUE  ━━━");
        QueueBenchmark.run(sizes, samples);

        // ── CSV export (after all timing is done) ─────────────────────────────
        try {
            ResultCollector.exportCsv(CSV_PATH);
            logger.info("Results written to " + CSV_PATH);
            System.out.println("\nResultados guardados en: " + CSV_PATH);
        } catch (IOException e) {
            logger.severe("Could not write CSV: " + e.getMessage());
        }

        logger.info("Benchmark completed.");
    }

    private static int[] parseSizes(String sizesStr) {
        String[] parts = sizesStr.split(",");
        int[] sizes = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            sizes[i] = Integer.parseInt(parts[i].trim());
        }
        return sizes;
    }
}
