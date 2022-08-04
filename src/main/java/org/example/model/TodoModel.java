package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data // getter, setter, toString, equals와 같은 함수 생성
@Entity // 해당 클래스의 인스턴스들이 엔티티임을 명시
@NoArgsConstructor // 파라미터가 없는 생성자를 자동으로 생성
@AllArgsConstructor // 클래스에 존재하는 모든 필드에 대한 생성자를 자동으로 생성
public class TodoModel {

    @Id // Primary key라는 것을 의미
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 주키의 값을 위한 자동 생성 전략 명시 (IDENTITY : DB의 identity 컬럼을 이용)
    private Long id;

    @Column(nullable = false) // DDL 생성 시 not null 제약 조건이 붙음
    private String title;

    @Column(name = "todoOrder", nullable = false) // 객체명과 DB명을 다르게 하고 싶은 경우, DB 컬럼명으로 설정할 이름을 name 속성으로 적음
    private Long order; // order 키워드를 h2DB에서 예약어로 사용하고 있기 때문에

    @Column(nullable = false)
    private Boolean completed;

}
