package com.mysl.api.controller.app;

import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.entity.dto.ClassDTO;
import com.mysl.api.entity.dto.CourseDTO;
import com.mysl.api.entity.dto.StudentSimpleDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/9
 */
@Api(tags = "班级课程接口")
@Slf4j
@RestController
@RequestMapping("/app/classes")
public class ClassController {

    @ApiOperation("查询班级列表")
    @GetMapping
    public ResponseData<List<ClassDTO>> list() {
        return ResponseData.ok();
    }

    @ApiOperation("查询班级详情")
    @GetMapping("/{id}")
    public ResponseData<ClassDTO> get(@PathVariable Long id) {
        return ResponseData.ok();
    }

    @ApiOperation("查询班级学员列表")
    @GetMapping("/{class_id}/students")
    public ResponseData<List<StudentSimpleDTO>> getStudents(@PathVariable("class_id") Long classId) {
        return ResponseData.ok();
    }

    @ApiOperation("查询班级课程列表")
    @GetMapping("/{class_id}/courses")
    public ResponseData<List<CourseDTO>> getCourses(@PathVariable("class_id") Long classId) {
        return ResponseData.ok();
    }

    @ApiOperation("申请课程")
    @PostMapping("/{class_id}/courses/{id}/apply")
    public ResponseData apply(@PathVariable("class_id") Long classId, @PathVariable Long id) {
        return ResponseData.ok();
    }
}
