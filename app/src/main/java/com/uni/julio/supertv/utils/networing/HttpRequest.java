package com.uni.julio.supertv.utils.networing;

<<<<<<< HEAD
import android.os.Build;
import android.util.Log;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
=======
import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

>>>>>>> parent of 74dd6739... test
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
<<<<<<< HEAD
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

=======
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

>>>>>>> parent of 74dd6739... test
public class HttpRequest {
    private static HttpRequest m_HttpRInstante;

    public static HttpRequest getInstance() {
        if(m_HttpRInstante == null) {
            m_HttpRInstante = new HttpRequest();
        }
        return m_HttpRInstante;
    }

    private HttpRequest() {

    }

<<<<<<< HEAD
    public void checkCertificate() {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    try {
                        if (certs != null && certs.length > 0) {
                            certs[0].checkValidity();
                        }
                    } catch (CertificateException e) {
                        Log.w("checkClientTrusted", e.toString());
                    }
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    try {
                        if (certs != null && certs.length > 0) {
                            certs[0].checkValidity();
                        }
                    } catch (CertificateException e) {
                        Log.w("checkServerTrusted", e.toString());
                    }
                }
            }};

            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection
                        .setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return (session.isValid() && hostname != null && (hostname.toLowerCase().contains("supertvplus.com") || hostname.toLowerCase().contains("superteve.com")));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

    }


=======
    private HttpURLConnection urlConnection;
    private BufferedReader reader;

    public String performRequest(String theUrl) {
        try {
            Uri uri = Uri.parse(theUrl);
            //Log.i("HttpRequest", "URI :" + uri.toString());
            URL url = new URL(uri.toString());

            if(theUrl.contains("https:")) {
                //TODO SuperTV CHECK THISSSSSSSSSSS
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                urlConnection = https;
            }
            else {
                urlConnection = (HttpURLConnection) url.openConnection();
            }
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                // Nothing to do.
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                urlConnection.disconnect();
                inputStream.close();
                reader.close();
                urlConnection = null;
                return null;
            }
            urlConnection.disconnect();
            inputStream.close();
            reader.close();
            urlConnection = null;
            return buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        urlConnection = null;
        return null;
    }


    // always verify the host - dont check for certificate
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Trust every server - dont check for any certificate
     */
    public void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        } };

        // Install the all-trusting trust manager
        try {
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            // trustAllCerts信任所有的证书
            sslContext.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.readTimeout(60, TimeUnit.SECONDS);
            builder.connectTimeout(30, TimeUnit.SECONDS);
            builder.writeTimeout(30, TimeUnit.SECONDS);
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
>>>>>>> parent of 74dd6739... test
}
