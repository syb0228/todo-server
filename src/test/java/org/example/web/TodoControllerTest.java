package org.example.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.TodoModel;
import org.example.model.TodoRequest;
import org.example.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class) // 컨트롤러가 예상대로 동작하는지 테스트
class TodoControllerTest {

    @Autowired // 등록한 mock 객체를 주입 받아서 의존성 해결
    MockMvc mvc;

    @MockBean // mock 객체를 spring context에 등록
    TodoService todoService;

    private TodoModel expected;

    @BeforeEach // 해당 annotation이 달린 메서드가 각 test method 전에 실행
    void setup(){ // 각 test method 실행 전마다 expected 값을 초기화
        this.expected = new TodoModel();
        this.expected.setId(123L);
        this.expected.setTitle("TEST TITLE");
        this.expected.setCompleted(false);
    }

    @Test
    void create() throws Exception{
        // 받은 request를 기반으로 TodoEntity를 생성
        when(this.todoService.add(any(TodoRequest.class)))
                .then((i) -> {
                    TodoRequest request = i.getArgument(0, TodoRequest.class);
                    return new TodoModel(this.expected.getId(),
                                        request.getTitle(), // title만 request로 들어온 값을 반환해줌
                                        this.expected.getOrder(),
                                        this.expected.getCompleted());
                });

        TodoRequest request = new TodoRequest();
        request.setTitle("ANY TITLE");

        // request를 string 형태로 변환
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(request);

        // 요청 전송
        this.mvc.perform(post("/") // HTTP 메소드를 post로 결정(인자로는 기본 경로를 보냄)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                        .andExpect(status().isOk()) // 응답을 검증 -> 상태 코드 (200)
                        .andExpect(jsonPath("$.title").value("ANY TITLE")); // title에 지정한 값이 존재하는지
    }

    @Test
    void readOne() {
    }
}