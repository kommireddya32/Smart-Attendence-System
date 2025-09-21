package com.san.sas.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class AdminServent {
	@Autowired
	AdminRepo repo;
	public boolean isAdmin(String emailId, String password) {
		Admin admin = repo.findById(emailId).orElse(null);
		if(admin!=null) {
			if(admin.getPassword().equals(password)) return true;
		}
		
		return false;
	}
}
