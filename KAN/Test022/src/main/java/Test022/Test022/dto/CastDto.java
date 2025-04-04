package Test022.Test022.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CastDto {
    private boolean adult;
    private int gender;
    private Long id;
    private String knownForDepartment;
    private String name;
    private String originalName;
    private Double popularity;
    private String profilePath;
    private int castId;
    private String character;
    private String creditId;
    private int order;
}
