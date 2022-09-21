package com.mysl.api.controller.app;

import com.github.pagehelper.PageInfo;
import com.mysl.api.common.OperateType;
import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.config.security.JwtTokenUtil;
import com.mysl.api.entity.dto.StoreDTO;
import com.mysl.api.entity.dto.StoreFullDTO;
import com.mysl.api.entity.dto.StoreSimpleDTO;
import com.mysl.api.service.StoreService;
import io.github.flyhero.easylog.annotation.EasyLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/4
 */
@Api(tags = "门店信息接口")
@RestController
@RequestMapping("/app/store")
@Slf4j
public class StoreController {

    @Autowired
    StoreService storeService;

    @ApiOperation("查询门店信息")
    @EasyLog(module = "App-查询门店信息", tenant = "{getClientIP{0}}", type = OperateType.SELECT, success = "", fail = "{{#_errMsg}}")
    @Secured({"ROLE_APP_USER", "ROLE_STORE_MANAGER"})
    @GetMapping
    public ResponseData<StoreDTO> get() {
        StoreDTO dto = new StoreDTO();
        List<StoreFullDTO> list = storeService.getStores(1, 1, null, null, null, null, null, JwtTokenUtil.getCurrentUserId(), null);
        if (!CollectionUtils.isEmpty(list)) {
            BeanUtils.copyProperties(list.get(0), dto);
        }
        return ResponseData.ok(dto);
    }

    @ApiOperation("查询门店加盟商列表")
    @EasyLog(module = "App-查询加盟商列表", tenant = "{getClientIP{0}}", type = OperateType.SELECT, success = "", fail = "{{#_errMsg}}")
    @Secured({"ROLE_STORE_MANAGER"})
    @GetMapping("/franchisees")
    public ResponseData<PageInfo<StoreSimpleDTO>> getFranchisees(@ApiParam(value = "页数，默认 1")
                                                                 @RequestParam(name = "page_num", defaultValue = "1", required = false) Integer pageNum,
                                                                 @ApiParam(value = "每页记录，默认 20")
                                                                 @RequestParam(name = "page_size", defaultValue = "20", required = false) Integer pageSize) {
        log.info("getFranchisees pageNum: {}, pageSize: {}", pageNum, pageSize);
        return ResponseData.ok(storeService.getFranchisees(pageNum, pageSize, JwtTokenUtil.getCurrentStoreId()));
    }
}
