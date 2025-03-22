package src.danik.postservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import src.danik.postservice.controller.PostController;
import src.danik.postservice.dto.post.PostCreateDto;
import src.danik.postservice.dto.post.PostReadDto;
import src.danik.postservice.dto.post.PostUpdateDto;
import src.danik.postservice.service.PostService;

import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {
    private MockMvc mockMvc;

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(postController)
                .setValidator(validator)
                .build();

        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testThatReturnAllPostsIsSuccess() throws Exception {
        List<PostReadDto> posts = List.of(
                PostReadDto.builder().id(1L).content("Hello world").published(true).deleted(false).build(),
                PostReadDto.builder().id(2L).content("Hello tests").published(true).deleted(false).build()
        );

        when(postService.getAllPosts()).thenReturn(posts);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("Hello world"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].content").value("Hello tests"));

        verify(postService).getAllPosts();
    }
    @Test
    public void testThatCreatePostIsSuccessful() throws Exception {
        PostCreateDto postCreateDto = PostCreateDto.builder().content("Hello tests").userId(1L).build();
        PostReadDto postReadDto = PostReadDto.builder().id(1L).userId(2L).content("Hello tests").published(false).deleted(false).build();

        when(postService.createPost(any(PostCreateDto.class))).thenReturn(postReadDto);

        String postCreateJson = objectMapper.writeValueAsString(postCreateDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postCreateJson))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Hello tests"));

        verify(postService).createPost(any(PostCreateDto.class));
    }
    @Test
    public void testThatPublishPostIsSuccessful() throws Exception {
        PostReadDto publishedPostReadDto = generateBasicPostReadDto();
        when(postService.publishPost(any(Long.class))).thenReturn(publishedPostReadDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/posts/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Hello tests"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.published").value(true));

        verify(postService).publishPost(1L);
    }
    @Test
    public void testThatGetPostByIdIsSuccessful() throws Exception {
        PostReadDto publishedPostReadDto = generateBasicPostReadDto();
        when(postService.getPostReadDtoById(any(Long.class))).thenReturn(publishedPostReadDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Hello tests"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.published").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deleted").value(false));
        verify(postService).getPostReadDtoById(2L);
    }
    @Test
    public void testThatDeletePostByIdIsSuccessful() throws Exception {
        Long exampleIdForDelete = 1L;

        doNothing().when(postService).deletePostById(exampleIdForDelete);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/posts/1"))
                .andExpect(status().isNoContent());

        verify(postService, times(1)).deletePostById(exampleIdForDelete);
    }
    @Test
    public void testThatUpdatePostIsSuccessful() throws Exception {
        Long postId = 1L;

        PostUpdateDto postUpdateDto = PostUpdateDto.builder()
                .content("Hello tests2")
                .id(postId)
                .build();

        PostReadDto postReadDto = PostReadDto.builder()
                .id(postId)
                .userId(3L)
                .content("Hello tests2")
                .published(true)
                .deleted(false)
                .build();

        when(postService.updatePost(any(Long.class), any(PostUpdateDto.class))).thenReturn(postReadDto);

        String json = objectMapper.writeValueAsString(postUpdateDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(postId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(3L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Hello tests2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.published").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deleted").value(false));

        verify(postService, times(1)).updatePost(eq(postId), any(PostUpdateDto.class));
    }

    public PostReadDto generateBasicPostReadDto() {
        return PostReadDto.builder().id(1L).userId(2L).content("Hello tests").published(true).deleted(false).build();
    }
}
