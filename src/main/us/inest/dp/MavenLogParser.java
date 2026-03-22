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
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static us.inest.dp.Util.checkF2PInBefore;
import static us.inest.dp.Util.getF2P_P2P;

public class MavenLogParser {

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
                    String[] tokens = text.split(" ");
                    String test = tokens[0];
                    if (!content.contains(test)) {
                        return test;
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
        try (BufferedReader reader = Files.newBufferedReader(inputPath);
             BufferedWriter writer = Files.newBufferedWriter(outputPath);) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("<<< FAILURE!")) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void parseMaven(String inputFile, String outputFile, String failureOutputFile) {
        // Change these paths as needed
        Path inputPath = Paths.get(inputFile);
        Path outputPath = Paths.get(outputFile);
        Path failureOutputPath = Paths.get(failureOutputFile);

        // Regex to capture key:value pairs (case-insensitive for Errors, Skipped, etc.)
        Pattern pattern = Pattern.compile("(failures):\\s*(\\d+)", Pattern.CASE_INSENSITIVE);
        Pattern pattern2 = Pattern.compile("(errors):\\s*(\\d+)", Pattern.CASE_INSENSITIVE);

        try (BufferedReader reader = Files.newBufferedReader(inputPath);
             BufferedWriter writer = Files.newBufferedWriter(outputPath);
             BufferedWriter writer2 = Files.newBufferedWriter(failureOutputPath)) {

            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String key = matcher.group(1);
                    String value = matcher.group(2);
                    int intValue =  Integer.parseInt(value);
                    if (intValue > 0) {
                        writer2.write(key + ": " + value);
                        writer2.newLine();
                    } else {
                        writer.write(key + ": " + value);
                        writer.newLine();
                    }
                }
                Matcher matcher2 = pattern2.matcher(line);
                while (matcher2.find()) {
                    String key = matcher2.group(1);
                    String value = matcher2.group(2);
                    int intValue =  Integer.parseInt(value);
                    if (intValue > 0) {
                        writer2.write(key + ": " + value);
                        writer2.newLine();
                    } else {
                        writer.write(key + ": " + value);
                        writer.newLine();
                    }
                }
            }

            System.out.println("Parsing complete. Output written to " + outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // parseMaven("base.log", "base-output.txt", "base-failure-output.txt");
        // parseMaven("after.log", "after-output.txt", "after-failure-output.txt");

        // getF2P_P2P("fail_to_pass", "result.json", "F2P.txt");
        // getF2P_P2P("pass_to_pass", "result.json", "P2P.txt");

        /* String f2pTest = checkMissingF2P_P2P("fail_to_pass");
        System.out.println(f2pTest);

        String p2pTest = checkMissingF2P_P2P("pass_to_pass");
        System.out.println(p2pTest); */

        // List<String> f2pTests = checkF2PInBefore();
        // System.out.println(f2pTests.size());

        parseMaven("before.log", "before-output.txt", "before-failure-output.txt");

        // extractFailures("before.log", "before-failure-details.txt");
        // List<String> failureF2PList = getFailureF2PFromReport("apache__fesod-394");
        // System.out.println(failureF2PList.size());


    }
}
