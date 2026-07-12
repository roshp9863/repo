package com.project.socialmedia.post.controller;

import com.project.socialmedia.post.dto.PostCreateDTO;
import com.project.socialmedia.post.model.Post;
import com.project.socialmedia.post.repository.PostRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("post/")
public class PostController {

    private final PostRepository postRepository;

    PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("{id}")
    public ResponseEntity<Post> byId(@PathVariable UUID id) {
        Optional<Post> post = postRepository.findById(id);
        return post
                .map(ResponseEntity::ok)                 // found → 200 + body
                .orElse(ResponseEntity.notFound().build()); // missing → 404, empty body
    }

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody PostCreateDTO postCreateDTO) {
        Post post = new Post();
        post.setCaption(postCreateDTO.getCaption());
        post.setUserId(postCreateDTO.getUserId());
        post.setPostId(UUID.randomUUID());
        post.setCreateTime(Instant.now());
        postRepository.save(post);

        URI location = URI.create("/post/" + post.getPostId());
        return ResponseEntity.created(location).body(post);
    }

}
