package com.minibyte.common;

import com.minibyte.bo.dto.dataprovider.DataAnalyseContext;
import com.minibyte.service.dataprovider.DataAnalyse;
import com.minibyte.service.dataprovider.DataAnalyseStorageRunService;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Map;

/**
 * 数据分析基类
 *
 * @author: AntHubTC
 * @date: 2023/2/23
 * @description:
 */
@Slf4j
public abstract class DataAnalyseBoot extends BaseSpringBootAppTest {
    @Autowired
    private DataAnalyseStorageRunService dataAnalyseStorageRun;

    private DataAnalyseContext dataAnalyseContext;

    @Before
    public void before() {
        File storeAnalyseData = setDataAnalyseFile();
        this.dataAnalyseContext = DataAnalyseContext.readData(storeAnalyseData);
    }

    public abstract File setDataAnalyseFile();

    @After
    public void after() {
        printAnalyseResultList();
    }

    protected void analyse(DataAnalyse dataAnalyse) {
        dataAnalyseStorageRun.analyse(this.dataAnalyseContext, dataAnalyse);
    }

    protected void analyseAndStore(DataAnalyse dataAnalyse) {
        dataAnalyseStorageRun.analyseAndStore(this.dataAnalyseContext, dataAnalyse);
    }

    /**
     * 打印目前所有的分析结果
     */
    private void printAnalyseResultList() {
        log.info(" ================当前所有分析结果==================");
        Map<String, Object> storeMap = this.dataAnalyseContext.getStoreMap();
        int row = 1;
        for (String analyseName : storeMap.keySet()) {
            log.info("{}、{}：{}", row++, analyseName, storeMap.get(analyseName));
        }

        log.info(" ===============================================");
    }
}
