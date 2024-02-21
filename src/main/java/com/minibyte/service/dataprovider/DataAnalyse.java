package com.minibyte.service.dataprovider;

import com.minibyte.bo.dto.dataprovider.DataAnalyseContext;

import javax.annotation.Resource;

/**
 * 点位转移数据验证分析
 *
 * @author: AntHubTC
 * @date: 2023/1/11
 * @description:
 */
public abstract class DataAnalyse {

    @Resource
    protected ArcherySQLQueryService archerySQLQueryService;

    public abstract String getAnalyseName();

    public abstract Object analyse(DataAnalyseContext dataAnalyseContext);
}
