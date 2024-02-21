package com.minibyte.controller.app;

import com.minibyte.bo.dto.DataNode;
import com.minibyte.bo.dto.DataNodeCompose;
import com.minibyte.bo.dto.GetTreeReq;
import com.minibyte.bo.dto.SetTokenReq;
import com.minibyte.common.MBResponse;
import com.minibyte.config.UserConfig;
import com.minibyte.service.query.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AppController {
    @Autowired
    private QueryService queryService;
    @Autowired
    private UserConfig userConfig;

//    @GetMapping("/")
//    public String index() {
//        return "index.html";
//    }

    @PostMapping("/treeData")
    @ResponseBody
    public MBResponse<DataNodeCompose> treeData(@RequestBody GetTreeReq getTreeReq) {
        String premisesId = getTreeReq.getPremisesId();
        DataNode sspDataNode = queryService.getSSPTreeDataNode(premisesId);
        DataNode rmDataNode = queryService.getRMTreeDataNode(premisesId);

        DataNodeCompose dataNodeCompose = DataNodeCompose.builder()
                .left(sspDataNode).right(rmDataNode).build();
        return MBResponse.ofSuccess(dataNodeCompose);
    }

    @PostMapping("/setUserConfig")
    @ResponseBody
    public MBResponse<Boolean> setUserConfig(@RequestBody SetTokenReq setTreeReq) {
        userConfig.setSspCookie(setTreeReq.getSspCookie());
        userConfig.setRmCookie(setTreeReq.getRmCookie());
        return MBResponse.ofSuccess(Boolean.TRUE);
    }

    @GetMapping("/getUserConfig")
    @ResponseBody
    public MBResponse<SetTokenReq> getToken() {
        SetTokenReq setTokenReq = new SetTokenReq();
        setTokenReq.setSspCookie(userConfig.getSspCookie());
        setTokenReq.setRmCookie(userConfig.getRmCookie());
        return MBResponse.ofSuccess(setTokenReq);
    }

    @PostMapping("/testConfig")
    @ResponseBody
    public MBResponse<Boolean> testConfig(@RequestBody SetTokenReq setTreeReq) {
        queryService.testConfig(setTreeReq);
        return MBResponse.ofSuccess(Boolean.TRUE);
    }
}
