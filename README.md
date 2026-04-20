# 🚀 Virality Engine - Anti-Spam Backend
Developed by Sahil, B.Tech CSE @ Malwa Institute of Technology

### The Problem
Social media platforms face massive concurrency issues. If 200 bots hit an API simultaneously, a standard database COUNT check might fail due to "race conditions," allowing bots to bypass limits.

## The Solution: Architecture & Logic
This engine uses a stateless architecture to ensure high-speed scalability:

* Spring Boot: Orchestrates the API and business logic.

* PostgreSQL: Acts as the permanent "Source of Truth" for all posts and comments.

* Redis: Serves as the "Gatekeeper." I used Atomic Increments (INCR) to handle the 100-comment horizontal cap. Because Redis operations are atomic, it is physically impossible for a 101st bot to sneak in during a high-speed spam attack.

### Technical Highlights
* Atomic Rate Limiting: Implemented Redis counters to prevent race conditions during the 200-request spam test.

* Stateless Milestone Notifications: Triggers console alerts at 10, 50, and 100 comments without querying the database for every hit.

* Bot Cooldowns: Enforced a 10-minute "digital timeout" for bots using Redis TTL (Time-To-Live).

Development Philosophy: AI-Augmented Workflow
To deliver this project within a tight timeframe, I adopted an AI-augmented development workflow. By leveraging advanced generative AI for infrastructure orchestration and rapid debugging of Docker networking and environment configurations, I was able to focus my primary effort on the concurrency logic and database integrity, significantly reducing the "boilerplate" phase of the project.

### How to Run
1. Ensure Docker Desktop is running.

2. Run docker-compose up -d.

3. Launch the Spring Boot application.

4. Import Virality_Engine_Sahil.postman_collection.json into Postman to test the endpoints.