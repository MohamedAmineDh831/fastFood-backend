package org.ff.fastfoodbackend.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
class UserControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setupMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void postThenGetUpdateDeleteUser_flow() throws Exception {
        // Create user
        String createJson = "{\"name\":\"Alice\",\"email\":\"alice@example.com\"}";
        String location = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/api/users/")))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andReturn().getResponse().getHeader("Location");

        // Extract id from Location
        assert location != null;
        String id = location.substring(location.lastIndexOf('/') + 1);

        // GET by id
        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Long.parseLong(id)))
                .andExpect(jsonPath("$.email").value("alice@example.com"));

        // Update email
        String updateJson = "{\"email\":\"alice2@example.com\"}";
        mockMvc.perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("alice2@example.com"));

        // Delete
        mockMvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isNoContent());

        // Ensure 404 after delete
        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void validations_and_conflict() throws Exception {
        // Missing name
        String bad1 = "{\"email\":\"no-name@example.com\"}";
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bad1))
                .andExpect(status().isBadRequest());

        // Missing email
        String bad2 = "{\"name\":\"No Email\"}";
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bad2))
                .andExpect(status().isBadRequest());

        // Create then conflict on duplicate email
        String ok = "{\"name\":\"Bob\",\"email\":\"bob@example.com\"}";
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ok))
                .andExpect(status().isCreated());

        String dup = "{\"name\":\"Bobby\",\"email\":\"bob@example.com\"}";
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dup))
                .andExpect(status().isConflict());
    }
}
