package ru.glitchless.telegrambridge.core.utils;

import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.List;

public class HttpUtils {

    /**
     * Executes a simple HTTP-GET request
     *
     * @param url URL to request
     * @param proxy proxy of request
     * @return The result of request
     * @throws Exception I/O Exception or HTTP errors
     */
    public static String httpGet(String url, Proxy proxy) throws Exception {
        URL u = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) u.openConnection(proxy);
        InputStream is = connection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = br.readLine()) != null) {
            response = response.append(line).append('\r');
        }
        connection.disconnect();
        return response.toString();
    }

    public static String httpGet(String url) throws Exception {
        return httpGet(url);
    }

    @Nullable
    public static String doPostRequest(String baseUrl, List<AbstractMap.SimpleEntry<String, Object>> params, Logger logger, Proxy proxy) throws IOException {
        HttpURLConnection connection = null;
        OutputStream os = null;
        InputStream is = null;
        try {
            byte[] postDataBytes = getQuery(params).getBytes(StandardCharsets.UTF_8);

            connection = (HttpURLConnection) new URL(baseUrl).openConnection(proxy);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "UTF-8");
            connection.setRequestProperty("Content-Length", Integer.toString(postDataBytes.length));

            os = new DataOutputStream(connection.getOutputStream());
            os.write(postDataBytes);
            os.flush();
            os.close();

            is = new DataInputStream(connection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = br.readLine()) != null) {
                response.append(line).append('\r');
            }
            if (connection.getResponseCode() != 200) {
                logger.warn("there were errors communicating with the Telegram Services! " + connection.getResponseCode() + "\nResponse: " + response);
            }
            return response.toString();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (is != null) {
                is.close();
            }
        }
    }

    public static String doPostRequest(String baseUrl, List<AbstractMap.SimpleEntry<String, Object>> params, Logger logger) throws IOException {
        return doPostRequest(baseUrl, params, logger, Proxy.NO_PROXY);
    }

    private static String getQuery(List<AbstractMap.SimpleEntry<String, Object>> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (AbstractMap.SimpleEntry<String, Object> pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue().toString(), "UTF-8"));
        }

        return result.toString();
    }

    public static boolean isAvailable(String address, Proxy proxy) {
        try {
            final URL url = new URL(address);
            final URLConnection conn = url.openConnection(proxy);
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean isAvailable(String address) {
        return isAvailable(address, Proxy.NO_PROXY);
    }
}
