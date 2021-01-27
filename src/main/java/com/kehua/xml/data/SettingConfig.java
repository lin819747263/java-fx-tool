package com.kehua.xml.data;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

/**
 * HuangTengfei
 * 2020/12/23
 * description：
 */
@Getter
public class SettingConfig {

    private final static SettingConfig config = new SettingConfig();

    private SettingConfig() {
    }

    public static SettingConfig getDefault() {
        return config;
    }

    public static SettingConfig getDebug() {
        return new SettingConfig();
    }

    @Getter
    public abstract static class ConnectWay {

        public ConnectWay setHost(String host) {
            return this;
        }

        public ConnectWay setPort(Integer port) {
            return this;
        }

        public ConnectWay setCommPort(String commPort) {
            return this;
        }

        public ConnectWay setBaudRate(Integer baudRate) {
            return this;
        }

        public abstract boolean check();
    }

    @Getter
    public static class TCPConnect extends ConnectWay {
        private String host = "127.0.0.1";
        private Integer port;

        @Override
        public TCPConnect setHost(String host) {
            this.host = host;
            return this;
        }

        @Override
        public TCPConnect setPort(Integer port) {
            this.port = port;
            return this;
        }

        @Override
        public boolean check() {
            return StringUtils.isNotEmpty(host) && port != null;
        }
    }

    @Getter
    public static class SERIALConnect extends ConnectWay {
        private String commPort;
        private Integer baudRate;

        @Override
        public SERIALConnect setCommPort(String commPort) {
            this.commPort = commPort;
            return this;
        }

        @Override
        public SERIALConnect setBaudRate(Integer baudRate) {
            this.baudRate = baudRate;
            return this;
        }

        @Override
        public boolean check() {
            return StringUtils.isNotEmpty(commPort) && baudRate != null;
        }
    }


    private Integer addr = 1;
    private ConnectWay connectWay = new TCPConnect();

    public void setAddr(Integer addr) {
        this.addr = addr;
    }

    public ConnectWay setTcp(String host, Integer port) {
        return connectWay = new SettingConfig.TCPConnect().setHost(host).setPort(port);
    }

    public ConnectWay setSerial(String commPort, Integer baudRate) {
        return connectWay = new SettingConfig.SERIALConnect().setCommPort(commPort).setBaudRate(baudRate);
    }
}
