package com.mbv.framework.util;

import com.mbv.framework.props.GCMProps;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by arindamnath on 06/04/16.
 */
public class GCMUtil {

    @Autowired
    GCMProps gcmProps;

    public GCMUtil() {

    }

    public GCMProps getGcmProps() {
        return gcmProps;
    }

    public void setGcmProps(GCMProps gcmProps) {
        this.gcmProps = gcmProps;
    }

    public boolean pushNotification(JSONObject data) {
        try {
            String decodedString;
            JSONParser jsonParser = new JSONParser();

            URL url = new URL("https://gcm-http.googleapis.com/gcm/send");
            HttpURLConnection httppost = (HttpURLConnection) url.openConnection();
            httppost.setRequestMethod("POST");
            httppost.setDoInput(true);
            httppost.setDoOutput(true);
            httppost.setRequestProperty("User-Agent", "Mozilla/5.0");
            httppost.setRequestProperty("Content-Type", "application/json");
            httppost.setRequestProperty("Authorization", "key=" + gcmProps.getKey());

            DataOutputStream out = new DataOutputStream(httppost.getOutputStream());
            out.writeBytes(data.toString());
            out.flush();
            out.close();
            //Read the response data
            StringBuilder sb = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    httppost.getInputStream()));
            while ((decodedString = in.readLine()) != null)
                sb.append(decodedString);
            in.close();
            //Parse the incoming response
            JSONObject response = (JSONObject) jsonParser.parse(sb.toString());
            if(response.containsKey("success")) {
                if((Long) response.get("success") == 1) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        try {
            GCMUtil gcmUtil = new GCMUtil();
            JSONObject data = new JSONObject();
            data.put("to", "c-8lbRTTv2s:APA91bGVg-2kFR4Q8Is2zjebM8H1a68dYcOpNHebExIfomKzGjMcmCOw-YpAzPBOwzHdMiwFQ5-_a02N_NboZYIQ_E4lg18O3aWYaenNfhqaFqdy4th8HgI7ARyxu2Cehit_i9SroROR");
            JSONObject loan = new JSONObject();
            loan.put("loanId", 22);
            JSONObject notification = new JSONObject();
            notification.put("title", "mPokket Approval");
            notification.put("text","Your request id KASABFASF, has been approved by a lender. Please accept the request to begin transaction processing.");
            data.put("data", loan);
            data.put("notification", notification);
            gcmUtil.pushNotification(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
