package com.messagequeue.controller;
import com.messagequeue.entity.Message;
import com.messagequeue.entity.DeadLetterQueue;
import com.messagequeue.entity.ConsumerGroup;
import com.messagequeue.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/queue")
@CrossOrigin(origins = "*")
public class QueueController {
    @Autowired private QueueService queueService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, String> request) {
        String queueName = request.get("queue");
        String body = request.get("message");
        if (queueName == null || body == null) return ResponseEntity.badRequest().body("Missing queue or message");
        Message message = queueService.sendMessage(queueName, body);
        Map<String, Object> response = new HashMap<>();
        response.put("messageId", message.getId());
        response.put("status", "queued");
        response.put("queue", queueName);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/consume")
    public ResponseEntity<?> consumeMessage(@RequestParam(name = "queue") String queue) {
        Message message = queueService.consumeMessage(queue);
        if (message == null) return ResponseEntity.ok(Map.of("message", "No messages in queue", "queue", queue));
        Map<String, Object> response = new HashMap<>();
        response.put("messageId", message.getId());
        response.put("body", message.getBody());
        response.put("queue", message.getQueueName());
        response.put("status", message.getStatus());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/consume-group")
    public ResponseEntity<?> consumeMessageForGroup(
            @RequestParam String queue,
            @RequestParam String groupName,
            @RequestParam String consumerId) {
        Message message = queueService.consumeMessageForGroup(queue, groupName, consumerId);
        if (message == null) return ResponseEntity.ok(Map.of("message", "No messages in queue"));
        Map<String, Object> response = new HashMap<>();
        response.put("messageId", message.getId());
        response.put("body", message.getBody());
        response.put("queue", message.getQueueName());
        response.put("consumer", consumerId);
        response.put("group", groupName);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/message/{id}/ack")
    public ResponseEntity<?> acknowledgeMessage(@PathVariable Long id) {
        Message message = queueService.getMessageById(id);
        if (message == null) return ResponseEntity.badRequest().body("Message not found");
        queueService.acknowledgeMessage(id);
        return ResponseEntity.ok(Map.of("status", "acknowledged", "messageId", id));
    }

    @PostMapping("/message/{id}/nack")
    public ResponseEntity<?> nackMessage(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String reason = request.getOrDefault("reason", "Processing failed");
        Message message = queueService.getMessageById(id);
        if (message == null) return ResponseEntity.badRequest().body("Message not found");
        queueService.nackMessage(id, reason);
        return ResponseEntity.ok(Map.of("status", "nacked", "messageId", id, "reason", reason));
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(@RequestParam String queue) {
        long pendingCount = queueService.getPendingCount(queue);
        long dlqCount = queueService.getDLQCount(queue);
        Map<String, Object> stats = new HashMap<>();
        stats.put("queue", queue);
        stats.put("pending_messages", pendingCount);
        stats.put("dlq_messages", dlqCount);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/dlq")
    public ResponseEntity<?> getDLQMessages(@RequestParam String queue) {
        List<DeadLetterQueue> messages = queueService.getDLQMessages(queue);
        return ResponseEntity.ok(Map.of("queue", queue, "dlq_messages", messages));
    }

    @GetMapping("/groups")
    public ResponseEntity<?> getConsumerGroups(@RequestParam String queue) {
        List<ConsumerGroup> groups = queueService.getConsumerGroups(queue);
        return ResponseEntity.ok(Map.of("queue", queue, "consumer_groups", groups));
    }
}