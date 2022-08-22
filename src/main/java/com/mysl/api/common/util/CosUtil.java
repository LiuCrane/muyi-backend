package com.mysl.api.common.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;

/**
 * Tecent Cloud COS Client Util
 * @author Ivan Su
 * @date 2022/8/18
 */
@Component
@Slf4j
public class CosUtil {

    private CosUtil() {}

    private volatile static COSClient cosClient;

    private static String endpoint;

    private static String secretId;

    private static String secretKey;

    private static String region;

    private static String bucketName;


    @Value("${mysl.tencent.api.secretId}")
    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    @Value("${mysl.tencent.api.secretKey}")
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Value("${mysl.tencent.cos.endpoint}")
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Value("${mysl.tencent.cos.region}")
    public void setRegion(String region) {
        this.region = region;
    }

    @Value("${mysl.tencent.cos.bucketName}")
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    private static COSClient getCosClient() {
        if (null == cosClient) {
            synchronized (CosUtil.class) {
                if (null == cosClient) {
                    // 1 初始化用户身份信息(secretId, secretKey)
                    COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
                    // 2 设置bucket的区域, COS地域的简称请参照 https://www.qcloud.com/document/product/436/6224
                    ClientConfig clientConfig = new ClientConfig(new Region(region));
                    // 如果要获取 https 的 url 则在此设置，否则默认获取的是 http url
                    clientConfig.setHttpProtocol(HttpProtocol.https);
                    // 3 生成cos客户端
                    cosClient = new COSClient(cred, clientConfig);
                }
            }
        }
        return cosClient;
    }

    // 获取下载的预签名连接
    public static String generateSimplePresignedDownloadUrl(String filePath, Date expirationDate) {
        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(bucketName, filePath, HttpMethodName.GET);
        // 设置签名过期时间(可选), 若未进行设置则默认使用ClientConfig中的签名过期时间(1小时)
//        Date expirationDate = new Date(System.currentTimeMillis() + 30 * 60 * 1000);
        req.setExpiration(expirationDate);

        URL url = getCosClient().generatePresignedUrl(req);

        return UriComponentsBuilder.fromUriString(url.toString()).queryParam("ci-process", "pm3u8").queryParam("expires", 3600).build().toString();

    }

    // 生成预签名的上传连接
    public static String generatePresignedUploadUrl(String key) {
        Date expirationTime = new Date(System.currentTimeMillis() + 5 * 60 * 1000);
        URL url = getCosClient().generatePresignedUrl(bucketName, key, expirationTime, HttpMethodName.PUT);
        return url.toString();
    }

}
