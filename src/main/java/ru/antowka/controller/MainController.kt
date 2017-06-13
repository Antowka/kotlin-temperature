package ru.antowka.controller;

import com.sun.org.apache.xerces.internal.dom.ElementImpl
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.concurrent.Task
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.Stage
import ru.antowka.model.Article
import ru.antowka.model.Property
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URL
import java.util.Arrays.asList
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Node
import org.w3c.dom.NodeList

/**
 * Created by anikanor on 09.06.2017.
 */
class MainController {

    @FXML var name = TableColumn<Property, String>()
    @FXML var value = TableColumn<Property, String>()
    @FXML var properties = TableView<Property>()
    var urlWheather: URL = URL("http://api.openweathermap.org/data/2.5/weather?id=520555&appid=6b355653425ae248764f197eb0e5b694&mode=xml&units=metric")
    var urlHabr: URL = URL("https://habrahabr.ru/rss/feed/posts/24c2423b61a9a5dbc2e721aa9d8f6e6c/")
    var proxy: Proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("proxy01.merann.ru", 8080))
    var taskList: ArrayList<Thread> = ArrayList()

    var propertyList: ArrayList<Property> = ArrayList()
    var propertiesObsrv : ObservableList<Property> = FXCollections.observableList(propertyList)

    var articles: ArrayList<Article> = ArrayList()
    var articlesObsrv: ObservableList<Article> = FXCollections.observableList(articles)

    fun initialize(primaryStage: Stage?) {
        name.cellValueFactory = PropertyValueFactory<Property, String>("name")
        value.cellValueFactory = PropertyValueFactory<Property, String>("value")
        properties.items = propertiesObsrv;
        refresh()

        primaryStage?.setOnCloseRequest{
            taskList.forEach(fun (task: Thread){
                if (!task.isInterrupted) task.interrupt()
            })
        }
    }

    fun refresh() {
        val task = object : Task<Unit>() {
            override fun call() {
                while (true) {
                    fetchWeatherData()
                    fetchHabrData()
                    Thread.sleep(1000)
                }
            }
        }

        val thread = Thread(task)
        thread.start()
        taskList.add(thread)
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

        val root = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(urlHabr.openConnection(proxy).getInputStream())

        val items = asList(root.getElementsByTagName("item"))
        items.forEach(fun (item){
            val title = (item as ElementImpl).getElementsByTagName("title").item(0)
            val test = ""
        })
    }
}
