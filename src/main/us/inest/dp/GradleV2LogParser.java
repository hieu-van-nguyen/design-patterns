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

import static us.inest.dp.Util.getF2P_P2P;

public class GradleV2LogParser {

    public static String checkMissingF2P_P2P_Gradle(String key) {
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
                    // extract the test name and package
                    if (!content.contains(text)) {
                        return text;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void parseGradleV2(String inputFile, String outputFile, String failureOutputFile) {
        // Change these paths as needed
        Path inputPath = Paths.get(inputFile);
        Path outputPath = Paths.get(outputFile);
        Path failureOutputPath = Paths.get(failureOutputFile);

        // Regex to capture key:value pairs (case-insensitive for Errors, Skipped, etc.)
        Pattern pattern = Pattern.compile("failures\\s*=\\s*\"(\\d+)\"", Pattern.CASE_INSENSITIVE);
        Pattern pattern2 = Pattern.compile("errors\\s*=\\s*\"(\\d+)\"", Pattern.CASE_INSENSITIVE);

        try (BufferedReader reader = Files.newBufferedReader(inputPath);
             BufferedWriter writer = Files.newBufferedWriter(outputPath);
             BufferedWriter writer2 = Files.newBufferedWriter(failureOutputPath)) {

            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String aMatch = matcher.group(0);
                    String value = matcher.group(1);
                    writer.write(aMatch);
                    writer.newLine();
                    int intValue =  Integer.parseInt(value);
                    if (intValue > 0) {
                        writer2.write(aMatch);
                        writer2.newLine();
                    }
                }
                Matcher matcher2 = pattern2.matcher(line);
                while (matcher2.find()) {
                    String aMatch = matcher2.group(0);
                    String value = matcher2.group(1);
                    writer.write(aMatch);
                    writer.newLine();
                    int intValue =  Integer.parseInt(value);
                    if (intValue > 0) {
                        writer2.write(aMatch);
                        writer2.newLine();
                    }
                }
            }

            System.out.println("Parsing complete. Output written to " + outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        parseGradleV2("base.log", "base-output.txt", "base-failure-output.txt");
        parseGradleV2("after.log", "after-output.txt", "after-failure-output.txt");
        // parseGradleV2("before.log", "before-output.txt", "before-failure-output.txt");

        getF2P_P2P("fail_to_pass", "result.json", "F2P.txt");
        getF2P_P2P("pass_to_pass", "result.json", "P2P.txt");

        String f2pTest = checkMissingF2P_P2P_Gradle("fail_to_pass");
        System.out.println(f2pTest);

        String p2pTest = checkMissingF2P_P2P_Gradle("pass_to_pass");
        System.out.println(p2pTest);
    }
}
