package fr.esic.mastering.dto;

import lombok.Data;

@Data
public class AuthRequest {
	
	private String email;
	private String password;
}
