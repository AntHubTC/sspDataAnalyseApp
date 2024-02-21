package com.minibyte.bo.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author AntHubTC
 * @version 1.0
 * @className DataNodeComposet
 * @description
 * @date 2023/7/17 10:38
 **/
@Data
@Builder
public class DataNodeCompose {
    private DataNode left;
    private DataNode right;
}
