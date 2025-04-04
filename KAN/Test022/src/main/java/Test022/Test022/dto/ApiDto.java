package Test022.Test022.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class ApiDto {
    private Long id;
    private List<CastDto> cast;  // 배우 리스트
    private List<CrewDto> crew;
}
