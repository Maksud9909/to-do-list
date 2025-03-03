package uz.ccrew.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uz.ccrew.todolist.model.Todo;
import uz.ccrew.todolist.service.TodoService;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TodoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TodoService todoService;

    @InjectMocks
    private TodoController todoController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(todoController).build();
    }

    @Test
    void testGetAllTodos() throws Exception {
        Todo todo1 = new Todo("Title1", "Description1");
        Todo todo2 = new Todo("Title2", "Description2");

        when(todoService.getAllTodos()).thenReturn(Arrays.asList(todo1, todo2));

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetTodoById_Found() throws Exception {
        Todo todo = new Todo("Title", "Description");
        when(todoService.getTodoById(1L)).thenReturn(Optional.of(todo));

        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title"));
    }

    @Test
    void testGetTodoById_NotFound() throws Exception {
        when(todoService.getTodoById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateTodo() throws Exception {
        Todo todo = new Todo("Title", "Description");
        when(todoService.createTodo(any(Todo.class))).thenReturn(todo);

        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title"));
    }

    @Test
    void testUpdateTodo_Found() throws Exception {
        Todo todo = new Todo("Title", "Description");
        when(todoService.updateTodo(any(Long.class), any(Todo.class))).thenReturn(todo);

        mockMvc.perform(put("/api/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title"));
    }

    @Test
    void testUpdateTodo_NotFound() throws Exception {
        when(todoService.updateTodo(any(Long.class), any(Todo.class)))
                .thenThrow(new RuntimeException("Todo not found"));

        Todo todo = new Todo("Title", "Description");

        mockMvc.perform(put("/api/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteTodo() throws Exception {
        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isNoContent());
    }
}
