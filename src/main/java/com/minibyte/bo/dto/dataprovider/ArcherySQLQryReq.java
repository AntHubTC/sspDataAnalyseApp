package com.minibyte.bo.dto.dataprovider;

import com.minibyte.common.exception.MBBizException;
import lombok.Data;

/**
 * 点位转移数据验证
 *
 * @author: AntHubTC
 * @date: 2023/1/11
 * @description:
 */
@Data
public class ArcherySQLQryReq {
    /**
     * 实例名称
     */
    private String instance_name;
    /**
     * 数据库名称
     */
    private String db_name;
    /**
     */
    private String schema_name;
    /**
     * 表名称
     */
    private String tb_name;
    /**
     * sql内容
     */
    private String sql_content;
    /**
     * 返回行数
     */
    private Integer limit_num;
    /**
     * cookie
     */
    private String cookie;
}
