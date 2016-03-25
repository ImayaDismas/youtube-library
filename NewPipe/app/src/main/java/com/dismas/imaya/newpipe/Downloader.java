package com.dismas.imaya.newpipe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;

import javax.net.ssl.HttpsURLConnection;

import info.guardianproject.netcipher.NetCipher;

/**
 * Created by imaya on 3/25/16.
 */
public class Downloader implements com.dismas.imaya.newpipe.extractor.Downloader {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0";

    /**Download the text file at the supplied URL as in download(String),
     * but set the HTTP header field "Accept-Language" to the supplied string.
     * @param siteUrl the URL of the text file to return the contents of
     * @param language the language (usually a 2-character code) to set as the preferred language
     * @return the contents of the specified text file*/
    public String download(String siteUrl, String language) throws IOException {
        URL url = new URL(siteUrl);
        //HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        HttpsURLConnection con = NetCipher.getHttpsURLConnection(url);
        con.setRequestProperty("Accept-Language", language);
        return dl(con);
    }

    /**Common functionality between download(String url) and download(String url, String language)*/
    private static String dl(HttpsURLConnection con) throws IOException {
        StringBuilder response = new StringBuilder();
        BufferedReader in = null;

        try {
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;

            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } catch(UnknownHostException uhe) {//thrown when there's no internet connection
            throw new IOException("unknown host or no network", uhe);
            //Toast.makeText(getActivity(), uhe.getMessage(), Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            throw new IOException(e);
        } finally {
            if(in != null) {
                in.close();
            }
        }

        return response.toString();
    }

    /**Download (via HTTP) the text file located at the supplied URL, and return its contents.
     * Primarily intended for downloading web pages.
     * @param siteUrl the URL of the text file to download
     * @return the contents of the specified text file*/
    public String download(String siteUrl) throws IOException {
        URL url = new URL(siteUrl);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        //HttpsURLConnection con = NetCipher.getHttpsURLConnection(url);
        return dl(con);
    }
}
