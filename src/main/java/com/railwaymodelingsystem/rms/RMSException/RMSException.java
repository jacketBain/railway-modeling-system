package com.railwaymodelingsystem.rms.RMSException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

public abstract class RMSException extends Exception {

    @NotNull
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private String message;
}
