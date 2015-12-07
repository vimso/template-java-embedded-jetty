package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        
        for (final Message message : messageList) {
            
            output.write(message.getAuthor().getBytes());
            output.write(": ".getBytes());
            output.write(message.getMessage().getBytes());
            output.write("\n".getBytes());
        }
        
        output.flush();
        output.close();
    }
}
