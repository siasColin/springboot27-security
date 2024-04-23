package cn.net.ssd.common.helper;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Date;

/**
 * @author SXF
 * @version 1.0
 * @description: TODO
 * @date 2023/3/22 15:23
 */
public class CustomDateDeSerializer extends JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String value = jsonParser.getText();
        try {
            return value == null ? null : DateUtil.parseDateTime(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}
