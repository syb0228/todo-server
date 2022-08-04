package org.example.service;

import org.example.model.TodoModel;
import org.example.model.TodoRequest;
import org.example.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class) // 외부 시스템에 의존하지 않고 자체 테스트를 실행하기 위해 Mock 객체 사용
class TodoServiceTest {

    @Mock // Mock 객체 생성
    private TodoRepository todoRepository;

    @InjectMocks // Mock 객체를 주입하여 사용할 수 있도록 만든 객체
    private TodoService todoService;

    @Test // 테스트를 만드는 모듈 역할 (main 메소드처럼 IDE로 직접 실행할 수 있는 메소드가 됨)
    void add() {
        when(this.todoRepository.save(any(TodoModel.class)))
                .then(AdditionalAnswers.returnsFirstArg()); // 받은 entity 값 반환

        TodoRequest expected = new TodoRequest();
        expected.setTitle("Test Title"); // 값을 넣어줌

        TodoModel actual = this.todoService.add(expected); // todoService에 request를 보냄

        assertEquals(expected.getTitle(), actual.getTitle()); // 넣은 값과 실제로 받은 값이 일치하면 테스트 성공
    }

    @Test
    void searchById() {
        TodoModel entity = new TodoModel();
        entity.setId(123L);
        entity.setTitle("TITLE");
        entity.setOrder(0L);
        entity.setCompleted(false);
        Optional<TodoModel> optional = Optional.of(entity); // Optional로 mapping

        given(this.todoRepository.findById(anyLong())) // 어떤 id 값이 주어졌을 때
                .willReturn(optional); // optinal 값을 return

        TodoModel actual = this.todoService.searchById(123L); // 실제 값

        TodoModel expected = optional.get(); // optional로 넣어준 값

        // 넣어준 값과 실제 값이 일치하면 테스트 성공
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getOrder(), actual.getOrder());
        assertEquals(expected.getCompleted(), actual.getCompleted());
    }

    @Test
    public void searchByIdFailed(){ // 존재하지 않는 값을 조회할 때 에러가 잘 발생하는지 확인
        given(this.todoRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            this.todoService.searchById(123L);
        });
    }
}