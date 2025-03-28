package com.donnan.git.guru.business.client;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;

/**
 * @author Donnan
 */
@Component
@Slf4j
@Data
public class GitHubUserClient {

    // 最大的用户数
    @Value("${github.client.user.max.num}")
    private int userMaxNum = 9930000;

    // 访问github，加速token
    @Value("${github.client.token}")
    private List<String> authGithub;

    // 最大线程数
    @Value("${github.client.thread.num}")
    private int threadNum;

    private final CloseableHttpClient httpClient;

    public GitHubUserClient() {
        // 创建连接池管理器
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        // 设置最大连接数
        connectionManager.setMaxTotal(100);
        // 设置每个路由的最大连接数
        connectionManager.setDefaultMaxPerRoute(20);

        // 请求配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(15000)
                .setConnectionRequestTimeout(5000)
                .build();

        // 创建HttpClient
        this.httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    public List<GitHubUser> getPageUser(int nums) throws IOException {
        return getGitHubResource("/user/" + userId, userId);
    }

    /**
     * 根据用户名获取GitHub用户信息
     * @param userName 用户名
     * @return 用户信息JSON字符串
     */
    public String getUserInfo(String userName) {
        try {
            // 使用数字作为token选择器索引，确保在列表范围内
            int tokenIndex = Math.abs(userName.hashCode() % authGithub.size());
            return getGitHubResource("/users/" + userName, tokenIndex);
        } catch (IOException e) {
            log.error("请求GitHub API异常: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 通用的GitHub API资源获取方法
     * @param resourcePath API资源路径
     * @param tokenSelector 用于选择token的索引或计算依据
     * @return API响应内容
     */
    private String getGitHubResource(String resourcePath, Object tokenSelector) throws IOException {
        if (authGithub == null || authGithub.isEmpty()) {
            throw new IllegalStateException("GitHub认证token未配置");
        }

        // 根据传入对象选择合适的token
        int tokenIndex;
        if (tokenSelector instanceof Number) {
            tokenIndex = Math.abs(((Number) tokenSelector).intValue() % authGithub.size());
        } else {
            tokenIndex = Math.abs(tokenSelector.hashCode() % authGithub.size());
        }
        String token = authGithub.get(tokenIndex);

        // 确保资源路径格式正确
        if (!resourcePath.startsWith("/")) {
            resourcePath = "/" + resourcePath;
        }

        HttpGet request = new HttpGet("https://api.github.com" + resourcePath);
        request.setHeader("User-Agent", "Mozilla/5.0");
        request.setHeader("Authorization", token);
        request.setHeader("Accept", "application/vnd.github.v3+json");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity);
            } else {
                log.warn("获取GitHub资源失败, 状态码: {}, 资源路径: {}", statusCode, resourcePath);
                return null;
            }
        } catch (Exception e) {
            log.error("请求GitHub API异常: {}", e.getMessage());
            throw e;
        }
    }

    @PreDestroy
    public void close() {
        if (httpClient != null) {
            try {
                httpClient.close();
                log.info("HttpClient资源已释放");
            } catch (IOException e) {
                log.error("关闭HttpClient时出错", e);
            }
        }
    }
}