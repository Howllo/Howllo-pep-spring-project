package com.example.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {
    @Modifying
    @Query("DELETE FROM Message m WHERE m.messageId = ?1")
    public Integer deleteByIDReturn(Integer message_id);

    @Modifying
    @Query("UPDATE Message m SET m.messageText = ?1 WHERE m.messageId = ?2")
    public Integer updateByMessage(String updateMessage, Integer messageID);

    @Query("SELECT m FROM Message m WHERE m.postedBy = :postedBy")
    public List<Message> getAllMessageFromAccountID(@Param("postedBy") Integer postedBy);
}
