package com.mysl.api.controller.app;

import cn.hutool.extra.cglib.CglibUtil;
import com.github.pagehelper.PageInfo;
import com.mysl.api.common.OperateType;
import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.config.security.JwtTokenUtil;
import com.mysl.api.entity.dto.*;
import com.mysl.api.entity.enums.Gender;
import com.mysl.api.service.CourseService;
import com.mysl.api.service.StudentService;
import io.github.flyhero.easylog.annotation.EasyLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


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
    @Autowired
    CourseService courseService;

    @ApiOperation("查询学员列表")
    @EasyLog(module = "App-查询学员列表", type = OperateType.SELECT, success = "", fail = "{{#_errMsg}}")
    @GetMapping("/students")
    public ResponseData<PageInfo<StudentDTO>> list(@ApiParam(value = "页数，默认 1")
                                                   @RequestParam(name = "page_num", defaultValue = "1", required = false) Integer pageNum,
                                                   @ApiParam(value = "每页记录，默认 20")
                                                   @RequestParam(name = "page_size", defaultValue = "20", required = false) Integer pageSize,
                                                   @ApiParam(value = "班级id")
                                                   @RequestParam(name = "class_id", required = false) Long classId,
                                                   @ApiParam(value = "true:查询复健学员列表，false:查询学习中的学员列表，不传查全部学员")
                                                   @RequestParam(value = "rehab", required = false) String rehab) {
        Boolean rehabVal = null;
        if ("true".equals(rehab) || "false".equals(rehab)) {
            rehabVal = Boolean.valueOf(rehab);
        }
        return ResponseData.ok(studentService.getStudents(pageNum, pageSize, JwtTokenUtil.getCurrentStoreId(), classId, rehabVal));
    }

    @ApiOperation("查询学员信息详情")
    @EasyLog(module = "App-查询学员信息详情", type = OperateType.SELECT, bizNo = "{{#id}}", success = "", fail = "{{#_errMsg}}")
    @GetMapping("/students/{id}")
    public ResponseData<StudentDTO> get(@PathVariable Long id) {
        StudentFullDTO fullDTO = studentService.getStudentByStoreIdAndId(JwtTokenUtil.getCurrentStoreId(), id);
        StudentDTO dto = new StudentDTO();
        CglibUtil.copy(fullDTO, dto);
        return ResponseData.ok(dto);
    }

    @ApiOperation("提交学员信息")
    @EasyLog(module = "App-提交学员信息", type = OperateType.ADD, success = "", fail = "{{#_errMsg}}", detail = "{{#dto.toString()}}")
    @PostMapping("/students")
    public ResponseData create(@Validated @RequestBody StudentCreateDTO dto) {
        log.info("create student dto: {}", dto);
        if (!Gender.男.name().equals(dto.getGender()) && !Gender.女.name().equals(dto.getGender())) {
            return ResponseData.generator(HttpStatus.BAD_REQUEST.value(), "性别数据错误", null);
        }
        studentService.save(dto);
        return ResponseData.ok();
    }

    @ApiOperation("更新学员视力信息")
    @EasyLog(module = "App-更新学员视力信息", type = OperateType.UPDATE, bizNo = "{{#id}}", success = "", fail = "{{#_errMsg}}", detail = "dto: {{#dto.toString()}}")
    @PutMapping("/students/{id}/vision")
    public ResponseData update(@PathVariable Long id, @RequestBody StudentVisionDTO dto) {
        log.info("update student vision dto: {}", dto);
        studentService.updateVision(JwtTokenUtil.getCurrentStoreId(), id, dto);
        return ResponseData.ok();
    }

    @ApiOperation("查询学员已完成的课程")
    @EasyLog(module = "App-查询学员已完成的课程", type = OperateType.UPDATE, bizNo = "{{#id}}", success = "", fail = "{{#_errMsg}}")
    @GetMapping("/students/{id}/courses")
    public ResponseData<ListData<CourseSimpleDTO>> getCompletedCourse(@PathVariable Long id) {
        return ResponseData.ok(new ListData<CourseSimpleDTO>().setList(courseService.getCompletedCourse(id)));
    }

    @ApiIgnore
    @ApiOperation(value = "学员签到", notes = "后台仅做记录")
    @EasyLog(module = "App-学员签到", type = OperateType.ADD, bizNo = "{{#id}}", success = "", fail = "{{#_errMsg}}")
    @PostMapping("/students/{id}/sign_in")
    public ResponseData signIn(@PathVariable Long id) {
        return ResponseData.ok();
    }

}
