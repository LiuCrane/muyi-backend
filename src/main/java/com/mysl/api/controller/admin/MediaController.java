package com.mysl.api.controller.admin;

import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.entity.dto.MediaDTO;
import com.mysl.api.entity.dto.MediaEditDTO;
import com.mysl.api.entity.enums.MediaType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
@Api(tags = "媒体接口")
@RestController("adminMediaController")
@RequestMapping("/admin/media")
public class MediaController {

    @ApiOperation("查询媒体列表")
    @GetMapping
    public ResponseData<List<MediaDTO>> list(Integer offset, Integer limit, MediaType type) {
        return ResponseData.ok();
    }

    @ApiOperation("添加媒体")
    @PostMapping
    public ResponseData create(@Validated @RequestBody MediaEditDTO dto) {
        return ResponseData.ok();
    }

    @ApiOperation("修改媒体")
    @PutMapping("/{id}")
    public ResponseData update(@PathVariable Long id, @Validated @RequestBody MediaEditDTO dto) {
        return ResponseData.ok();
    }

    @ApiOperation("删除媒体")
    @DeleteMapping("/{id}")
    public ResponseData delete(@PathVariable Long id) {
        return ResponseData.ok();
    }
}
