package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.AccountService;
import com.example.service.MessageService;
import com.example.entity.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService)
    {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /**
     * @param account Takes in a {@code Account} request body to be processed.
     * @return A {@code Account} ResponseEntity of the information as a body.
     * 
     * If the request fail it will return a 409 (Conflict) if there is already username that exist.
     * If the request fail in a different way it will return a 400.
     */
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account){
        if(accountService.checkIfUsernameExist(account.getUsername())){
            return ResponseEntity.status(409).body(account);
        }

        Account returnAccount = accountService.register(account);

        if(returnAccount == null){
            return ResponseEntity.status(400).body(null);
        }

        return ResponseEntity.ok(returnAccount);
    }

    /**
     * The API call for logging into the system.
     * @param account Take in a request body of type {@code Account} that will be used to process information. 
     * @return ResponseEntity body of the {@code Account} object.
     */
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account){
        if(!accountService.checkIfUsernameExist(account.getUsername())){
            return ResponseEntity.status(401).body(null);
        }

        Account returnAccount = accountService.login(account);
        if(returnAccount == null){
            return ResponseEntity.status(401).body(null);
        }

        return ResponseEntity.ok(returnAccount);
    }

    /**
     * The API call for creating message.
     * @param account Take in a request body of type {@code Message} that will be used to process information. 
     * @return ResponseEntity body of the {@code Message} object.
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message){
        Message returnMessage = messageService.createMessage(message);

        if(returnMessage == null){
            return ResponseEntity.status(400).body(null);
        }

        return ResponseEntity.ok(returnMessage);
    }

    /**
     * Get all message that has been sent by the system.
     * @return ResponseEntity body of the {@code List<Message>} objects.
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessage(){
        return ResponseEntity.ok(messageService.getAllMessage());
    }

    /**
     * The API call for get a message by message ID.
     * @param message_id Take in a {@code Integer} to get certain message.
     * @return ResponseEntity body of the {@code Message} object.
     */
    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessageByID(@PathVariable Integer message_id){
        Message returnMessage = messageService.getMessageByID(message_id);
        if(returnMessage != null){
            return ResponseEntity.ok(returnMessage);
        }
        return ResponseEntity.ok().body(null);
    }
    
    /**
     * The API call for delete a message by message ID.
     * @param message_id Take in a {@code Integer} to get certain message.
     * @return ResponseEntity body of the {@code Integer} object.
     */
    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Integer> deleteMessageByID(@PathVariable Integer message_id){
        Integer rowAff = messageService.deleteMessageByID(message_id);
        if(rowAff > 0){
            return ResponseEntity.ok(rowAff);
        }
        return ResponseEntity.ok(null);
    }

    /**
     * The API call for updating a message by message ID.
     * @param message_id Take in a {@code Integer} to get certain message.
     * @param message Take in a {@code @RequestBody} that is a {@code String} to be used in message 
     * update.
     * @return ResponseEntity body of the {@code Message} object.
     */
    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> updateMessageByID(@PathVariable Integer message_id,
                                                     @RequestBody Message updateMessage)
    {
        Integer affMessage = messageService.updateMessageByID(message_id, updateMessage.getMessageText());

        if(affMessage > 0){
            return ResponseEntity.ok(affMessage);
        }

        return ResponseEntity.status(400).body(null);
    }

    /**
     * The API call for getting all message from a specific user by their account ID.
     * @param accountID Take in a {@code Integer} to get certain account.
     * @return ResponseEntity body of the {@code List<Message>} object.
     */
    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getAllMessageFromUserAccountID(@PathVariable Integer account_id){
        List<Message> getMessage = messageService.getAllMessageFromAccountID(account_id);

        if(getMessage != null){
            return ResponseEntity.ok(getMessage);
        }

        return ResponseEntity.status(400).body(null);
    }
}
