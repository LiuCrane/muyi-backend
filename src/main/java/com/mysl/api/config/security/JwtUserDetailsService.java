package com.mysl.api.config.security;

import com.mysl.api.entity.Store;
import com.mysl.api.entity.User;
import com.mysl.api.service.RoleService;
import com.mysl.api.service.StoreService;
import com.mysl.api.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class JwtUserDetailsService implements UserDetailsService {

    @Lazy
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private StoreService storeService;

    @Cacheable(key = "#username", condition = "#username != null")
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isBlank(username)) {
            throw new UsernameNotFoundException(String.format("该'%s'用户名不存在.", username));
        }
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("该'%s'用户名不存在.", username));
        }
        user.setRoles(roleService.listByUserId(user.getId()));

        Long storeId = null;
        Store store = storeService.findByUserId(user.getId());
        if (store != null) {
            storeId = store.getId();
        }
        return new JwtUserDetails(user, storeId);
    }


}










