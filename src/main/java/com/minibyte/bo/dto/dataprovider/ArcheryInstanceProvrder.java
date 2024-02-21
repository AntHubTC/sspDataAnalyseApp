package com.minibyte.bo.dto.dataprovider;

import com.minibyte.config.UserConfig;

/**
 * Archery常用实例
 *
 * @author: AntHubTC
 * @date: 2023/1/11
 * @description:
 */
public class ArcheryInstanceProvrder {

    /**
     * 获取ssp数据库查询实例
     *
     * @param sql        要执行的查询语句
     * @param userConfig
     * @return
     */
    public static ArcherySQLQryReq genSSP3Instance(String sql, UserConfig userConfig) {
        return genSSP3Instance(sql, userConfig.getSspCookie());
    }

    public static ArcherySQLQryReq genSSP3Instance(String sql, String token) {
        ArcherySQLQryReq req = new ArcherySQLQryReq();
        req.setInstance_name("prod-ssp3");
        req.setDb_name("ssp3");
        req.setSchema_name("");
        req.setLimit_num(0);
        req.setCookie(token);

        sql = sql.replaceAll("\n", " ");
        req.setSql_content(sql);
        return req;
    }


    /**
     * 获取ssp数据库查询实例
     *
     * @param sql        要执行的查询语句
     * @param userConfig
     * @return
     */
    public static ArcherySQLQryReq genRMInstance(String sql, UserConfig userConfig) {
        return genRMInstance(sql, userConfig.getRmCookie());
    }

    public static ArcherySQLQryReq genRMInstance(String sql, String token) {
        ArcherySQLQryReq req = new ArcherySQLQryReq();
        req.setInstance_name("prod-mkrm");
        req.setDb_name("common-crm");
        req.setSchema_name("");
        req.setLimit_num(0);
        req.setCookie(token);

        sql = sql.replaceAll("\n", " ");
        req.setSql_content(sql);
        return req;
    }
}
