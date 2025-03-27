package com.kh.memo.entity;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name="api_response")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String data;
}
