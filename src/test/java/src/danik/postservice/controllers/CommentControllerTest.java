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
import src.danik.postservice.controller.CommentController;
import src.danik.postservice.dto.comment.CommentCreateDto;
import src.danik.postservice.dto.comment.CommentReadDto;
import src.danik.postservice.dto.comment.CommentUpdateDto;
import src.danik.postservice.service.CommentService;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .setValidator(validator)
                .build();

        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testThatFindAllCommentsByPostIdIsSuccessful() throws Exception {
        List<CommentReadDto> comments = List.of(
                CommentReadDto.builder().content("Hello comment test 1").userId(1L).postId(2L).commentId(3L).build(),
                CommentReadDto.builder().content("Hello comment test 2").userId(2L).postId(2L).commentId(4L).build()
        );
        when(commentService.getCommentsByPostId(2L)).thenReturn(comments);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/comments/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("Hello comment test 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].postId").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].commentId").value("3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].content").value("Hello comment test 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].userId").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].postId").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].commentId").value("4"));

        verify(commentService).getCommentsByPostId(2L);
    }

    @Test
    public void testThatCreateCommentIsCreated() throws Exception {
        CommentReadDto answer = createExampleCommentReadDto();
        CommentCreateDto commentCreateDtoBody = CommentCreateDto.builder().postId(2L).userId(1L).content("Hello comment test").build();

        String json = objectMapper.writeValueAsString(commentCreateDtoBody);

        when(commentService.createComment(any(CommentCreateDto.class))).thenReturn(answer);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postId").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentId").value(3L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Hello comment test"));

        verify(commentService).createComment(any(CommentCreateDto.class));
    }

    @Test
    public void testThatDeleteCommentIsNoContent() throws Exception {
        Long exampleId = 2L;
        doNothing().when(commentService).deleteComment(exampleId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/comments/" + exampleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(commentService).deleteComment(exampleId);
    }

    @Test
    public void testThatUpdateCommentIsSuccessful() throws Exception {
        CommentReadDto answer = createExampleCommentReadDto();
        CommentUpdateDto commentUpdateDtoBody = CommentUpdateDto.builder().commentId(3L).content("Hello comment test").build();

        when(commentService.updateComment(any(CommentUpdateDto.class))).thenReturn(answer);

        String json = objectMapper.writeValueAsString(commentUpdateDtoBody);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postId").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentId").value(3L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Hello comment test"));


        verify(commentService).updateComment(any(CommentUpdateDto.class));
    }

    private CommentReadDto createExampleCommentReadDto() {
        return CommentReadDto.builder().content("Hello comment test").userId(1L).postId(2L).commentId(3L).build();
    }
}
