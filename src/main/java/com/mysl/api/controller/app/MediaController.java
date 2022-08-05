package com.mysl.api.controller.app;

import cn.hutool.core.collection.ListUtil;
import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.entity.dto.MediaBrowseRecordDTO;
import com.mysl.api.entity.dto.MediaDTO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * @author Ivan Su
 * @date 2022/7/28
 */
@Api(tags = "媒体接口")
@Slf4j
@RestController("appMediaController")
@RequestMapping("/app/media")
public class    MediaController {

    /**
     * 查询媒体列表
     *
     * @param offset
     * @param limit
     * @param type
     * @return
     */
    @ApiOperation("查询媒体列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "offset", value = "默认 0"),
            @ApiImplicitParam(name = "limit", value = "默认 20"),
            @ApiImplicitParam(name = "type", value = "媒体类型")
    })
    @GetMapping
    public ResponseData<List<MediaDTO>> list(@RequestParam(defaultValue = "0") int offset,
                                             @RequestParam(defaultValue = "20") int limit,
                                             @RequestParam String type) {
        log.info("get media list, offset: {}, limit: {}, type: {}", offset, limit, type);
        List<MediaDTO> list = ListUtil.of(
                MediaDTO.builder().id(1L).title("视力提高3行 就是这么神奇")
                        .type("aac")
                        .img("https://mysl.tianyuekeji.ltd/upload/img/1447033719652114433.png")
                        .description("视力提高")
                        .url("http://npcjvxut.test.com/sxpa")
                        .createdAt("2022-06-18 05:55:14").build(),
                MediaDTO.builder().title("告别近视必须看这一段")
                        .type("m3u8")
                        .img("https://mysl.tianyuekeji.ltd/upload/img/1435853275845902337.jpg")
                        .description("必看")
                        .url("http://rsvdq.mq/sdmcqwed")
                        .createdAt("2022-03-18 14:16:10").build()
        );
        return ResponseData.ok(list);
    }

    /**
     * 查询媒体详情
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id查询媒体详情")
    @GetMapping("{id}")
    public ResponseData<MediaDTO> get(@PathParam("id") Long id) {
        log.info("get media by id: {}", id);
        MediaDTO dto = MediaDTO.builder().id(2L).title("视力提高3行 就是这么神奇")
                .type("aac")
                .img("https://mysl.tianyuekeji.ltd/upload/img/1447033719652114433.png")
                .description("视力提高")
                .url("http://npcjvxut.test.com/sxpa")
                .createdAt("2022-06-18 05:55:14").build();
        return ResponseData.ok(dto);
    }

    /**
     * 查询媒体浏览记录
     *
     * @param offset
     * @param limit
     * @return
     */
    @ApiOperation("查询媒体浏览记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "offset", value = "默认 0"),
            @ApiImplicitParam(name = "limit", value = "默认 20")
    })
    @GetMapping("/browse_records")
    public ResponseData<List<MediaBrowseRecordDTO>> getRecords(@ApiParam(defaultValue = "0") @RequestParam(defaultValue = "0") int offset,
                                                               @ApiParam(defaultValue = "20") @RequestParam(defaultValue = "20") int limit) {
        log.info("get media browse records, offset: {}, limit: {}", offset, limit);
        List<MediaBrowseRecordDTO> list = ListUtil.of(
                MediaBrowseRecordDTO.builder().id(1L)
                        .title("家长与孩子必知的真相").type("aac").description("视力自查")
                        .startTime("2021-11-23 13:09:08").endTime("2021-11-23 14:09:08")
                        .time("60分").location("山东省XX市").build(),
                MediaBrowseRecordDTO.builder().id(2L)
                        .title("视力改善的前提").type("acc").description("放松音频")
                        .startTime("2022-05-29 09:18:37").endTime("2022-05-28 10:18:40")
                        .time("1天1时0分3秒").location("山东省XX市").build()
        );
        return ResponseData.ok(list);
    }

}
