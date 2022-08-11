package com.mysl.api.config.security;

import cn.hutool.core.collection.ListUtil;
import com.mysl.api.entity.Role;
import com.mysl.api.entity.User;
import com.mysl.api.service.RoleService;
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
//@CacheConfig( cacheNames = GlobalCacheConstant.USER_DETAILS_SERVICE_NAMESPACE )
public class JwtUserDetailsService implements UserDetailsService {

    @Lazy
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

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
        // 虽然说可以对SuperAdmin和Root直接放行,但在程序上还是应该让他们有归属,该有的角色和权限信息还是得有
        user.setRoles(roleService.listByUserId(user.getId()));
//		final List< RolePermissionResource > rolePermissionResources = rolePermissionResourceService.listByUserId( user.getId() );
//		final List< PermissionResourceVO >   permissionResource      = permissionResourceService.listUserPermissionByRolePermissionResource(
//			rolePermissionResources );
//        Role role = new Role();
//        role.setCode("ROLE_STORE_MANAGER");
//        List<Role> roles = ListUtil.of(role);
//        user.setRoles(roles);
        return new JwtUserDetails(user);
    }


}










