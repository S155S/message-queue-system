# Message Queue System

A distributed message queue system built with **Spring Boot** backend and **React** frontend. Features producer-consumer pattern, message persistence, acknowledgments, and dead letter queue handling.

## 🎯 Features

- ✅ **Producer API** - Send messages to queues
- ✅ **Consumer API** - Consume messages from queues  
- ✅ **Acknowledgments** - Mark messages as processed
- ✅ **Dead Letter Queue** - Failed messages after max retries
- ✅ **Consumer Groups** - Load balancing across consumers
- ✅ **React Dashboard** - Beautiful UI to manage queues
- ✅ **H2 In-Memory DB** - Quick setup, no external DB needed

## 🏗️ Architecture

### 🔄 Message Flow Diagram

This diagram illustrates how a message moves between the Frontend, Backend, and Database in this system.

```mermaid
sequenceDiagram
    participant Frontend as Frontend (React)
    participant Backend as Backend (Spring Boot)
    participant DB as Database (H2)

    Frontend->>Backend: --Send Message------------->
    Backend->>DB: |--Save to Queue--------->
    
    Frontend-->>Backend: |<--Get Message-------------
    Backend-->>DB: |<--Fetch Message---------

    Frontend->>Backend: |--Acknowledge Message------->
    Backend->>DB: |--Mark as Processed--->
## 🚀 Tech Stack

**Backend:**
- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database
- Maven

**Frontend:**
- React 18
- CSS3
- Fetch API

## 📋 Setup & Installation

### Prerequisites
- Java 17+
- Node.js 18+
- Maven 3.9+

### Backend Setup

```bash
cd C:\mq\message-queue-system
mvn clean package -DskipTests
java -jar target/message-queue-system-1.0-SNAPSHOT.jar
```

Backend runs on: `http://localhost:8080`

### Frontend Setup

```bash
cd frontend
npm install
npm start
```
Frontend runs on: `http://localhost:3000`

## 📡 API Endpoints

### Send Message
POST /api/queue/send
Body: {"queue": "orders", "message": "hello world"}
Response: {"messageId": 1, "status": "queued", "queue": "orders"}
### Consume Message
GET /api/queue/consume?queue=orders
Response: {"messageId": 1, "body": "hello world", "status": "PENDING", "queue": "orders"}
### Acknowledge Message
POST /api/queue/message/1/ack
Response: {"status": "acknowledged", "messageId": 1}
### Queue Stats
GET /api/queue/stats?queue=orders
Response: {"queue": "orders", "pending_messages": 5, "dlq_messages": 0}

## 🎨 Dashboard Features

- **Send Message** - Type a message and send to queue
- **Consume Message** - Get next message from queue
- **Acknowledge** - Mark message as processed
- **Queue Stats** - View pending and DLQ messages in real-time
- **Beautiful UI** - Purple gradient theme with responsive design

## 📊 System Design

### Message Flow
1. **Producer** sends message via `/api/queue/send`
2. Message stored in H2 database with `PENDING` status
3. **Consumer** fetches via `/api/queue/consume` (FIFO order)
4. Consumer processes message
5. Consumer calls `/api/queue/message/{id}/ack`
6. Message marked as `PROCESSED` and removed

### Message Statuses
- `PENDING` - Waiting to be consumed
- `PROCESSING` - Being processed by consumer
- `PROCESSED` - Successfully processed & acknowledged
- `FAILED` - Moved to DLQ after 3 retries

## 🗂️ Project Structure

# Message Queue System

A full-stack message queue application featuring a Spring Boot backend and a React frontend.

---

## 📂 Project Structure

```text
message-queue-system/
├── src/main/java/com/messagequeue/
│   ├── Application.java                 # Spring Boot entry point
│   ├── controller/
│   │   └── QueueController.java         # REST endpoints
│   ├── service/
│   │   └── QueueService.java            # Business logic
│   ├── repository/
│   │   ├── MessageRepository.java
│   │   ├── DeadLetterQueueRepository.java
│   │   └── ConsumerGroupRepository.java
│   └── entity/
│       ├── Message.java
│       ├── DeadLetterQueue.java
│       └── ConsumerGroup.java
├── frontend/                            # React app
│   ├── src/
│   │   ├── App.js
│   │   ├── App.css
│   │   └── index.js
│   └── package.json
├── pom.xml                              # Maven config
└── README.md                            # Project documentation

## 💡 How It Works

### Example Flow

**Step 1: Send Message**
```bash
curl -X POST http://localhost:8080/api/queue/send \
  -H "Content-Type: application/json" \
  -d '{"queue":"orders","message":"Process order #123"}'
```
Response: Message saved to DB with ID 1, status PENDING

**Step 2: Consume Message**
```bash
curl http://localhost:8080/api/queue/consume?queue=orders
```
Response: Returns message ID 1 with body "Process order #123"

**Step 3: Process & Acknowledge**
After processing, call:
```bash
curl -X POST http://localhost:8080/api/queue/message/1/ack
```
Message marked as PROCESSED

## 🎯 Use Cases

- **Order Processing** - Queue customer orders, process asynchronously
- **Email Notifications** - Queue emails, send in background
- **Task Scheduling** - Queue long-running tasks
- **Microservices** - Decouple services with message queues
- **Rate Limiting** - Control throughput with queue

## 🔧 Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
server.port=8080
```

## 📈 Future Enhancements

- [ ] Persistent database (PostgreSQL)
- [ ] Message priority levels
- [ ] Message TTL (time-to-live)
- [ ] Batch consuming
- [ ] Message filtering
- [ ] Metrics & monitoring
- [ ] Docker containerization
- [ ] Kubernetes deployment

## 👨‍💻 Author

**Your Name** - Computer Engineering Student

## 📝 License

This project is open source and available under the MIT License.

## 🤝 Contributing

Pull requests are welcome! For major changes, please open an issue first.

---

**Built with ❤️ using Spring Boot & React**
