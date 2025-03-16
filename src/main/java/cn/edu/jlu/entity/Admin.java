package cn.edu.jlu.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "admins")
@Data
public class Admin {
	@Id
	@Column(name = "admin_id", length = 20)
	private String adminId;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, length = 100)
	private String password;
}