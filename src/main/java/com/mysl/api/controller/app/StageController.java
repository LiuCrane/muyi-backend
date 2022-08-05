package com.mysl.api.controller.app;

import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.entity.dto.MediaApplyDTO;
import com.mysl.api.entity.dto.StageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/5
 */
@Api(tags = "学习阶段接口")
@RestController
@RequestMapping("/app/stages")
@Slf4j
public class StageController {

    @ApiOperation("查询学习阶段列表")
    @GetMapping
    public ResponseData<List<StageDTO>> list() {
        return ResponseData.ok();
    }

    @ApiOperation("申请学习阶段音视频")
    @PostMapping("/{id}/apply")
    public ResponseData apply(@PathVariable Long id, @RequestBody MediaApplyDTO dto) {
        return ResponseData.ok();
    }

}
