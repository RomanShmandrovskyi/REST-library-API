package ua.com.epam.service.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ua.com.epam.exception.entity.type.InvalidDateTypeException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomDateDeserializer extends StdDeserializer<LocalDate> {
    public CustomDateDeserializer() {
        this(null);
    }

    public CustomDateDeserializer(Class t) {
        super(t);
    }

    @Override
    public LocalDate deserialize(JsonParser jp, DeserializationContext dc) throws IOException {
        String date = jp.getValueAsString();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
        LocalDate parsed;

        try {
            parsed = LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new InvalidDateTypeException(jp.currentName(), jp.getText());
        }

        return parsed;
    }
}
