package ru.antowka.controller;

import com.sun.org.apache.xerces.internal.dom.ElementImpl
import javafx.application.HostServices
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.concurrent.Task
import javafx.fxml.FXML
import javafx.scene.control.Hyperlink
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.Stage
import ru.antowka.model.Property
import ru.antowka.model.habr.Item
import ru.antowka.model.habr.Rss
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URL
import java.util.*
import javax.xml.bind.JAXBContext
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Created by anikanor on 09.06.2017.
 */
class MainController {

    @FXML var name = TableColumn<Property, String>()
    @FXML var value = TableColumn<Property, String>()
    @FXML var properties = TableView<Property>()
    var urlWheather: URL = URL("http://api.openweathermap.org/data/2.5/weather?id=520555&appid=6b355653425ae248764f197eb0e5b694&mode=xml&units=metric")
    var propertyList: ArrayList<Property> = ArrayList()
    var propertiesObsrv: ObservableList<Property> = FXCollections.observableList(propertyList)

    @FXML var link = TableColumn<Item, Hyperlink>()
    @FXML var habrTable = TableView<Item>()
    var urlHabr: URL = URL("https://habrahabr.ru/rss/best/")
    var habrArticlesObsrv: ObservableList<Item> = FXCollections.observableList(ArrayList())


    var proxy: Proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("proxy01.merann.ru", 8080))
    var taskList: ArrayList<Thread> = ArrayList()
    var hostService: HostServices? = null


    fun initialize(primaryStage: Stage?, mainHostService: HostServices) {

        this.hostService = mainHostService

        //load weather
        name.cellValueFactory = PropertyValueFactory<Property, String>("name")
        value.cellValueFactory = PropertyValueFactory<Property, String>("value")
        properties.items = propertiesObsrv

        //load habr articles
        link.cellValueFactory = PropertyValueFactory<Item, Hyperlink>("hyperlink")
        habrTable.items = habrArticlesObsrv

        refresh()

        primaryStage?.setOnCloseRequest {
            taskList.forEach(fun(task: Thread) {
                if (!task.isInterrupted) task.interrupt()
            })
        }
    }

    fun refresh() {

        val taskWeather = object : Task<Unit>() {
            override fun call() {
                while (true) {
                    fetchWeatherData()
                    Thread.sleep(60000)
                }
            }
        }

        val threadWeather = Thread(taskWeather)
        threadWeather.name = "Wheather reader"
        threadWeather.start()
        taskList.add(threadWeather)


        val taskHabr = object : Task<Unit>() {
            override fun call() {
                while (true) {
                    fetchHabrData()
                    Thread.sleep(600000)
                }
            }
        }

        val threadHabr = Thread(taskHabr)
        threadHabr.name = "Habr reader"
        threadHabr.start()

        taskList.add(threadHabr)
    }

    fun fetchWeatherData() {

        val root = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(urlWheather.openConnection(proxy).getInputStream())

        val tempr = (root.getElementsByTagName("temperature").item(0) as ElementImpl).getAttribute("value")
        val hmdty = (root.getElementsByTagName("humidity").item(0) as ElementImpl).getAttribute("value")
        val pressure = (root.getElementsByTagName("pressure").item(0) as ElementImpl).getAttribute("value")
        val clouds = (root.getElementsByTagName("clouds").item(0) as ElementImpl).getAttribute("value")

        propertyList.addAll(listOf(
                Property("Temperature", tempr + " C"),
                Property("Humidity", hmdty + " %"),
                Property("Pressure", pressure + " hPa"),
                Property("Clouds", clouds + " %"))
        )
    }

    fun fetchHabrData() {

        val jaxbContext = JAXBContext.newInstance(Rss::class.java)
        val unmarshaller = jaxbContext.createUnmarshaller()

        val inputStream = urlHabr.openConnection(proxy).getInputStream()
        val articles = unmarshaller.unmarshal(inputStream) as Rss
        habrTable.items.clear()

        articles
                .channel
                .item
                .forEach { item ->
                    item.hyperlink = Hyperlink(item.title)
                    item.hyperlink.setOnAction {
                        hostService?.showDocument(item.link)
                    }
                }

        habrArticlesObsrv.addAll(articles.channel.item)
    }
}
