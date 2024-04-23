package cn.net.ssd.common.helper;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

/**
 * @author SXF
 * @version 1.0
 * @description: TODO
 * @date 2023/3/22 15:23
 */
public class CustomDateSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date value,
                          JsonGenerator jsonGenerator,
                          SerializerProvider provider)
            throws IOException {
        jsonGenerator.writeString(DateUtil.formatDateTime(value));
    }
}
