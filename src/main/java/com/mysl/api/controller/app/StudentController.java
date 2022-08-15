package com.mysl.api.controller.app;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.config.security.JwtTokenUtil;
import com.mysl.api.entity.dto.*;
import com.mysl.api.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/5
 */
@Api(tags = "门店学员接口")
@RestController
@RequestMapping("/app")
@Slf4j
@Secured("ROLE_STORE_MANAGER")
public class StudentController {

    @Autowired
    StudentService studentService;

    @ApiOperation("查询学员列表")
    @GetMapping("/students")
    public ResponseData<List<StudentDTO>> list(@ApiParam(value = "默认 0", defaultValue = "0")
                                               @RequestParam(defaultValue = "0", required = false) Integer offset,
                                               @ApiParam(value = "默认 20", defaultValue = "20")
                                               @RequestParam(defaultValue = "20", required = false) Integer limit,
                                               @ApiParam(value = "班级id")
                                               @RequestParam(name = "class_id", required = false) Long classId,
                                               @ApiParam(value = "true:查询复训学员列表，false:查询学习中的学员列表，不传查全部学员")
                                               @RequestParam(value = "rehab", required = false) String rehab) {
        Boolean rehabVal = null;
        if ("true".equals(rehab) || "false".equals(rehab)) {
            rehabVal = Boolean.valueOf(rehab);
        }
        List<StudentFullDTO> dtoList = studentService.getStudents(offset, limit, null, null, JwtTokenUtil.getCurrentStoreId(), classId, rehabVal);
        List<StudentDTO> list = CglibUtil.copyList(dtoList, StudentDTO::new);
        return ResponseData.ok(list);
    }

    @ApiOperation("查询学员信息详情")
    @GetMapping("/students/{id}")
    public ResponseData<StudentDTO> get(@PathVariable Long id) {
        StudentFullDTO fullDTO = studentService.getStudentByStoreIdAndId(JwtTokenUtil.getCurrentStoreId(), id);
        StudentDTO dto = new StudentDTO();
        CglibUtil.copy(fullDTO, dto);
        return ResponseData.ok(dto);
    }

    @ApiOperation("提交学员信息")
    @PostMapping("/students")
    public ResponseData create(@Validated @RequestBody StudentCreateDTO dto) {
        log.info("create student dto: {}", dto);
        studentService.save(dto);
        return ResponseData.ok();
    }

    @ApiOperation("更新学员信息")
    @PutMapping("/students/{id}/vision")
    public ResponseData update(@PathVariable Long id, @RequestBody StudentVisionDTO dto) {
        log.info("update student vision dto: {}", dto);
        studentService.updateVision(JwtTokenUtil.getCurrentStoreId(), id, dto);
        return ResponseData.ok();
    }

    @ApiOperation(value = "学员签到", notes = "后台仅做记录")
    @PostMapping("/students/{id}/sign_in")
    public ResponseData signIn(@PathVariable Long id) {
        return ResponseData.ok();
    }

}
