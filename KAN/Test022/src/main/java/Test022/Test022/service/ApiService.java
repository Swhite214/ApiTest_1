package Test022.Test022.service;

import Test022.Test022.dto.ApiDto;
import Test022.Test022.dto.CastDto;
import Test022.Test022.dto.CrewDto;
import Test022.Test022.entity.ApiResponseEntity;
import Test022.Test022.repository.ApiResponseRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApiService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ApiResponseRepository apiResponseRepository;

    public ApiService(ApiResponseRepository apiResponseRepository) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.apiResponseRepository = apiResponseRepository;;
    }

    public ApiDto fetchData() throws Exception {
        String url = "https://api.themoviedb.org/3/movie/496243/credits?language=ko";
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhZDg3Mjg2ZGE1Nzk2NGMwMTZiYzIyNmQ4ZWFiNWMyMyIsIm5iZiI6MTc0MzU4NDkyNy4xOCwic3ViIjoiNjdlY2ZlOWYwNjA3MjA3MDcwY2U1MDIzIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.X7cPR1PMvx0A2-PA9PRtljwENiHfk7UPRLJ2436A154");


        HttpEntity<String> entity = new HttpEntity<>(headers);


        ResponseEntity<ApiDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, ApiDto.class);

        return response.getBody();
    }
    public ApiDto fetchData2(String movie_id) throws Exception{
        String url = "https://api.themoviedb.org/3/movie/"+movie_id+"/credits?language=ko";
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhZDg3Mjg2ZGE1Nzk2NGMwMTZiYzIyNmQ4ZWFiNWMyMyIsIm5iZiI6MTc0MzU4NDkyNy4xOCwic3ViIjoiNjdlY2ZlOWYwNjA3MjA3MDcwY2U1MDIzIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.X7cPR1PMvx0A2-PA9PRtljwENiHfk7UPRLJ2436A154");


        HttpEntity<String> entity = new HttpEntity<>(headers);


        ResponseEntity<ApiDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, ApiDto.class);

        return response.getBody();

    }



    public void saveData(ApiDto apiDto){
        List<CastDto> castList = apiDto.getCast();

        for(CastDto cast : castList) {
            ApiResponseEntity are = new ApiResponseEntity();
            are.setActorId(cast.getId());
            are.setMovieId(apiDto.getId());
            are.setAdult(cast.isAdult());
            are.setGender(cast.getGender());
            are.setName(cast.getName());
            are.setOriginalName(cast.getOriginalName());
            are.setPopularity(cast.getPopularity());
            are.setProfilePath(cast.getProfilePath());
            are.setCastId(cast.getCastId());
            are.setCharacter(cast.getCharacter());
            are.setCreditId(cast.getCreditId());
            are.setOrder(cast.getOrder());

            apiResponseRepository.save(are);
        }
    }

}
