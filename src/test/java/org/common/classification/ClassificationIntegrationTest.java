package org.common.classification;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.mahout.classifier.sgd.AdaptiveLogisticRegression;
import org.apache.mahout.classifier.sgd.CrossFoldLearner;
import org.apache.mahout.classifier.sgd.L1;
import org.apache.mahout.classifier.sgd.ModelSerializer;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;
import org.apache.mahout.ep.State;
import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.function.DoubleFunction;
import org.apache.mahout.math.function.Functions;
import org.apache.mahout.vectorizer.encoders.ConstantValueEncoder;
import org.apache.mahout.vectorizer.encoders.Dictionary;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;
import org.apache.mahout.vectorizer.encoders.TextValueEncoder;
import org.junit.Test;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

public class ClassificationIntegrationTest {

    static final int FEATURES = 10000;
    static final TextValueEncoder encoder = new TextValueEncoder("body");
    static final FeatureVectorEncoder bias = new ConstantValueEncoder("Intercept");
    private static final String[] LEAK_LABELS = { "none", "month-year", "day-month-year" };

    // tests

    @Test
    public final void whenClassifierLogic_thenNoException() {
        // training
        // ModelSerializer.writeBinary(path, model);

        // deployment
        // final Writable m = ModelSerializer.readBinary(in, clazz);
        // m.classifyScalar(featureVector);
    }

    @Test
    public final void whenTry1_thenNoException() throws IOException {
        final int leakType = 0;
        // TODO code application logic here
        final AdaptiveLogisticRegression learningAlgorithm = new AdaptiveLogisticRegression(20, FEATURES, new L1());
        final Dictionary newsGroups = new Dictionary();
        // ModelDissector md = new ModelDissector();
        final ListMultimap<String, String> noteBySection = LinkedListMultimap.create();
        noteBySection.put("good", "I love this product, the screen is a pleasure to work with and is a great choice for any business");
        noteBySection.put("good", "What a product!! Really amazing clarity and works pretty well");
        noteBySection.put("good", "This product has good battery life and is a little bit heavy but I like it");

        noteBySection.put("bad", "I am really bored with the same UI, this is their 5th version(or fourth or sixth, who knows) and it looks just like the first one");
        noteBySection.put("bad", "The phone is bulky and useless");
        noteBySection.put("bad", "I wish i had never bought this laptop. It died in the first year and now i am not able to return it");

        encoder.setProbes(2);
        double step = 0;
        final int[] bumps = { 1, 2, 5 };
        double averageCorrect = 0;
        double averageLL = 0;
        int k = 0;
        // -------------------------------------
        // notes.keySet()
        for (final String key : noteBySection.keySet()) {
            System.out.println(key);
            final List<String> notes = noteBySection.get(key);
            for (final Iterator<String> it = notes.iterator(); it.hasNext();) {
                final String note = it.next();

                final int actual = newsGroups.intern(key);
                final Vector v = ClassificationUtil.encodeFeatureVector(note);
                learningAlgorithm.train(actual, v);

                k++;
                final int bump = bumps[(int) Math.floor(step) % bumps.length];
                final int scale = (int) Math.pow(10, Math.floor(step / bumps.length));
                final State<AdaptiveLogisticRegression.Wrapper, CrossFoldLearner> best = learningAlgorithm.getBest();
                double maxBeta;
                double nonZeros;
                double positive;
                double norm;

                double lambda = 0;
                double mu = 0;
                if (best != null) {
                    final CrossFoldLearner state = best.getPayload().getLearner();
                    averageCorrect = state.percentCorrect();
                    averageLL = state.logLikelihood();

                    final OnlineLogisticRegression model = state.getModels().get(0);
                    // finish off pending regularization
                    model.close();

                    final Matrix beta = model.getBeta();
                    maxBeta = beta.aggregate(Functions.MAX, Functions.ABS);
                    nonZeros = beta.aggregate(Functions.PLUS, new DoubleFunction() {

                        @Override
                        public double apply(final double v) {
                            return Math.abs(v) > 1.0e-6 ? 1 : 0;
                        }
                    });
                    positive = beta.aggregate(Functions.PLUS, new DoubleFunction() {

                        @Override
                        public double apply(final double v) {
                            return v > 0 ? 1 : 0;
                        }
                    });
                    norm = beta.aggregate(Functions.PLUS, Functions.ABS);

                    lambda = learningAlgorithm.getBest().getMappedParams()[0];
                    mu = learningAlgorithm.getBest().getMappedParams()[1];
                } else {
                    maxBeta = 0;
                    nonZeros = 0;
                    positive = 0;
                    norm = 0;
                }
                System.out.println(k % (bump * scale));
                if (k % (bump * scale) == 0) {
                    if (learningAlgorithm.getBest() != null) {
                        System.out.println("----------------------------");
                        ModelSerializer.writeBinary("c:/tmp/news-group-" + k + ".model", learningAlgorithm.getBest().getPayload().getLearner().getModels().get(0));
                    }

                    step += 0.25;
                    System.out.printf("%.2f\t%.2f\t%.2f\t%.2f\t%.8g\t%.8g\t", maxBeta, nonZeros, positive, norm, lambda, mu);
                    System.out.printf("%d\t%.3f\t%.2f\t%s\n", k, averageLL, averageCorrect * 100, LEAK_LABELS[leakType % 3]);
                }
            }

        }
        learningAlgorithm.close();
    }

}
