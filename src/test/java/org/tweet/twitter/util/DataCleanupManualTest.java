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
import org.classification.data.ClassificationData.Commercial;
import org.classification.data.ClassificationData.Commercial.Accept;
import org.classification.data.ClassificationData.Commercial.Reject;
import org.classification.data.GenericClassificationDataUtil;
import org.junit.Test;
import org.stackexchange.gather.CleanupStringFunction;
import org.stackexchange.gather.ContainsKeywordPredicate;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class DataCleanupManualTest {

    private static final String EXTERNAL_PATH = "/opt/sandbox/";

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
        final List<String> filesToClean = Lists.newArrayList(Reject.FILE_WIN, Accept.FILE_WIN, Reject.FILE_DEAL, Accept.FILE_DEAL, Reject.FILE_DEALS, Accept.FILE_DEALS);
        for (final String fileToClean : filesToClean) {
            cleanAndOrganizeFile(Commercial.path(fileToClean), fileToWrite(fileToClean));
        }
    }

    @Test
    public final void givenCommercialDataFile1_whenCleaningUpDuplicatesInFile_thenFileIsCleaned() throws IOException {
        final String file = "rejected-remix.txt";
        cleanAndOrganizeFile("/notes/" + file, fileToWrite(file));
    }

    // note: do not run this for accept files - these should actually contain non-exact matches of the keyword
    @Test
    public final void givenProcessingWin_whenSeparatingExamplesBasedOnIfTheyActuallyContainTheKeyword_thenOK() throws IOException {
        final Set<String> uniqueLines = lines(Reject.WIN);
        final List<String> doContainTheRightKeyword = Lists.newArrayList(Iterables.filter(uniqueLines, new ContainsKeywordPredicate(Lists.newArrayList("win", "winwin", "win-win", "wins", "winner", "winners", "winning", "wining"))));
        uniqueLines.removeAll(doContainTheRightKeyword);
        System.out.println("Not OK" + uniqueLines);
        write(doContainTheRightKeyword, EXTERNAL_PATH + "withKeyword_" + Reject.FILE_WIN);
    }

    @Test
    public final void givenProcessingDeal_whenSeparatingExamplesBasedOnIfTheyActuallyContainTheKeyword_thenOK() throws IOException {
        final Set<String> uniqueLines = lines(Reject.DEAL);
        final List<String> doContainTheRightKeyword = Lists.newArrayList(Iterables.filter(uniqueLines, new ContainsKeywordPredicate(Lists.newArrayList("deal", "deals"))));
        uniqueLines.removeAll(doContainTheRightKeyword);
        System.out.println("Not OK" + uniqueLines);
        write(doContainTheRightKeyword, EXTERNAL_PATH + "withKeyword_" + Reject.FILE_DEAL);
    }

    // util

    private final void cleanAndOrganizeFile(final String inputFile, final String outputFile) throws IOException {
        final Set<String> uniqueLines = lines(inputFile);

        final List<String> uniqueLinesCleanWithEmpty = Lists.newArrayList(Iterables.transform(uniqueLines, new CleanupStringFunction()));
        final List<String> uniqueLinesCleanNoEmpty = Lists.newArrayList(Iterables.filter(uniqueLinesCleanWithEmpty, new IsNotEmpty()));
        final List<String> uniqueLinesClean = Lists.newArrayList(Iterables.filter(uniqueLinesCleanNoEmpty, new IsNotComment()));
        final List<String> banned = Lists.newArrayList(Iterables.filter(uniqueLinesClean, new BannedByCommercialAnalysis()));
        uniqueLinesClean.removeAll(banned);
        uniqueLinesClean.add("\n");
        uniqueLinesClean.addAll(banned);

        // write to file
        write(uniqueLinesClean, outputFile);
    }

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

    private static String fileToWrite(final String fileName) {
        return EXTERNAL_PATH + "new_" + fileName;
    }

}
