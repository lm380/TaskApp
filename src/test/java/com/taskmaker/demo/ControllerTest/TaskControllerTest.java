package com.taskmaker.demo.ControllerTest;

import com.taskmaker.demo.Controller.TaskController;
import com.taskmaker.demo.Service.TaskService;
import com.taskmaker.demo.entity.Task;
import net.bytebuddy.utility.RandomString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TaskController.class)
public class TaskControllerTest {

    private static final long UNUSED_ID = 2L;
    private static final int OVER_MAXIMUM_DURATION = 10001;
    private static final int OVER_MAXIMUM_NAME_SIZE = 256;
    private static final String LONG_NAME = RandomString.make(OVER_MAXIMUM_NAME_SIZE);

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

    @Test
    public void add_should_add_valid_tasks_to_database() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/tasks/add")
                .accept(MediaType.APPLICATION_JSON).content("{\"id\" : 1,\"name\" : \"task1\", \"estimatedDurationInHours\" : 2.0}")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void add_should_not_add_tasks_with_estimatedDurations_over_10000_hours_to_database() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/tasks/add")
                .accept(MediaType.APPLICATION_JSON).content("{\"id\" : 1,\"name\" : \"task1\", \"estimatedDurationInHours\" : "+OVER_MAXIMUM_DURATION+"}")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    @Test
    public void add_should_not_add_tasks_with_estimatedDurations_under_1_hour_to_database() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/tasks/add")
                .accept(MediaType.APPLICATION_JSON).content("{\"id\" : 1,\"name\" : \"task1\", \"estimatedDurationInHours\" : 0.5}")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    @Test
    public void add_should_not_add_tasks_with_names_longer_than_255_chars_to_database() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/tasks/add")
                .accept(MediaType.APPLICATION_JSON).content("{\"id\" : 1,\"name\" : \""+LONG_NAME+"\", \"estimatedDurationInHours\" : 2.0}")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

//    @Test
//    public void update() throws Exception {
//        Task task = makeTask();
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/tasks/"+task.getId())
//                .accept(MediaType.APPLICATION_JSON).content("{\"id\" : 1,\"name\" : \"task1\", \"estimatedDurationInHours\" : 2.0}")
//                .contentType(MediaType.APPLICATION_JSON);
//        mockMvc.perform(requestBuilder).andExpect(status().isOk())
//                .andExpect(content().json("{'id' : 1,'name':'task1', 'estimatedDurationInHours' : 3.0}"));
//    }


    private Task makeTask() {
        Task task1 = new Task();
        task1.setName("task1");
        task1.setEstimatedDurationInHours(2.0f);
        task1.setId(1L);
        return task1;
    }

}