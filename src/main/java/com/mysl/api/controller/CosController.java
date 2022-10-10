package com.mysl.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.mysl.api.common.util.CosUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author Ivan Su
 * @date 2022/8/18
 */
@RestController
@RequestMapping
@Slf4j
public class CosController {

//    @GetMapping("/cos/signed_url")
//    public ResponseEntity getSignedUrl(@RequestParam String key) {
//        String url = CosUtil.generateSimplePresignedDownloadUrl(key, new Date(System.currentTimeMillis() + 20 * 1000));
//        return ResponseEntity.ok(url);
//    }

    @PostMapping("/cos/queue/callback")
    public ResponseEntity callback(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStream is = request.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String s = "";
        while ((s = br.readLine()) != null) {
            sb.append(s);
        }
        if (sb.toString().length() > 0) {
            JSONObject jsonObject = JSONObject.parseObject(sb.toString());
            log.info("cos callback json: {}", jsonObject.toJSONString());
        }
        return ResponseEntity.ok(null);
    }
}
