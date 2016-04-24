package com.aolisov.EDI.dto

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.Canonical
import groovy.transform.ToString

/**
 * Created by A.P.Olisov on 4/22/2016.
 */

@Canonical
@ToString
class UpdateFromTelegram {
    @JsonProperty("update_id")
    String updateId
    Message message
}
