package com.tree.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tree.dto.category.CategoryRequest;
import com.tree.entity.Category;
import com.tree.entity.User;
import com.tree.repository.CategoryRepository;
import com.tree.repository.UserRepository;
import com.tree.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String authToken;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        // Create admin user and get token
        User admin = userRepository.save(User.builder()
                .email("admin@test.com")
                .password(passwordEncoder.encode("password"))
                .fullName("Admin")
                .role(User.Role.ADMIN)
                .active(true)
                .build());

        var userPrincipal = new com.tree.security.UserPrincipal(
                admin.getId(), admin.getEmail(), admin.getPassword(),
                admin.getFullName(), admin.getRole().name(), admin.isActive(),
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
        var auth = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        authToken = jwtTokenProvider.generateToken(auth);
    }

    @Test
    void createCategory_Success() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("Technology");
        request.setDescription("Tech articles");

        mockMvc.perform(post("/api/admin/categories")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Technology"))
                .andExpect(jsonPath("$.data.slug").value("technology"));
    }

    @Test
    void getCategories_Success() throws Exception {
        categoryRepository.save(Category.builder()
                .name("Category 1").slug("category-1").active(true).sortOrder(1).build());
        categoryRepository.save(Category.builder()
                .name("Category 2").slug("category-2").active(true).sortOrder(2).build());

        mockMvc.perform(get("/api/admin/categories")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void updateCategory_Success() throws Exception {
        Category category = categoryRepository.save(Category.builder()
                .name("Old Name").slug("old-name").active(true).sortOrder(1).build());

        CategoryRequest request = new CategoryRequest();
        request.setName("New Name");

        mockMvc.perform(put("/api/admin/categories/" + category.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("New Name"));
    }

    @Test
    void deleteCategory_Success() throws Exception {
        Category category = categoryRepository.save(Category.builder()
                .name("To Delete").slug("to-delete").active(true).sortOrder(1).build());

        mockMvc.perform(delete("/api/admin/categories/" + category.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void accessWithoutAuth_Fails() throws Exception {
        mockMvc.perform(get("/api/admin/categories"))
                .andExpect(status().isForbidden());
    }
}
