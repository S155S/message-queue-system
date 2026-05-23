package com.messagequeue.service;
import com.messagequeue.entity.Message;
import com.messagequeue.entity.DeadLetterQueue;
import com.messagequeue.entity.ConsumerGroup;
import com.messagequeue.repository.MessageRepository;
import com.messagequeue.repository.DeadLetterQueueRepository;
import com.messagequeue.repository.ConsumerGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QueueService {
    @Autowired private MessageRepository messageRepository;
    @Autowired private DeadLetterQueueRepository dlqRepository;
    @Autowired private ConsumerGroupRepository consumerGroupRepository;

    private static final int MAX_RETRIES = 3;

    public Message sendMessage(String queueName, String body) {
        Message message = new Message();
        message.setQueueName(queueName);
        message.setBody(body);
        message.setStatus("PENDING");
        message.setRetryCount(0);
        return messageRepository.save(message);
    }

    public Message consumeMessage(String queueName) {
        return messageRepository.findFirstPendingMessage(queueName, "PENDING").orElse(null);
    }

    public Message consumeMessageForGroup(String queueName, String groupName, String consumerId) {
        Message message = messageRepository.findFirstPendingMessage(queueName, "PENDING").orElse(null);
        if (message != null) {
            ConsumerGroup group = new ConsumerGroup(groupName, queueName, consumerId);
            consumerGroupRepository.save(group);
            message.setStatus("PROCESSING");
            messageRepository.save(message);
        }
        return message;
    }

    public void acknowledgeMessage(Long messageId) {
        Message message = messageRepository.findById(messageId).orElse(null);
        if (message != null) {
            message.setStatus("PROCESSED");
            messageRepository.save(message);
        }
    }

    public void nackMessage(Long messageId, String reason) {
        Message message = messageRepository.findById(messageId).orElse(null);
        if (message != null) {
            int retries = message.getRetryCount() + 1;
            if (retries >= MAX_RETRIES) {
                moveToDLQ(message, reason);
                messageRepository.delete(message);
            } else {
                message.setRetryCount(retries);
                message.setStatus("PENDING");
                messageRepository.save(message);
            }
        }
    }

    private void moveToDLQ(Message message, String reason) {
        DeadLetterQueue dlq = new DeadLetterQueue(
            message.getId(),
            message.getQueueName(),
            message.getBody(),
            reason,
            message.getRetryCount()
        );
        dlqRepository.save(dlq);
    }

    public Message getMessageById(Long id) { 
        return messageRepository.findById(id).orElse(null); 
    }

    public List<Message> getQueueMessages(String queueName) { 
        return messageRepository.findByQueueName(queueName); 
    }

    public long getPendingCount(String queueName) { 
        return messageRepository.findByQueueNameAndStatus(queueName, "PENDING").size(); 
    }

    public List<DeadLetterQueue> getDLQMessages(String queueName) {
        return dlqRepository.findByQueueName(queueName);
    }

    public long getDLQCount(String queueName) {
        return dlqRepository.findByQueueName(queueName).size();
    }

    public List<ConsumerGroup> getConsumerGroups(String queueName) {
        return consumerGroupRepository.findByQueueName(queueName);
    }
}
