package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import net.lingala.zip4j.core.ZipFile;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by Botan on 22/10/2016.
 */
public class BruteForce extends Task<String> {
    private long startTime = System.currentTimeMillis();

    private final char[] dictionary;
    private final ZipFile zipFile;
    private final Controller controller;

    private int passwordTested = 0;
    private boolean stop = false;

    private long maxPossibilites;

    public BruteForce(String value, ZipFile zipFile, Controller controller) {
        this.dictionary = value.toCharArray();
        this.zipFile = zipFile;
        this.controller = controller;
        this.maxPossibilites = binomial(dictionary.length,dictionary.length).longValue();
        System.err.println(maxPossibilites);
    }

    public void stop() {
        stop = true;
    }

    public void apply(char[] result, int offset, int length) {
        try {
            String password = new String(result, offset, length);
            passwordTested++;

            if(passwordTested % 200 == 0) {
                long seconds = (System.currentTimeMillis() - startTime)/1000;
                updateValue("Speed (pass/s) : " + passwordTested/(seconds == 0 ? 1 : seconds));
            }

            updateProgress(passwordTested,maxPossibilites);
            updateMessage("Passwords tested : " + passwordTested);
            updateTitle(password);

            zipFile.setPassword(password);
            zipFile.extractAll(zipFile.getFile().getPath().substring(0, zipFile.getFile().getPath().length() - 4));
            //stop();
        } catch (Exception e) { //bad password
        }
    }

    @Override
    protected String call() throws Exception {
        updateValue("Speed (pass/s) : 0");

        char[] result = new char[dictionary.length];
        int[] index = new int[dictionary.length];

        Arrays.fill(result, 0, result.length, dictionary[0]);
        Arrays.fill(index, 0, index.length, 0);

        IntStream.range(1, dictionary.length).forEach(value -> {
            int updateIndex;
            do {

                if (stop) { //password find
                    Platform.runLater(() -> controller.jobComplete());
                    return;
                }

                apply(result, 0, value);

                for (updateIndex = value - 1; updateIndex != -1 && ++index[updateIndex] == dictionary.length; result[updateIndex] = dictionary[0], index[updateIndex] = 0, updateIndex--)
                    ;

                if (updateIndex != -1)
                    result[updateIndex] = dictionary[index[updateIndex]];

            } while (updateIndex != -1);
        });
        return null;
    }

    private BigInteger binomial(final int N, final int K) {
        BigInteger ret = BigInteger.ONE;
        for (int k = 0; k < K; k++) {
            ret = ret.multiply(BigInteger.valueOf(N-k)).divide(BigInteger.valueOf(k+1));
        }
        return ret;
    }
}
