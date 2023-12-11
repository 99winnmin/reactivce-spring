package com.example.nio.outputstream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OutputStreamExample {
    // write 시 바로 전송하지 않고 버퍼에 저장한 다음 일정량의 데이터가 모이면 한번에 전달
    // Closable 을 구현하고 있어서 try-with-resource 문법을 사용할 수 있음
    // write : stream 으로 데이터를 씀
    // flush : 버퍼에 저장된 데이터를 출력하고 비움
    // close : stream 을 닫고 더 이상 쓰지 않음
    // 어떤 destination 에 데이터를 쓸 지에 따라 다양한 구현체 존재
    // FileOutputStream, ByteArrayOutputStream, FilterOutputStream, ObjectOutputStream, PipedOutputStream, PrintStream, BufferedOutputStream, DataOutputStream, FileOutputStream, ObjectOutputStream, PipedOutputStream, ZipOutputStream
    @SneakyThrows
    public static void main(String[] args) {
        // ByteArrayOutputStream
        // byte 단위로 데이터를 쓸 수 있음
        try (var baos = new ByteArrayOutputStream()) {
            baos.write(65);
            baos.write(66);
            baos.write(67);

            var bytes = baos.toByteArray();
            log.info("bytes: {}", bytes);
            //22:22 [main] - bytes: [65, 66, 67]
        }

    }

}
