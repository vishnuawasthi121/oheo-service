package com.ogive.oheo.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ogive.oheo.dto.UserDTO;
import com.ogive.oheo.persistence.entities.UserInfo;
import com.ogive.oheo.persistence.repo.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public Long addUser(UserDTO userDTO) {
		UserInfo user = new UserInfo();
		// Object source, Object target
		BeanUtils.copyProperties(userDTO, user);
		userRepository.save(user);
		return user.getId();
	}

}
