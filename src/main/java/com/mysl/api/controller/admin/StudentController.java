package com.mysl.api.controller.admin;

import com.github.pagehelper.PageInfo;
import com.mysl.api.common.OperateType;
import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.entity.dto.StudentFullDTO;
import com.mysl.api.service.StudentService;
import io.github.flyhero.easylog.annotation.EasyLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ivan Su
 * @date 2022/8/29
 */
@Api(tags = "学员管理接口")
@RestController("adminStudentController")
@RequestMapping("/admin/students")
@Slf4j
@Secured({"ROLE_ADMIN"})
public class StudentController {

    @Autowired
    StudentService studentService;

    @ApiOperation("查询学员列表")
    @EasyLog(module = "Admin-查询学员列表", type = OperateType.SELECT, success = "", fail = "{{#_errMsg}}")
    @GetMapping
    public ResponseData<PageInfo<StudentFullDTO>> list(@ApiParam(value = "页数，默认 1")
                                                       @RequestParam(name = "page_num", defaultValue = "1", required = false) Integer pageNum,
                                                       @ApiParam(value = "每页记录，默认 20")
                                                       @RequestParam(name = "page_size", defaultValue = "20", required = false) Integer pageSize,
                                                       @ApiParam(value = "搜索关键字")
                                                       @RequestParam(name = "key_word", required = false) String keyWord) {
        return ResponseData.ok(studentService.getStudents(pageNum, pageSize, null, null, null, null, null, keyWord));
    }

    @ApiOperation("查询学员详情")
    @EasyLog(module = "Admin-查询学员详情", type = OperateType.SELECT, bizNo = "{{#id}}", success = "", fail = "{{#_errMsg}}")
    @GetMapping("/students/{id}")
    public ResponseData<StudentFullDTO> get(@PathVariable Long id) {
        return ResponseData.ok(studentService.getStudentById(id));
    }
}

