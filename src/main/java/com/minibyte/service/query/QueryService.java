package com.minibyte.service.query;

import cn.hutool.core.collection.CollUtil;
import com.minibyte.bo.dto.DataNode;
import com.minibyte.bo.dto.SetTokenReq;
import com.minibyte.bo.dto.dataprovider.ArcheryInstanceProvrder;
import com.minibyte.bo.dto.dataprovider.ArcherySQLQryReq;
import com.minibyte.common.exception.MBBizException;
import com.minibyte.config.UserConfig;
import com.minibyte.service.dataprovider.ArcherySQLQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author AntHubTC
 * @version 1.0
 * @className QueryService
 * @description
 * @date 2023/6/9 17:39
 **/
@Service
public class QueryService {
    @Autowired
    private UserConfig userConfig;
    @Resource
    protected ArcherySQLQueryService archerySQLQueryService;

    public List<Map<String, String>> querySSPPremisesBuildUnitElePoints(String premisesId) {
        List<Map<String, String>> dataMapList = new ArrayList<>();
        // 查询屏点位
        dataMapList.addAll(getSSPScreenPoints(premisesId));
        // 查询框点位
        dataMapList.addAll(getSSPFramePoints(premisesId));
        return dataMapList;
    }

    private List<Map<String, String>> getSSPScreenPoints(String premisesId) {
        String sql = "select " +
                // 楼盘信息
                "  premises.name as premisesName, " +
                "  premises.id as premisesId, " +
                // 楼栋信息
                "  build.name as buildName, " +
                "  build.id as buildId, " +
                // 单元信息
                "  unit.name as unitName, " +
                "  unit.id as unitId, " +
                "  unit.premises_id as unitPremisesId, " + // 单元冗余的信息
                // 电梯信息
                "  elevator.name as eleName, " +
                "  elevator.id as eleId, " +
                "  elevator.ele_num as eleNum, " +
                "  elevator.premises_id as elePremisesId, " + // 电梯冗余的信息
                "  elevator.build_id as eleBuildId, " + // 电梯冗余的信息
                // 点位信息
                "  m.point_id as pointNum, " +
                "  m.id as pointId, " +
                "  m.premises_id as pointPremisesId, " + // 点位冗余的信息
                "  m.build_id as pointBuildId, " + // 点位冗余的信息
                "  m.unit_id as pointUnitId, " + // 点位冗余的信息
                "  '1' as type " +
                "from premises " +
                "  left join build on premises.id = build.premises_id " +
                "  left join unit on unit.build_id = build.id " +
                "  left join elevator on unit.id = elevator.unit_id " +
                "  left join `point` m on elevator.id = m.ele_id " +
                "where " +
                "  premises.id = " + premisesId +
                    " and premises.is_delete=0 and build.is_delete=0 " +
                    " and unit.is_delete=0 and elevator.is_delete=0 and m.is_delete=0 " +
                "order by buildName asc, unitName asc, eleName asc, pointNum asc;";
        ArcherySQLQryReq archerySQLQryReq = ArcheryInstanceProvrder.genSSP3Instance(sql, userConfig);
        return archerySQLQueryService.runSQL(archerySQLQryReq);
    }

    private List<Map<String, String>> getSSPFramePoints(String premisesId) {
        String sql = "select " +
                // 楼盘信息
                "  premises.name as premisesName, " +
                "  premises.id as premisesId, " +
                // 楼栋信息
                "  build.name as buildName, " +
                "  build.id as buildId, " +
                // 单元信息
                "  unit.name as unitName, " +
                "  unit.id as unitId, " +
                "  unit.premises_id as unitPremisesId, " + // 单元冗余的信息
                // 电梯信息
                "  elevator.name as eleName, " +
                "  elevator.id as eleId, " +
                "  elevator.ele_num as eleNum, " +
                "  elevator.premises_id as elePremisesId, " + // 电梯冗余的信息
                "  elevator.build_id as eleBuildId, " + // 电梯冗余的信息
                // 点位信息
                "  m.code as pointNum, " +
                "  m.id as pointId, " +
                "  m.premises_id as pointPremisesId, " + // 点位冗余的信息
                "  m.build_id as pointBuildId, " + // 点位冗余的信息
                "  m.unit_id as pointUnitId, " + // 点位冗余的信息
                "  '2' as type " +
                "from premises " +
                "  left join build on premises.id = build.premises_id " +
                "  left join unit on unit.build_id = build.id " +
                "  left join elevator on unit.id = elevator.unit_id " +
                "  left join `media` m on elevator.id = m.elevator_id " +
                "where " +
                "  premises.id = " + premisesId + " " +
                " and premises.is_delete=0 and build.is_delete=0 " +
                " and unit.is_delete=0 and elevator.is_delete=0  and m.deleted=0 " +
                " order by buildName asc, unitName asc, eleName asc, pointNum asc;";
        ArcherySQLQryReq archerySQLQryReq = ArcheryInstanceProvrder.genSSP3Instance(sql, userConfig);
        return archerySQLQueryService.runSQL(archerySQLQryReq);
    }

    /**
     * 获取SSP楼盘下的所有树节点
     *
     * @param premisesId 楼盘id
     * @return 树节点
     */
    public DataNode getSSPTreeDataNode(String premisesId) {
        List<Map<String, String>> premisesDataList = querySSPPremisesBuildUnitElePoints(premisesId);
        return buildTreeDataNode(premisesId, premisesDataList);
    }

    /**
     * 构建树节点
     *
     * @param premisesId 楼按id
     * @param premisesDataList 楼盘下点位信息
     * @return
     */
    private static DataNode buildTreeDataNode(String premisesId, List<Map<String, String>> premisesDataList) {
        if (CollUtil.isEmpty(premisesDataList)) {
            return null;
        }

        // 楼盘
        Map<String, String> premisesDataMap = premisesDataList.get(0);
        DataNode premisesDataNode = new DataNode();
        premisesDataNode.setId(premisesDataMap.get("premisesId"));
        premisesDataNode.setSspId(premisesDataMap.get("sspPremisesId"));
        premisesDataNode.setTitle(premisesDataMap.get("premisesName"));
        premisesDataNode.setNodeType("premises");
        premisesDataNode.setData(getPremisesOtherData(premisesDataMap));
        premisesDataNode.setItems(new ArrayList<>());

        // 楼栋
        Map<String, List<Map<String, String>>> buildDataMap = premisesDataList.stream()
                .filter(map -> Objects.nonNull(map.get("buildId")))
                .collect(Collectors.groupingBy(map -> map.get("buildId"), LinkedHashMap::new, Collectors.toList()));
        for (String buildId : buildDataMap.keySet()) {
            List<Map<String, String>> unitDataList = buildDataMap.getOrDefault(buildId, Collections.emptyList());

            DataNode buildDataNode = new DataNode();
            buildDataNode.setId(buildId);
            buildDataNode.setSspId(unitDataList.get(0).get("sspBuildId"));
            buildDataNode.setTitle(unitDataList.get(0).get("buildName"));
            buildDataNode.setNodeType("build");
            buildDataNode.setData(getBuildOtherData(unitDataList.get(0)));
            buildDataNode.setItems(new ArrayList<>());
            premisesDataNode.getItems().add(buildDataNode);

            // 单元
            Map<String, List<Map<String, String>>> unitDataMap = unitDataList.stream()
                    .filter(map -> Objects.nonNull(map.get("unitId")))
                    .collect(Collectors.groupingBy(map -> map.get("unitId"), LinkedHashMap::new, Collectors.toList()));
            for (String unitId : unitDataMap.keySet()) {
                List<Map<String, String>> eleDataList = unitDataMap.getOrDefault(unitId, Collections.emptyList());

                DataNode unitDataNode = new DataNode();
                unitDataNode.setId(unitId);
                unitDataNode.setSspId(eleDataList.get(0).get("sspUnitId"));
                unitDataNode.setTitle(eleDataList.get(0).get("unitName"));
                unitDataNode.setNodeType("unit");
                unitDataNode.setData(getUnitOtherData(eleDataList.get(0)));
                unitDataNode.setItems(new ArrayList<>());
                buildDataNode.getItems().add(unitDataNode);

                // 电梯
                Map<String, List<Map<String, String>>> eleDataMap = eleDataList.stream()
                        .filter(map -> Objects.nonNull(map.get("eleId")))
                        .collect(Collectors.groupingBy(map -> map.get("eleId"), LinkedHashMap::new, Collectors.toList()));
                for (String eleId : eleDataMap.keySet()) {
                    List<Map<String, String>> pointDataList = eleDataMap.getOrDefault(eleId, Collections.emptyList());

                    DataNode eleDataNode = new DataNode();
                    eleDataNode.setId(eleId);
                    eleDataNode.setSspId(pointDataList.get(0).get("sspEleId"));
                    eleDataNode.setTitle(pointDataList.get(0).get("eleName"));
                    eleDataNode.setNodeType("ele");
                    eleDataNode.setData(getEleOtherData(pointDataList.get(0)));
                    eleDataNode.setItems(new ArrayList<>());
                    unitDataNode.getItems().add(eleDataNode);

                    Map<String, Map<String, String>> pointDataMap = pointDataList.stream()
                            .filter(map -> Objects.nonNull(map.get("pointId")))
                            .collect(Collectors.toMap(map -> map.get("pointId"), Function.identity()));
                    for (String pointId : pointDataMap.keySet()) {
                        Map<String, String> pointData = pointDataMap.get(pointId);

                        DataNode pointDataNode = new DataNode();
                        pointDataNode.setId(pointId);
                        pointDataNode.setSspId(pointData.get("sspPointId"));
                        pointDataNode.setTitle(pointData.get("pointNum"));
                        pointDataNode.setNodeType("point");
                        pointDataNode.setItems(Collections.emptyList());
                        pointDataNode.setData(getPointOtherData(pointData));
                        eleDataNode.getItems().add(pointDataNode);
                    }
                }
            }
        }
        return premisesDataNode;
    }

    private static Object getPointOtherData(Map<String, String> pointData) {
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("pointPremisesId", pointData.get("pointPremisesId"));
        dataMap.put("pointBuildId", pointData.get("pointBuildId"));
        dataMap.put("pointUnitId", pointData.get("pointUnitId"));
        dataMap.put("sspId", pointData.get("sspPointId"));
        dataMap.put("type", pointData.get("type"));
        return dataMap;
    }

    private static Object getEleOtherData(Map<String, String> eleMap) {
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("elePremisesId", eleMap.get("elePremisesId"));
        dataMap.put("eleBuildId", eleMap.get("eleBuildId"));
        dataMap.put("eleNum", eleMap.get("eleNum"));
        dataMap.put("sspId", eleMap.get("sspEleId"));
        return dataMap;
    }

    private static Object getUnitOtherData(Map<String, String> unitMap) {
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("unitPremisesId", unitMap.get("unitPremisesId"));
        dataMap.put("sspId", unitMap.get("sspUnitId"));
        return dataMap;
    }

    private static Object getBuildOtherData(Map<String, String> buildMap) {
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("sspId", buildMap.get("sspBuildId"));
        return dataMap;
    }

    private static Object getPremisesOtherData(Map<String, String> premisesDataMap) {
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("sspId", premisesDataMap.get("sspPremisesId"));
        return dataMap;
    }

    /**
     * 获取融媒楼盘下的所有树节点
     *
     * @param premisesId 楼盘id
     * @return 树节点
     */
    public DataNode getRMTreeDataNode(String premisesId) {
        List<Map<String, String>> premisesDataList = queryRMPremisesBuildUnitElePoints(premisesId);
        return buildTreeDataNode(premisesId, premisesDataList);
    }

    public List<Map<String, String>> queryRMPremisesBuildUnitElePoints(String premisesId) {
        return getRMFramePoints(premisesId);
    }

    private List<Map<String, String>> getRMFramePoints(String premisesId) {
        String sql = "  select " +
                // 楼盘信息
                "       rp.project_name as premisesName,  " +
                "       rp.id as premisesId,  " +
                "       rp.ssp_id as sspPremisesId,  " +
                // 楼栋信息
                "       rb.id as buildId,  " +
                "       rb.ssp_id as sspBuildId,  " +
                "       rb.building_name as buildName,  " +
                // 单元信息
                "       ru.id as unitId,  " +
                "       ru.ssp_id as sspUnitId,  " +
                "       ru.unit_name as unitName,  " +
                // 电梯信息
                "       re.id as eleId,  " +
                "       re.ssp_id as sspEleId,  " +
                "       re.code as eleNum,  " +
                "       re.elevator_name as eleName,  " +
                "       re.building_id as eleBuildId, " + // 电梯冗余的信息
                // 点位信息
                "       rm.id as pointId,  " +
                "       rm.ssp_id as sspPointId,  " +
                "       rm.code as pointNum,  " +
                "       rm.media_name as pointName  , " +
                "       rm.building_id as pointBuildId, " + // 点位冗余的信息
                "       rm.unit_id as pointUnitId, " + // 点位冗余的信息
                "       '2' as type " +
                "from resource_project rp  " +
                "         left join resource_building rb on rp.id = rb.project_id  " +
                "         left join resource_unit ru on rb.id = ru.building_id  " +
                "         left join resource_elevator re on ru.id = re.unit_id  " +
                "         left join resource_media rm on re.id = rm.elevator_id  " +
                "where rp.deleted = false  " +
                "  and rb.deleted = false  " +
                "  and ru.deleted = false  " +
                "  and re.deleted = false  " +
                "  and rm.deleted = false  " +
                "  and rp.ssp_id = " + premisesId + " " +
                "order by buildName asc, unitName asc, eleName asc, pointNum asc;";
        ArcherySQLQryReq archerySQLQryReq = ArcheryInstanceProvrder.genRMInstance(sql, userConfig);
        return archerySQLQueryService.runSQL(archerySQLQryReq);
    }

    public void testConfig(SetTokenReq setTreeReq) {
        String sql = "select 1 from dual;";

        // 如果token有问题，内层会报错的
        try {
            ArcherySQLQryReq sspTestReq = ArcheryInstanceProvrder.genSSP3Instance(sql, setTreeReq.getSspCookie());
            archerySQLQueryService.runSQL(sspTestReq);
        } catch (MBBizException e) {
            throw new MBBizException("ssp token配置无效");
        }

        try {
            ArcherySQLQryReq rmTestReq = ArcheryInstanceProvrder.genRMInstance(sql, setTreeReq.getRmCookie());
            archerySQLQueryService.runSQL(rmTestReq);
        } catch (MBBizException e) {
            throw new MBBizException("融媒 token配置无效");
        }
    }
}
