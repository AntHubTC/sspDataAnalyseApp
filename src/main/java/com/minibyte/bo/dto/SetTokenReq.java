package com.minibyte.bo.dto;

import lombok.Data;

/**
 * @author AntHubTC
 * @version 1.0
 * @className SetTokenReq
 * @description
 * @date 2023/7/19 11:15
 **/
@Data
public class SetTokenReq {
    /**
     * ssp Cookie
     */
    private String sspCookie;
    /**
     * rongmei Cookie
     */
    private String rmCookie;
}
