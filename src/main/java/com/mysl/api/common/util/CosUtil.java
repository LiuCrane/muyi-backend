package com.mysl.api.common.util;

import com.mysl.api.entity.dto.UploadInfo;
import com.mysl.api.entity.enums.FileType;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.region.Region;
import com.tencent.cloud.CosStsClient;
import com.tencent.cloud.Credentials;
import com.tencent.cloud.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.shaded.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

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

    private static final Map<FileType, String> DIR_MAP = Map.of(FileType.IMAGE, "img/", FileType.AUDIO, "aud/", FileType.VIDEO, "vid/");

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

    private static Credentials getCredential(String allowPrefix) {
        TreeMap<String, Object> config = new TreeMap<String, Object>();
        try {
            // 云 api 密钥 SecretId
            config.put("secretId", secretId);
            // 云 api 密钥 SecretKey
            config.put("secretKey", secretKey);

            // 设置域名
            //config.put("host", "sts.internal.tencentcloudapi.com");

            // 临时密钥有效时长，单位是秒
            config.put("durationSeconds", 1800);

            // 换成你的 bucket
            config.put("bucket", bucketName);
            // 换成 bucket 所在地区
            config.put("region", region);

            // 可以通过 allowPrefixes 指定前缀数组, 例子： a.jpg 或者 a/* 或者 * (使用通配符*存在重大安全风险, 请谨慎评估使用)
            config.put("allowPrefixes", new String[] {
                    allowPrefix
            });

            // 密钥的权限列表。简单上传和分片需要以下的权限，其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
            String[] allowActions = new String[] {
                    // 简单上传
                    "name/cos:PutObject",
                    "name/cos:PostObject",
                    // 分片上传
                    "name/cos:InitiateMultipartUpload",
                    "name/cos:ListMultipartUploads",
                    "name/cos:ListParts",
                    "name/cos:UploadPart",
                    "name/cos:CompleteMultipartUpload"
            };
            config.put("allowActions", allowActions);

            Response response = CosStsClient.getCredential(config);
            return response.credentials;
        } catch (Exception e) {
            log.error("CosUtil getCredential error: ", e);
            throw new IllegalArgumentException("no valid secret !");
        }
    }

    // 获取下载的预签名连接
    public static String generateSimplePresignedDownloadUrl(String filePath, Date expirationDate) {
        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(bucketName, filePath, HttpMethodName.GET);
        // 设置签名过期时间(可选), 若未进行设置则默认使用ClientConfig中的签名过期时间(1小时)
//        Date expirationDate = new Date(System.currentTimeMillis() + 30 * 60 * 1000);
        req.setExpiration(expirationDate);

        URL url = getCosClient().generatePresignedUrl(req);
        return url.toString();
//        return UriComponentsBuilder.fromUriString(url.toString()).queryParam("ci-process", "pm3u8").queryParam("expires", 3600).build().toString();

    }

    // 生成包含预签名上传连接的信息
    public static UploadInfo generatePresignedUploadUrl(FileType fileType, String filename) {
        Date expirationTime = new Date(System.currentTimeMillis() + 20 * 60 * 1000);
//        URL url = getCosClient().generatePresignedUrl(bucketName, key, expirationTime, HttpMethodName.PUT);
//        return url.toString();
        HttpMethodName method = HttpMethodName.PUT;
        String dir = DIR_MAP.get(fileType);
        String ext = FilenameUtils.getExtension(filename);
        filename = DigestUtils.md5DigestAsHex(String.format("%s%s", filename, System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8));
        String key = String.format("%s%s.%s", dir, filename, ext);
        Credentials credentials = getCredential(String.format("%s*", dir));
        COSCredentials cred = new BasicCOSCredentials(credentials.tmpSecretId, credentials.tmpSecretKey);
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        COSClient client = new COSClient(cred, clientConfig);
        URL url = client.generatePresignedUrl(bucketName, key, expirationTime, method);
        return UploadInfo.builder()
                .endpoint(endpoint)
                .url(UriComponentsBuilder.fromUriString(url.toString())
                        .queryParam("x-cos-security-token", credentials.sessionToken).build().toString())
                .key(key).method(method.name()).build();
    }


}
