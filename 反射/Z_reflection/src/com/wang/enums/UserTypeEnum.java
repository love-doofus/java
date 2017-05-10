package com.wang.enums;

import java.io.Serializable;


/**
 * @author wxe
 * @since 1.0.0
 */
public enum UserTypeEnum implements Serializable{

	BORROWER("借款者"),
	INVESTER("投资者");

	private final String key;

	private UserTypeEnum(String key) {
		this.key = key;
	}
	
	@SuppressWarnings("unused")
	private String getKey(){
		return key;
	}

}
