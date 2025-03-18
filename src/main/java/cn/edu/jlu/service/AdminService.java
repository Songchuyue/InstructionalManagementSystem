package cn.edu.jlu.service;

import cn.edu.jlu.dto.AdminDTO;
import cn.edu.jlu.entity.Admin;

public interface AdminService {
	Admin validateLogin(AdminDTO adminDTO);
}
