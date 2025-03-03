package uz.ccrew.todolist.repository;

import uz.ccrew.todolist.model.Todo;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
}
