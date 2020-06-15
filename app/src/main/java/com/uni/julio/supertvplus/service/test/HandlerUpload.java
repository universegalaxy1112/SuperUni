package com.uni.julio.supertvplus.service.test;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class HandlerUpload extends Thread {

    URL url;
    private int retryCount = 0;


    public HandlerUpload(URL url) {
        this.url = url;
    }

    public void run() {
        byte[] buffer = new byte[150 * 1024];
        long startTime = System.currentTimeMillis();
        int timeout = 10;

        while (true) {
            if(retryCount > 100) break;
            try {
                HttpURLConnection conn = null;
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");

                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                dos.write(buffer, 0, buffer.length);
                dos.flush();

                conn.getResponseCode();

                HttpUploadTest.uploadedKByte += buffer.length / 1024.0;
                long endTime = System.currentTimeMillis();
                double uploadElapsedTime = (endTime - startTime) / 1000.0;
                if (uploadElapsedTime >= timeout) {
                    break;
                }
                dos.close();
                conn.disconnect();
            } catch (Exception ex) {
                retryCount++;
                ex.printStackTrace();
            }
        }
    }
}
