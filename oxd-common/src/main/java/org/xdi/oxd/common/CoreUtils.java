/**
 * All rights reserved -- Copyright 2015 Gluu Inc.
 */
package org.xdi.oxd.common;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * Core utility class.
 *
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 27/07/2013
 */
public class CoreUtils {

    /**
     * Lazy initialization of jackson mapper via static holder
     */
    private static class JacksonMapperHolder {
        private static final ObjectMapper MAPPER = jsonMapper();

        public static ObjectMapper jsonMapper() {
            final AnnotationIntrospector jackson = new JacksonAnnotationIntrospector();

            final ObjectMapper mapper = new ObjectMapper();
            final DeserializationConfig deserializationConfig = mapper.getDeserializationConfig().withAnnotationIntrospector(jackson);
            final SerializationConfig serializationConfig = mapper.getSerializationConfig().withAnnotationIntrospector(jackson);
            if (deserializationConfig != null && serializationConfig != null) {
                // do nothing for now
            }
            return mapper;
        }
    }

    /**
     * UTF-8 encoding string
     */
    public static final String UTF8 = "UTF-8";

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(CoreUtils.class);

    public static final int COMMAND_STR_LENGTH_SIZE = 4;
    public static final int BUFFER_SIZE = 8192;

    /**
     * Avoid instance creation.
     */
    private CoreUtils() {
    }

    public static ScheduledExecutorService createExecutor() {
        return Executors.newSingleThreadScheduledExecutor(daemonThreadFactory());
    }

    public static ThreadFactory daemonThreadFactory() {
        return new ThreadFactory() {
            public Thread newThread(Runnable p_r) {
                Thread thread = new Thread(p_r);
                thread.setDaemon(true);
                return thread;
            }
        };
    }

    public static void sleep(int i) {
        try {
            Thread.sleep(i * 1000);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }
    }


    /**
     * Converts object to json string.
     *
     * @param p_object object to convert to string
     * @return json object representation in string format
     * @throws java.io.IOException if io problems occurs
     */
    public static String asJson(Object p_object) throws IOException {
        final ObjectMapper mapper = createJsonMapper().configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
        return mapper.writeValueAsString(p_object);
    }

    public static String asJsonSilently(Object p_object) {
        try {
            final ObjectMapper mapper = createJsonMapper().configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
            return mapper.writeValueAsString(p_object);
        } catch (Exception e) {
            LOG.error("Failed to serialize object into json.", e);
            return "";
        }
    }

    /**
     * Creates json mapper for json object serialization/deserialization.
     *
     * @return object mapper
     */
    public static ObjectMapper createJsonMapper() {
        return JacksonMapperHolder.MAPPER;
    }

    public static Command asCommand(String commandAsJson) throws IOException {
        return createJsonMapper().readValue(commandAsJson, Command.class);
    }

    public static long parseSilently(String p_str) {
        try {
            return Long.parseLong(p_str);
        } catch (Exception e) {
            return -1;
        }
    }

    public static String normalizeLengthPrefixString(int p_length) {
        if (p_length <= 0) {
            throw new IllegalArgumentException("Length must be more than zero.");
        }
        final String s = Integer.toString(p_length);
        final StringBuilder sb = new StringBuilder(s);
        final int sbLength = sb.length();
        if (sbLength < COMMAND_STR_LENGTH_SIZE) {
            for (int i = sbLength; i < COMMAND_STR_LENGTH_SIZE; i++) {
                sb.insert(0, '0');
            }
        }
        if (sb.length() != COMMAND_STR_LENGTH_SIZE) {
            throw new IllegalArgumentException("Normalized length size must be exactly: " + COMMAND_STR_LENGTH_SIZE);
        }
        return sb.toString();
    }

    public static ReadResult readCommand(String p_leftString, BufferedReader p_reader) throws IOException {
        int commandSize = -1;
        final StringBuilder storage = new StringBuilder(p_leftString != null ? p_leftString : "");
        while (true) {
            LOG.trace("commandSize: {}, stringStorage: {}", commandSize, storage.toString());

            final char[] buffer = new char[BUFFER_SIZE];
            final int readCount = p_reader.read(buffer, 0, BUFFER_SIZE);
            if (readCount == -1) {
                LOG.trace("End of stream. Quit.");
                return null;
            }

            storage.append(buffer, 0, readCount);

            final int storageLength = storage.length();
            if (commandSize == -1 && storageLength >= CoreUtils.COMMAND_STR_LENGTH_SIZE) {
                final String sizeString = storage.substring(0, CoreUtils.COMMAND_STR_LENGTH_SIZE);
                commandSize = (int) CoreUtils.parseSilently(sizeString);
                LOG.trace("Parsed sizeString: {}, commandSize: {}", sizeString, commandSize);

                if (commandSize == -1) {
                    LOG.trace("Unable to identify command size. Quit.");
                    return null;
                }
            }

            final int totalSize = commandSize + CoreUtils.COMMAND_STR_LENGTH_SIZE;
            if (commandSize != -1 && storageLength >= totalSize) {
                final String commandAsString = storage.substring(
                        CoreUtils.COMMAND_STR_LENGTH_SIZE, totalSize);

                String leftString = "";
                if (storageLength > (totalSize + 1)) {
                    storage.substring(totalSize + 1);
                }
                final ReadResult result = new ReadResult(commandAsString, leftString);
                LOG.trace("Read result: {}", result);
                return result;
            }
        }
    }

    public static boolean allNotBlank(String... p_strings) {
        if (p_strings != null && p_strings.length > 0) {
            for (String s : p_strings) {
                if (org.apache.commons.lang.StringUtils.isBlank(s)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * @param pathToKeyStore path to key store, e.g. D:/Development/gluu_conf/etc/certs/DA855F9895A1CA3B9E7D4BF5-java.jks
     * @param password       key store password
     * @return http client
     * @throws Exception
     */
    public static HttpClient createHttpClientWithKeyStore(File pathToKeyStore, String password) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream instream = new FileInputStream(pathToKeyStore);
        try {
            keyStore.load(instream, password.toCharArray());
        } finally {
            instream.close();
        }

        HttpClient httpClient = new DefaultHttpClient();

        SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore);
        socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", socketFactory, 443));
        httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

        return httpClient;
    }

    public static HttpClient createHttpClientTrustAll() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
//        System.setProperty("javax.net.debug", "SSL,handshake,trustmanager");

//        SSLSocketFactory sf = new SSLSocketFactory(new TrustStrategy() {
//            @Override
//            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//                return true;
//            }
//        }, new AllowAllHostnameVerifier());

        SSLSocketFactory sf = new SSLSocketFactory(new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        }, new X509HostnameVerifier() {
            @Override
            public void verify(String host, SSLSocket ssl) throws IOException {
            }

            @Override
            public void verify(String host, X509Certificate cert) throws SSLException {
            }

            @Override
            public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
            }

            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        }
        );

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        registry.register(new Scheme("https", 443, sf));
        ClientConnectionManager ccm = new PoolingClientConnectionManager(registry);
        return new DefaultHttpClient(ccm);
    }

    public static String secureRandomString() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }
}
