package com.mysl.api.controller.app;

import cn.hutool.core.collection.ListUtil;
import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.entity.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
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
public class StudentController {

    @ApiOperation("查询学员列表")
    @GetMapping("/students")
    public ResponseData<List<StudentDTO>> list(@ApiParam(name = "offset", value = "默认 0", defaultValue = "0")
                             @RequestParam(defaultValue = "0") int offset,
                             @ApiParam(name = "limit", value = "默认 20", defaultValue = "20")
                             @RequestParam(defaultValue = "20") int limit) {
        List<StudentDTO> list = ListUtil.toList();
        return ResponseData.ok(list);
    }

    @ApiOperation("查询学员信息详情")
    @GetMapping("/students/{id}")
    public ResponseData<StudentDTO> get(@PathVariable Long id) {
        return ResponseData.ok(new StudentDTO());
    }

    @ApiOperation("提交学员信息")
    @PostMapping("/students")
    public ResponseData create(@RequestBody StudentCreateDTO dto) {
        log.info("create student dto: {}", dto);
        return ResponseData.ok();
    }

//    @ApiOperation("更新学习进度")
//    @PatchMapping("/student/stage")
//    public ResponseData updateStage(@RequestBody StageUpdateDTO dto) {
//        log.info("update learn stage, dto: {}", dto);
//        return ResponseData.ok();
//    }

    @ApiOperation("更新学员视力信息")
    @PatchMapping("/students/{id}/vision")
    public ResponseData updateStageVision(@PathVariable Long id, @RequestBody VisionUpdateDTO dto) {
        log.info("update student {} vision {}", id, dto);
        return ResponseData.ok();
    }

    @ApiOperation("查询学员学习阶段的音视频")
    @GetMapping("/students/{student_id}/stages/{stage_id}/media")
    public ResponseData<List<MediaDTO>> getMedia(@PathVariable("student_id") Long studentId,
                                                 @PathVariable("stage_id") Long stageId) {

        return ResponseData.ok();
    }


}
