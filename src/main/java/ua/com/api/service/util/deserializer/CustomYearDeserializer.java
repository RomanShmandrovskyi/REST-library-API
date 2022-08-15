package ua.com.api.service.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ua.com.api.exception.entity.type.InvalidTypeException;
import ua.com.api.exception.entity.type.InvalidYearValueException;

import java.io.IOException;
import java.time.Year;

public class CustomYearDeserializer extends StdDeserializer<Integer> {

    public CustomYearDeserializer() {
        this(null);
    }

    public CustomYearDeserializer(Class t) {
        super(t);
    }

    @Override
    public Integer deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        Integer year;

        try {
            year = jsonParser.getIntValue();
            if (year > Year.now().getValue()) {
                throw new InvalidYearValueException();
            }
        } catch (NumberFormatException | IOException e) {
            throw new InvalidTypeException(jsonParser.getCurrentName(), jsonParser.getText(), Integer.class);
        }

        return year;
    }
}
