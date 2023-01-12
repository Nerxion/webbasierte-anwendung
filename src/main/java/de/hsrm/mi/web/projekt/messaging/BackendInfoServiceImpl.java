package de.hsrm.mi.web.projekt.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class BackendInfoServiceImpl implements BackendInfoService {

    Logger logger = LoggerFactory.getLogger(BackendInfoServiceImpl.class);
    
    @Autowired
    private SimpMessagingTemplate messaging;

    @Override
    public void sendInfo(String topicname, BackendOperation operation, long id) {
        BackendInfoMessage message = new BackendInfoMessage(topicname, operation, id);
        logger.warn("message = {}", message);
        messaging.convertAndSend("/topic/"+topicname, message);
    }
}
