package com.railwaymodelingsystem.model;

import lombok.Getter;
import lombok.Setter;

public class AjaxResponseBody {
    @Getter
    @Setter
    String status;

    @Getter
    @Setter
    String message;

    public AjaxResponseBody(String message, String status){
        this.message = message;
        this.status = status;
    }
}
