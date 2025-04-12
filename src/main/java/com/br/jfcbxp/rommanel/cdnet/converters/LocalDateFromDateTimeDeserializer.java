package com.br.jfcbxp.rommanel.cdnet.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateFromDateTimeDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateTimeStr = p.getText();
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_DATE_TIME);
        return dateTime.toLocalDate();
    }
}
