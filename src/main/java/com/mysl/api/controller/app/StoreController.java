package com.mysl.api.controller.app;

import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.config.security.JwtTokenUtil;
import com.mysl.api.entity.Store;
import com.mysl.api.entity.dto.StoreDTO;
import com.mysl.api.entity.dto.StoreSimpleDTO;
import com.mysl.api.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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
    @Secured({"ROLE_APP_USER", "ROLE_STORE_MANAGER"})
    @GetMapping
    public ResponseData<StoreDTO> get() {
        StoreDTO dto = new StoreDTO();
        Store store = storeService.findByUserId(JwtTokenUtil.getCurrentUserId());
        BeanUtils.copyProperties(store, dto);
        return ResponseData.ok(dto);
    }

    @ApiOperation("查询门店加盟商列表")
    @Secured({"ROLE_STORE_MANAGER"})
    @GetMapping("/franchisees")
    public ResponseData<List<StoreSimpleDTO>> getFranchisees(@RequestParam(defaultValue = "0", required = false) Integer offset,
                                                             @RequestParam(defaultValue = "20", required = false) Integer limit){
        return ResponseData.ok();
    }
}
