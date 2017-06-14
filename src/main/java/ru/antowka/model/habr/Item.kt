package ru.antowka.model.habr

import javax.xml.bind.annotation.*

/**
 * Created by anikanor on 13.06.2017.
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
class Item(@XmlElement val title: String, @XmlElement val link: String) {
    constructor() : this("No parsed", "No parsed")
}
