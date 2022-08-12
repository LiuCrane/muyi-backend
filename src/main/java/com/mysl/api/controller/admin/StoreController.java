package com.mysl.api.controller.admin;

import com.mysl.api.common.exception.ResourceNotFoundException;
import com.mysl.api.common.exception.ServiceException;
import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.entity.dto.StoreAuditDTO;
import com.mysl.api.entity.dto.StoreDTO;
import com.mysl.api.entity.dto.StoreFullDTO;
import com.mysl.api.entity.enums.StoreStatus;
import com.mysl.api.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/12
 */
@Api(tags = "门店信息接口")
@RestController("adminStoreController")
@RequestMapping("/admin/stores")
@Slf4j
@Secured({"ROLE_ADMIN"})
public class StoreController {

    @Autowired
    StoreService storeService;

    @ApiOperation("查询门店列表")
    @GetMapping
    public ResponseData<List<StoreFullDTO>> list(@RequestParam(defaultValue = "0", required = false) Integer offset,
                                                 @RequestParam(defaultValue = "20", required = false) Integer limit,
                                                 @ApiParam(value = "门店状态(SUBMITTED:待审核, APPROVED: 审核通过, REJECTED: 拒绝)")
                                                 @RequestParam(required = false) StoreStatus status) {
        return ResponseData.ok(storeService.getStores(null, offset, limit, status));
    }

    @ApiOperation("查询门店详情")
    @GetMapping("/{id}")
    public ResponseData<StoreFullDTO> get(@PathVariable Long id) {
        List<StoreFullDTO> list = storeService.getStores(id, 0, 1, null);
        if (CollectionUtils.isEmpty(list)) {
            throw new ResourceNotFoundException("找不到门店信息");
        }
        return ResponseData.ok(list.get(0));
    }

    @ApiOperation("审核门店信息")
    @PostMapping("/{id}/audit")
    public ResponseData audit(@PathVariable Long id, @Validated @RequestBody StoreAuditDTO dto) {
        storeService.updateStatus(id, dto.getResult());
        return ResponseData.ok();
    }

}
