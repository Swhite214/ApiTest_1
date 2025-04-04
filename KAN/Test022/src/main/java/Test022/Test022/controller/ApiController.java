package Test022.Test022.controller;

import Test022.Test022.dto.ApiDto;
import Test022.Test022.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ApiService apiService;

    @Autowired
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    // API 호출 테스트 엔드포인트
    @GetMapping("/movie")
    public ApiDto fetchData() throws Exception {
        return apiService.fetchData();
    }
    @GetMapping("/actor/{movie_id}")
    public ApiDto fetchData2(@PathVariable String movie_id) throws Exception{
        return apiService.fetchData2(movie_id);
    }
    @GetMapping("/actor/{movie_id}/save")
    public String saveActors(@PathVariable String movie_id) throws  Exception{
        ApiDto apiDto = apiService.fetchData2(movie_id);
        apiService.saveData(apiDto);

        return "WOW";
    }
}

