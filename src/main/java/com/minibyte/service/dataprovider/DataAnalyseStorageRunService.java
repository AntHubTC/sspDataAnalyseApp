package com.minibyte.service.dataprovider;

import com.alibaba.fastjson.JSON;
import com.minibyte.bo.dto.dataprovider.DataAnalyseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 具备存储的数据分析允许容器
 * @author: AntHubTC
 * @date: 2023/1/11
 * @description:
 */
@Slf4j
@Service
public class DataAnalyseStorageRunService {

    /**
     * 分析数据
     * @param dataAnalyseContext
     * @param dataAnalyse
     * @return
     */
    public Object analyse(DataAnalyseContext dataAnalyseContext, DataAnalyse dataAnalyse) {
        String analyseName = dataAnalyse.getAnalyseName();
        log.info("分析名称:" + analyseName);
        Object analyseResData = dataAnalyse.analyse(dataAnalyseContext);

        log.info("==================================");
        log.info("分析名称:" + analyseName);
        log.info("分析结果:" + JSON.toJSONString(analyseResData));
        log.info("==================================");

        return analyseResData;
    }

    /**
     * 分析数据并将结果存储
     *
     * @param dataAnalyseContext
     * @param dataAnalyse
     * @return
     */
    public Object analyseAndStore(DataAnalyseContext dataAnalyseContext, DataAnalyse dataAnalyse) {
        String analyseName = dataAnalyse.getAnalyseName();
        Object analyseResData = this.analyse(dataAnalyseContext, dataAnalyse);

        dataAnalyseContext.getStoreMap().put(analyseName, analyseResData);
        dataAnalyseContext.writeData();
        return analyseResData;
    }
}
