package com.mysl.api.entity;

import com.mysl.api.entity.enums.MediaType;
import lombok.Data;

/**
 * @author Ivan Su
 * @date 2022/8/13
 */
@Data
public class Media extends BaseModel<Media> {
    private static final long serialVersionUID = -5960133452542949800L;

    private String title;
    private MediaType type;
    private String description;
    private String img;
    private String url;
    private Boolean publicly;
}
