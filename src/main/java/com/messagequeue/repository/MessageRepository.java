package com.messagequeue.repository;

import com.messagequeue.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    // Find first PENDING message by queue name
    @Query(value = "SELECT * FROM messages WHERE queue_name = :queueName AND status = :status ORDER BY created_at ASC LIMIT 1", nativeQuery = true)
    Optional<Message> findFirstPendingMessage(@Param("queueName") String queueName, @Param("status") String status);
    
    // Find all messages in a queue
    List<Message> findByQueueName(String queueName);
    
    // Find all PENDING messages in a queue
    List<Message> findByQueueNameAndStatus(String queueName, String status);
}