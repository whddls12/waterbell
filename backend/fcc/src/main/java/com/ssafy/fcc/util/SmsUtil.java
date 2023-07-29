package com.ssafy.fcc.util;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class SmsUtil {
    @Value("${accessKey}")
    private String accessKey;

    @Value("${secretKey}")
    private String secretKey;

    @Value("${serviceId}")
    private String serviceId;

    public void sendSMS(String from, String to, String content) throws IOException, NoSuchAlgorithmException, InvalidKeyException {

        String hostNameUrl = "https://sens.apigw.ntruss.com";
        String requestUrl = "/sms/v2/services/";
        String requestUrlType = "/messages";
        String method = "POST";
        String timestamp = Long.toString(System.currentTimeMillis());
        requestUrl += serviceId + requestUrlType;
        String apiUrl = hostNameUrl + requestUrl;

        JSONObject bodyJson = new JSONObject();
        JSONObject toJson = new JSONObject();
        JSONArray toArr = new JSONArray();
        toJson.put("content", content);
        toJson.put("to", to);
        toArr.add(toJson);

        bodyJson.put("type", "sms");
        bodyJson.put("contentType", "comm");
        bodyJson.put("countryCode", "82");
        bodyJson.put("from", from);
        bodyJson.put("content", content);
        bodyJson.put("messages", toArr);

        String body = bodyJson.toJSONString();

        System.out.println(body);


        URL url = new URL(apiUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setUseCaches(false);
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("content-type", "application/json");
        con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
        con.setRequestProperty("x-ncp-iam-access-key", accessKey);
        con.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(requestUrl, timestamp, method, accessKey, secretKey));
        con.setRequestMethod(method);
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());

        wr.write(body.getBytes());
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        BufferedReader br;
        System.out.println("responseCode" + " " + responseCode);
        if (responseCode == 202) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String errorLine;
            while ((errorLine = br.readLine()) != null) {
                errorResponse.append(errorLine);
            }
            br.close();
            throw new RuntimeException("Failed : HTTP error code : " + responseCode + ", message : " + errorResponse.toString());
        }

        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }
        br.close();

        System.out.println(response.toString());


    }

    public static String makeSignature(String url, String timestamp, String method, String accessKey, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String space = " ";
        String newLine = "\n";

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey;
        String encodeBase64String;
        try {
            signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
            encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);
        } catch (UnsupportedEncodingException e) {
            encodeBase64String = e.toString();
        }

        return encodeBase64String;
    }

    public String makeSmsContent(String certificationNumber){
        return "[WaterBell] 인증번호 ["+certificationNumber+"]를 입력해주세요.";
    }
}

