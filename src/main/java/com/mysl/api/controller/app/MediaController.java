package com.mysl.api.controller.app;

import com.github.pagehelper.PageInfo;
import com.mysl.api.common.OperateType;
import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.config.security.JwtTokenUtil;
import com.mysl.api.entity.dto.MediaDTO;
import com.mysl.api.entity.dto.PlayerEventDTO;
import com.mysl.api.entity.enums.MediaType;
import com.mysl.api.service.MediaService;
import io.github.flyhero.easylog.annotation.EasyLog;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ivan Su
 * @date 2022/7/28
 */
@Api(tags = "媒体接口")
@Slf4j
@RestController("appMediaController")
@RequestMapping("/app/media")
@Secured("ROLE_STORE_MANAGER")
public class MediaController {

    @Autowired
    MediaService mediaService;

    @ApiOperation("查询媒体列表（App首页导学媒体）")
    @EasyLog(module = "App-查询首页导学媒体", type = OperateType.SELECT, success = "", fail = "{{#_errMsg}}")
    @GetMapping
    public ResponseData<PageInfo<MediaDTO>> list(@ApiParam(value = "页数，默认 1")
                                                 @RequestParam(name = "page_num", defaultValue = "1", required = false) Integer pageNum,
                                                 @ApiParam(value = "每页记录，默认 5")
                                                 @RequestParam(name = "page_size", defaultValue = "5", required = false) Integer pageSize,
                                                 @ApiParam(value = "媒体类型(AUDIO:音频, VIDEO:视频)")
                                                 @RequestParam(required = false) MediaType type) {
        log.info("get app media list, page: {}, size: {}, type: {}", pageNum, pageSize, type);
        return ResponseData.ok(mediaService.getMediaList(pageNum, pageSize, null, type, Boolean.TRUE));
    }

//    @ApiOperation("根据id查询媒体详情")
//    @GetMapping("/{id}")
//    public ResponseData<MediaDTO> get(@PathParam("id") Long id) {
//        log.info("get media by id: {}", id);
//        MediaDTO dto = MediaDTO.builder().id(2L).title("视力提高3行 就是这么神奇")
//                .type("AUDIO")
//                .img("https://mysl.tianyuekeji.ltd/upload/img/1447033719652114433.png")
//                .description("视力提高")
//                .url("http://npcjvxut.test.com/sxpa")
//                .createdAt("2022-06-18 05:55:14").build();
//        return ResponseData.ok(dto);
//    }

    @ApiOperation(value = "记录媒体播放操作", notes = "后台记录播放事件，用于判断课程是否结束")
    @EasyLog(module = "App-记录媒体播放操作", type = OperateType.ADD, bizNo = "{{#id}}", success = "", fail = "{{#_errMsg}}", detail = "mediaId: {{#id}}, eventDTO: {{#dto.toString}}")
    @PostMapping("/{id}/player")
    public ResponseData savePlayerEvent(@ApiParam("媒体id") @PathVariable Long id,
                                        @Validated @RequestBody PlayerEventDTO dto) {
        log.info("media {} player event: {}", id, dto);
        mediaService.savePlayerEvent(id, dto, JwtTokenUtil.getCurrentStoreId());
        return ResponseData.ok();
    }

//    /**
//     * 查询媒体浏览记录
//     *
//     * @param offset
//     * @param limit
//     * @return
//     */
//    @ApiOperation("查询媒体浏览记录列表")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "offset", value = "默认 0"),
//            @ApiImplicitParam(name = "limit", value = "默认 20")
//    })
//    @GetMapping("/browse_records")
//    public ResponseData<List<MediaBrowseRecordDTO>> getRecords(@ApiParam(defaultValue = "0") @RequestParam(defaultValue = "0") int offset,
//                                                               @ApiParam(defaultValue = "20") @RequestParam(defaultValue = "20") int limit) {
//        log.info("get media browse records, offset: {}, limit: {}", offset, limit);
//        List<MediaBrowseRecordDTO> list = ListUtil.of(
//                MediaBrowseRecordDTO.builder().id(1L)
//                        .title("家长与孩子必知的真相").type("aac").description("视力自查")
//                        .startTime("2021-11-23 13:09:08").endTime("2021-11-23 14:09:08")
//                        .time("60分").location("山东省XX市").build(),
//                MediaBrowseRecordDTO.builder().id(2L)
//                        .title("视力改善的前提").type("acc").description("放松音频")
//                        .startTime("2022-05-29 09:18:37").endTime("2022-05-28 10:18:40")
//                        .time("1天1时0分3秒").location("山东省XX市").build()
//        );
//        return ResponseData.ok(list);
//    }


}
