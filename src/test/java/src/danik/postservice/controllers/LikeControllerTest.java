package src.danik.postservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import src.danik.postservice.controller.LikeController;
import src.danik.postservice.controller.PostController;
import src.danik.postservice.dto.LikeDto;
import src.danik.postservice.entity.Like;

import static org.mockito.ArgumentMatchers.eq;

import src.danik.postservice.repository.types.LikeType;
import src.danik.postservice.service.LikeService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class LikeControllerTest {
    private MockMvc mockMvc;

    @Mock
    private LikeService likeService;

    @InjectMocks
    private LikeController likeController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(likeController)
                .setValidator(validator)
                .build();

        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testThatUserLikePostIsCreated() throws Exception {
        LikeDto answer = createExampleLikeDto();
        LikeDto likeDtoBody = createExampleLikeDto();

        String json = objectMapper.writeValueAsString(likeDtoBody);

        when(likeService.userLike(any(LikeDto.class), eq(LikeType.POST))).thenReturn(answer);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/likes/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.likeId").value(5));

        verify(likeService).userLike(any(LikeDto.class), eq(LikeType.POST));
    }

    @Test
    public void testThatUserLikeCommentIsCreated() throws Exception {
        LikeDto answer = createExampleLikeDto();
        LikeDto likeDtoBody = createExampleLikeDto();

        String json = objectMapper.writeValueAsString(likeDtoBody);

        when(likeService.userLike(any(LikeDto.class), eq(LikeType.COMMENT))).thenReturn(answer);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/likes/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.likeId").value(5));

        verify(likeService).userLike(any(LikeDto.class), eq(LikeType.COMMENT));
    }

    @Test
    public void testThatUserRemoveLikeFromPostIsNoContent() throws Exception {
        LikeDto likeDtoBody = createExampleLikeDto();
        doNothing().when(likeService).removeLike(any(Long.class), eq(LikeType.POST), any(LikeDto.class));

        String json = objectMapper.writeValueAsString(likeDtoBody);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/likes/post/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent());

        verify(likeService).removeLike(any(Long.class), eq(LikeType.POST), any(LikeDto.class));
    }
    @Test
    public void testThatUserRemoveLikeFromCommentIsNoContent() throws Exception {
        LikeDto likeDtoBody = createExampleLikeDto();
        doNothing().when(likeService).removeLike(any(Long.class), eq(LikeType.COMMENT), any(LikeDto.class));

        String json = objectMapper.writeValueAsString(likeDtoBody);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/likes/comment/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent());

        verify(likeService).removeLike(any(Long.class), eq(LikeType.COMMENT), any(LikeDto.class));
    }

    public static LikeDto createExampleLikeDto() {
        return LikeDto.builder().likeId(5L).build();
    }
}
