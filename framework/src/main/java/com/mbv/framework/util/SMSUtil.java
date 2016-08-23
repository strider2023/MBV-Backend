package com.mbv.framework.util;

import com.mbv.framework.props.SMSProps;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by arindamnath on 24/02/16.
 */
public class SMSUtil {

    @Autowired
    SMSProps smsProps;

    public SMSUtil() {

    }

    public SMSProps getSmsProps() {
        return smsProps;
    }

    public void setSmsProps(SMSProps smsProps) {
        this.smsProps = smsProps;
    }

    public String sendSMS(String to, String message, String senderId) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("http://login.cheapsmsbazaar.com/vendorsms/pushsms.aspx?user=");
            builder.append(smsProps.getUsername());
            builder.append("&password=");
            builder.append(smsProps.getPassword());
            builder.append("&msisdn=");
            builder.append(to);
            builder.append("&sid=");
            builder.append((senderId != null) ? senderId : smsProps.getDefaultSender());
            builder.append("&msg=");
            builder.append(URLEncoder.encode(message, "UTF-8"));
            builder.append("&fl=0&gwid=2");

            URL url = new URL(builder.toString());
            HttpURLConnection httppost= (HttpURLConnection) url.openConnection();;
            httppost.setRequestProperty("User-Agent", "Mozilla/5.0");
            httppost.setRequestMethod("GET");
            httppost.setDoOutput(true);
            httppost.setDoOutput(true);
            StringBuilder sb = new StringBuilder();
            String decodedString;
            BufferedReader in = new BufferedReader(new InputStreamReader(httppost.getInputStream()));
            while ((decodedString = in.readLine()) != null)
                sb.append(decodedString);
            in.close();
            JSONParser jsonParser = new JSONParser();
            JSONObject response = (JSONObject) jsonParser.parse(sb.toString());
            return ((response.containsKey("JobId")) ? (String) response.get("JobId") : "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
