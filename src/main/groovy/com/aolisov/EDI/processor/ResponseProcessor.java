package com.aolisov.EDI.processor;

import com.aolisov.EDI.dto.Message;

/**
 * Created by A.P.Olisov on 4/22/2016.
 */
public interface ResponseProcessor {
    String getResponse(Message request);
}
