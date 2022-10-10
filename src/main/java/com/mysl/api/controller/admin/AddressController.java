package com.mysl.api.controller.admin;

import com.mysl.api.common.lang.ResponseData;
import com.mysl.api.entity.dto.AddressDTO;
import com.mysl.api.entity.dto.ListData;
import com.mysl.api.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Ivan Su
 * @date 2022/9/12
 */
@Api(tags = "地区数据接口")
@RestController("adminAddressController")
@RequestMapping("/admin/address")
public class AddressController {

    @Autowired
    AddressService addressService;

    @ApiOperation("查询所有地区数据")
    @GetMapping("/all")
    public ResponseData<ListData<AddressDTO>> getAll() {
        return ResponseData.ok(new ListData<AddressDTO>().setList(addressService.getAll()));
    }

    @ApiOperation("根据父级id查询子级地区")
    @GetMapping("/children")
    public ResponseData<ListData<AddressDTO>> getByParentId(@ApiParam("父级id")
                                                        @RequestParam(value = "pid", required = false, defaultValue = "0")
                                                        Long parentId) {
        return ResponseData.ok(new ListData<AddressDTO>().setList(addressService.getByParentId(parentId)));
    }

    @ApiOperation("根据子级id查询父级地区")
    @GetMapping("/parent")
    public ResponseData<AddressDTO> getByChildrenId(@ApiParam("子级id")
                                                    @RequestParam(value = "cid", required = true) Long childId) {
        return ResponseData.ok(addressService.getByChildId(childId));
    }

}
