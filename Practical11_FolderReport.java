import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FolderReport {

    public static void main(String[] args) {

        Path folder = Paths.get(".");

        Path report = Paths.get("folder_report.txt");

        try {

            try (var writer = Files.newBufferedWriter(report)) {

                writer.write("FOLDER FILE REPORT");
                writer.newLine();

                writer.write("==================");
                writer.newLine();
                writer.newLine();

                Files.walkFileTree(folder,
                    new SimpleFileVisitor<Path>() {

                    @Override
                    public FileVisitResult visitFile(
                        Path file,
                        BasicFileAttributes attributes
                    ) throws IOException {

                        long size = attributes.size();

                        String modifiedTime =
                            attributes.lastModifiedTime().toString();

                        writer.write(
                            "Name: " + file.getFileName()
                        );
                        writer.newLine();

                        writer.write(
                            "Size: " + size + " bytes"
                        );
                        writer.newLine();

                        writer.write(
                            "Modified: " + modifiedTime
                        );
                        writer.newLine();

                        writer.write("-------------------");
                        writer.newLine();

                        return FileVisitResult.CONTINUE;
                    }
                });

            }

            System.out.println(
                "Folder report created successfully."
            );

            System.out.println(
                "Report saved as: " + report.toAbsolutePath()
            );

        } catch (IOException e) {

            System.out.println(
                "Error: " + e.getMessage()
            );
        }
    }
}