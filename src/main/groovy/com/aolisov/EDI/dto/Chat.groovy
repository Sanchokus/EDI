package com.aolisov.EDI.dto

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.Canonical
import groovy.transform.ToString

@Canonical
@ToString
class Chat {

    int id
    String type = ""
    String title = ""
    @JsonProperty("username")
    String userName = ""
    @JsonProperty("first_name")
    String firstName = ""
    @JsonProperty("last_name")
    String lastName = ""
}