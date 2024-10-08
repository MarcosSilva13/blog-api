package com.blog.domain.services;

import com.blog.api.dtos.PageResponseDTO;
import com.blog.api.dtos.postdto.PostRequestDTO;
import com.blog.api.dtos.postdto.PostResponseDTO;
import com.blog.domain.entities.Post;
import com.blog.domain.entities.User;
import com.blog.api.mappers.PostMapper;
import com.blog.api.filters.PostQueryFilter;
import com.blog.domain.repositories.PostRepository;
import com.blog.domain.util.Util;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    private static final int PAGE_SIZE = 10;

    public PostService(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    public PageResponseDTO getAll(int page, PostQueryFilter postQueryFilter) {
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        Page<Post> postPaged = postRepository.findAll(postQueryFilter.toSpecification(), pageRequest);

        int totalPages = postPaged.getTotalPages();
        List<PostResponseDTO> postResponseDTO = postPaged.getContent().stream().map(PostResponseDTO::new).toList();

        return new PageResponseDTO(postResponseDTO, totalPages);
    }

    @Transactional
    public PostResponseDTO createPost(PostRequestDTO requestDTO) {
        Post post = postMapper.toPostEntity(requestDTO);
        User user = Util.getCurrentUser();

        LocalDateTime now = LocalDateTime.now();
        post.setCreatedAt(now);
        post.setUpdatedAt(now);
        post.setUser(user);

        return new PostResponseDTO(postRepository.save(post));
    }

    @Transactional
    public PostResponseDTO updatePost(Long postId, PostRequestDTO requestDTO) {
        Post post = getPostById(postId);

        postMapper.toUpdatePost(requestDTO, post);
        post.setUpdatedAt(LocalDateTime.now());

        return new PostResponseDTO(postRepository.save(post));
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = getPostById(postId);
        postRepository.delete(post);
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post não encontrado."));
    }
}
