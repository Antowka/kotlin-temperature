package ru.antowka.model.habr

import javax.xml.bind.annotation.*

/**
 * Created by anikanor on 13.06.2017.
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
class Channel(@XmlElement val item: MutableList<Item>) {
    constructor() : this(ArrayList())
}
