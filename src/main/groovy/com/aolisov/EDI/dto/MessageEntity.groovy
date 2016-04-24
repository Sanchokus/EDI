package com.aolisov.EDI.dto

import groovy.transform.Canonical
import groovy.transform.ToString

/**
 * Created by A.P.Olisov on 4/21/2016.
 */
@Canonical
@ToString
class MessageEntity {
    String type = ""
    int offset
    int length
    String url = ""
}
