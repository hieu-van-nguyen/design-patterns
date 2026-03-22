package us.inest.dp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static us.inest.dp.Util.checkF2PInBefore;
import static us.inest.dp.Util.getF2P_P2P;

public class GradleV1LogParser {

    public static String checkMissingF2P_P2P(String key) {
        Path afterPath = Paths.get("after.log");
        ObjectMapper mapper = new ObjectMapper();
        try {
            String content = Files.readString(afterPath);

            JsonNode root = mapper.readTree(new File("result.json"));
            JsonNode nodes = root.get(key);

            if (nodes != null && nodes.isArray()) {
                Iterator<JsonNode> it = nodes.elements();
                while (it.hasNext()) {
                    String text = it.next().asText();
                    int index = text.lastIndexOf(".");
                    String testName = text.substring(index + 1).trim();
                    String packageName = text.substring(0, index).trim();
                    if (!content.contains(testName) || !content.contains(packageName)) {
                        return text;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void extractFailures(String inputFile, String outputFile) {
        Path inputPath = Paths.get(inputFile);
        Path outputPath = Paths.get(outputFile);
        Pattern pattern = Pattern.compile("<<< FAILURE(.*?)<<< FAILURE", Pattern.DOTALL);
        try {
            String content = Files.readString(inputPath);
            Matcher matcher = pattern.matcher(content);
            List<String> failures = new ArrayList<>();

            while (matcher.find()) {
                String failure = matcher.group(1).trim();
                failures.add(failure);
            }
            if (failures.isEmpty()) {
                System.out.println("No failures found.");
            } else {
                System.out.println("Extracted failures:");
                BufferedWriter writer = Files.newBufferedWriter(outputPath);
                for (int i = 0; i < failures.size(); i++) {
                    String detail = "Failure " + (i + 1) + ": " + failures.get(i);
                    writer.write(detail);
                    writer.newLine();
                    // System.out.println("Failure " + (i + 1) + ": " + failures.get(i));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading log file: " + e.getMessage());
        }
    }

    public static void parseGradleV1(String inputFile, String outputFile, String failureOutputFile) {
        // Change these paths as needed
        Path inputPath = Paths.get(inputFile);
        Path outputPath = Paths.get(outputFile);
        Path failureOutputPath = Paths.get(failureOutputFile);

        // Regex to capture key:value pairs (case-insensitive for Errors, Skipped, etc.)
        String pattern1 = "\"status\":\"SUCCESS\"";
        String pattern2 = "\"status\":\"FAILURE\"";

        try (BufferedReader reader = Files.newBufferedReader(inputPath);
             BufferedWriter writer = Files.newBufferedWriter(outputPath);
             BufferedWriter writer2 = Files.newBufferedWriter(failureOutputPath)) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(pattern1)) {
                    writer.write(line);
                    writer.newLine();
                } else if (line.contains(pattern2)) {
                    writer2.write(line);
                    writer2.newLine();
                }
            }

            System.out.println("Parsing complete. Output written to " + outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> checkF2PInBefore() {
        Path beforePath = Paths.get("before.log");
        ObjectMapper mapper = new ObjectMapper();
        List<String> f2pTests = new ArrayList<>();
        try {
            String content = Files.readString(beforePath);

            JsonNode root = mapper.readTree(new File("result.json"));
            JsonNode nodes = root.get("fail_to_pass");

            if (nodes != null && nodes.isArray()) {
                Iterator<JsonNode> it = nodes.elements();
                while (it.hasNext()) {
                    String text = it.next().asText();
                    int index = text.lastIndexOf(".");
                    String testName = text.substring(index + 1).trim();
                    String packageName = text.substring(0, index).trim();
                    if (content.contains(testName) && content.contains(packageName)) {
                        f2pTests.add(text);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return f2pTests;
    }

    public static void main(String[] args) {
        // parseGradleV1("base.log", "base-output.txt", "base-failure-output.txt");
        // parseGradleV1("after.log", "after-output.txt", "after-failure-output.txt");
        parseGradleV1("before.log", "before-output.txt", "before-failure-output.txt");

        // getF2P_P2P("fail_to_pass", "result.json", "F2P.txt");
        // getF2P_P2P("pass_to_pass", "result.json", "P2P.txt");

        /* String f2pTest = checkMissingF2P_P2P("fail_to_pass");
        System.out.println(f2pTest);

        String p2pTest = checkMissingF2P_P2P("pass_to_pass");
        System.out.println(p2pTest); */

        // List<String> f2pTestsInBefore = checkF2PInBefore();
        // System.out.println(f2pTestsInBefore.size());
    }
}
