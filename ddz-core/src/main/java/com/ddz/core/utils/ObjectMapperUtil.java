package com.ddz.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;

public class ObjectMapperUtil {

    // 公共静态常量，提供全局访问的 ObjectMapper 实例
    // INSTANCE: 默认的 ObjectMapper 实例（JSON格式）
    public static final ObjectMapper INSTANCE = new ObjectMapper();

    // JSON: 专门用于 JSON 处理的 ObjectMapper 实例
    // 初始时与 INSTANCE 引用同一个对象，后续会单独配置
    public static final ObjectMapper JSON = INSTANCE;

    // XML: 用于 XML 处理的 XmlMapper 实例（XmlMapper 继承自 ObjectMapper）
    // 不排除 null 值
    public static final ObjectMapper XML = new XmlMapper();

    // XML_WITHOUT_NULL: 用于 XML 处理的 XmlMapper 实例
    // 会排除 null 值，生成更简洁的 XML
    public static final ObjectMapper XML_WITHOUT_NULL = new XmlMapper();

    // 默认日期格式
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";


    // 静态初始化块：在类加载时执行，用于配置所有的 ObjectMapper 实例
    static {
        // 创建 Spring 提供的 Jackson 配置构建器，简化配置过程
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

        // 创建 JSR310 模块，用于支持 Java 8 的日期时间 API（如 LocalDateTime, ZonedDateTime 等）
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // 配置构建器，注册自定义模块
        // 注意：原 Groovy 代码中的 ToStringModule 在这里被省略，因为 Java 中没有对应的实现
        // 如果需要，可以替换为实际的模块类
        builder.modules(
                javaTimeModule
                // 如果需要 ToStringModule，可以添加：
                // , new ToStringModule()
        );

        // ==================== 配置 JSON 专用的 ObjectMapper ====================

        // 为 JSON 实例注册 JSR310 模块，使其支持 Java 8 日期时间类型
        JSON.registerModule(javaTimeModule);

        // 设置日期格式：年-月-日 时:分:秒
        // 注意：从 Jackson 2.11+ 开始，推荐使用 builder.dateFormat() 方式
        JSON.setDateFormat(new SimpleDateFormat(DATE_FORMAT));

        // 配置反序列化特性：遇到未知属性时不抛出异常
        // 这在处理可能增加新字段的 API 时很有用，保持向前兼容
        JSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 配置 JsonParser 特性：允许非引号的控制字符
        // 这在处理一些非标准 JSON 时可能有用（如包含换行符、制表符等）
        JSON.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

        // 配置 JsonParser 特性：允许反斜杠转义任何字符
        // 这放宽了对转义字符的限制
        JSON.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);

        // 设置序列化包含规则：不包含 null 值
        // 这会使生成的 JSON 更简洁，不包含值为 null 的字段
        JSON.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 使用构建器应用配置到 JSON 实例
        // 这会应用构建器上设置的所有默认配置
        builder.configure(JSON);

        // ==================== 配置 XML 专用的 ObjectMapper ====================

        // 为 XML 实例注册 JSR310 模块
        XML.registerModule(javaTimeModule);

        // 设置日期格式
        XML.setDateFormat(new SimpleDateFormat(DATE_FORMAT));

        // 配置反序列化特性：遇到未知属性时不抛出异常
        XML.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 使用构建器应用配置到 XML 实例
        builder.configure(XML);

        // ==================== 配置不包含 null 值的 XML ObjectMapper ====================

        // 为 XML_WITHOUT_NULL 实例注册 JSR310 模块
        XML_WITHOUT_NULL.registerModule(javaTimeModule);

        // 设置日期格式
        XML_WITHOUT_NULL.setDateFormat(new SimpleDateFormat(DATE_FORMAT));

        // 配置反序列化特性：遇到未知属性时不抛出异常
        XML_WITHOUT_NULL.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 配置序列化特性：不写入值为 null 的 Map 条目
        // 注意：这个特性只影响 Map 类型，对于普通对象属性需要使用 @JsonInclude 注解或全局设置
        XML_WITHOUT_NULL.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);

        // 使用构建器应用配置到 XML_WITHOUT_NULL 实例
        builder.configure(XML_WITHOUT_NULL);
    }

    /**
     * 根据类型获取对应的 ObjectMapper 实例
     *
     * @param type 字符串类型，支持 "xml"（不区分大小写）和其他任意字符串
     * @return 对应的 ObjectMapper 实例：
     *         - 如果 type 为 "xml"（不区分大小写），返回 XML 实例（包含 null 值）
     *         - 其他情况返回 JSON 实例
     *
     * 注意：如果需要不包含 null 值的 XML 实例，请直接使用 XML_WITHOUT_NULL 常量
     */
    public static ObjectMapper getObjectMapper(String type) {
        // 检查传入的类型字符串是否为 "xml"（不区分大小写）
        if ("xml".equalsIgnoreCase(type.trim())) {
            // 返回 XML 实例（包含 null 值）
            return XML;
        } else {
            // 返回 JSON 实例
            return JSON;
        }
    }

    // 私有构造方法，防止实例化
    private ObjectMapperUtil() {
        throw new UnsupportedOperationException("这是一个工具类，不能实例化");
    }
}
