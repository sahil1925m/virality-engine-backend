# 🚀 Virality Engine - Anti-Spam Backend
Developed by Sahil, B.Tech CSE @ Malwa Institute of Technology

### The Problem
Social media platforms face massive concurrency issues. If 200 bots hit an API simultaneously, a standard database COUNT check might fail due to "race conditions," allowing bots to bypass limits.

### The Solution: Architecture & Logic
This engine uses a stateless architecture to ensure high-speed scalability:

* Spring Boot: Orchestrates the API and core business logic.

* PostgreSQL: Acts as the permanent "Source of Truth" for all posts and comments.

* Redis: Serves as the "Gatekeeper." I implemented Atomic Increments (INCR) to handle the 100-comment horizontal cap. Because Redis operations are atomic, it is physically impossible for a 101st bot to sneak in during a high-speed spam attack.

### Technical Highlights
* Atomic Rate Limiting: Manually implemented Redis counters to prevent race conditions during concurrent request testing.

* Stateless Milestone Notifications: Triggers console alerts at 10, 50, and 100 comments using Redis-backed counters to avoid expensive database aggregation queries.

* Bot Cooldowns: Enforced a 10-minute "digital timeout" for bots using Redis TTL (Time-To-Live) keys to ensure strict rate-limiting compliance.

### Core Implementation Focus: Concurrency & Scalability
The primary focus of this project was solving for data consistency in a distributed system. By leveraging the low-latency nature of Redis alongside the relational integrity of PostgreSQL, the system ensures that spam is blocked at the entry point before it can affect the primary database. The logic was carefully structured to be stateless, allowing the application to be horizontally scaled without losing track of bot cooldowns or comment counts.

### How to Run
1. Ensure Docker Desktop is running.

2. Execute docker-compose up -d to launch the database and cache layers.

3. Launch the Spring Boot application through your IDE or terminal.

4. Import Virality_Engine_Sahil.postman_collection.json into Postman to test the endpoints and verify the rate-limiting behavior.