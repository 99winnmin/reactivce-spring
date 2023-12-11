package com.example.nio.outputstream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SocketOutputStreamExample {
    @SneakyThrows
    public static void main(String[] args) {

        // SocketOutputStream
        // public 이 아니기 때문에 직접 접근이 불가능
        // Socket 의 getOutputStream() 을 통해 접근 가능
        // Socket 의 OutputStream 은 blocking 이기 때문에 별도의 thread 로 처리해야 함
        // 서버 소켓 생성
        ServerSocket serverSocket = new ServerSocket(9999);

        // 클라이언트 접속 대기
        Socket clientSocket = serverSocket.accept();

        byte[] buffer = new byte[1024];
        clientSocket.getInputStream().read(buffer);

        var outputStream = clientSocket.getOutputStream();
        var bos = new BufferedOutputStream(outputStream);
        var bytes = "Hello World".getBytes();
        bos.write(bytes);
        bos.flush();

        clientSocket.close();
        serverSocket.close();

    }

}
