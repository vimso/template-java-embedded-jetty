package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author dkocsan
 */
public class MessageServlet extends HttpServlet {

    List<Message> messageList = new ArrayList();
    /**
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader requestReader = req.getReader();
        String line;
        StringBuffer RequestBodyBuffer = new StringBuffer();
        String body;
        ObjectMapper mapper = new ObjectMapper();
        ServletOutputStream output = resp.getOutputStream();
        
        do {
            line = requestReader.readLine();

            if (line != null) {
                RequestBodyBuffer.append(line);
            }
        } while (line != null);
        
        body = RequestBodyBuffer.toString();
        
        Message message = mapper.readValue(body, Message.class);
        message.setDate(new Date());
        messageList.add(message);
        
        resp.setStatus(201);
        output.flush();
        output.close();
    }
    
    /**
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletOutputStream output = resp.getOutputStream();
        JsonFactory messageListFactory = new JsonFactory();
        
        JsonGenerator JGenerator = messageListFactory.createJsonGenerator(output);
        
        resp.setContentType("application/json");
        
        JGenerator.writeStartArray();
                
        for (final Message message : messageList) {
            JGenerator.writeStartObject();
            
            JGenerator.writeStringField("author", message.getAuthor());
            JGenerator.writeStringField("message", message.getMessage());
            JGenerator.writeStringField("date", message.getDate().toString());

            JGenerator.writeEndObject();
        }
        
        JGenerator.writeEndArray();

        JGenerator.close();
        
        output.flush();
        output.close();
    }
}
