package com.kh.memo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kh.memo.dto.ApiDto;
import com.kh.memo.entity.ApiResponseEntity;
import com.kh.memo.service.ApiService;

@RestController
@RequestMapping("/api")

public class ApiController {
	private final ApiService apiService;
	
	public ApiController(ApiService apiService) {
		this.apiService = apiService;
	}
	@GetMapping("/fetch")
	public List<ApiDto> fetchData() throws Exception{
		return apiService.fetchAndSaveData();
	}
	@GetMapping("/all")
	public List<ApiDto> getAllData() {
	    return apiService.getAllData();
	}
	@GetMapping("/{id}")
	public ResponseEntity <ApiDto> getDataById(@PathVariable Long id) {
	    return apiService.getDataById(id)
	            .map(ResponseEntity::ok)
	            .orElse(ResponseEntity.notFound().build());
	}
	@PutMapping("/update/{id}")
	public ResponseEntity<ApiDto> updateData(@PathVariable Long id, @RequestBody String newData) {
	    return apiService.updateData(id, newData)
	            .map(ResponseEntity::ok)
	            .orElse(ResponseEntity.notFound().build());
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteData(@PathVariable Long id) {
	    if (apiService.deleteData(id)) {
	        return ResponseEntity.noContent().build();
	    }
	    return ResponseEntity.notFound().build();
	}
	@GetMapping("/search")
	public ResponseEntity<List<ApiDto>> searchMovies(@RequestParam(required = false, defaultValue = "") String keyword){
		List<ApiDto> results = apiService.searchByKeyword(keyword);

		if(results.isEmpty()){
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(results);
	}
	@GetMapping("/boxoffice")
	public ResponseEntity<List<ApiDto>> getBoxOfficeByDate(@RequestParam String date){
		List<ApiDto> results = apiService.getBoxOfficeByDate(date);

		if (results.isEmpty()){
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(results);
	}
}
