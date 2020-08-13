package com.fans.wechat.robot.param;

import com.alibaba.fastjson.JSONObject;
import com.fans.wechat.robot.RobotEnum;
import lombok.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;

/**
 * className: FileBody
 *
 * @author k
 * @version 1.0
 * @description 文件类型消息入参
 * @date 2019-04-13 08:43
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class FileBody extends BodyBase implements Serializable {

    private static final long serialVersionUID = -5624114788495952798L;

    private static final String URL = "https://qyapi.weixin.qq.com/cgi-bin/webhook/upload_media?key={0}&type=file";

    /**
     * 文件id，通过下文的文件上传接口获取
     */
    private String media_id;

    private RobotEnum robotEnum;

    public static String getMediaId(RobotEnum robotEnum, String path) {
        RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());
        File file = new File(path);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);
        headers.setContentLength(file.length());
        headers.setContentDispositionFormData("media", file.getName());
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        FileSystemResource resource = new FileSystemResource(file.getPath());
        param.add("file", resource);
        HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(param, headers);
        ResponseEntity<String> data = restTemplate.postForEntity(MessageFormat.format(URL, robotEnum.getKey()), formEntity, String.class);
        JSONObject jsonObject = JSONObject.parseObject(data.getBody());
        return jsonObject.getString("media_id");
    }

    @SuppressWarnings("NullableProblems")
    static class HttpsClientRequestFactory extends SimpleClientHttpRequestFactory {
        @Override
        protected void prepareConnection(HttpURLConnection connection, String httpMethod) {
            try {
                if (!(connection instanceof HttpsURLConnection)) {
                    throw new RuntimeException("An instance of HttpsURLConnection is expected");
                }

                HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;

                TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
                            }

                            @Override
                            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
                            }

                            @Override
                            public X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }
                        }
                };
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                httpsConnection.setSSLSocketFactory(new MyCustomSSLSocketFactory(sslContext.getSocketFactory()));

                httpsConnection.setHostnameVerifier((s, sslSession) -> true);
                super.prepareConnection(httpsConnection, httpMethod);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * We need to invoke sslSocket.setEnabledProtocols(new String[] {"SSLv3"});
         * see http://www.oracle.com/technetwork/java/javase/documentation/cve-2014-3566-2342133.html (Java 8 section)
         */
        // SSLSocketFactory用于创建 SSLSockets
        private static class MyCustomSSLSocketFactory extends SSLSocketFactory {
            private final SSLSocketFactory delegate;

            public MyCustomSSLSocketFactory(SSLSocketFactory delegate) {
                this.delegate = delegate;
            }

            // 返回默认启用的密码套件。除非一个列表启用，对SSL连接的握手会使用这些密码套件。
            // 这些默认的服务的最低质量要求保密保护和服务器身份验证
            @Override
            public String[] getDefaultCipherSuites() {
                return delegate.getDefaultCipherSuites();
            }

            // 返回的密码套件可用于SSL连接启用的名字
            @Override
            public String[] getSupportedCipherSuites() {
                return delegate.getSupportedCipherSuites();
            }

            @Override
            public Socket createSocket(final Socket socket, final String host, final int port,
                                       final boolean autoClose) throws IOException {
                final Socket underlyingSocket = delegate.createSocket(socket, host, port, autoClose);
                return overrideProtocol(underlyingSocket);
            }

            @Override
            public Socket createSocket(final String host, final int port) throws IOException {
                final Socket underlyingSocket = delegate.createSocket(host, port);
                return overrideProtocol(underlyingSocket);
            }

            @Override
            public Socket createSocket(final String host, final int port, final InetAddress localAddress,
                                       final int localPort) throws
                    IOException {
                final Socket underlyingSocket = delegate.createSocket(host, port, localAddress, localPort);
                return overrideProtocol(underlyingSocket);
            }

            @Override
            public Socket createSocket(final InetAddress host, final int port) throws IOException {
                final Socket underlyingSocket = delegate.createSocket(host, port);
                return overrideProtocol(underlyingSocket);
            }

            @Override
            public Socket createSocket(final InetAddress host, final int port, final InetAddress localAddress,
                                       final int localPort) throws
                    IOException {
                final Socket underlyingSocket = delegate.createSocket(host, port, localAddress, localPort);
                return overrideProtocol(underlyingSocket);
            }

            private Socket overrideProtocol(final Socket socket) {
                if (!(socket instanceof SSLSocket)) {
                    throw new RuntimeException("An instance of SSLSocket is expected");
                }
                ((SSLSocket) socket).setEnabledProtocols(new String[]{"TLSv1"});
                return socket;
            }
        }
    }
}
