package com.mysl.api.controller.admin;

import com.github.pagehelper.PageInfo;
import com.mysl.api.common.OperateType;
import com.mysl.api.common.exception.ResourceNotFoundException;
import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.config.param.ParameterConvert;
import com.mysl.api.entity.Media;
import com.mysl.api.entity.dto.MediaEditDTO;
import com.mysl.api.entity.dto.MediaFullDTO;
import com.mysl.api.entity.dto.MediaSearchDTO;
import com.mysl.api.service.MediaService;
import io.github.flyhero.easylog.annotation.EasyLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author Ivan Su
 * @date 2022/8/13
 */
@Api(tags = "媒体管理接口")
@RestController("adminMediaController")
@RequestMapping("/admin/media")
@Secured("ROLE_ADMIN")
@Slf4j
public class MediaController {

    @Autowired
    MediaService mediaService;

    @ApiOperation("查询媒体列表")
    @EasyLog(module = "Admin-查询媒体列表", type = OperateType.SELECT, success = "", fail = "{{#_errMsg}}", detail = "request: {{#dto.toString()}}")
    @GetMapping
    public ResponseData<PageInfo<MediaFullDTO>> list(@ParameterConvert MediaSearchDTO dto) {
        log.info("search media: {}", dto);
        return ResponseData.ok(mediaService.getMediaList(dto));
    }

    @ApiOperation("查询媒体详情")
    @EasyLog(module = "Admin-查询媒体详情", type = OperateType.SELECT, bizNo = "{{#id}}", success = "", fail = "{{#_errMsg}}")
    @GetMapping("/{id}")
    public ResponseData<MediaFullDTO> get(@PathVariable Long id) {
        return ResponseData.ok(mediaService.getMediaById(id));
    }

    @ApiOperation("添加媒体")
    @EasyLog(module = "Admin-添加媒体", type = OperateType.ADD, success = "", fail = "{{#_errMsg}}", detail = "{{#dto.toString()}}")
    @PostMapping
    public ResponseData create(@Validated @RequestBody MediaEditDTO dto) {
        Media media = new Media();
        BeanUtils.copyProperties(dto, media);
        mediaService.save(media);
        return ResponseData.ok();
    }

    @ApiOperation("修改媒体")
    @EasyLog(module = "Admin-修改媒体", type = OperateType.UPDATE, bizNo = "{{#id}}", success = "", fail = "{{#_errMsg}}", detail = "{{#dto.toString()}}")
    @PutMapping("/{id}")
    public ResponseData update(@PathVariable Long id, @Validated @RequestBody MediaEditDTO dto) {
        Media media = mediaService.getById(id);
        if (media == null) {
            throw new ResourceNotFoundException("找不到媒体");
        }
        BeanUtils.copyProperties(dto, media);
        mediaService.updateById(media);
        return ResponseData.ok();
    }

    @ApiOperation("删除媒体")
    @EasyLog(module = "Admin-删除媒体", type = OperateType.DELETE, bizNo = "{{#id}}", success = "", fail = "{{#_errMsg}}")
    @DeleteMapping("/{id}")
    public ResponseData delete(@PathVariable Long id) {
        mediaService.remove(id);
        return ResponseData.ok();
    }
}
