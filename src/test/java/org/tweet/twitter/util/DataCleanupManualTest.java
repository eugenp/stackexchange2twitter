package org.tweet.twitter.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.classification.data.GenericClassificationDataUtil;
import org.junit.Test;
import org.stackexchange.gather.CleanupStringFunction;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class DataCleanupManualTest {

    private static final String INTERNAL_PATH = "/notes/test/";
    private static final String EXTERNAL_PATH = "/opt/sandbox/";

    static final String WIN_FILE1 = "win-toreject.txt";
    static final String WIN_FILE2 = "win-toaccept.txt";
    static final String DEAL_FILE1 = "deal-toreject.txt";
    static final String DEAL_FILE2 = "deal-toaccept.txt";
    static final String DEALS_FILE1 = "deals-toreject.txt";
    static final String DEALS_FILE2 = "deals-toaccept.txt";

    private final class BannedByCommercialAnalysis implements Predicate<String> {
        @Override
        public final boolean apply(final String input) {
            return TwitterUtil.isTweetBannedForCommercialAnalysis(input.toLowerCase());
        }
    }

    private final class IsNotComment implements Predicate<String> {
        @Override
        public final boolean apply(final String input) {
            return !input.trim().startsWith("//");
        }
    }

    private final class IsNotEmpty implements Predicate<String> {
        @Override
        public final boolean apply(final String input) {
            return !input.trim().isEmpty();
        }
    }

    // tests

    @Test
    public final void givenCommercialData_whenCleaningUpDuplicatesInFile_thenFileIsCleaned() throws IOException {
        final String fileToClean = DEALS_FILE2;
        final Set<String> uniqueLines = lines(fileToRead(fileToClean));

        final List<String> uniqueLinesCleanWithEmpty = Lists.newArrayList(Iterables.transform(uniqueLines, new CleanupStringFunction()));
        final List<String> uniqueLinesCleanNoEmpty = Lists.newArrayList(Iterables.filter(uniqueLinesCleanWithEmpty, new IsNotEmpty()));
        final List<String> uniqueLinesClean = Lists.newArrayList(Iterables.filter(uniqueLinesCleanNoEmpty, new IsNotComment()));
        final List<String> banned = Lists.newArrayList(Iterables.filter(uniqueLinesClean, new BannedByCommercialAnalysis()));
        uniqueLinesClean.removeAll(banned);
        uniqueLinesClean.add("\n");
        uniqueLinesClean.addAll(banned);

        // write to file
        write(uniqueLinesClean, fileToWrite(fileToClean));
    }

    // util

    private final void write(final List<String> lines, final String path) throws IOException {
        final File file = new File(path);
        file.createNewFile();
        final FileWriter fileWriter = new FileWriter(file);
        IOUtils.writeLines(lines, "\n", fileWriter);
        fileWriter.close();
    }

    private final Set<String> lines(final String path) throws IOException {
        final InputStream is = GenericClassificationDataUtil.class.getResourceAsStream(path);
        final List<String> lines = IOUtils.readLines(new BufferedReader(new InputStreamReader(is)));
        final Set<String> uniqueLines = Sets.newHashSet(lines);
        return uniqueLines;
    }

    // util

    private static String fileToRead(final String fileName) {
        return INTERNAL_PATH + fileName;
    }

    private static String fileToWrite(final String fileName) {
        return EXTERNAL_PATH + "new_" + fileName;
    }

}
