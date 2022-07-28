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
public class MediaBrowseRecord implements Serializable {

    private static final long serialVersionUID = 6392420328965042009L;

    private Long id;
    private String title;
    private String type;
    private String description;
    @JsonProperty("start_time")
    private String startTime;
    @JsonProperty("end_time")
    private String endTime;
    private String time;
    private String location;

}
