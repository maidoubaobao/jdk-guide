package io.github.maidoubaobao.jdk11;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * jdk11中 HttpClient 正式可用
 *
 * @author ming
 * @since 2024-02-16
 */
@SuppressWarnings("ALL")
public class $23HttpClient {

    public void jdk11() throws IOException, InterruptedException {
        // 构建 HttpClient
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://127.0.0.1:8080/demo")).build();
        HttpResponse.BodyHandler<String> respnoseBodyHandler= HttpResponse.BodyHandlers.ofString();

        // HttpClient 同步调用
        HttpResponse<String> response =client.send(request,respnoseBodyHandler);
        System.out.println(response.body());

        // HttpClient 异步调用
        CompletableFuture<HttpResponse<String>> sendAsync = client.sendAsync(request, respnoseBodyHandler);
        sendAsync.thenApply(HttpResponse::body).thenAccept(System.out::println);
    }
}
