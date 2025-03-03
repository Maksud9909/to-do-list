package uz.ccrew.todolist.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uz.ccrew.todolist.model.Todo;
import uz.ccrew.todolist.repository.TodoRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTodos() {
        Todo todo1 = new Todo("Title1", "Description1");
        Todo todo2 = new Todo("Title2", "Description2");
        when(todoRepository.findAll()).thenReturn(Arrays.asList(todo1, todo2));

        List<Todo> todos = todoService.getAllTodos();
        assertEquals(2, todos.size());
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    void testGetTodoById() {
        Todo todo = new Todo("Title", "Description");
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        Optional<Todo> found = todoService.getTodoById(1L);
        assertTrue(found.isPresent());
        assertEquals("Title", found.get().getTitle());
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateTodo() {
        Todo todo = new Todo("Title", "Description");
        when(todoRepository.save(todo)).thenReturn(todo);

        Todo created = todoService.createTodo(todo);
        assertNotNull(created);
        assertEquals("Title", created.getTitle());
        verify(todoRepository, times(1)).save(todo);
    }

    @Test
    void testUpdateTodo() {
        Todo existingTodo = new Todo("Old Title", "Old Description");
        existingTodo.setId(1L);
        Todo updatedDetails = new Todo("New Title", "New Description");

        when(todoRepository.findById(1L)).thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(existingTodo)).thenReturn(existingTodo);

        Todo updatedTodo = todoService.updateTodo(1L, updatedDetails);
        assertEquals("New Title", updatedTodo.getTitle());
        assertEquals("New Description", updatedTodo.getDescription());
        verify(todoRepository, times(1)).findById(1L);
        verify(todoRepository, times(1)).save(existingTodo);
    }

    @Test
    void testDeleteTodo() {
        doNothing().when(todoRepository).deleteById(1L);
        todoService.deleteTodo(1L);
        verify(todoRepository, times(1)).deleteById(1L);
    }
}
