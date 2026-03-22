package us.inest.dp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Util {
    public static void getF2P_P2P(String key, String inputFile, String outputFile) {
        Path outputPath = Paths.get(outputFile);
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(new File(inputFile));
            JsonNode nodes = root.get(key);

            List<String> failToPassList = new ArrayList<>();
            if (nodes != null && nodes.isArray()) {
                Iterator<JsonNode> it = nodes.elements();
                while (it.hasNext()) {
                    String text = it.next().asText();
                    failToPassList.add(text);
                }
            }
            try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
                for (String text : failToPassList) {
                    writer.write(text);
                    writer.newLine();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<String> getFailureF2PFromReport(String subRootName) {
        ObjectMapper mapper = new ObjectMapper();
        List<String> failureF2PList = new ArrayList<>();
        try {
            JsonNode root = mapper.readTree(new File("report.json"));
            JsonNode subRoot = root.get(subRootName);
            JsonNode testStatus = subRoot.get("tests_status");
            JsonNode F2P = testStatus.get("FAIL_TO_PASS");
            JsonNode failures = F2P.get("failure");

            if (failures != null && failures.isArray()) {
                Iterator<JsonNode> it = failures.elements();
                while (it.hasNext()) {
                    String text = it.next().asText();
                    failureF2PList.add(text);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return failureF2PList;
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
                    if (content.contains(text)) {
                        f2pTests.add(text);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return f2pTests;
    }
}
