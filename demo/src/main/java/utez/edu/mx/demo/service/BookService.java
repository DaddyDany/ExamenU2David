package utez.edu.mx.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utez.edu.mx.demo.model.Book;
import utez.edu.mx.demo.repository.BookRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BookService {

    @Autowired
    private BookRepository repo;

    public List<Book> getAllBooks() {
        return repo.findAll();
    }

    public Book saveBook(Book bean){
        return repo.save(bean);
    }

    public boolean deleteBook(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Book updateBook(Long id, Book book) {
        Optional<Book> existingBookOptional = repo.findById(id);
        if (existingBookOptional.isPresent()) {
            Book existingBook = existingBookOptional.get();
            existingBook.setName(book.getName());
            existingBook.setAutor(book.getAutor());
            existingBook.setFecha(book.getFecha());
            return repo.save(existingBook);
        } else {
            return null;
        }
    }

    public List<Book> getBooksOrderedByFechaDesc() {
        return repo.findByOrderByFechaDesc();
    }

    public List<Book> getBooksOrderedByAutorAsc() {
        return repo.findByOrderByAutorAsc();
    }
}
