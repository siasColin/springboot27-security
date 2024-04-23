package cn.net.ssd.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author SXF
 * @version 1.0
 * @description: TODO
 * @date 2023/5/12 13:36
 */
@Component
@ConfigurationProperties(prefix = "ssd")
public class AntMatchersProperties {
    /**
     * 支持 Apache Ant的样式路径，有三种通配符的匹配方式
     * ?（匹配任何单字符）
     * *（匹配0或者任意数量的字符）
     * **（匹配0或者更多的目录）
     */
    private String[] permitUrls;
    private String[] ignoreUrls;
    private String[] checkTokenUrls;

    public String[] getPermitUrls() {
        return permitUrls;
    }

    public void setPermitUrls(String[] permitUrls) {
        this.permitUrls = permitUrls;
    }

    public String[] getIgnoreUrls() {
        return ignoreUrls;
    }

    public void setIgnoreUrls(String[] ignoreUrls) {
        this.ignoreUrls = ignoreUrls;
    }

    public String[] getCheckTokenUrls() {
        return checkTokenUrls;
    }

    public void setCheckTokenUrls(String[] checkTokenUrls) {
        this.checkTokenUrls = checkTokenUrls;
    }
}
