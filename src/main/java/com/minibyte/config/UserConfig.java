package com.minibyte.config;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author AntHubTC
 * @version 1.0
 * @className UserConfig
 * @description
 * @date 2023/7/19 11:16
 **/
@Component
@Data
public class UserConfig {
    /**
     * ssp Cookie
     */
    private String sspCookie;
    /**
     * rongmei Cookie
     */
    private String rmCookie;
}
