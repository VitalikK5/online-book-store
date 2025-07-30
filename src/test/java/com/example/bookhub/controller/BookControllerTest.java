package com.example.bookhub.controller;

import static com.example.bookhub.util.BookUtil.createBookDto;
import static com.example.bookhub.util.BookUtil.createBookRequestDto;
import static com.example.bookhub.util.BookUtil.createListOfBookDtos;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookhub.dto.book.BookDto;
import com.example.bookhub.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class BookControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    void beforeAll() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
    }

    @BeforeEach
    void beforeEach() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/insert-books-and-categories.sql"));
        }
    }

    @AfterEach
    void afterEach() {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/delete-books-and-categories-from-db.sql"));
        }
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get all books with pagination and compare content")
    void findAll_WithValidPagination_ReturnsPagedBooks() throws Exception {
        List<BookDto> expected = createListOfBookDtos();
        MvcResult result = mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsByteArray());
        List<BookDto> actual = objectMapper.readValue(
                root.get("content").toString(),
                new TypeReference<>() {});
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get book by id and validate content")
    void findById_WithValidId_ReturnsBookDto() throws Exception {
        MvcResult result = mockMvc.perform(get(
                "/books/{id}", 1))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class
        );
        BookDto expected = createListOfBookDtos().get(0);
        assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create a new book")
    void save_WithValidRequestDto_ReturnsCreatedBook() throws Exception {
        CreateBookRequestDto requestDto = createBookRequestDto(List.of(1L));
        BookDto expected = createBookDto(1L, List.of(1L));
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class
        );
        assertNotNull(actual.getId());
        assertTrue(reflectionEquals(expected, actual, "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update book")
    void update_WithValidIdAndRequestDto_ReturnsUpdatedBook() throws Exception {
        CreateBookRequestDto requestDto = createBookRequestDto(List.of(1L));
        BookDto expected = createBookDto(1L, List.of(1L));
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(put("/books/{id}", 1)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class
        );
        assertTrue(reflectionEquals(expected, actual, "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete book")
    void delete_WithValidId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/books/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Search books by author")
    void search_WithAuthorParameter_ReturnsPagedBooks() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/search")
                        .param("authors", "Test Author 1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Test Author 1"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Search books by title")
    void search_WithTitleParameter_ReturnsPagedBooks() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/search")
                        .param("titles", "Test Book 1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Test Book 1"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Search books by multiple parameters")
    void search_WithMultipleParameters_ReturnsPagedBooks() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/search")
                        .param("titles", "Test Book 1")
                        .param("authors", "Test Author 1")
                        .param("isbns", "1111111111111")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Test Book 1"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get book by non-existing id")
    void findById_WithInvalidId_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/books/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create book with invalid data")
    void save_WithInvalidRequestDto_ReturnsBadRequest() throws Exception {
        CreateBookRequestDto invalidRequest = new CreateBookRequestDto()
                .setTitle("")
                .setAuthor("")
                .setIsbn("")
                .setPrice(BigDecimal.valueOf(-10))
                .setDescription("")
                .setCoverImage("")
                .setCategoryIds(List.of());
        String jsonRequest = objectMapper.writeValueAsString(invalidRequest);
        mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update book with invalid id")
    void update_WithInvalidId_ReturnsNotFound() throws Exception {
        CreateBookRequestDto requestDto = createBookRequestDto(List.of(1L));
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(put("/books/{id}", 999)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete book with invalid id")
    void delete_WithInvalidId_ReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/books/{id}", 999))
                .andExpect(status().isNotFound());
    }
}
