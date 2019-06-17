package com.taskmaker.demo.ControllerTest;

import com.taskmaker.demo.Controller.TaskController;
import com.taskmaker.demo.Service.TaskService;
import com.taskmaker.demo.entity.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TaskController.class)
public class TaskControllerTest {

    private static final long UNUSED_ID = 2L;
    @MockBean
    private TaskService taskService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void findAll_should_return_all_tasks_in_the_database() throws Exception {
        when(taskService.findAll()).thenReturn(Arrays.asList(new Task(), new Task()));
        mockMvc.perform(get("/tasks/list")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }


    @Test
    public void findById_should_return_task_with_specified_id() throws Exception {
        Task task1 = makeTask();
        String request = "/tasks/" + task1.getId();
        when(taskService.findTaskById(task1.getId())).thenReturn(java.util.Optional.of(task1));
        mockMvc.perform(get(request)).andExpect(status().isOk())
                .andExpect(content().json("{'id' : 1,'name':'task1', 'estimatedDurationInHours' : 2.0}"));

    }

    @Test
    public void findById_should_return_bad_request_if_id_not_found() throws Exception {
        Task task1 = makeTask();
        String request = "/tasks/" + UNUSED_ID;
        when(taskService.findTaskById(task1.getId())).thenReturn(java.util.Optional.of(task1));
        mockMvc.perform(get(request)).andExpect(status().isBadRequest());
    }


    private Task makeTask() {
        Task task1 = new Task();
        task1.setName("task1");
        task1.setEstimatedDurationInHours(2.0f);
        task1.setId(1L);
        return task1;
    }

}