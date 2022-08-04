package org.example.repository;

import org.example.model.TodoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// Entity의 기본적인 CRUD가 가능하도록 JpaRepository 인터페이스를 상속 받음
// 사용될 Entity 클래스와 ID 값이 들어감
public interface TodoRepository extends JpaRepository<TodoModel, Long> {
}
