package ru.antowka

import javafx.application.Application
import javafx.fxml.FXMLLoader.load
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class SimpleMonitor : Application() {

    override fun start(primaryStage: Stage?) {
        System.setProperty("prism.lcdtext", "false")
        primaryStage?.scene = Scene(load<Parent?>(SimpleMonitor::class.java.getResource("/resources/main.fxml")))
        primaryStage?.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(SimpleMonitor::class.java)
        }
    }
}