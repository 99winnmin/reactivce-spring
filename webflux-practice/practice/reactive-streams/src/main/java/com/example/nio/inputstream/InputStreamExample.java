package com.example.nio.inputstream;

import java.io.ByteArrayInputStream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InputStreamExample {

    @SneakyThrows
    public static void main(String[] args) {
        // ByteArrayInputStream
        var bytes = new byte[]{101, 102, 103, 104, 105};
        try(var bais = new ByteArrayInputStream(bytes)) {
            var value = 0;
            while((value = bais.read()) != -1) {
                log.info("value: {}", value);
            }
            //13:31 [main] - value: 101
            //13:31 [main] - value: 102
            //13:31 [main] - value: 103
            //13:31 [main] - value: 104
            //13:31 [main] - value: 105
        }

        try(var bais = new ByteArrayInputStream(bytes)) {
            var value = bais.readAllBytes();
            log.info("value: {}", value);
            //14:37 [main] - value: [101, 102, 103, 104, 105]
        }
    }
}
