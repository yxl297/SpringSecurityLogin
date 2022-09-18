package org.dxc.SpringSecurity.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.dxc.SpringSecurity.dto.UserRegistrationDto;
import org.dxc.SpringSecurity.entity.Role;
import org.dxc.SpringSecurity.entity.User;
import org.dxc.SpringSecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
	
	private UserRepository userRepository;
	
	@Lazy
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public UserServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public User save(UserRegistrationDto userRegDto) {
		User user = new User(userRegDto.getFirstName(),
							userRegDto.getLastName(),
							userRegDto.getEmail(),
							passwordEncoder.encode(userRegDto.getPassword()), 
							Arrays.asList(new Role("ROLE_USER")));
		
		return userRepository.save(user);
		
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException("Invalid username or password");
		}
		
		return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(), mapRolesToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}
}
