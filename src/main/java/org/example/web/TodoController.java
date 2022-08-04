package org.example.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.TodoModel;
import org.example.model.TodoRequest;
import org.example.model.TodoResponse;
import org.example.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j // log 사용을 위해
@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/") // base url 설정
public class TodoController {

    private final TodoService service;

    @PostMapping
    public ResponseEntity<TodoResponse> create(@RequestBody TodoRequest request){
        log.info("CREATE");

        if(ObjectUtils.isEmpty(request.getTitle())) // 타이틀이 없으면 정상적인 요청이 아님
            return ResponseEntity.badRequest().build(); // 잘못된 요청이라는 응답을 내려줌

        if(ObjectUtils.isEmpty(request.getOrder())) // 값이 없어도 잘못된 요청은 아님
            request.setOrder(0L); // 기본 default 값 입력
        if(ObjectUtils.isEmpty(request.getCompleted()))
            request.setCompleted(false);

        TodoModel result = this.service.add(request);
        return ResponseEntity.ok(new TodoResponse(result));
    }

    @GetMapping("{id}") // 해당 경로로 받은 id 값을 사용하기 위해 @PathVariable
    public ResponseEntity<TodoResponse> readOne(@PathVariable Long id){
        log.info("READ ONE");

        TodoModel result = this.service.searchById(id);
        return ResponseEntity.ok(new TodoResponse(result));
    }

    @GetMapping
    public ResponseEntity<List<TodoResponse>> readAll(){
        log.info("READ ALL");

        List<TodoModel> list = this.service.searchAll();
        List<TodoResponse> response = list.stream().map(TodoResponse::new).collect(Collectors.toList()); // list를 TodoResponse에 매핑
        return ResponseEntity.ok(response);
    }

    @PatchMapping("{id}")
    public ResponseEntity<TodoResponse> update(@PathVariable Long id, @RequestBody TodoRequest request){
        log.info("UPDATE");

        TodoModel result = this.service.updateById(id, request);
        return ResponseEntity.ok(new TodoResponse(result));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOne(@PathVariable Long id){
        log.info("DELETE ONE");

        this.service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(){
        log.info("DELETE ALL");

        this.service.deleteAll();
        return ResponseEntity.ok().build();
    }

}
