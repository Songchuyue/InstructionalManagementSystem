package cn.edu.jlu.service.impl;

import cn.edu.jlu.dto.AdminDTO;
import cn.edu.jlu.entity.Admin;
import cn.edu.jlu.repository.AdminRepository;
import cn.edu.jlu.service.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
	private final AdminRepository adminRepository;

	public AdminServiceImpl(AdminRepository adminRepository) {
		this.adminRepository = adminRepository;
	}

	@Override
	public Admin validateLogin(AdminDTO adminDTO) {
		Admin admin = adminRepository.findById(adminDTO.getAdminId()).orElse(null);
		if (admin == null || !admin.getPassword().equals(adminDTO.getPassword())) {
			return null;
		}
		return admin;
	}
}
