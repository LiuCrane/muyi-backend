package com.mysl.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @author Ivan Su
 * @date 2022/7/28
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaDTO implements Serializable {

    private static final long serialVersionUID = -6274958250092964315L;

    private Long id;
    private String title;
    private String type;
    private String img;
    private String description;
    private String url;
    @JsonProperty("created_at")
    private String createdAt;

}
