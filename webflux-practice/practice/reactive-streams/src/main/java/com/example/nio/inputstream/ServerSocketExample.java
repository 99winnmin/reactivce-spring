package com.example.nio.inputstream;

import java.io.BufferedInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerSocketExample {
    @SneakyThrows
    public static void main(String[] args) {
        // ServerSocket
        // serverSocket 을 open 하여 외부의 요청을 수신
        // bind, accept 를 통해서 serverSocket open 을 준비
        // accept 가 끝나면 반환값으로 client 의 socket 을 전달
        // client socket 의 getInputStream 으로 socket 의 inputStream 에 접근
        // accept 가 끝나면 반환값으로 client 의 socket 을 전달
        // client socket 의 getOutputStream 으로 socket 의 outputStream 에 접근
        // SocketInputStream 은 public 이 아니기 때문에 직접 접근이 불가능
        // socket.getInputStream() 으로만 접근 가능
        // blocking 이 발생함

        // 서버 소켓 생성
        ServerSocket serverSocket = new ServerSocket(8080);

        // 클라이언트 접속 대기
        Socket clientSocket = serverSocket.accept();

        var inputStream = clientSocket.getInputStream();
        // 클라이언트로부터 데이터를 읽어들임
        try (BufferedInputStream bis =
                new BufferedInputStream(inputStream)) {
            // 1KB 크기의 버퍼 생성
            byte[] buffer = new byte[1024];
            int bytesRead = bis.read(buffer);
            String inputLine = new String(buffer, 0, bytesRead);
            log.info("inputLine: {}", inputLine);
        }

        clientSocket.close();
        serverSocket.close();
    }
}
