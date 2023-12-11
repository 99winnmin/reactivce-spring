package com.example.nio.inputstream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BufferedInputStreamExample {
    @SneakyThrows
    public static void main(String[] args) {
        // BufferedInputStream
        // 다른 InputStream과 조합해서 사용
        // 임시 저장 공간인 buffer 를 사용
        // 한 번 read 를 호출할 때 buffer 사이즈만큼 미리 조회
        // 그 이후 read 를 호출할 때 미리 저장한 buffer 데이터 반환
        var file = new File(BufferedInputStream.class
            .getClassLoader()
            .getResource("data.txt").getFile());

        try(var fis = new FileInputStream(file)) {
            // 여기서부터는 파일에 계속 접근하지 않음
            // 버퍼에 저장해놓고 진행
            try(var bis = new BufferedInputStream(fis)) {
                var value = 0;
                while((value = bis.read()) != -1) {
                    log.info("value: {}", (char)value);
                }
            }
        }
    }
}
