package com.blog.domain.services;

import com.blog.api.dtos.PageResponseDTO;
import com.blog.api.dtos.postdto.PostRequestDTO;
import com.blog.api.dtos.postdto.PostResponseDTO;
import com.blog.api.filters.PostQueryFilter;
import com.blog.api.mappers.PostMapper;
import com.blog.domain.entities.Post;
import com.blog.domain.entities.User;
import com.blog.domain.repositories.PostRepository;
import com.blog.domain.utils.TestUtils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostMapper postMapper;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    private Post post;

    private User user;

    @BeforeEach
    void setUp() {
        user = TestUtils.createDefaultUser();

        post = new Post();
        post.setPostId(1L);
        post.setTitle("Post de teste");
        post.setContent("Este é o primeiro post apenas para teste");
        post.setUser(user);
    }

    private void getAuthentication() {
        when(authentication.getPrincipal()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("getAll should return list of post paged when successful")
    void getAllShouldReturnListOfPostPagedWhenSuccessful() {
        int page = 0;
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Post> postPage = new PageImpl<>(Collections.singletonList(post));
        PostQueryFilter postQueryFilter = new PostQueryFilter("");

        when(postRepository.findAll(any(Specification.class), eq(pageRequest))).thenReturn(postPage);

        List<PostResponseDTO> postResponseDTOS = postPage.getContent().stream().map(PostResponseDTO::new).toList();
        PageResponseDTO expectedPageResponse = new PageResponseDTO(postResponseDTOS, 1);
        PageResponseDTO pageResponseDTO = postService.getAll(page, postQueryFilter);

        assertThat(pageResponseDTO).isNotNull().isEqualTo(expectedPageResponse);

        verify(postRepository).findAll(any(Specification.class), eq(pageRequest));
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    @DisplayName("createPost should create post when successful")
    void createPostShouldCreatePostWhenSuccessful() {
        getAuthentication();
        PostRequestDTO requestDTO = new PostRequestDTO("Post de teste", "Este é o primeiro post apenas para teste");
        PostResponseDTO responseDTO = new PostResponseDTO(post);

        when(postMapper.toPostEntity(requestDTO)).thenReturn(post);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        PostResponseDTO postResponseDTO = postService.createPost(requestDTO);

        assertThat(postResponseDTO).isNotNull();
        assertThat(postResponseDTO.title()).isEqualTo(responseDTO.title());
        assertThat(postResponseDTO.content()).isEqualTo(responseDTO.content());
        assertThat(postResponseDTO.createdAt()).isNotNull();
        assertThat(postResponseDTO.updatedAt()).isNotNull();
        assertThat(postResponseDTO.user()).isEqualTo(responseDTO.user());

        verify(postMapper).toPostEntity(requestDTO);
        verify(postRepository).save(any(Post.class));
        verifyNoMoreInteractions(postMapper, postRepository);
    }

    @Test
    @DisplayName("updatePost should update post when successful")
    void updatePostShouldUpdatePostWhenSuccessful() {
        Long postId = 1L;
        Post postUpdated = new Post();
        postUpdated.setPostId(1L);
        postUpdated.setTitle("Post alterado");
        postUpdated.setContent("Conteúdo alterado");
        postUpdated.setUpdatedAt(LocalDateTime.now());
        postUpdated.setUser(user);

        PostRequestDTO requestToUpdate = new PostRequestDTO("Post alterado", "Conteúdo alterado");
        PostResponseDTO expectedResponse = new PostResponseDTO(postUpdated);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        doNothing().when(postMapper).toUpdatePost(requestToUpdate, post);
        when(postRepository.save(any(Post.class))).thenReturn(postUpdated);

        PostResponseDTO postResponseDTO = postService.updatePost(postId, requestToUpdate);

        assertThat(postResponseDTO).isNotNull().isEqualTo(expectedResponse);

        verify(postMapper).toUpdatePost(requestToUpdate, post);
        verify(postRepository).findById(postId);
        verify(postRepository).save(any(Post.class));
        verifyNoMoreInteractions(postMapper, postRepository);
    }

    @Test
    @DisplayName("updatePost should throw EntityNotFoundException when post not found")
    void updatePostShouldThrowEntityNotFoundExceptionWhenPostNotFound() {
        Long postId = 10L;
        PostRequestDTO requestToUpdate = new PostRequestDTO("Post alterado", "Conteúdo alterado");
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> postService.updatePost(postId, requestToUpdate))
                .withMessage("Post não encontrado.");

        verify(postRepository).findById(postId);
        verifyNoInteractions(postMapper);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    @DisplayName("deletePost should delete post when successful")
    void deletePostShouldDeletePostWhenSuccessful() {
        Long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        doNothing().when(postRepository).delete(post);

        assertThatCode(() -> postService.deletePost(postId)).doesNotThrowAnyException();

        verify(postRepository).findById(postId);
        verify(postRepository).delete(post);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    @DisplayName("deletePost should throw EntityNotFoundException when post not found")
    void deletePostShouldThrowEntityNotFoundExceptionWhenPostNotFound() {
        Long postId = 10L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> postService.deletePost(postId))
                .withMessage("Post não encontrado.");

        verify(postRepository).findById(postId);
        verifyNoMoreInteractions(postRepository);
    }
}
