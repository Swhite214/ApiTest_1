package Test022.Test022.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "actor")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseEntity {
    @Id
    private Long actorId;
    private Long movieId;
    private boolean adult;
    private Integer gender;
    @Column(name = "`name`")
    private String name;
    private String originalName;
    private Double popularity;
    private String profilePath;
    private Integer castId;
    @Column(name = "`character`")
    private String character;
    private String creditId;
    @Column(name = "`order`")
    private Integer order;
}
