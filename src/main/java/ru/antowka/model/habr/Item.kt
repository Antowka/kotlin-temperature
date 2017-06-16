package ru.antowka.model.habr

import javafx.scene.control.Hyperlink
import javax.xml.bind.annotation.*

/**
 * Created by anikanor on 13.06.2017.
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
class Item(@XmlElement val title: String, @XmlElement val link: String, @XmlTransient var hyperlink: Hyperlink) {
    constructor() : this("No parsed", "No parsed", Hyperlink("No parsed"))
}
