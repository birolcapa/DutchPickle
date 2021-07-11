/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.tiobe.gherkin;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RulesTests {
    public static List<Arguments> getTestFiles() {
        final ArrayList<Arguments> result = new ArrayList<>();
        try {
            int index = 1;
            while (true) {
                result.add(Arguments.of(String.valueOf(index), new File(RulesTests.class.getResource(String.format("/com/tiobe/gherkin/Rule%s.feature", index)).getFile()).getAbsolutePath()));
                index++;
            }
        } catch (final Exception e) {
            return result;
        }
    }

    public static List<Integer> getExpectedViolationLineNumbers(final List<String> lines) {
        final List<Integer> lineNumbers = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);
            final int lineNumber = i + 1;
            final Matcher matcher = Pattern.compile("# Violation").matcher(line);
            while (matcher.find()) {
                lineNumbers.add(lineNumber);
            }
        }
        return lineNumbers;
    }

    @ParameterizedTest(name = "testRule{0}")
    @MethodSource("getTestFiles")
    public void testRule(final String rule, final Path path) throws IOException {
        final String contents = Files.readString(path);
        final List<String> lines = contents.lines().collect(Collectors.toUnmodifiableList());
        final List<Integer> expected = getExpectedViolationLineNumbers(lines);
        final List<Violation> violations = App.getViolations(path.toString(), List.of("Rule" + rule));
        violations.forEach(x -> x.printToStdout(path.toString()));
        final List<Integer> actual = violations.stream()
                .map(Violation::getLineNumber)
                .sorted()
                .collect(Collectors.toUnmodifiableList());

        assertEquals(expected, actual);
    }
}
