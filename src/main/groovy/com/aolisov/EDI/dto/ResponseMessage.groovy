package com.aolisov.EDI.dto

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.Canonical
import groovy.transform.ToString

/**
 * Created by A.P.Olisov on 4/21/2016.
 */
@Canonical
@ToString
class ResponseMessage {
    @JsonProperty("chat_id")
    int chatId
    String text = ""
}
