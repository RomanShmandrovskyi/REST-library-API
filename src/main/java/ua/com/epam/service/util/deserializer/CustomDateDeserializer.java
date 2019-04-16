package ua.com.epam.service.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ua.com.epam.entity.exception.type.InvalidDateTypeException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

public class CustomDateDeserializer extends StdDeserializer<Date> {
    public CustomDateDeserializer() {
        this(null);
    }

    public CustomDateDeserializer(Class t) {
        super(t);
    }

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext dc) throws IOException {
        String date = jp.getValueAsString();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone(ZoneOffset.ofHours(0)));
        sdf.setLenient(false);

        Date parsed;

        try {
            parsed = sdf.parse(date);
        } catch (ParseException e) {
            throw new InvalidDateTypeException(jp.currentName(), jp.getText());
        }

        return parsed;
    }
}
