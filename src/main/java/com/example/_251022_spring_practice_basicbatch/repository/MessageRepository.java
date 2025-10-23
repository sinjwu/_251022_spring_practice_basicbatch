package com.example._251022_spring_practice_basicbatch.repository;

import com.example._251022_spring_practice_basicbatch.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByProcessed(boolean processed);
    List<Message> findByProcessedFalse();
    List<Message> findByProcessedTrue();
    List<Message> findByAuthor(String author);
    List<Message> findByAuthorAndProcessed(String author, boolean processed);
    List<Message> findByContentContaining(String keyword);

    @Query("SELECT m FROM Message m WHERE m.processed = false ORDER BY m.id")
    List<Message> findUnprocessedMessagesOrderById();

    @Query("SELECT COUNT(m) FROM Message m WHERE m.processed = :processed")
    long countByProcessedStatus(@Param("processed") boolean processed);

    @Query(value = "SELECT * FROM messages WHERE author = ?1 AND processed = false", nativeQuery = true)
    List<Message> findUnprocessedMessagesByAuthorNative(String author);
}
