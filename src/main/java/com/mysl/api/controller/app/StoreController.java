package com.mysl.api.controller.app;

import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.entity.dto.StoreDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ivan Su
 * @date 2022/8/4
 */
@Api(tags = "门店信息接口")
@RestController
@RequestMapping("/app/store")
@Slf4j
public class StoreController {

    @ApiOperation("查询门店信息")
    @GetMapping
    public ResponseData<StoreDTO> get() {
        StoreDTO dto = new StoreDTO();
        return ResponseData.ok(dto);
    }
}
