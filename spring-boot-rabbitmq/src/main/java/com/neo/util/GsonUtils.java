package com.neo.util;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class GsonUtils {
    public static Gson getGson() {
        return getGson(false);
    }

    public static Gson getGson(boolean serializeNulls) {
        return getGson(serializeNulls, null);
    }

    public static Gson getGson(boolean serializeNulls, boolean trimString) {
        Map<Class, TypeAdapter> customTypeAdapterMap = null;
        if (trimString) {
            customTypeAdapterMap = new HashMap<>(1);
            customTypeAdapterMap.put(String.class, STRING_TYPE_ADAPTER);
        }
        return getGson(serializeNulls, customTypeAdapterMap);
    }

    public static Gson getGson(boolean serializeNulls, Map<Class, TypeAdapter> customTypeAdapterMap) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLongSerializationPolicy(LongSerializationPolicy.STRING);
        gsonBuilder.registerTypeAdapter(BigDecimal.class, BIG_DECIMAL_TYPE_ADAPTER);
        if (customTypeAdapterMap != null && customTypeAdapterMap.size() > 0) {
            for (Map.Entry<Class, TypeAdapter> item : customTypeAdapterMap.entrySet()) {
                gsonBuilder.registerTypeAdapter(item.getKey(), item.getValue());
            }
        }
        if (serializeNulls) {
            gsonBuilder.serializeNulls();
        }
        return gsonBuilder.create();
    }

    public static final TypeAdapter<BigDecimal> BIG_DECIMAL_TYPE_ADAPTER = new TypeAdapter<BigDecimal>() {
        @Override
        public BigDecimal read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            try {
                return new BigDecimal(in.nextString());
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public void write(JsonWriter out, BigDecimal value) throws IOException {
            // 优化序列化
            if (value == null) {
                String nullString = null;
                out.value(nullString);
            } else {
                if (BigDecimal.ZERO.compareTo(value) == 0) {
                    out.value("0");
                } else {
                    // 不使用指数
                    String valueString = value.toPlainString();
                    // 仅当有内容，且带有小数点，且结尾是0
                    // 去掉尾部的0
                    while (StringUtils.hasText(valueString) && valueString.contains(".") && valueString.endsWith("0")) {
                        valueString = valueString.substring(0, valueString.length() - 1);
                    }
                    // 如果是以小数点结尾，则去除小数点
                    if (StringUtils.hasText(valueString) && valueString.endsWith(".")) {
                        valueString = valueString.substring(0, valueString.length() - 1);
                    }
//                    out.value(Double.valueOf(valueString));
                    out.value(valueString);
                }
            }
        }
    };

    public static final TypeAdapter<String> STRING_TYPE_ADAPTER = new TypeAdapter<String>() {
        @Override
        public String read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            try {
                String value = in.nextString();
                if (value != null) {
                    value = value.trim();
                }
                return value;
            } catch (Throwable e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public void write(JsonWriter out, String value) throws IOException {
            if (StringUtils.hasText(value)) {
                value = value.trim();
            }
            out.value(value);
        }
    };
    //
    //public static void main(String[] args) {
    //    String valueString = "50.0000";
    //    while (StringUtils.hasText(valueString) && valueString.contains(".") && valueString.endsWith("0")) {
    //        valueString = valueString.substring(0, valueString.length() - 1);
    //    }
    //    // 如果是以小数点结尾，则去除小数点
    //    if (StringUtils.hasText(valueString) && valueString.endsWith(".")) {
    //        valueString = valueString.substring(0, valueString.length() - 1);
    //    }
    //    System.out.println(valueString);
    //}
}
