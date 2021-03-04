package uz.java.simplerestapi.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import uz.java.simplerestapi.model.Book;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final List<Book> books;

    public BookController() {
        books = new ArrayList<>(Arrays.asList(
                new Book(1L, "Book1"),
                new Book(2L, "Book2")
        ));
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return books;
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return books.stream().filter(book -> book.getId().equals(id)).findAny().orElse(new Book());
    }

    @GetMapping("/filter")
    public List<Book> filterBynName(@RequestParam(value = "name") String name) {
        return books.stream().filter(book -> book.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
    }

    @PostMapping
    public Book addBook(@RequestBody Book book) {
        if (!StringUtils.hasText(book.getName())) {
            throw new IllegalArgumentException("Kitob nomi kiritilmagan.");
        }
        if (books.size() == 0) {
            book.setId(1L);
            books.add(book);
            return book;
        }
        book.setId(books.get(books.size() - 1).getId() + 1);
        books.add(book);
        return book;
    }

    @PutMapping("/{id}")
    public Book editBookById(@PathVariable Long id, @RequestBody Book reqBook) {
        Optional<Book> updateBook = books.stream().filter(book -> book.getId().equals(id)).findAny();
        if (updateBook.isEmpty()) {
            throw new IllegalArgumentException("Kitob topilmadi");
        }
        Book book = updateBook.get();
        book.setName(reqBook.getName());
        return book;
    }

    @DeleteMapping("/{id}")
    public Long deleteBookById(@PathVariable Long id) {
        Optional<Book> deleteBook = books.stream().filter(book -> book.getId().equals(id)).findAny();
        if (deleteBook.isEmpty())
            return null;
        books.remove(deleteBook.get());
        return deleteBook.get().getId();
    }

}
