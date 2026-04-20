package com.sahil.virality_engine.service;

import com.sahil.virality_engine.entity.Comment;
import com.sahil.virality_engine.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.Duration;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Comment addComment(Comment comment) {
        // --- PHASE 2: SPAM PROTECTION ---
        if (comment.isBotAuthor()) {
            String botCooldownKey = "cooldown:bot:" + comment.getAuthorId() + ":post:" + comment.getPostId();
            String botCountKey = "count:bot:post:" + comment.getPostId();

            // 1. Rule: 10-Minute Cooldown
            if (Boolean.TRUE.equals(redisTemplate.hasKey(botCooldownKey))) {
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Bot is on a 10-minute cooldown!");
            }

            // 2. Rule: 100-Comment Horizontal Cap (Atomic!)
            Long botCount = redisTemplate.opsForValue().increment(botCountKey);
            if (botCount != null && botCount > 100) {
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Post has reached the 100-bot-comment limit.");
            }

            // Set the cooldown for 10 minutes
            redisTemplate.opsForValue().set(botCooldownKey, "active", Duration.ofMinutes(10));
        }

        // --- SAVE TO DATABASE ---
        Comment savedComment = commentRepository.save(comment);

        // --- PHASE 3: MILESTONE NOTIFICATIONS ---
        triggerMilestoneCheck(comment.getPostId());

        return savedComment;
    }

    private void triggerMilestoneCheck(Long postId) {
        String totalCountKey = "count:total:post:" + postId;
        Long total = redisTemplate.opsForValue().increment(totalCountKey);

        // Notify at 10, 50, and 100 total comments
        if (total == 10 || total == 50 || total == 100) {
            System.out.println("🔔 NOTIFICATION: Post #" + postId + " has reached " + total + " comments!");
        }
    }
}