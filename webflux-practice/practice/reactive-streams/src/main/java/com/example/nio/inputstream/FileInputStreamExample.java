package com.example.nio.inputstream;

import java.io.File;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileInputStreamExample {

    @SneakyThrows
    public static void main(String[] args) {
        // FileInputStream
        // file 로부터 byte 단위로 값을 읽을 수 있음
        // application 에서 blocking 이 일어남
        var file = new File(FileInputStreamExample.class
            .getClassLoader()
            .getResource("data.txt").getFile());
        try(var fis = new java.io.FileInputStream(file)) {
            var value = 0;
            while((value = fis.read()) != -1) {
                log.info("value: {}", value);
                log.info("value: {}", (char)value);
            }
//            22:22 [main] - value: 97
//            22:22 [main] - value: a
//            22:22 [main] - value: 98
//            22:22 [main] - value: b
//            22:22 [main] - value: 99
//            22:22 [main] - value: c
//            22:22 [main] - value: 100
//            22:22 [main] - value: d
//            22:22 [main] - value: 101
//            22:22 [main] - value: e
        }
    }
}
