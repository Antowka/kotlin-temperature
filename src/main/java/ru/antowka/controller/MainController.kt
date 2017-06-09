package ru.antowka.controller;

import com.sun.org.apache.xerces.internal.dom.ElementImpl
import javafx.concurrent.Task
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import org.xml.sax.InputSource
import org.xml.sax.helpers.XMLReaderFactory
import ru.antowka.model.Property
import java.io.File
import java.net.URI
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Created by anikanor on 09.06.2017.
 */
class MainController {

    @FXML var name = TableColumn<Property, String>()
    @FXML var value = TableColumn<Property, String>()
    @FXML var properties = TableView<Property>()
    var url: URL = URL("http://api.openweathermap.org/data/2.5/weather?id=520555&appid=6b355653425ae248764f197eb0e5b694&mode=xml&units=metric")

    fun initialize() {
        name.cellValueFactory = PropertyValueFactory<Property, String>("name")
        value.cellValueFactory = PropertyValueFactory<Property, String>("value")
        refresh()
    }

    fun refresh() {
        val task = object : Task<Unit>() {
            override fun call()  {
                while (true) {
                    properties.items.clear()
                    properties.items.addAll(fetchData())
                    Thread.sleep(1000)
                }
            }
        }

        Thread(task).start()
    }

    fun fetchData() : List<Property> {


        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()
        //val doc = db.parse(url.openStream())
        val path = MainController::class.java.getResource("/resources/mock.xml")
        val root = db.parse(File(path.toURI()))
        val tempr = (root.getElementsByTagName("temperature").item(0) as ElementImpl).getAttribute("value")
        val hmdty = (root.getElementsByTagName("humidity").item(0) as ElementImpl).getAttribute("value")
        val pressure = (root.getElementsByTagName("pressure").item(0) as ElementImpl).getAttribute("value")
        val clouds = (root.getElementsByTagName("clouds").item(0) as ElementImpl).getAttribute("value")

        return listOf(
                Property("Temperature", tempr + " C"),
                Property("Humidity", hmdty + " %"),
                Property("Pressure", pressure + " hPa"),
                Property("Clouds", clouds + " %")
        )
    }
}
