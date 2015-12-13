package com.example;

import java.io.BufferedReader;
import java.io.IOException;
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
    
    MessageRepository myMessageRepository = new MessageRepository();

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
        
        ServletOutputStream output = resp.getOutputStream();
        
        try {
            body = getBody(requestReader, RequestBodyBuffer);
            
            myMessageRepository.addMessage(body);
            
            resp.setStatus(201);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    private String getBody(BufferedReader requestReader, StringBuffer RequestBodyBuffer) throws IOException {
        String line;

        do {
            line = requestReader.readLine();
            
            if (line != null) {
                RequestBodyBuffer.append(line);
            }
        } while (line != null);
        
        return RequestBodyBuffer.toString();
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
        JsonGenerator jGenerator = messageListFactory.createJsonGenerator(output);
        List<Message> messageList = myMessageRepository.getAll();
        
        resp.setContentType("application/json");
        
        jGenerator.writeStartArray();
        
        ObjectMapper maper = new ObjectMapper();
                   
        for (final Message message : messageList) {
            maper.writeValue(jGenerator, message);
        }
        
        jGenerator.writeEndArray();

        jGenerator.close();
        
        output.close();
    }
}
