package utez.edu.mx.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utez.edu.mx.demo.model.Book;

import java.time.LocalDate;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>{
    List<Book> findByOrderByAutorAsc();
    List<Book> findByOrderByFechaDesc();
}
