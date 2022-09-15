package com.mysl.api.controller.admin;

import com.github.pagehelper.PageInfo;
import com.mysl.api.common.OperateType;
import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.entity.dto.ApplicationDTO;
import com.mysl.api.entity.dto.AuditDTO;
import com.mysl.api.entity.enums.ApplicationType;
import com.mysl.api.service.ApplicationService;
import com.mysl.api.service.ClassCourseApplicationService;
import com.mysl.api.service.StoreService;
import io.github.flyhero.easylog.annotation.EasyLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ivan Su
 * @date 2022/9/4
 */
@Api(tags = "门店/学习申请接口")
@RestController()
@RequestMapping("/admin")
@Slf4j
@Secured({"ROLE_ADMIN"})
public class ApplicationController {

    @Autowired
    StoreService storeService;
    @Autowired
    ClassCourseApplicationService courseApplicationService;
    @Autowired
    ApplicationService applicationService;

    @ApiOperation("查询申请列表")
    @EasyLog(module = "Admin-查询列表", tenant = "{getClientIP{0}}", type = OperateType.SELECT, success = "", fail = "{{#_errMsg}}")
    @GetMapping("/applications")
    public ResponseData<PageInfo<ApplicationDTO>> list(@ApiParam(value = "页数，默认 1")
                             @RequestParam(name = "page_num", defaultValue = "1", required = false) Integer pageNum,
                                                      @ApiParam(value = "每页记录，默认 20")
                             @RequestParam(name = "page_size", defaultValue = "20", required = false) Integer pageSize,
                                                      @ApiParam(value = "类型(STORE:门店申请，STUDY:学习申请)")
                             @RequestParam String type,
                                                      @ApiParam(value = "搜索关键字")
                             @RequestParam(name = "key_word", required = false) String keyWord) {
        log.info("pageNum: {}, pageSize: {}", pageNum, pageSize);
        if (!EnumUtils.isValidEnum(ApplicationType.class, type)) {
            return ResponseData.generator(HttpStatus.BAD_REQUEST.value(), "申请类型数据错误", null);
        }
        if (ApplicationType.STORE.name().equals(type)) {
            return ResponseData.ok(storeService.getStores(pageNum, pageSize, keyWord));
        } else if (ApplicationType.STUDY.name().equals(type)) {
            return ResponseData.ok(courseApplicationService.getApplications(pageNum, pageSize, keyWord));
        }

        return ResponseData.ok();
    }

    @ApiOperation("审核申请")
    @EasyLog(module = "Admin-审核申请", type = OperateType.UPDATE, success = "", fail = "{{#_errMsg}}", detail = "{{#dto.toString()}}")
    @PostMapping("/applications/audit")
    public ResponseData audit(@Validated @RequestBody AuditDTO dto) {
        if (!EnumUtils.isValidEnum(ApplicationType.class, dto.getType())) {
            return ResponseData.generator(HttpStatus.BAD_REQUEST.value(), "申请类型数据错误", null);
        }
        boolean ret = false;
        if (ApplicationType.STORE.name().equals(dto.getType())) {
            ret = storeService.updateStatus(dto.getIds(), dto.getResult());
        } else if (ApplicationType.STUDY.name().equals(dto.getType())) {
            ret = courseApplicationService.audit(dto.getIds(), dto.getResult());
        }
        if (!ret) {
            return ResponseData.generator(HttpStatus.BAD_REQUEST.value(), "操作失败", null);
        }
        return ResponseData.ok();
    }

    @ApiOperation("查询待审核申请数量")
    @EasyLog(module = "Admin-查询待审核申请数量", type = OperateType.SELECT, success = "", fail = "{{#_errMsg}}")
    @GetMapping("/get_application_count")
    public ResponseData count() {
        return ResponseData.ok(applicationService.countApplications());
    }

}
