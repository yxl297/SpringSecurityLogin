package org.dxc.SpringSecurity.service;

import org.dxc.SpringSecurity.dto.UserRegistrationDto;
import org.dxc.SpringSecurity.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
	User save(UserRegistrationDto userRegDto);
}
