package com.example.nio.outputstream;

import java.io.File;
import java.io.FileOutputStream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileOutputStreamExample {
    @SneakyThrows
    public static void main(String[] args) {
        // FilterOutputStream
        // file 에 값을 쓸 수 있음, application 에서 blocking 발생
        var file = new File(FileOutputStreamExample.class
            .getClassLoader()
            .getResource("dest.txt")
            .getFile());
        try (var fos = new FileOutputStream(file)) {
            var content = "Hello World";
            fos.write(content.getBytes());
            fos.flush();
        }

    }

}
