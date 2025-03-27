// VideoRequestDTO.java
package com.omnedu.videoservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoRequestDTO {
    private String title;
    private String url;
}