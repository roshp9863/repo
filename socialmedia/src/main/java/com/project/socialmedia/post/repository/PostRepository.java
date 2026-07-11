package com.project.socialmedia.post.repository;

import com.project.socialmedia.post.model.Post;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface PostRepository extends CassandraRepository<Post, UUID> {
}
