package com.neo.web;

import java.util.List;

import com.neo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neo.entity.UserEntity;
import com.neo.mapper.UserMapper;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;

	@RequestMapping("/getUsers")
	public List<UserEntity> getUsers() {
		List<UserEntity> users=userService.getUsers();
		return users;
	}

    @RequestMapping("/getUser")
    public UserEntity getUser(Long id) {
    	UserEntity user=userService.getUser(id);
        return user;
    }

    @RequestMapping("/add")
    public void save(UserEntity user) {
        userService.save(user);
    }

    @RequestMapping(value="update")
    public void update(UserEntity user) {
        userService.update(user);
    }

    @RequestMapping(value="/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }

    
}