package com.mysl.api.lib;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class AesFile {
  public static void test() {
    location();
    // AesFile.clear();
    // String s = "CReateTime";
    // System.out.println(s.replaceAll("([A-Z])", "_$0").toLowerCase());
    // StaticSql.service.billType.getById("3");
  }
  private static void location() {
    String path = AesFile.formatFilename("public", true);
    if (path == null) {
      return;
    }
    File fi = new File(path);// 参数为空
    if (!fi.exists() && !fi.mkdirs()) {
      return;
    }

    // registry.addResourceHandler("*").addResourceLocations("file:" + path);
    AesFile.clear();
    // log.info("端口: " + port);
    log.info("自定义静态资源目录: " + path);

    // mediaReadService.upLocationAll();

  }

  public static String byteToHex(byte b) {
    String hex = Integer.toHexString(b & 0xFF);
    if (hex.length() < 2) {
      hex = "0" + hex;
    }
    return hex;
  }

  // #region 文件读取
  public static String fileExists(String str) {
    File fi = new File(str);
    String ret = null;
    try {
      ret = fi.getCanonicalPath();
    } catch (Exception e) {
    }
    return fi.exists() ? ret : null;
  }

  public static String formatFilename(String str) {
    File fi = new File(str);
    String ret = null;
    try {
      ret = fi.getCanonicalPath();
    } catch (IOException e) {
      // e.printStackTrace();
      // log.info(e.toString().substring(0, 60));
      log.info("出错: " + str);
    }
    return ret;
  }

  public static String formatFilename(String str, boolean createPath) {
    File fi = new File(str);
    String ret = null;
    try {
      ret = fi.getCanonicalPath();
      if (fi.getParentFile() != null) {
        fi = fi.getParentFile();
      }
      if (createPath && !fi.exists()) {
        fi.mkdirs();
      }
    } catch (IOException e) {
      // e.printStackTrace();
      // log.info(e.toString().substring(0, 60));
      log.info("出错: " + str);
    }
    return ret;
  }

  public static byte[] readFile(String fileName) {
    File file = new File(fileName);
    InputStream fi = null;
    long fileSize = file.length();
    if (fileSize > Integer.MAX_VALUE) {
      throw new RuntimeException("文件太大");
    }

    byte[] ret = new byte[(int) fileSize];
    try {
      // 一次读一个字节
      fi = new FileInputStream(file);
      int offset = 0, numRead = 0;
      while (offset < fileSize && (numRead = fi.read(ret, offset, (int) fileSize - offset)) >= 0) {
        offset += numRead;
      }
      fi.close();
      return ret;
    } catch (Exception e) {
      throw new RuntimeException("读取文件出错:" + fileName);
    }
  }

  public static boolean writeFile(String fileName, byte[] data) {
    try {
      FileOutputStream fos = new FileOutputStream(fileName);
      fos.write(data);
      fos.close();
      return true;
    } catch (Exception e) {
      log.info("写文件 错误");
      log.info(e);
      return false;
    }
  }

  /**
   * 根据路径删除指定的目录或文件，无论存在与否
   * 
   * @param sPath 要删除的目录或文件
   * @return 删除成功返回 true，否则返回 false。
   */
  public static boolean DeleteFolder(String sPath) {
    boolean flag = false;
    File file = new File(sPath);
    // 判断目录或文件是否存在
    if (!file.exists()) { // 不存在返回 false
      return flag;
    } else {
      // 判断是否为文件
      if (file.isFile()) { // 为文件时调用删除文件方法
        return deleteFile(sPath);
      } else { // 为目录时调用删除目录方法
        return deleteDirectory(sPath);
      }
    }
  }

  /**
   * 删除单个文件
   * 
   * @param sPath 被删除文件的文件名
   * @return 单个文件删除成功返回true，否则返回false
   */
  public static boolean deleteFile(String sPath) {
    boolean flag = false;
    File file = new File(sPath);
    // 路径为文件且不为空则进行删除
    if (file.isFile() && file.exists()) {
      file.delete();
      flag = true;
    }
    return flag;
  }

  /**
   * 删除目录（文件夹）以及目录下的文件
   * 
   * @param sPath 被删除目录的文件路径
   * @return 目录删除成功返回true，否则返回false
   */
  public static boolean deleteDirectory(String sPath) {
    // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
    if (!sPath.endsWith(File.separator)) {
      sPath = sPath + File.separator;
    }
    File dirFile = new File(sPath);
    // 如果dir对应的文件不存在，或者不是一个目录，则退出
    if (!dirFile.exists() || !dirFile.isDirectory()) {
      return false;
    }
    boolean flag = true;
    // 删除文件夹下的所有文件(包括子目录)
    File[] files = dirFile.listFiles();
    for (int i = 0; i < files.length; i++) {
      // 删除子文件
      if (files[i].isFile()) {
        flag = deleteFile(files[i].getAbsolutePath());
        if (!flag)
          break;
      } // 删除子目录
      else {
        flag = deleteDirectory(files[i].getAbsolutePath());
        if (!flag)
          break;
      }
    }
    if (!flag)
      return false;
    // 删除当前目录
    if (dirFile.delete()) {
      return true;
    } else {
      return false;
    }
  }
  // #endregion

  // #region AES部分
  private static final String ALGORITHM = "AES";
  private static String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

  public static SecretKey generatKey() {
    KeyGenerator keyGenerator = null;
    try {
      keyGenerator = KeyGenerator.getInstance(ALGORITHM);
    } catch (Exception e) {
      log.info("keyGenerator 出错");
      log.info(e);
      return null;
    }
    // Random
    SecureRandom secureRandom = new SecureRandom();
    keyGenerator.init(secureRandom);
    return keyGenerator.generateKey();
  }

  public static SecretKey generatKey(byte[] key) {
    return key.length == 16 ? new SecretKeySpec(key, ALGORITHM) : generatKey();
  }

  public static SecretKey generatKey(String key) {
    byte[] bk = null;
    bk = key.getBytes(StandardCharsets.UTF_8);
    if (bk.length != 16) {
      bk = Base64.getDecoder().decode(key);
    }
    return generatKey(bk);
  }

  static {
    try {
      Security.addProvider(new BouncyCastleProvider());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static byte[] crypto(byte[] data, int mode, SecretKey secretKey, byte[] iv) {
    Cipher cipher = null;
    try {
      cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
    } catch (Exception e) {
      throw new RuntimeException("cipher 出错:" + e.getMessage());
    }
    try {
      cipher.init(mode, secretKey, new IvParameterSpec(iv));
    } catch (Exception e) {
      throw new RuntimeException("cipher.init 出错:" + e.getMessage());
    }
    try {
      byte[] ret = cipher.doFinal(data);
      return ret;
    } catch (Exception e) {
      throw new RuntimeException("cipher.doFinal 出错:" + e.getMessage());
    }
  }

  public static byte[] aes(byte[] data, String key, Object de) {
    return crypto(data, de == null ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, generatKey(key), new byte[16]);
  }

  public static byte[] aes(byte[] data, byte[] key, Object de) {
    return crypto(data, de == null ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, generatKey(key), new byte[16]);

    // Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
    // SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
    // AlgorithmParameterSpec paramSpec = new IvParameterSpec(new byte[16]);
    // try {
    // cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
    // } catch (Exception e) {
    // log.info("还是出错");
    // log.info(e.getMessage());
    // throw new Exception(e.getMessage());
    // }

    // return cipher.doFinal(data);
  }

  public static byte[] aes(byte[] data, String key) {
    return crypto(data, Cipher.ENCRYPT_MODE, generatKey(key), new byte[16]);
  }

  public static byte[] aes(byte[] data, byte[] key) {
    return crypto(data, Cipher.ENCRYPT_MODE, generatKey(key), new byte[16]);
  }

  // #endregion

  // #region 数据处理
  public static long time() {
    return System.currentTimeMillis() / 1000;
  }

  public static byte[] getBytes(long value) {
    int l = 8;
    byte[] buffer = new byte[l];
    for (int i = 0; i < l; i++) {
      int offset = 64 - (i + 1) * 8;
      buffer[i] = (byte) ((value >> offset) & 0xff);
    }
    return buffer;
  }

  public static byte[] getBytes(int value) {
    int l = 8;
    byte[] buffer = new byte[l];
    for (int i = 0; i < l; i++) {
      int offset = 64 - (i + 1) * 8;
      buffer[i] = (byte) ((value >> offset) & 0xff);
    }
    return buffer;
  }

  public static byte[] getBytes(short value) {
    int l = 2;
    byte[] buffer = new byte[l];
    for (int i = 0; i < l; i++) {
      int offset = 64 - (i + 1) * 8;
      buffer[i] = (byte) ((value >> offset) & 0xff);
    }
    return buffer;
  }

  public static byte[] reverse(byte[] buffer) {
    int l = buffer.length, i = 0;
    byte[] ret = new byte[l];
    while (l-- > 0) {
      ret[i++] = buffer[l];
    }
    return ret;
  }

  public static byte[] randByte(int size) {
    byte[] r = new byte[size];
    Random rand = new Random(IdWorker.getId());
    for (int i = 0; i < size; i++) {
      r[i] = (byte) (rand.nextInt(0xff));
    }
    return r;
  }

  public static String Complement(int size, String cstr) {
    String ret = "";
    while (size-- > 0) {
      ret += cstr;
    }
    return ret;
  }

  public static long Placeholder(long value) {
    byte[] d = getBytes(value);
    String r = "0";
    for (int i = 0; i < d.length; i++) {
      if (d[i] != 0) {
        r = Complement(d.length - i, "ff");
      }
    }
    return Long.parseUnsignedLong(r, 16);
  }

  public static JSONObject request2json(HttpServletRequest request) {
    try {
      BufferedReader reader = request.getReader();
      StringBuilder builder = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        builder.append(line);
      }
      reader.close();
      String reqBody = builder.toString();
      return JSONObject.parseObject(reqBody);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void clear() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  public static void clear(Object p) {
    System.out.print("\033[H\033[2J");
    System.out.flush();
    log.info(p);
  }

  public static int s2i(String str) {
    if (str == null || str.length() == 0)
      return 0;
    char[] array = str.toCharArray();
    long result = 0; // 要返回的结果result
    int count = 0; // 记录‘+'或者‘-'出现的次数
    int num = 0; // 判断空格出现的位置
    int flag = 1; // 正数还是负数
    for (int i = 0; i < array.length; i++) {
      Character c = array[i];
      if (c >= '0' && c <= '9') {
        result = result * 10 + c - '0';
        // 判断是否溢出
        if (flag == 1 && result > Integer.MAX_VALUE) {
          return Integer.MAX_VALUE;
        } else if (flag == -1 && -result < Integer.MIN_VALUE)
          return Integer.MIN_VALUE;
        num++;
      } else if (c == ' ' && num == 0 && count == 0)
        continue;
      else if (c == '+' && count == 0) {
        count = 1;
      } else if (c == '-' && count == 0) {
        flag = -1;
        count = 1;
      } else {
        return (int) (flag * result);

      }
    }
    return (int) (flag * result);
  }

  public static long s2l(String str) {
    if (str == null || str.length() == 0) {
      return 0;
    }
    char[] array = str.toCharArray();
    long result = 0; // 要返回的结果result
    long count = 0; // 记录‘+'或者‘-'出现的次数
    long num = 0; // 判断空格出现的位置
    long flag = 1; // 正数还是负数
    for (int i = 0; i < array.length; i++) {
      Character c = array[i];
      if (c >= '0' && c <= '9') {
        result = result * 10 + c - '0';
        // 判断是否溢出
        if (flag == 1 && result > Long.MAX_VALUE) {
          return Long.MAX_VALUE;
        } else if (flag == -1 && -result < Long.MIN_VALUE)
          return Long.MIN_VALUE;
        num++;
      } else if (c == ' ' && num == 0 && count == 0)
        continue;
      else if (c == '+' && count == 0) {
        count = 1;
      } else if (c == '-' && count == 0) {
        flag = -1;
        count = 1;
      } else {
        return (long) (flag * result);
      }
    }
    return (long) (flag * result);
  }

  public static String getAddress(Location location) {
    try {
      String t = "http://apis.map.qq.com/ws/geocoder/v1?key=DRXBZ-EMXWX-Q6E4A-ZJIO3-XJUJF-NUBJH&output=json&location="
          + location.latitude + "," + location.longitude;
      t = new String(httpGet(t), "utf8");
      JSONObject j = JSONObject.parseObject(t.substring(t.indexOf("{"), t.lastIndexOf("}") + 1));
      t = j.getJSONObject("result").getString("address");
      if (t != null) {
        return t.length() < 128 ? t : t.substring(0, 128);
      }
    } catch (Exception e) {
    }
    return null;
  }

  public static <T> Page<T> getPage(JSONObject p, java.lang.Class<T> clazz) {
    long page = p.getLongValue("page");
    long size = p.getLongValue("size");
    if (size < 10) {
      size = 10;
    } else if (size > 100) {
      size = 100;
    }
    return page > 0 ? new Page<T>(page, size) : null;
  }
  // #endregion

  // #region mime
  private static int findString(String[] strAry, String str) {
    for (int i = 0; i < strAry.length; i++) {
      if (str.equals(strAry[i])) {
        return i;
      }
    }
    return -1;
  }

  private static String[] html = { "html", "htm", "shtml" };
  private static String[] text = { "css", "xml", "mml", "txt", "jad", "wml", "htc" };
  private static String[] img = { "gif", "jpg", "png", "svgz", "tiff", "wbmp", "webp", "ico", "jng", "bmp" };
  private static String[] audio = { "mid", "midi", "kar", "mp3", "ogg", "m4a", "ra", "aac" };
  private static String[] video = { "3gpp", "3gp", "mp4", "mpeg", "mpg", "mov", "webm", "flv", "m4v", "mng", "asf",
      "wmv", "avi" };

  public static String mime(String str) {
    String ret = null;
    if (findString(html, str) >= 0) {
      ret = "text/html;charset=utf-8";
    } else if (findString(text, str) >= 0) {
      ret = "text/" + str + ";charset=utf-8";
    } else if (findString(img, str) >= 0) {
      ret = "image/" + str;
    } else if (findString(audio, str) >= 0) {
      ret = "audio/" + str;
    } else if (findString(video, str) >= 0) {
      ret = "video/" + str;
    } else {
      switch (str) {
      case "js":
        ret = "application/javascript;charset=utf-8";
        break;
      case "woff":
        ret = "font/woff";
        break;
      case "woff2":
        ret = "font/woff2";
        break;
      case "ts":
        ret = "video/mp2t";
        break;
      case "m3u8":
        ret = "application/vnd.apple.mpegurl";
        break;

      default:
        ret = "application/" + str;
        break;
      }
    }

    return ret;
  }
  // #endregion

  // #region 获取私有属性
  public static Object getPrivate(Object s, String n) {
    Class<?> cls = s.getClass();
    try {
      Field field = cls.getDeclaredField(n);
      field.setAccessible(true);
      return field.get(s);
    } catch (Exception e) {
      // 获取属性错误
      log.info("获取属性错误");
    }

    return null;
  }

  public static Object getPrivate(Object s, String[] n) {
    Object ret = s;
    for (String nd : n) {
      ret = getPrivate(ret, nd);
      if (ret == null) {
        break;
      }
    }

    return ret;
  }
  // #endregion

  // #region 网络请求
  public static byte[] httpGet(String spec) {
    HttpURLConnection httpURLConnection = null;
    try {
      // 1. 得到访问地址的URL
      URL url = new URL(spec);
      // 2. 得到网络访问对象java.net.HttpURLConnection
      httpURLConnection = (HttpURLConnection) url.openConnection();
      /* 3. 设置请求参数（过期时间，输入、输出流、访问方式），以流的形式进行连接 */
      // 设置是否向HttpURLConnection输出
      httpURLConnection.setDoOutput(false);
      // 设置是否从httpUrlConnection读入
      httpURLConnection.setDoInput(true);
      // 设置请求方式 默认为GET
      httpURLConnection.setRequestMethod("GET");
      // 设置是否使用缓存
      httpURLConnection.setUseCaches(true);
      // 设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向
      httpURLConnection.setInstanceFollowRedirects(true);
      // 设置超时时间
      httpURLConnection.setConnectTimeout(3000);
      // 连接
      httpURLConnection.connect();
      // 4. 得到响应状态码的返回值 responseCode
      int code = httpURLConnection.getResponseCode();
      // 5. 如果返回值正常，数据在网络中是以流的形式得到服务端返回的数据
      String msg = "";
      if (code == 200) { // 正常响应
        // // 从流中读取响应信息
        // BufferedReader reader = new BufferedReader(new
        // InputStreamReader(httpURLConnection.getInputStream()));
        // String line = null;
        // while ((line = reader.readLine()) != null) { // 循环从流中读取
        // msg += line + "\n";
        // }
        // reader.close(); // 关闭流
        InputStream inputStream = httpURLConnection.getInputStream();
        byte[] buff = new byte[4096];
        int len;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        while ((len = inputStream.read(buff)) > 0) {
          os.write(buff, 0, len);
        }
        return os.toByteArray();
      }
      // 显示响应结果
      log.info(msg);
    } catch (IOException e) {
      log.info("转发出错，错误信息：" + e.getLocalizedMessage() + ";" + e.getClass());
    } finally {
      // 6. 断开连接，释放资源
      if (null != httpURLConnection) {
        try {
          httpURLConnection.disconnect();
        } catch (Exception e) {
          log.info("httpURLConnection 流关闭异常：" + e.getLocalizedMessage());
        }
      }
    }
    throw new RuntimeException("网络异常");
  }

  public static void doPost(String[] args) {
    HttpURLConnection httpURLConnection = null;
    try {
      // 1. 获取访问地址URL
      URL url = new URL("http://localhost:8080/Servlet/do_login.do");
      // 2. 创建HttpURLConnection对象
      httpURLConnection = (HttpURLConnection) url.openConnection();
      /* 3. 设置请求参数等 */
      // 请求方式 默认 GET
      httpURLConnection.setRequestMethod("POST");
      // 超时时间
      httpURLConnection.setConnectTimeout(3000);
      // 设置是否输出
      httpURLConnection.setDoOutput(true);
      // 设置是否读入
      httpURLConnection.setDoInput(true);
      // 设置是否使用缓存
      httpURLConnection.setUseCaches(false);
      // 设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向
      httpURLConnection.setInstanceFollowRedirects(true);
      // 设置请求头
      httpURLConnection.addRequestProperty("sysId", "sysId");
      // 设置使用标准编码格式编码参数的名-值对
      httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      // 连接
      httpURLConnection.connect();
      /* 4. 处理输入输出 */
      // 写入参数到请求中
      String params = "username=test&password=123456";
      OutputStream out = httpURLConnection.getOutputStream();
      out.write(params.getBytes());
      // 简化
      // httpURLConnection.getOutputStream().write(params.getBytes());
      out.flush();
      out.close();
      // 从连接中读取响应信息
      String msg = "";
      int code = httpURLConnection.getResponseCode();
      if (code == 200) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
          msg += line + "\n";
        }
        reader.close();
      }
      // 处理结果
      log.info(msg);
    } catch (IOException e) {
      log.info("转发出错，错误信息：" + e.getLocalizedMessage() + ";" + e.getClass());
    } finally {
      // 5. 断开连接
      if (null != httpURLConnection) {
        try {
          httpURLConnection.disconnect();
        } catch (Exception e) {
          log.info("httpURLConnection 流关闭异常：" + e.getLocalizedMessage());
        }
      }
    }
  }
  // #endregion
}
