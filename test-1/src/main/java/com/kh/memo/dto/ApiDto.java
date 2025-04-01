package com.kh.memo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class ApiDto {
	
	private String rank;
	private String movieNm;
	private String audiCnt;
	private String showDate;

}
