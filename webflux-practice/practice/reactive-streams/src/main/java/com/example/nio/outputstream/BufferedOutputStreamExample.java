package com.example.nio.outputstream;

import java.io.File;
import java.io.FileOutputStream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BufferedOutputStreamExample {
    @SneakyThrows
    public static void main(String[] args) {

        // BufferedOutputStream
        // 다른 OutputStream 과 조합해서 사용
        // 한번 write 를 호출하면 buffer 에만 write
        // 추후 flush 하여 한번에 outputStream 에 write
        var file = new File(BufferedOutputStreamExample.class
            .getClassLoader()
            .getResource("dest2.txt")
            .getFile());
        var fos = new FileOutputStream(file);
        try (var bos = new java.io.BufferedOutputStream(fos)) {
            var content = "Hello World in bufferd";
            bos.write(content.getBytes());
            bos.flush(); // 자동으로 flush 됌
        }

    }

}
