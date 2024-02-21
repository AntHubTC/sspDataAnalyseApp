package com.minibyte.service.dataprovider;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.minibyte.bo.dto.dataprovider.ArcherySQLQryReq;
import com.minibyte.common.exception.MBBizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: AntHubTC
 * @date: 2023/1/11
 * @description:
 */
@Slf4j
@Service
public class ArcherySQLQueryService {
    public static final String ARCHERY_SQL_QUERY_URL = "https://archery.ag.com/query/";

    private String runSqlSelf(ArcherySQLQryReq archerySQLQryReq) {
        String csrftoken = getCsrftoken(archerySQLQryReq);
        if (Objects.isNull(csrftoken)) {
            throw new MBBizException("未从cookie中获取到token");
        }


        return HttpRequest.post(ARCHERY_SQL_QUERY_URL)
                .header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                .header(Header.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8")
                .header(Header.COOKIE, archerySQLQryReq.getCookie())
                .header("X-CSRFToken", csrftoken)
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .charset("UTF-8")
                .form(getFormMap(archerySQLQryReq))//表单内容
                .timeout(50000)//超时，毫秒
                .execute().body();
    }

    private static String getCsrftoken(ArcherySQLQryReq archerySQLQryReq) {
        if (Objects.isNull(archerySQLQryReq.getCookie())) {
            throw new MBBizException("未配置Archery的token信息");
        }
        String[] sections = archerySQLQryReq.getCookie().split(";");
        for (String section : sections) {
            if (section.startsWith(" csrftoken") || section.startsWith("csrftoken")) {
                 return section.split("=")[1];
            }
        }
        return null;
    }

    public static void main(String[] args) {
        int total = 1;
        int pageNum = (total - 1) / 1000 + 1;
        System.out.println(pageNum);
    }

    public List<Map<String, String>> runSQLPage(ArcherySQLQryReq archerySQLQryReq, boolean isLog) {
        String sql_content = archerySQLQryReq.getSql_content();

        String sql = "select count(1) as num from (" + sql_content + ") tttt";
        archerySQLQryReq.setSql_content(sql);
        List<Map<String, String>> dataMapList = runSQL(archerySQLQryReq, isLog);
        int total = Integer.parseInt(dataMapList.get(0).get("num"));

        if (isLog) {
            System.out.println("记录数：" + total);
        }

        int pageNum = (total - 1) / 1000 + 1;
        List<Map<String, String>> resultDataMapList = new ArrayList<>();
        int n = 0;
        for (int pageIndex = 0; pageIndex < pageNum; pageIndex++) {
            sql = sql_content + " limit " + pageIndex + ", 1000";
            archerySQLQryReq.setSql_content(sql);
            dataMapList = runSQL(archerySQLQryReq, isLog);
            n += dataMapList.size();
            resultDataMapList.addAll(dataMapList);
        }

        return resultDataMapList;
    }

    // TODO:tc: 不知道archery平台分页查询记录数量会大于总记录数量。
    public <T> List<T> runSQLPage(ArcherySQLQryReq archerySQLQryReq, Class<T> clazz) {
        return runSQLPage(archerySQLQryReq, clazz, false);
    }

    public <T> List<T> runSQLPage(ArcherySQLQryReq archerySQLQryReq, Class<T> clazz, boolean isLog) {
        List<Map<String, String>> dataMapList = runSQLPage(archerySQLQryReq, isLog);
        return convertMapToBean(dataMapList, clazz);
    }

    private <T> List<T> convertMapToBean(List<Map<String, String>> dataMapList, Class<T> clazz) {
        return Optional.ofNullable(dataMapList).orElse(Collections.emptyList())
                .stream()
                .map(dataMap -> BeanUtil.mapToBean(dataMap, clazz, false))
                .collect(Collectors.toList());
    }

    public <T> List<T> runSQL(ArcherySQLQryReq archerySQLQryReq, Class<T> clazz) {
        return runSQL(archerySQLQryReq, clazz, false);
    }

    public <T> List<T> runSQL(ArcherySQLQryReq archerySQLQryReq, Class<T> clazz, boolean isLog) {
        List<Map<String, String>> dataMapList = runSQL(archerySQLQryReq, isLog);
        return convertMapToBean(dataMapList, clazz);
    }
    public List<Map<String, String>> runSQL(ArcherySQLQryReq archerySQLQryReq) {
        return runSQL(archerySQLQryReq, true);
    }

    public List<Map<String, String>> runSQL(ArcherySQLQryReq archerySQLQryReq, boolean isLogSql) {
        if (isLogSql) {
             log.info("要执行的sql:{}", archerySQLQryReq.getSql_content());
        }

        String resJson = runSqlSelf(archerySQLQryReq);
        if ("".equals(resJson)) {
            // 不知道为啥第二次执行就会失败，所以这里补救执行一下
            resJson = runSqlSelf(archerySQLQryReq);
        }
        if ("".equals(resJson)) {
            throw new MBBizException("返回之后为空，应该是登录信息失效了");
        }
        JSONObject resObj = JSON.parseObject(resJson);
        Integer status = resObj.getInteger("status");
        if (0 != status) {
            log.info("返回值异常:{}", resJson);
            String msg = resObj.getString("msg");
            throw new MBBizException("返回值异常" + msg);
        }

        JSONObject data = (JSONObject) resObj.get("data");
        JSONArray column_list = data.getJSONArray("column_list");
        JSONArray rows_list = data.getJSONArray("rows");

        List<Map<String, String>> resList = new ArrayList<>();
        for (int i = 0; i < rows_list.size(); i++) {
            JSONArray record = (JSONArray) rows_list.get(i);
            Map<String, String> recordMap = new HashMap<>();
            for (int j = 0; j < column_list.size(); j++) {
                String columnName = column_list.getString(j);
                recordMap.put(columnName, record.getString(j));
            }
            resList.add(recordMap);
        }

        return resList;
    }

    private Map<String, Object> getFormMap(ArcherySQLQryReq archerySQLQryReq) {
        Map<String, Object> formMap = new HashMap<>();
        formMap.put("instance_name", archerySQLQryReq.getInstance_name());
        formMap.put("db_name", archerySQLQryReq.getDb_name());
        formMap.put("schema_name", archerySQLQryReq.getSchema_name());
        formMap.put("tb_name", archerySQLQryReq.getTb_name());
        formMap.put("sql_content", archerySQLQryReq.getSql_content());
        formMap.put("limit_num", archerySQLQryReq.getLimit_num());
        return formMap;
    }
}
