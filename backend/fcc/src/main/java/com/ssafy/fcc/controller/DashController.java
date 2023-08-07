package com.ssafy.fcc.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import com.ssafy.fcc.domain.log.SensorType;
import com.ssafy.fcc.service.SystemService;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.Math;

@RestController
@RequestMapping("/dash")
@RequiredArgsConstructor
public class DashController {

    private static String nx;
    private static String ny;
    private static String baseDate;
    private static String baseTime;
    static int[] month_day = new int[]{0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private final SystemService systemService;

    //api base 날짜 입력하기 위해 계산하는 메서드
    public static void setBaseDate_Time(int year, int month, int day, int hour, int minute) {
        boolean flag = false;

        if (minute <= 40) {
            hour = hour - 7;

        } else {
            hour = hour - 6;

        }
        if (hour < 0) {
            flag = true;
            hour = 24 + hour;
            day--;
            if (day == 0) {
                day = month_day[month];
            }
        }


        baseDate = String.format("%04d", year) + String.format("%02d", month) + String.format("%02d", day);
        baseTime = String.format("%02d", hour) + "30";
        System.out.println(baseDate);
        System.out.println(baseTime);
    }


    @GetMapping("/map/rain")
    public ResponseEntity<Map<Integer, Double>> getRain(String year, String month, String day, String hour, String minute, String lat ,String lon) throws IOException {
        int y = Integer.parseInt(year);
        int m = Integer.parseInt(month);
        int d = Integer.parseInt(day);
        int h = Integer.parseInt(hour);
        int mm = Integer.parseInt(minute);
        double lon1 = Double.parseDouble(lon);
        double lat1 = Double.parseDouble(lat);
        System.out.println(lat);
        int[] location = mapConv(lon1, lat1, map);
        System.out.println(location[0]);
        System.out.println(location[1]);
        String nx = String.valueOf(location[1]);
        String ny = String.valueOf(location[0]);

        System.out.println("nx : " + nx);
        System.out.println(ny);
        setBaseDate_Time(y, m, d, h, mm);



        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst";
        String serviceKey = "v1eq8IhYcwTLBo4wgQV%2BhZ5AEd%2Bjklda8XsUmabyU0gdDxPWP0A%2FVTOXGT5KktfpCS5yTQKpaVLtekYDVrjl0Q%3D%3D";

        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /*‘21년 6월 28일 발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /*06시 발표(정시단위) */
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); /*예보지점의 Y 좌표값*/

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        String json = sb.toString();
//        System.out.println(json);
        JSONObject obj = new JSONObject(json);

        JSONObject response = obj.getJSONObject("response");
//        System.out.println(response.toString());
        JSONObject body = response.getJSONObject("body");
//        System.out.println(body.toString());
        JSONObject items = body.getJSONObject("items");
        JSONArray item = items.getJSONArray("item");
//        System.out.println();
//        System.out.println(item.toString());
//        System.out.println();
        Map<Integer, Double> result = new TreeMap<>(Comparator.naturalOrder());

        System.out.println(item.length());

        for (int i = 0; i < item.length(); i++) {
            JSONObject object = item.getJSONObject(i);
            String category = object.getString("category");
            String fcstValue = object.getString("fcstValue");
            Integer time = Integer.parseInt(object.getString("fcstTime").substring(0, 2));

            if ("RN1".equals(category)) {

                try {
                    Double value = Double.parseDouble(fcstValue.substring(0, fcstValue.length() - 2));
                    result.put(time, value);
                } catch (NumberFormatException e) {
                    System.out.println("Non-numeric value encountered in fcstValue: " + fcstValue);
                    // 예외를 무시하고 기본값 사용
                    result.put(time, 0.0);
                }
            }

        }
        System.out.println(result.toString());


        return new ResponseEntity<>(result, HttpStatus.OK);

    }


    LamcParameter map = new LamcParameter();

    public static class LamcParameter {
        double Re, grid, slat1, slat2, olon, olat, xo, yo;
        boolean first;
    }


    public static int[] mapConv(double lon, double lat, LamcParameter map) {


        // TODO: args를 받아서 처리하는 로직을 구현해야 합니다.
        // x, y 또는 lon, lat 값을 args로부터 설정하세요.

        map.Re = 6371.00877;
        map.grid = 5.0;
        map.slat1 = 30.0;
        map.slat2 = 60.0;
        map.olon = 126.0;
        map.olat = 38.0;
        map.xo = 210 / map.grid;
        map.yo = 675 / map.grid;
        map.first = true;


        double lon1 = 0, lat1 = 0, x1 = 0, y1 = 0;


        lon1 = lon;
        lat1 = lat;
        double[] tmp = lamcproj(lon1, lat1, x1, y1, map);
        int x = (int) (tmp[0] + 1.5);
        int y = (int) (tmp[1] + 1.5);

        int[] result = new int[]{x, y};

        return result;
    }

    public static double[] lamcproj(double lon, double lat, double x, double y, LamcParameter map) {
        double PI = Math.asin(1.0) * 2.0;
        double DEGRAD = PI / 180.0;
        double RADDEG = 180.0 / PI;
        double re = map.Re / map.grid;
        double slat1 = map.slat1 * DEGRAD;
        double slat2 = map.slat2 * DEGRAD;
        double olon = map.olon * DEGRAD;
        double olat = map.olat * DEGRAD;

        double sn = Math.tan(PI * 0.25 + slat2 * 0.5) / Math.tan(PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);
        map.first = false;

        double ra, theta;

        ra = Math.tan(PI * 0.25 + lat * DEGRAD * 0.5);
        ra = re * sf / Math.pow(ra, sn);
        theta = lon * DEGRAD - olon;
        if (theta > PI) theta -= 2.0 * PI;
        if (theta < -PI) theta += 2.0 * PI;
        theta *= sn;
        x = ra * Math.sin(theta) + map.xo;
        y = ro - ra * Math.cos(theta) + map.yo;

        return new double[]{x, y};

    }

    @GetMapping("/map/river")
    public ResponseEntity<List<Map<String, Object>>> getWaterHeight() throws IOException {
        String apiUrl =
                "http://api.hrfco.go.kr/CB761FAB-95F7-42CB-BA81-741A0D283E98/waterlevel/info.json";
        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        String json = sb.toString();


        JSONObject jsonObject = new JSONObject(json);
        JSONArray content = jsonObject.getJSONArray("content");


        List<Map<String, Object>> resultList = new ArrayList<>();
        for (int i = 0; i < content.length(); i++) {
            Map<String, Object> resultMap = new HashMap<>();
            JSONObject obj = content.getJSONObject(i);
            String[] lon_tmp = obj.getString("lon").split("-");
            String[] lat_tmp = obj.getString("lat").split("-");


//            System.out.println("lat : " + lat_tmp);
            try {
//                if (lon_tmp[0].equals) {
//                    throw new Exception();
//                }
//                if(lat_tmp[0] ==null){
//                    throw new Exception();
//                }
                double num = Double.parseDouble(lon_tmp[0]);
                double num2 = Double.parseDouble(lat_tmp[0]);
                double m1 = Double.parseDouble((lon_tmp[1]));
                double s1 = Double.parseDouble(lon_tmp[2]);
                double m2 = Double.parseDouble(lat_tmp[1]);
                double s2 = Double.parseDouble(lat_tmp[2]);
//                System.out.println("ParseInt ");
//                System.out.println(m1);
                String lon = String.valueOf(num+(m1/60.0)+(s1/3600.0));
                String lat = String.valueOf(num2 + (m2/60.0)+(s2/3600.0));


                String address = obj.getString("addr");
                String name = obj.getString("etcaddr");

//                System.out.println("lon : " + lon_tmp);

                String isDanger = obj.getString("fstnyn");

                resultMap.put("address", address);
                resultMap.put("name", name);
                resultMap.put("lon", lon);
                resultMap.put("lat", lat);
                resultMap.put("isDanger", isDanger);
//                System.out.println(resultMap.toString());
                resultList.add(resultMap);
            } catch (Exception e) {
//                System.out.println("에러발생 : "+ obj.toString());
                continue;
            }
        }


        if (resultList.size() > 0) return new ResponseEntity<>(resultList, HttpStatus.OK);
        else return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

    }


    @GetMapping("/weather")
    public ResponseEntity<Map<String, Object>>  getWeather(String year, String month, String day, String hour, String minute, Double lat , Double lon)throws Exception{


        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        System.out.println(year+", "+ month +", "+day+", "+hour+", "+minute+", "+lon+", "+lat);

        int[] location = mapConv(lon, lat, new LamcParameter());
        String ny = String.valueOf(location[1]);
        String nx = String.valueOf(location[0]);

        int tempHour=Integer.parseInt(hour);
        int m = Integer.parseInt(minute);
        if(m<=45) tempHour--;
        if(tempHour<0) tempHour=23;
        String hourStr= tempHour<=10 ? "0"+tempHour : tempHour+"";

        int baseHour = Integer.parseInt(hour);
        if(m>45)  baseHour = Integer.parseInt(hour)+1;
        String baseHourStr= baseHour<=10 ? "0"+baseHour : baseHour+"";
        baseHourStr+="00";




        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=k5EKhcvEVcfEXIn2mnb2nd7hZtlbvquM7M0AbKZeLZmT58550frSYtSymQCTNjpqQNDu0S7bfuYN0Q18CYRQzg%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(year+month+day, "UTF-8")); /*‘21년 6월 28일 발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(hourStr+"00", "UTF-8")); /*06시 발표(정시단위) */
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); /*예보지점의 Y 좌표값*/

        System.out.println(urlBuilder.toString());

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        String data= sb.toString();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> weatherData = objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {});
        Map<String, Object> relevantWeatherDataMap = new HashMap<>();


        Map<String, Object> items = (Map<String, Object>) ((Map<String, Object>)((Map<String, Object>) weatherData.get("response")).get("body")).get("items");
        List<Map<String, Object>> itemList = (List<Map<String, Object>>) items.get("item");
        for (Map<String, Object> weatherObject : itemList) {
            String category = (String) weatherObject.get("category");

            // SKY, PCP, PTY 카테고리인 경우에만 처리
            if (category.equals("SKY") || category.equals("RN1") || category.equals("PTY")) {

                String fcstTime = (String) weatherObject.get("fcstTime");
                if(fcstTime.equals(baseHourStr)){
                    Map<String, String> dataMap = new HashMap<>();
                    dataMap.put("category", category);
                    dataMap.put("fcstValue", (String) weatherObject.get("fcstValue"));
                    resultMap.put(category, dataMap);
                }
            }
        }



        System.out.println(resultMap);

        resultMap.put("message", "success");
        status = HttpStatus.ACCEPTED;
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @GetMapping("/facilities/{facility_id}/sensors/{category}")
    public int dashSensor(@PathVariable("facility_id") int facilityId, @PathVariable String category) {
        return systemService.getSensorData(facilityId,category);
    }



}