package com.mysl.api.controller.admin;

import com.github.pagehelper.PageInfo;
import com.mysl.api.common.OperateType;
import com.mysl.api.common.exception.ResourceNotFoundException;
import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.entity.dto.StoreAuditDTO;
import com.mysl.api.entity.dto.StoreFullDTO;
import com.mysl.api.entity.dto.StoreUpdateDTO;
import com.mysl.api.entity.enums.StoreStatus;
import com.mysl.api.service.StoreService;
import io.github.flyhero.easylog.annotation.EasyLog;
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
@Api(tags = "门店管理接口")
@RestController("adminStoreController")
@RequestMapping("/admin/stores")
@Slf4j
@Secured({"ROLE_ADMIN"})
public class StoreController {

    @Autowired
    StoreService storeService;

    @ApiOperation("查询门店列表")
    @EasyLog(module = "Admin-查询门店列表", tenant = "{getClientIP{0}}", type = OperateType.SELECT, success = "", fail = "{{#_errMsg}}")
    @GetMapping
    public ResponseData<PageInfo<StoreFullDTO>> list(@ApiParam(value = "页数，默认 1")
                                                     @RequestParam(name = "page_num", defaultValue = "1", required = false) Integer pageNum,
                                                     @ApiParam(value = "每页记录，默认 20")
                                                     @RequestParam(name = "page_size", defaultValue = "20", required = false) Integer pageSize,
                                                     @ApiParam(value = "门店状态(SUBMITTED:待审核, APPROVED: 审核通过, REJECTED: 拒绝)")
                                                     @RequestParam(required = false, defaultValue = "APPROVED") StoreStatus status,
                                                     @ApiParam(value = "门店名称")
                                                     @RequestParam(name = "name", required = false) String name,
                                                     @ApiParam(value = "店长")
                                                     @RequestParam(name = "manager_name", required = false) String managerName) {
        return ResponseData.ok(new PageInfo<>(storeService.getStores(pageNum, pageSize, null, status, name, managerName, null, null)));
    }

    @ApiOperation("查询门店详情")
    @EasyLog(module = "Admin-查询门店详情", tenant = "{getClientIP{0}}", type = OperateType.SELECT, bizNo = "{{#id}}", success = "", fail = "{{#_errMsg}}")
    @GetMapping("/{id}")
    public ResponseData<StoreFullDTO> get(@PathVariable Long id) {
        List<StoreFullDTO> list = storeService.getStores(1, 1, id, null, null, null, null, null);
        if (CollectionUtils.isEmpty(list)) {
            throw new ResourceNotFoundException("找不到门店信息");
        }
        return ResponseData.ok(list.get(0));
    }

//    @ApiOperation("审核门店信息")
//    @EasyLog(module = "Admin-审核门店信息", type = OperateType.UPDATE, bizNo = "{{#id}}", success = "", fail = "{{#_errMsg}}", detail = "{{#dto.toString()}}")
//    @PostMapping("/{id}/audit")
//    public ResponseData audit(@PathVariable Long id, @Validated @RequestBody StoreAuditDTO dto) {
//        storeService.updateStatus(id, dto.getResult());
//        return ResponseData.ok();
//    }

    @ApiOperation("修改门店信息")
    @EasyLog(module = "Admin-修改门店信息", tenant = "{getClientIP{0}}", type = OperateType.UPDATE, bizNo = "{{#id}}", success = "", fail = "{{#_errMsg}}", detail = "{{#dto.toString()}}")
    @PutMapping("/{id}")
    public ResponseData update(@PathVariable Long id, @Validated @RequestBody StoreUpdateDTO dto) {
        log.info("update store dto: {}", dto);
        storeService.update(id, dto);
        return ResponseData.ok();
    }

    @ApiOperation("注销门店")
    @EasyLog(module = "Admin-注销门店", tenant = "{getClientIP{0}}", type = OperateType.UPDATE, bizNo = "{{#id}}", success = "", fail = "{{#_errMsg}}")
    @PostMapping("/{id}/cancel")
    public ResponseData cancel(@PathVariable Long id) {
        storeService.cancel(id);
        return ResponseData.ok();
    }

}
