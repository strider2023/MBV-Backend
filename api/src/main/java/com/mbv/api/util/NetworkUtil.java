package com.mbv.api.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by arindamnath on 30/03/16.
 */
public class NetworkUtil {

    public static String getSystemPublicIP() {
        try {
            URL url = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String ip = in.readLine();
            return ip;
        } catch (Exception e) {
            return null;
        }
    }
}
