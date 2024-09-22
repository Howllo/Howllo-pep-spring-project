package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private MessageRepository messageRepository = null;
    private AccountService accountService;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountService accountService){
        this.messageRepository = messageRepository;
        this.accountService = accountService;
    }

    /**
     * @param message Takes in a {@code Message} object be processed.
     * @return {@code Message} object that is either {@code null} if it fails to meet requirements
     * or returns an object if it successful. 
     */
    public Message createMessage(Message message){
        if(    message.getMessageText().equals("") 
            || message.getMessageText().length() > 255 
            || !accountService.checkIfUserExistByID(message.getPostedBy())
        )
        {
            return null;
        }

        return messageRepository.save(message);
    }

    /**
     * @return {@code List} of all possible {@code Messsage} object in the database. 
     */
    public List<Message> getAllMessage(){
        return (List<Message>) messageRepository.findAll();
    }

    /**
     * @param message_id Takes in a {@code Integer} message_id to process the request.
     * @return Returns a Message object that may or may not be null.
     */
    public Message getMessageByID(Integer message_id){
        Optional<Message> returnMessage = messageRepository.findById(message_id);

        if(returnMessage.isPresent()){
            return returnMessage.get();
        }

        return null;
    }

    /**
     * @param message_id Takes in a {@code Integer} message_id to process the request.
     * @return The amount of rows that were affected by the deletion.
     */
    public Integer deleteMessageByID(Integer message_id){
        return messageRepository.deleteByIDReturn(message_id);
    }

   
    /**
     * @param message_id Take in a message id to get that message and update it.
     * @param updateMessage Take in a {@code String} that will be set in the message to update the message.
     * @return A {@code Integer} of the number of rows that were affected.
     */
    public Integer updateMessageByID(Integer message_id, String updateMessage){
        if(    updateMessage.equals("") 
            || updateMessage.length() > 255
            || !messageRepository.existsById(message_id)){
            return 0;
        }

        return messageRepository.updateByMessage(updateMessage, message_id);
    }

    /**
     * @param accountID
     * @return
     */
    public List<Message> getAllMessageFromAccountID(Integer accountID){
        if(!accountService.checkIfUserExistByID(accountID)){
            return null;
        }
        return messageRepository.getAllMessageFromAccountID(accountID);
    }
}
