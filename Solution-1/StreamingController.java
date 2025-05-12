package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.OutputStreamWriter;

@RestController
public class StreamingController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/records")
    public void streamAll(HttpServletResponse response) throws Exception {
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setHeader(HttpHeaders.TRANSFER_ENCODING, "chunked");
        OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
        JsonGenerator gen = new JsonFactory().createGenerator(writer);
        gen.writeStartArray();
        jdbcTemplate.query("SELECT id, col1, col2, col3 FROM big_table",
            (ResultSetExtractor<Void>) rs -> {
                while (rs.next()) {
                    gen.writeStartObject();
                    gen.writeNumberField("id", rs.getLong("id"));
                    gen.writeStringField("col1", rs.getString("col1"));
                    gen.writeStringField("col2", rs.getString("col2"));
                    gen.writeStringField("col3", rs.getString("col3"));
                    gen.writeEndObject();
                }
                return null;
            });
        gen.writeEndArray();
        gen.flush();
        gen.close();
    }
}
