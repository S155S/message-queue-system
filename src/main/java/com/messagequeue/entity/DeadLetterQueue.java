package com.messagequeue.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dead_letter_queue")
public class DeadLetterQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "original_message_id")
    private Long originalMessageId;
    
    @Column(name = "queue_name")
    private String queueName;
    
    @Column(name = "body", columnDefinition = "TEXT")
    private String body;
    
    @Column(name = "reason")
    private String reason;
    
    @Column(name = "retry_count")
    private Integer retryCount;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public DeadLetterQueue() {}
    
    public DeadLetterQueue(Long originalMessageId, String queueName, String body, String reason, Integer retryCount) {
        this.originalMessageId = originalMessageId;
        this.queueName = queueName;
        this.body = body;
        this.reason = reason;
        this.retryCount = retryCount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOriginalMessageId() { return originalMessageId; }
    public void setOriginalMessageId(Long originalMessageId) { this.originalMessageId = originalMessageId; }
    public String getQueueName() { return queueName; }
    public void setQueueName(String queueName) { this.queueName = queueName; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
