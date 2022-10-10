package com.mysl.api.controller.admin;

import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageInfo;
import com.mysl.api.common.OperateType;
import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.config.security.JwtTokenUtil;
import com.mysl.api.entity.dto.*;
import com.mysl.api.entity.enums.PlayerEvent;
import com.mysl.api.service.MediaBrowseRecordService;
import com.mysl.api.service.MediaService;
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

import java.util.Date;
import java.util.List;


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
    @Autowired
    MediaBrowseRecordService browseRecordService;

    @ApiOperation("查询媒体列表")
    @EasyLog(module = "Admin-查询媒体列表", tenant = "{getClientIP{0}}", type = OperateType.SELECT, success = "", fail = "{{#_errMsg}}")
    @GetMapping
    public ResponseData<PageInfo<MediaFullDTO>> list(@ApiParam(value = "页数，默认 1")
                                                     @RequestParam(name = "page_num", defaultValue = "1", required = false) Integer pageNum,
                                                     @ApiParam(value = "每页记录，默认 20")
                                                     @RequestParam(name = "page_size", defaultValue = "20", required = false) Integer pageSize,
                                                     @ApiParam(value = "搜索关键字")
                                                     @RequestParam(name = "key_word", required = false) String keyWord) {
        log.info("search media, pageNum: {}, pageSize: {}, keyWord: {}", pageNum, pageSize, keyWord);
        return ResponseData.ok(mediaService.getMediaList(pageNum, pageSize, keyWord));
    }

    @ApiOperation("查询媒体详情")
    @EasyLog(module = "Admin-查询媒体详情", tenant = "{getClientIP{0}}", type = OperateType.SELECT, bizNo = "{{#id}}", success = "", fail = "{{#_errMsg}}")
    @GetMapping("/{id}")
    public ResponseData<MediaFullDTO> get(@PathVariable Long id) {
        return ResponseData.ok(mediaService.getMediaById(id));
    }

    @ApiOperation("添加媒体")
    @EasyLog(module = "Admin-添加媒体", tenant = "{getClientIP{0}}", type = OperateType.ADD, success = "", fail = "{{#_errMsg}}", detail = "{{#dto.toString()}}")
    @PostMapping
    public ResponseData create(@Validated @RequestBody MediaEditDTO dto) {
        return ResponseData.ok(mediaService.save(dto));
    }

    @ApiOperation("修改媒体")
    @EasyLog(module = "Admin-修改媒体", tenant = "{getClientIP{0}}", type = OperateType.UPDATE, bizNo = "{{#id}}", success = "", fail = "{{#_errMsg}}", detail = "{{#dto.toString()}}")
    @PutMapping("/{id}")
    public ResponseData update(@PathVariable Long id, @Validated @RequestBody MediaEditDTO dto) {
        return ResponseData.ok(mediaService.update(id, dto));
    }

    @ApiOperation("删除媒体")
    @EasyLog(module = "Admin-删除媒体", tenant = "{getClientIP{0}}", type = OperateType.DELETE, bizNo = "{{#id}}", success = "", fail = "{{#_errMsg}}")
    @DeleteMapping("/{id}")
    public ResponseData delete(@PathVariable Long id) {
        mediaService.remove(id);
        return ResponseData.ok();
    }

    @ApiOperation("查询浏览记录")
    @EasyLog(module = "Admin-查询浏览记录", tenant = "{getClientIP{0}}", type = OperateType.SELECT, success = "", fail = "{{#_errMsg}}")
    @GetMapping("/browse_records")
    public ResponseData<PageInfo<MediaBrowseRecordDTO>> getBrowseRecords(@ApiParam(value = "页数，默认 1")
                                                                         @RequestParam(name = "page_num", defaultValue = "1", required = false) Integer pageNum,
                                                                         @ApiParam(value = "每页记录，默认 20")
                                                                         @RequestParam(name = "page_size", defaultValue = "20", required = false) Integer pageSize,
                                                                         @ApiParam(value = "用户id")
                                                                         @RequestParam(value = "user_id", required = false) Long userId,
                                                                         @ApiParam(value = "起始时间，格式：yyyy-MM-dd HH:mm:ss")
                                                                         @RequestParam(value = "start_time", required = false) String startTime,
                                                                         @ApiParam(value = "结束时间，格式：yyyy-MM-dd HH:mm:ss")
                                                                         @RequestParam(value = "end_time", required = false) String endTime) {
        log.info("get browse records, pageNum: {}, pageSize:{}, userId: {}, startTime: {}, endTime: {}", pageNum, pageSize, userId, startTime, endTime);
        Date start, end;
        try {
            start = DateUtil.parse(startTime);
            end = DateUtil.parse(endTime);
        } catch (DateException e) {
            log.info("date parse error: ", e);
            return ResponseData.generator(HttpStatus.BAD_REQUEST.value(), "时间格式错误", null);
        }
        return ResponseData.ok(browseRecordService.getRecords(pageNum, pageSize, userId, start, end));
    }

    @ApiOperation("查询媒体分类")
    @EasyLog(module = "Admin-查询媒体分类", tenant = "{getClientIP{0}}", type = OperateType.SELECT, success = "", fail = "{{#_errMsg}}")
    @GetMapping("/categories")
    public ResponseData<List<MediaCategoryDTO>> getCategories() {
        return ResponseData.ok(mediaService.getCategories());
    }

    @ApiOperation(value = "记录媒体播放操作")
    @EasyLog(module = "Admin-记录媒体播放操作", tenant = "{getClientIP{0}}", type = OperateType.ADD, bizNo = "{{#id}}", success = "", fail = "{{#_errMsg}}")
    @PostMapping("/{id}/player/{event}")
    public ResponseData savePlayerEvent(@ApiParam("媒体id") @PathVariable Long id, @ApiParam("播放事件(START/PAUSE/END)") @PathVariable PlayerEvent event) {
        log.info("admin media {} player event: {}", id, event);
        mediaService.savePlayerEvent(id, new PlayerEventDTO().setEvent(event), JwtTokenUtil.getCurrentStoreId(), JwtTokenUtil.getCurrentUserId());
        return ResponseData.ok();
    }
}
