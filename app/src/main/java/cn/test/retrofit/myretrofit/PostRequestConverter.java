package cn.test.retrofit.myretrofit;

import android.util.Log;

import com.google.common.base.Joiner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

public class PostRequestConverter<T extends Map<String, Object>> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse(javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED);
    private static final String ENCODING = "UTF-8";
    @SuppressWarnings("unused") private static final Charset UTF_8 = Charset.forName(ENCODING);

    public PostRequestConverter() {
    }

    @Override
    @SuppressWarnings("all")
    public RequestBody convert(T parameters) throws IOException {
        Log.i("post covert", "called");
        return RequestBody.create(
                MEDIA_TYPE,
                queryString(parameters)
        );
    }

    private String queryString(Map<String, Object> parameters) {
        ArrayList<String> sortedKeys = new ArrayList<>(parameters.keySet());
        Collections.sort(sortedKeys, String::compareTo);

        List<QueryStringPair> queryStringPairs = new ArrayList<>();
        queryStringPairs.addAll(queryStringPairs(parameters));
        return Joiner.on("&").join(queryStringPairs);
    }

    private List<QueryStringPair> queryStringPairs(Map<String, Object> parameters) {
        return queryStringPairs(null, parameters);
    }

    private List<QueryStringPair> queryStringPairs(String key, @Nonnull Object value) {
        ArrayList<QueryStringPair> components = new ArrayList<>();

        if (value instanceof Map) {
            @SuppressWarnings("unchecked") Map<String, Object> map = (Map<String, Object>) value;

            ArrayList<String> sortedKeys = new ArrayList<>(map.keySet());
            Collections.sort(sortedKeys, String::compareTo);
            for (String nestedKey : sortedKeys) {
                Object nestedValue = map.get(nestedKey);
                if (nestedValue != null) {
                    components.addAll(
                            queryStringPairs(
                                    key != null
                                            ? (key + "[" + nestedKey + "]")
                                            : nestedKey,
                                    nestedValue
                            )
                    );
                }
            }
        } else if (value instanceof List) {
            for (int i = 0; i < ((List) value).size(); i++) {
                Object nestedValue = ((List) value).get(i);

                if (nestedValue instanceof String) {
                    components.addAll(
                        queryStringPairs(
                                key + "[]",
                                nestedValue
                        )
                    );
                } else {
                    for (Field field : nestedValue.getClass().getDeclaredFields()) {
                        if(("$change".equals(field.getName())) || ("serialVersionUID".equals(field.getName()))) {
                            continue;
                        }

                        boolean accessible = field.isAccessible();
                        field.setAccessible(true);
                        String itemValue;
                        try {
                            itemValue = String.valueOf(field.get(nestedValue));
                            components.addAll(
                                    queryStringPairs(
                                            key + "[" + i + "][" + field.getName() + "]",
                                            itemValue
                                    )
                            );
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        field.setAccessible(accessible);
                    }
                }
            }
        } else if (value instanceof Set) {
            @SuppressWarnings("unchecked") Set<Object> set = (Set<Object>) value;

            ArrayList<Object> sortedValues = new ArrayList<>(set);
            Collections.sort(sortedValues, (Object x, Object y) -> x.toString().compareTo(y.toString()));
            for (Object object : sortedValues) {
                components.addAll(
                        queryStringPairs(key, object)
                );
            }
        } else {
            components.add(new QueryStringPair(key, value));
        }

        return components;
    }

    private class QueryStringPair {
        @SuppressWarnings("NullableProblems") @Nonnull
        private Object mField;
        private Object mValue;

        QueryStringPair(@Nonnull Object field, Object value) {
            mField = field;
            mValue = value;
        }

        private String percentEscapedString(@Nonnull String string) {
            try {
                return URLEncoder.encode(string, ENCODING)
                        .replace("+", "%20")
                        .replace("*", "%2A")
                        .replace("%7E", "~");
            } catch (UnsupportedEncodingException exception) {
                throw new RuntimeException(exception.getMessage(), exception);
            }
        }

        @Override
        public String toString() {
            if (mValue == null) {
                return percentEscapedString(mField.toString());
            } else {
                return percentEscapedString(mField.toString()) + "=" + percentEscapedString(mValue.toString());
            }
        }

    }
}