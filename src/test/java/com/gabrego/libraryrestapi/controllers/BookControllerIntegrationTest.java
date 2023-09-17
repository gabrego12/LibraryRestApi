package com.gabrego.libraryrestapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrego.libraryrestapi.TestDataUtil;
import com.gabrego.libraryrestapi.domain.entities.Book;
import com.gabrego.libraryrestapi.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    private BookService bookService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public BookControllerIntegrationTest(MockMvc mockMvc, BookService bookService) {
        this.mockMvc = mockMvc;
        this.bookService = bookService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateBookSuccessfullyReturnsHttp201Created() throws Exception {

        Book testBookA = TestDataUtil.createTestBookA(null);
        String bookJson = objectMapper.writeValueAsString(testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/"+testBookA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception {
        Book testBookA = TestDataUtil.createTestBookA(null);
        String bookJson = objectMapper.writeValueAsString(testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/"+testBookA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookA.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBookA.getTitle())
        );
    }

    @Test
    public void testThatListBooksReturnsHttpStatus200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListBooksReturnsListOfBooks() throws Exception {
        Book testBook = TestDataUtil.createTestBookA(null);
        bookService.createBook(testBook.getIsbn(), testBook);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].isbn").value(testBook.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].title").value(testBook.getTitle())
        );
    }

    @Test
    public void testThatGetBookReturnsHttpStatus200OkWhenBookExist() throws Exception {
        Book testBook = TestDataUtil.createTestBookA(null);
        bookService.createBook(testBook.getIsbn(), testBook);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/978-1-2345-6789-0")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetBookReturnsHttpStatus404NotFoundWhenNoBookExist() throws Exception {
        Book testBook = TestDataUtil.createTestBookA(null);
        bookService.createBook(testBook.getIsbn(), testBook);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/978-1-2345-6789-1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetBookReturnsBookWhenBookExist() throws Exception {
        Book testBook = TestDataUtil.createTestBookA(null);
        bookService.createBook(testBook.getIsbn(), testBook);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/978-1-2345-6789-0")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBook.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBook.getTitle())
        );
    }


}