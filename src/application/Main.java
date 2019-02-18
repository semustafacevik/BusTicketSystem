package application;
	
import java.sql.Connection;
import java.sql.DriverManager;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	public static Connection connection;
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("MainPage.fxml"));
			Scene scene = new Scene(root,650,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("BUS TICKET SYSTEM");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String JDBC_Driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String Database_URL = "jdbc:sqlserver://localhost:1433;databaseName=BusTicketSystemDB;"
							+ "username=cevik;password=c123";
		try {
			Class.forName(JDBC_Driver);
			connection = DriverManager.getConnection(Database_URL);
			launch(args);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
}
