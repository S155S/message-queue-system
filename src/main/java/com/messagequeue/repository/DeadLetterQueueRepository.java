package com.messagequeue.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.messagequeue.entity.DeadLetterQueue;

@Repository
public interface DeadLetterQueueRepository extends JpaRepository<DeadLetterQueue, Long> {
    List<DeadLetterQueue> findByQueueName(String queueName);
}