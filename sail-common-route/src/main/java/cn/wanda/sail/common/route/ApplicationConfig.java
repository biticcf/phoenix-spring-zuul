package cn.wanda.sail.common.route;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: DanielCao
 * @Date:   2017年10月29日
 * @Time:   下午8:07:21
 *
 */
@Component
@ConfigurationProperties(prefix="route")
@Setter
@Getter
public class ApplicationConfig {
    private String defaultGroup;
    private String deployMode;
}