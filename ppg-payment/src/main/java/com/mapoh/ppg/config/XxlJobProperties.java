package com.mapoh.ppg.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author mabohv
 * @date 2025/2/13 21:40
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {

    private Admin admin;
    private Executor executor;
    private String accessToken;

    // Getters and Setters

    public static class Admin {
        private String addresses;

        public String getAddresses() {
            return addresses;
        }

        public void setAddresses(String addresses) {
            this.addresses = addresses;
        }
    }

    public static class Executor {
        private String appname;
        private String ip;
        private int port;
        private String logpath;
        private int logRetentionDays;

        // Getters and Setters

        public String getAppname() {
            return appname;
        }

        public void setAppname(String appname) {
            this.appname = appname;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getLogpath() {
            return logpath;
        }

        public void setLogpath(String logpath) {
            this.logpath = logpath;
        }

        public int getLogRetentionDays() {
            return logRetentionDays;
        }

        public void setLogRetentionDays(int logRetentionDays) {
            this.logRetentionDays = logRetentionDays;
        }
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
