import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class LogAnalyzer {

    public static void main(String[] args) {

        String keyword = "ERROR";

        Path file1 = Paths.get("log1.txt");
        Path file2 = Paths.get("log2.txt");
        Path file3 = Paths.get("log3.txt");

        try {

            Files.write(file1, Arrays.asList(
                "INFO: Application started",
                "ERROR: Database connection failed",
                "INFO: User logged in",
                "ERROR: Invalid password"
            ));

            Files.write(file2, Arrays.asList(
                "INFO: Server started",
                "INFO: Request received",
                "ERROR: File not found",
                "INFO: Request completed"
            ));

            Files.write(file3, Arrays.asList(
                "INFO: Backup started",
                "INFO: Backup completed",
                "WARNING: Low disk space"
            ));

            Path[] files = {file1, file2, file3};

            int totalLines = 0;
            int totalKeywordLines = 0;

            System.out.println("Log Analysis Report");
            System.out.println("--------------------");

            for (Path file : files) {

                int fileLines = 0;
                int fileKeywordLines = 0;

                try {

                    for (String line : Files.readAllLines(file)) {

                        fileLines++;

                        if (line.contains(keyword)) {
                            fileKeywordLines++;
                        }
                    }

                    long size = Files.size(file);

                    totalLines += fileLines;
                    totalKeywordLines += fileKeywordLines;

                    System.out.println("File: " + file.getFileName());
                    System.out.println("Size: " + size + " bytes");
                    System.out.println("Lines: " + fileLines);

                    System.out.println(
                        "Lines containing " + keyword + ": "
                        + fileKeywordLines
                    );

                    System.out.println();

                } catch (IOException e) {
                    System.out.println(
                        "Error reading " + file.getFileName()
                    );
                }
            }

            System.out.println("--------------------");

            System.out.println("Total lines: " + totalLines);

            System.out.println(
                "Total lines containing "
                + keyword + ": " + totalKeywordLines
            );

        } catch (IOException e) {

            System.out.println(
                "Error creating log files: " + e.getMessage()
            );
        }
    }
}