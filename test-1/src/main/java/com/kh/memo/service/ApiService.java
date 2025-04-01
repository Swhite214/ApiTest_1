package com.kh.memo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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
	    return repository.findAll().stream()  // 레포지토리에서 모든 데이터를 가져온 후
	            .map(entity -> convertJsonToDtoList(entity.getData()))  // JSON 데이터를 리스트로 변환
	            .flatMap(List::stream)  // 리스트를 평탄화하여 스트림 형태로 만듬
	            .collect(Collectors.toList());  // 최종적으로 리스트로 수집
	}
	public Optional<ApiDto> getDataById(Long id) {
		System.out.println("조회할 ID: " + id); // 사용자가 입력한 ID 출력

		Optional<ApiResponseEntity> entityOptional = repository.findById(id);
		System.out.println("DB에서 찾은 데이터: " + entityOptional); // DB 조회 결과 출력

		return entityOptional.map(entity -> {
			System.out.println("저장된 데이터: " + entity.getData()); // 엔터티의 데이터 출력
			return convertJsonToDto(entity.getData());
		});
	}
	public Optional<ApiDto> updateData(Long id, String newData) {
		 return repository.findById(id).map(entity -> {
	            entity.setData(newData);
	            repository.save(entity);
	            return convertJsonToDto(newData);
	        });
	} // 수정
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
			JsonNode boxOfficeResult = rootNode.path("boxOfficeResult");
			JsonNode boxOfficeList = boxOfficeResult.path("dailyBoxOfficeList");

			String showRange = boxOfficeResult.path("showRange").asText();
			String showDate = showRange.split("~")[0];

            for (JsonNode node : boxOfficeList) {
                ApiDto dto = new ApiDto(
                    node.get("rank").asText(),
                    node.get("movieNm").asText(),
                    node.get("audiCnt").asText(),
						showDate
                );
				System.out.println("영화 제목: " + dto.getMovieNm());
				System.out.println("순위: " + dto.getRank());
				System.out.println("관객 수: " + dto.getAudiCnt());
				System.out.println("상영 날짜: " + dto.getShowDate());
				System.out.println("----------------------------");
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
	public List<ApiDto> searchByKeyword(String keyword){
		if (keyword == null || keyword.trim().isEmpty()) {
			return new ArrayList<>(); // 빈 리스트 반환
		}

		return repository.findByDataContaining(keyword).stream()
				.map(entity -> convertJsonToDto(entity.getData()))
				.collect(Collectors.toList());
	}
	public List<ApiDto> getBoxOfficeByDate(String date){
		List<ApiResponseEntity> existingData = repository.findByShowDate(date);

		if(!existingData.isEmpty()){
			return existingData.stream()
					.map(entity -> convertJsonToDto(entity.getData()))
					.collect(Collectors.toList());
		}
		String url = "https://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=1292a16846b3252f61d7d4f418306639&targetDt=" + date;
		String response = restTemplate.getForObject(url, String.class);
		List<ApiDto> movieList = convertJsonToDtoList(response);

		for (ApiDto dto : movieList) {
			ApiResponseEntity entity = new ApiResponseEntity();
			entity.setShowDate(date); // 날짜 추가

			try {
				String jsonData = objectMapper.writeValueAsString(dto);
				entity.setData(jsonData);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				System.out.println("JSON 변환 오류 발생! dto 내용: " + dto);
				continue;  // 오류 발생 시 해당 객체는 저장하지 않고 다음 데이터로 진행
			}

			repository.save(entity);
		}
		return movieList;
	}
}
