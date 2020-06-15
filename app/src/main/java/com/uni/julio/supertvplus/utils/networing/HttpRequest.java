package com.uni.julio.supertvplus.utils.networing;

import android.util.Log;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
                        return true;
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

    }


}
