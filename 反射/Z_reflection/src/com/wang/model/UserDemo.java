package com.wang.model;

import com.wang.annotation.Comment;
import com.wang.enums.UserTypeEnum;

/**
 * @author wxe
 * 1.0.0
 */
public class UserDemo {
	
	private String name;
	
	private int age;
	
	@Comment
	private UserTypeEnum userType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public UserTypeEnum getUserType() {
		return userType;
	}

	public void setUserType(UserTypeEnum userType) {
		this.userType = userType;
	}

	public UserDemo(String name, int age, UserTypeEnum userType) {
		super();
		this.name = name;
		this.age = age;
		this.userType = userType;
	}

	
}
