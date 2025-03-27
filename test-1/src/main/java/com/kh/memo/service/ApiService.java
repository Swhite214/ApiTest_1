package com.kh.memo.service;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.memo.dto.ApiDto;
import com.kh.memo.entity.ApiResponseEntity;
import com.kh.memo.repository.ApiResponseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ApiService {
	private final ApiResponseRepository repository;
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;
	
	@Autowired
	public ApiService(ApiResponseRepository repository, ObjectMapper objectMapper) {
		this.repository = repository;
		this.restTemplate = new RestTemplate();
		this.objectMapper = objectMapper;
	}
	public List<ApiDto> fetchAndSaveData() throws Exception{
		String url = "https://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=1292a16846b3252f61d7d4f418306639&targetDt=20120101";
		String response = restTemplate.getForObject(url, String.class);
		
		List<ApiDto> movieList = convertJsonToDtoList(response);
		
		for(ApiDto dto : movieList) {
		ApiResponseEntity entity = new ApiResponseEntity();
		entity.setData(objectMapper.writeValueAsString(dto));
		repository.save(entity);
		}
		return movieList;
	}
	public List<ApiDto> getAllData() {
		return repository.findAll().stream()
				.map(entity -> convertJsonToDto(entity.getData()))
				.collect(Collectors.toList());
	}
	public Optional<ApiDto> getDataById(Long id){
		return repository.findById(id)
                .map(entity -> convertJsonToDto(entity.getData()));
	}
	public Optional<ApiDto> updateData(Long id, String newData) {
		 return repository.findById(id).map(entity -> {
	            entity.setData(newData);
	            repository.save(entity);
	            return convertJsonToDto(newData);
	        });
	}
	public boolean deleteData(Long id) {
	    if (repository.existsById(id)) {
	        repository.deleteById(id);
	        return true;
	    }
	    return false;
	}
    private List<ApiDto> convertJsonToDtoList(String json) {
        List<ApiDto> movieList = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode boxOfficeList = rootNode.path("boxOfficeResult").path("dailyBoxOfficeList");

            for (JsonNode node : boxOfficeList) {
                ApiDto dto = new ApiDto(
                    node.get("rank").asText(),
                    node.get("movieNm").asText(),
                    node.get("audiCnt").asText()
                );
                movieList.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movieList;
    }
    private ApiDto convertJsonToDto(String json) {
        try {
            return objectMapper.readValue(json, ApiDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiDto();
        }
    }
}
