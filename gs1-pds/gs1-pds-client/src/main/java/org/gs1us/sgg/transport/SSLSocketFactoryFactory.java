package org.gs1us.sgg.transport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

public class SSLSocketFactoryFactory
{
    public SSLSocketFactory createSSLSocketFactory(String keyFilename, String keyPassword) 
            throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException, UnrecoverableKeyException, KeyManagementException 
    {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        
        ClassLoader classLoader = getClass().getClassLoader();
        URL keyFileResource = classLoader.getResource(keyFilename);
        
        
        InputStream keyInput = keyFileResource.openStream();
        keyStore.load(keyInput, keyPassword.toCharArray());
        keyInput.close();

        keyManagerFactory.init(keyStore, keyPassword.toCharArray());

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

        return context.getSocketFactory();
      }
}
