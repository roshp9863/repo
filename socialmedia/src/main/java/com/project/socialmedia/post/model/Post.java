package com.project.socialmedia.post.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table("posts")
@Data
@EqualsAndHashCode(of = "postId")
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @PrimaryKey
    @Column("post_id")
    private UUID postId;

    @Column("user_id")
    private UUID userId;

    @Column("caption")
    private String caption;

    @Column("create_time")
    private Instant createTime;

}