package src.danik.postservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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
import src.danik.postservice.controller.LikeController;
import src.danik.postservice.controller.PostController;
import src.danik.postservice.dto.LikeDto;
import src.danik.postservice.entity.Like;
import src.danik.postservice.service.LikeService;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LikeControllerTest {
    private MockMvc mockMvc;

    @Mock
    private LikeService likeService;

    @InjectMocks
    private LikeController likeController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnAllPosts() throws Exception {
        List<LikeDto> posts = List.of(new LikeDto(1L), new LikeDto(2L));
//
//        // Говорим, что при вызове getAllPosts(), сервис должен вернуть наш список
//        when(postService.getAllPosts()).thenReturn(posts);
//
//        // Выполняем GET-запрос к контроллеру
//        mockMvc.perform(get("/api/v1/posts")
//                        .contentType(MediaType.APPLICATION_JSON))
//                // Проверяем результат
//                .andExpect(status().isOk())  // HTTP 200 OK
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$", hasSize(2)))  // Массив из 2 элементов
//                .andExpect(jsonPath("$[0].id", is(1)))
//                .andExpect(jsonPath("$[0].title", is("Заголовок 1")))
//                .andExpect(jsonPath("$[1].id", is(2)))
//                .andExpect(jsonPath("$[1].title", is("Заголовок 2")));
//
//        // Проверяем, что метод сервиса был вызван
//        verify(postService).getAllPosts();
    }

    public static LikeDto createExampleLikeDto() {
        return LikeDto.builder().likeId(5L).build();
    }
}
