package com.minibyte.bo.dto;

import cn.hutool.core.collection.CollUtil;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * @author AntHubTC
 * @version 1.0
 * @className DataNode
 * @description
 * @date 2023/7/14 20:07
 **/
@Data
public class DataNode {
    /**
     * 节点id
     */
    private String id;
    /**
     * 节点名称
     */
    private String title;
    /**
     * 节点类型
     */
    private String nodeType;
    /**
     * sspId
     */
    private String sspId;
    /**
     * 附带数据
     */
    private Object data;
    /**
     * 子节点
     */
    private List<DataNode> items;

    @Override
    public String toString() {
        return "DataNode{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", nodeType='" + nodeType + '\'' +
                ", sspId='" + sspId + '\'' +
                ", data=" + data +
                ", items-size=" + (Objects.isNull(items) ? 0 : items.size()) +
                '}';
    }
}
