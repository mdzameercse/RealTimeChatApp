package com.zameer.ChatApp.Repository;

import com.zameer.ChatApp.DTO.ChatListView;
import com.zameer.ChatApp.DTO.ChatMessageView;
import com.zameer.ChatApp.DTO.RecentChatDTO;
import com.zameer.ChatApp.Model.Message;
import com.zameer.ChatApp.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message,Integer> {
    @Query("SELECT m.content AS content, m.timestamp AS timestamp, m.sender.username AS senderUsername " +
            "FROM Message m WHERE " +
            "(m.sender.id = :currentUserId AND m.receiver.id = :friendId) OR " +
            "(m.sender.id = :friendId AND m.receiver.id = :currentUserId) " +
            "ORDER BY m.timestamp ASC")
    List<ChatMessageView> findChatBetweenUsers(@Param("currentUserId") int currentUserId,
                                               @Param("friendId") int friendId);

    @Query("SELECT new com.zameer.ChatApp.DTO.ChatListView( " +
            "CASE WHEN m.sender.id = :userId THEN m.receiver.id ELSE m.sender.id END, " +
            "CASE WHEN m.sender.id = :userId THEN m.receiver.username ELSE m.sender.username END, " +
            "MAX(m.timestamp)) " +
            "FROM Message m " +
            "WHERE m.sender.id = :userId OR m.receiver.id = :userId " +
            "GROUP BY CASE WHEN m.sender.id = :userId THEN m.receiver.id ELSE m.sender.id END, " +
            "CASE WHEN m.sender.id = :userId THEN m.receiver.username ELSE m.sender.username END " +
            "ORDER BY MAX(m.timestamp) DESC")
    List<Object[]> findRecentChats(@Param("userId") Long userId);
    @Query("SELECT new com.zameer.ChatApp.DTO.ChatListView( " +
            "CASE WHEN m.sender.id = :userId THEN m.receiver.id ELSE m.sender.id END, " +
            "CASE WHEN m.sender.id = :userId THEN m.receiver.username ELSE m.sender.username END, " +
            "MAX(m.timestamp)) " +
            "FROM Message m " +
            "WHERE m.sender.id = :userId OR m.receiver.id = :userId " +
            "GROUP BY CASE WHEN m.sender.id = :userId THEN m.receiver.id ELSE m.sender.id END, " +
            "CASE WHEN m.sender.id = :userId THEN m.receiver.username ELSE m.sender.username END " +
            "ORDER BY MAX(m.timestamp) DESC")
    List<ChatListView> findUserChats(@Param("userId") int userId);
    @Query("SELECT new com.zameer.ChatApp.DTO.RecentChatDTO( " +
            "CASE WHEN m.sender.username = :username THEN m.receiver.id ELSE m.sender.id END, " +
            "CASE WHEN m.sender.username = :username THEN m.receiver.username ELSE m.sender.username END, " +
            "MAX(m.timestamp)) " +
            "FROM Message m " +
            "WHERE m.sender.username = :username OR m.receiver.username = :username " +
            "GROUP BY CASE WHEN m.sender.username = :username THEN m.receiver.id ELSE m.sender.id END, " +
            "CASE WHEN m.sender.username = :username THEN m.receiver.username ELSE m.sender.username END " +
            "ORDER BY MAX(m.timestamp) DESC")
    List<RecentChatDTO> findRecentChats(@Param("username") String username);

}
