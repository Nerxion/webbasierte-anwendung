package de.hsrm.mi.web.projekt.messaging;

public interface BackendInfoService {
    public void sendInfo(String topicname, BackendOperation operation, long id);
}
