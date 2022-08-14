package com.mysl.api.controller.admin;

import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.entity.dto.CourseCreateDTO;
import com.mysl.api.entity.dto.CourseFullDTO;
import com.mysl.api.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
@Api(tags = "课程接口")
@RestController
@RequestMapping("/admin/courses")
public class CourseController {

    @Autowired
    CourseService courseService;

    @ApiOperation("查询课程列表")
    @GetMapping
    public ResponseData<List<CourseFullDTO>> list(@RequestParam(required = false, defaultValue = "0") Integer offset,
                                                  @RequestParam(required = false, defaultValue = "20") Integer limit) {
        return ResponseData.ok();
    }

    @ApiOperation("添加课程")
    @PostMapping
    public ResponseData create(@Validated @RequestBody CourseCreateDTO dto) {
        courseService.save(dto);
        return ResponseData.ok();
    }

    @ApiOperation("修改课程")
    @PutMapping("/{id}")
    public ResponseData update(@PathVariable Long id, @RequestBody CourseCreateDTO dto) {
        return ResponseData.ok();
    }

    @ApiOperation("删除课程")
    @DeleteMapping("/{id}")
    public ResponseData delete(@PathVariable Long id) {
        return ResponseData.ok();
    }

}
