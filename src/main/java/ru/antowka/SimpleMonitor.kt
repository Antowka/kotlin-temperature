package ru.antowka

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import ru.antowka.controller.MainController

class SimpleMonitor : Application() {

    override fun start(primaryStage: Stage?) {
        System.setProperty("prism.lcdtext", "false")
        val loader = FXMLLoader(SimpleMonitor::class.java.getResource("/resources/main.fxml"))
        val load = loader.load<Parent?>()
        primaryStage?.scene = Scene(load)
        val controller = loader.getController<MainController>()

        controller?.initialize(primaryStage, hostServices)
        primaryStage?.isResizable = false
        primaryStage?.sizeToScene()
        primaryStage?.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(SimpleMonitor::class.java)
        }
    }
}