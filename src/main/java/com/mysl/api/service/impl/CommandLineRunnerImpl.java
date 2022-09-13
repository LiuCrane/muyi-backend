package com.mysl.api.service.impl;

import com.mysl.api.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author Ivan Su
 * @date 2022/9/14
 */
@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    @Autowired
    AddressService addressService;

    @Override
    public void run(String... args) throws Exception {
        addressService.getAll();
    }
}
