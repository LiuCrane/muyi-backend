package com.mysl.api.config;

import java.io.File;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mysl.api.interceptor.MyInterceptor;
import com.mysl.api.lib.AesFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    @Autowired
    private MyInterceptor myInterceptor;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8)); // @ResponseBody 解决乱码
        converters.add(new ResourceHttpMessageConverter());
        converters.add(new AllEncompassingFormHttpMessageConverter());
        converters.add(jackson2HttpMessageConverter());
        AesFile.test();
        MyConfig m = config;
        System.out.println(m.port);

        String path = AesFile.formatFilename("public", true);
        if (path == null) {
            return;
        }
        File fi = new File(path);// 参数为空
        if (!fi.exists() && !fi.mkdirs()) {
            return;
        }
        log.info("http://localhost:" + config.port + "\n   资源目录:" + path + "\n");
        // codeGenerator.generator();
    }

    /**
     * 返回json时候将long类型转换为String类型的转换器
     */
    @Bean
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();

        // 日期格式转换
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        // Long类型转String类型
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        mapper.registerModule(simpleModule);

        converter.setObjectMapper(mapper);
        return converter;
    }

    @Autowired
    MyConfig config;

    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        // Long类型转String类型
        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        // ToStringSerializer 是这个包 com.alibaba.fastjson.serializer.ToStringSerializer
        serializeConfig.put(BigInteger.class, com.alibaba.fastjson.serializer.ToStringSerializer.instance);
        serializeConfig.put(Long.class, com.alibaba.fastjson.serializer.ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, com.alibaba.fastjson.serializer.ToStringSerializer.instance);
        config.setSerializeConfig(serializeConfig);
        config.setSerializerFeatures(
                // SerializerFeature.WriteMapNullValue, // 保留map空的字段
                // SerializerFeature.WriteNullStringAsEmpty, // 将String类型的null转成""
                // SerializerFeature.WriteNullNumberAsZero, // 将Number类型的null转成0
                // SerializerFeature.WriteNullListAsEmpty, // 将List类型的null转成[]
                // SerializerFeature.WriteNullBooleanAsFalse, // 将Boolean类型的null转成false
                // SerializerFeature.WriteDateUseDateFormat, // 日期格式转换
                SerializerFeature.DisableCircularReferenceDetect // 避免循环引用
        );
        config.setSerializeFilters(valueFilter);
        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(StandardCharsets.UTF_8); // 解决中文乱码问题，相当于在Controller上的@RequestMapping中加了个属性produces =
        // "application/json"
        List<MediaType> mediaTypeList = new ArrayList<>();
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(mediaTypeList);
        return converter;
    }

    /**
     * FastJson过滤器将null值转换为字符串 obj 是class s 是key值 o1 是value值
     */
    public static final ValueFilter valueFilter = (obj, s, v) -> {
        if (v == null) {
            return "";
        }
        return v;
    };

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor).addPathPatterns("/app/**").excludePathPatterns("/app/auth/login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("addResourceHandlers for swagger-ui");
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
        super.addResourceHandlers(registry);
    }
//
//    @Override
//    protected void addViewControllers(ViewControllerRegistry registry) {
//        log.info("addViewControllers for swagger-ui");
//        registry.addViewController("/swagger-ui").setViewName("forward:/swagger-ui/index.html");
//    }
}
