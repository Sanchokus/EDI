package com.aolisov.EDI.dto

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.Canonical
import groovy.transform.ToString

/**
 * Created by A.P.Olisov on 4/21/2016.
 */
@Canonical
@ToString
class Message {
    @JsonProperty("message_id")
    int messageId
    User from
    int date
    Chat chat
    String text = ""
    List<MessageEntity> entities = new ArrayList<>()
}
