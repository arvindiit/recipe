package com.assignment.abn.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundException extends RuntimeException{
    private Long resourceId;
    public NotFoundException(String message) {
        super(message);
        log.error(message);
    }
    
    public NotFoundException(String message, Long id) {
        super(message);
        this.resourceId = id;
        log.error(message+"{}", id);
    }
    
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
        log.error(message, cause);
    }
    
    public Long getResourceId() {
        return resourceId;
    }
}
