package com.mysl.api.controller.admin;

import com.github.pagehelper.PageInfo;
import com.mysl.api.common.OperateType;
import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.entity.dto.ClassCourseApplicationDTO;
import com.mysl.api.entity.dto.CourseAuditDTO;
import com.mysl.api.service.ClassCourseApplicationService;
import io.github.flyhero.easylog.annotation.EasyLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
@Api(tags = "课程申请审核接口")
@ApiIgnore
@RestController
@RequestMapping("/admin/course/applications")
@Secured("ROLE_ADMIN")
public class CourseApplicationController {

    @Autowired
    ClassCourseApplicationService classCourseApplicationService;

    @ApiOperation("查询门店课程申请列表")
    @EasyLog(module = "Admin-查询门店课程申请列表", tenant = "{getClientIP{0}}", type = OperateType.SELECT, success = "", fail = "{{#_errMsg}}")
    @GetMapping
    public ResponseData<PageInfo<ClassCourseApplicationDTO>> list(@ApiParam("页数，默认 1")
                                                                  @RequestParam(name = "page_num", required = false, defaultValue = "1")
                                                                  Integer pageNum,
                                                                  @ApiParam("每页记录，默认 20")
                                                                  @RequestParam(name = "page_size", required = false, defaultValue = "20")
                                                                  Integer pageSize) {
        return ResponseData.ok(classCourseApplicationService.getApplications(pageNum, pageSize));
    }

    @ApiOperation("审核课程申请")
    @EasyLog(module = "Admin-审核课程申请", tenant = "{getClientIP{0}}", type = OperateType.UPDATE, success = "", fail = "{{#_errMsg}}", bizNo = "{{#id}}", detail = "{{#dto.toString()}}")
    @PostMapping("/{id}/audit")
    public ResponseData audit(@PathVariable Long id, @Validated @RequestBody CourseAuditDTO dto) {
//        classCourseApplicationService.audit(id, dto.getResult());
        return ResponseData.ok();
    }
}
