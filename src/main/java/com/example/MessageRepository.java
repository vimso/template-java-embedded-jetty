/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import com.example.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author dkocsan
 */
public class MessageRepository {
    List<Message> messageList = new ArrayList();

    
    public void addMessage(String messageJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        Message message = mapper.readValue(messageJson, Message.class);
        message.setDate(new Date());
        messageList.add(message);
    }
    
    public List getAll() {
        return messageList;
    }
}
