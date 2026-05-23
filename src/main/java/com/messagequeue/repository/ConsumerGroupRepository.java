package com.messagequeue.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.messagequeue.entity.ConsumerGroup;

@Repository
public interface ConsumerGroupRepository extends JpaRepository<ConsumerGroup, Long> {
    List<ConsumerGroup> findByQueueName(String queueName);
    Optional<ConsumerGroup> findByGroupName(String groupName);
}