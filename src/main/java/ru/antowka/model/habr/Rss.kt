package ru.antowka.model.habr

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

/**
 * Created by anikanor on 13.06.2017.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class Rss(@XmlElement val channel: Channel) {
    constructor() : this(Channel())
}
