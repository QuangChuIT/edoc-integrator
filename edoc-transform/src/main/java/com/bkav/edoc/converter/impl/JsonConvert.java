package com.bkav.edoc.converter.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class JsonConvert {
    private static final String filePath = "/home/huynq/Desktop/old_json.txt";

    private static String readFileToString(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    private static List<String> getValuesForGivenKey(String jsonArrayStr, String key) {
        JSONArray jsonArray = new JSONArray(jsonArrayStr);
        return IntStream.range(0, jsonArray.length())
                .mapToObj(index -> ((JSONObject)jsonArray.get(index)).optString(key))
                .collect(Collectors.toList());
    }

    private static List<String> convertDate(List<String> NgayCapThuoc) {
        List<String> ngaycapthuoc = new ArrayList<>();
        for (String ngaycap: NgayCapThuoc) {
            String ngay = ngaycap.substring(0, 10);
            ngaycapthuoc.add(ngay);
        }
        return ngaycapthuoc;
    }

    public static void main(String[] args) throws IOException {
        String input = readFileToString(filePath);
        Map<String, Object> result = null;
        JSONObject jsonObject = new JSONObject(input);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray record = data.getJSONArray("record");

        List<JSONObject> listdata = new ArrayList<>();
        if (record != null) {
            for (int i = 0; i < record.length(); i++){
                listdata.add(record.getJSONObject(i));
            }
        }
        for (JSONObject o: listdata) {
            int i = 0;
            Map<String, Object>  map = new ObjectMapper().readValue(o.toString(), HashMap.class);
            if (i == 0) {
                result.put("NgayKe", map.get("NgayCapThuoc"));
                result.put("NguoiKe", map.get("TenBacSy"));
                result.put("MaBacSyKe", map.get("MaBSDieuTri"));
                result.put("record", map.get("Thuoc"));
                i++;
            }
            if (result.containsValue(map.get("NgayCapThuoc")) && result.containsValue(map.get("TenBacSy"))) {
                result.put("record", map.get("Thuoc"));
            }
            System.out.println(result.toString());
        }

        List<String> BacSi = getValuesForGivenKey(record.toString(), "TenBacSy");
        System.out.println(BacSi);
        System.out.println(BacSi.size());

        List<String> NgayCapThuoc = convertDate(getValuesForGivenKey(record.toString(), "NgayCapThuoc"));
        Set<String> ngaycapthuoc = NgayCapThuoc.stream().filter(ngay -> Collections.frequency(NgayCapThuoc, ngay) > 1).collect(Collectors.toSet());

        System.out.println(ngaycapthuoc);

        List<String> Thuoc = getValuesForGivenKey(record.toString(), "Thuoc");
        System.out.println(Thuoc);
    }
}