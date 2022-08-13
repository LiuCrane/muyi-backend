package com.mysl.api.controller.admin;

import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.entity.dto.CourseAuditDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
@Api(tags = "课程申请审核接口")
@RestController
@RequestMapping("/admin/course/applications")
public class CourseApplicationController {

    @ApiOperation("查询门店课程申请列表")
    @GetMapping
    public ResponseData list(@RequestParam(required = false, defaultValue = "0") Integer offset,
                             @RequestParam(required = false, defaultValue = "20") Integer limit) {
        return ResponseData.ok();
    }

    @ApiOperation("审核课程申请")
    @PostMapping("/{id}/audit")
    public ResponseData audit(@PathVariable Long id, @Validated @RequestBody CourseAuditDTO dto) {
        return ResponseData.ok();
    }
}
