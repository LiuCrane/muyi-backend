package com.mysl.api.controller.admin;

import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.common.util.CosUtil;
import com.mysl.api.entity.dto.UploadInfo;
import com.mysl.api.entity.enums.FileType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ivan Su
 * @date 2022/8/27
 */
@Api(tags = "文件接口")
@RestController
@RequestMapping("/admin/file")
@Secured("ROLE_ADMIN")
@Slf4j
public class FileController {

    @ApiOperation(value = "获取上传文件信息", notes = "用于后台文件直传COS")
    @GetMapping("/upload_info")
    public ResponseData<UploadInfo> getUploadInfo(@ApiParam(value = "文件类型", required = true)
                                     @RequestParam("file_type") FileType fileType,
                                                  @ApiParam(value = "文件名称，包含扩展名，例如：xxx.jpg")
                                     @RequestParam("filename") String filename) {
        UploadInfo info = CosUtil.generatePresignedUploadUrl(fileType, filename);
        log.info("upload info: {}", info);
        return ResponseData.ok(info);
    }
}
