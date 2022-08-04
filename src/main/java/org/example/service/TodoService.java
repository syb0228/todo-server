package org.example.service;

import lombok.AllArgsConstructor;
import org.example.model.TodoModel;
import org.example.model.TodoRequest;
import org.example.repository.TodoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    // 1 todo 리스트 목록에 아이템을 추가
    public TodoModel add(TodoRequest request){
        TodoModel todoModel = new TodoModel();
        todoModel.setTitle(request.getTitle());
        todoModel.setOrder(request.getOrder());
        todoModel.setCompleted(request.getCompleted());

        return this.todoRepository.save(todoModel); // 해당 엔티티를 데이터베이스에 저장
    }

    // 2 todo 리스트 목록 중 특정 아이템을 조회
    public TodoModel searchById(Long id){
        // 값이 존재하지 않으면 throw NOT_FOUND exception (404 error)
        return this.todoRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // 3 todo 리스트 전체 목록을 조회
    public List<TodoModel> searchAll(){
        return this.todoRepository.findAll();
    }

    // 4 todo 리스트 목록 중 특정 아이템을 수정
    public TodoModel updateById(Long id, TodoRequest request){
        TodoModel todoModel = this.searchById(id); // 엔티티를 먼저 가져옴

        if (request.getTitle() != null){ // title 수정 요청이 왔다면
            todoModel.setTitle(request.getTitle()); // title 수정
        }
        if(request.getOrder() != null){
            todoModel.setOrder(request.getOrder());
        }
        if(request.getCompleted() != null){
            todoModel.setCompleted(request.getCompleted());
        }
        return this.todoRepository.save(todoModel); // 수정된 엔티티를 저장
    }

    // 5 todo 리스트 목록 중 특정 아이템을 삭제
    public void deleteById(Long id){
        this.todoRepository.deleteById(id);
    }

    // 6 todo 리스트 전체 목록을 삭제
    public void deleteAll(){
        this.todoRepository.deleteAll();
    }

}
