package cn.wanda.sail.common.route;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: chengyang.
 * @email: chengyang14@wanda.cn
 * @time: 17/5/5
 * @description: 描述
 */
@Component
@ConfigurationProperties(prefix="route")
@Setter
@Getter
public class ApplicationConfig {
    private String defaultGroup;
    private String deployMode;
}