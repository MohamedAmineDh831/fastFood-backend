package org.ff.fastfoodbackend.controllers;

import org.ff.fastfoodbackend.testutil.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;

import static org.ff.fastfoodbackend.entities.user.UserRole.Constants.RESTAURANT_MANAGER;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BrandControllerIT extends AbstractIntegrationTest {

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    @WithMockUser(roles = {RESTAURANT_MANAGER})
    void postGetPutDelete_flow_and_conflicts() throws Exception {

        String body = "{\"name\":\"FastPizza\"}";
        String location = mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/api/brands/")))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name").value("FastPizza"))
                .andReturn().getResponse().getHeader("Location");

        assert location != null;
        String id = location.substring(location.lastIndexOf('/') + 1);

        // GET by id
        mockMvc.perform(get("/api/brands/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Long.parseLong(id)))
                .andExpect(jsonPath("$.name").value("FastPizza"));

        // Update name
        String upd = "{\"name\":\"FastPizzaX\"}";
        mockMvc.perform(put("/api/brands/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(upd))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("FastPizzaX"));

        // Conflict on duplicate name
        String other = "{\"name\":\"Another\"}";
        mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(other))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/api/brands/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Another\"}"))
                .andExpect(status().isConflict());

        // Delete
        mockMvc.perform(delete("/api/brands/{id}", id))
                .andExpect(status().isNoContent());

        // 404 after delete
        mockMvc.perform(get("/api/brands/{id}", id))
                .andExpect(status().isNotFound());

        // Bad request on missing name
        mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}