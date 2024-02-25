package moe.seikimo.general;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public interface FileUtils {
    /**
     * Reads a resource from the specified path.
     *
     * @param path The path.
     * @return The resource.
     */
    static byte[] getResource(String path) {
        try (var resource = FileUtils.class
                .getClassLoader()
                .getResourceAsStream(path)) {
            return resource == null ?
                    new byte[0] :
                    resource.readAllBytes();
        } catch (IOException ignored) {
            return new byte[0];
        }
    }

    /**
     * Gets a file from the specified path.
     *
     * @param path The path.
     * @return The file.
     */
    static byte[] readFile(File path) {
        try {
            return Files.readAllBytes(path.toPath());
        } catch (IOException ignored) {
            return new byte[0];
        }
    }

    /**
     * Gets a file from the specified path.
     *
     * @param parent The parent path.
     * @param child The child path.
     * @return The file.
     */
    static byte[] readFile(String parent, String child) {
        return FileUtils.readFile(new File(parent, child));
    }

    /**
     * Reads all lines from a file.
     *
     * @param file The file.
     * @return The lines.
     */
    static List<String> readLines(File file) {
        try {
            return Files.readAllLines(file.toPath());
        } catch (IOException ignored) {
            return List.of();
        }
    }

    /**
     * Reads all lines from a file.
     *
     * @param path The path.
     * @return The lines.
     */
    static List<String> readLines(String path) {
        return FileUtils.readLines(new File(path));
    }

    /**
     * Deletes a directory.
     *
     * @param directory The directory.
     *                  This must be a directory, and not a file.
     */
    static void clearContents(File directory)
            throws IOException {
        if (!directory.isDirectory()) return;

        // Get all files in the directory.
        var list = directory.listFiles();
        if (list == null) return;

        for (var file : list) {
            if (file.isDirectory()) {
                FileUtils.clearContents(file);
                if (!file.delete()) throw new IOException("Failed to delete directory: " + file);
            } else if (!file.delete()) {
                throw new IOException("Failed to delete file: " + file);
            }
        }
    }

    /**
     * Extracts the contents from the ZIP stream to the specified directory.
     *
     * @param stream The ZIP stream.
     * @param outputDirectory The output directory.
     */
    static void extractZip(ZipInputStream stream, File outputDirectory)
            throws IOException {
        ZipEntry entry;
        while ((entry = stream.getNextEntry()) != null) {
            // Create the output file.
            var outputPath = new File(outputDirectory, entry.getName());
            if (entry.isDirectory()) {
                if (!outputPath.mkdirs()) {
                    throw new IOException("Failed to create directory: " + outputPath);
                } else continue;
            }

            // Write the data to the file.
            try (var outputStream = new FileOutputStream(outputPath)) {
                var buffer = new byte[1024];
                int length;
                while ((length = stream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            }
        }

        stream.closeEntry(); // Close the entry.
    }

    /**
     * Recursively deletes a directory.
     *
     * @param file The directory.
     */
    static void deleteDirectory(File file) throws IOException {
        FileUtils.deleteDirectory(file.toPath());
    }

    /**
     * Recursively deletes a directory.
     *
     * @param path The directory.
     */
    static void deleteDirectory(Path path) throws IOException {
        FileUtils.clearContents(path.toFile());
        Files.deleteIfExists(path);
    }

    /**
     * Copies a directory to another directory.
     *
     * @param source The source directory.
     * @param destination The destination directory.
     * @throws IOException If an error occurs.
     */
    static void copyDirectory(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists() && !destination.mkdirs()) {
                throw new IOException("Failed to create directory: " + destination);
            }

            var files = source.list();
            if (files == null) {
                throw new IOException("Failed to list files in directory: " + source);
            }

            for (var file : files) {
                var srcFile = new File(source, file);
                var destFile = new File(destination, file);
                FileUtils.copyDirectory(srcFile, destFile);
            }
        } else {
            Files.copy(source.toPath(), destination.toPath());
        }
    }
}
