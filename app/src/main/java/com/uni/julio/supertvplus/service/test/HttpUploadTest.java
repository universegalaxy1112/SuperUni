package com.uni.julio.supertvplus.service.test;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author erdigurbuz
 */
public class HttpUploadTest extends Thread {

    public String fileURL = "";
    static int uploadedKByte = 0;
    double uploadElapsedTime = 0;
    boolean finished = false;
    double elapsedTime = 0;
    double finalUploadRate = 0.0;
    long startTime;

    public HttpUploadTest(String fileURL) {
        this.fileURL = fileURL;
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd;
        try {
            bd = new BigDecimal(value);
        } catch (Exception ex) {
            return 0.0;
        }
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public boolean isFinished() {
        return finished;
    }

    public double getInstantUploadRate() {
        try {
            BigDecimal bd = new BigDecimal(uploadedKByte);
        } catch (Exception ex) {
            return 0.0;
        }

        if (uploadedKByte >= 0) {
            long now = System.currentTimeMillis();
            elapsedTime = (now - startTime) / 1000.0;
            return round((Double) (((uploadedKByte / 1000.0) * 8) / elapsedTime), 2);
        } else {
            return 0.0;
        }
    }

    public double getFinalUploadRate() {
        return round(finalUploadRate, 2);
    }

    @Override
    public void run() {
        try {
            URL url = new URL(fileURL);
            uploadedKByte = 0;
            startTime = System.currentTimeMillis();

            ExecutorService executor = Executors.newFixedThreadPool(4);
            for (int i = 0; i < 4; i++) {
                executor.execute(new HandlerUpload(url));
            }
            executor.shutdown();
            while (!executor.isTerminated()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }
            }

            long now = System.currentTimeMillis();
            uploadElapsedTime = (now - startTime) / 1000.0;
            finalUploadRate = (Double) (((uploadedKByte / 1000.0) * 8) / uploadElapsedTime);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        finished = true;
    }
}

