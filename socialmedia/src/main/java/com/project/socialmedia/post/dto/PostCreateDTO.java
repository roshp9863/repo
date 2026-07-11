package com.project.socialmedia.post.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateDTO {
    private String caption;
    private UUID userId;
}
