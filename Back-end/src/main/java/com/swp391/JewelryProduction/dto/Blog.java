package com.swp391.JewelryProduction.dto;

import com.swp391.JewelryProduction.pojos.Staff;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Blog {
    private String id;
    private String title;
    private Staff author;
    private List<ContentBlock> content = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ContentBlock {
        private String type;
        private String text;
        private String url;
        private String caption;
        private String altText;
    }
}
