package src.danik.postservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import src.danik.postservice.dto.LikeDto;
import src.danik.postservice.entity.Like;
import src.danik.postservice.service.LikeService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LikeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LikeService likeService;

    private final ObjectMapper objectMapper = new ObjectMapper();

//    @Test
//    public void testUserLikePost() throws Exception {
//        LikeDto likeDto = createExampleLikeDto();
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/likes/post/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isNotFound())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404));
//    }

    public static LikeDto createExampleLikeDto() {
        return LikeDto.builder().likeId(5L).build();
    }
}
