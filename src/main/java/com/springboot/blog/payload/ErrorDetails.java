package com.springboot.blog.payload;

import java.util.Date;

public class ErrorDetails {
    private Date time_stamp;
    private String message;
    private String details;

    public ErrorDetails(Date time_stamp, String message, String details) {
        this.time_stamp = time_stamp;
        this.message = message;
        this.details = details;
    }

    public Date getTime_stamp() {
        return time_stamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
