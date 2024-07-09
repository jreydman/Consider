import java.io.InputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(final Stage stage) throws Exception {
        final String javafxVersion = System.getProperty("javafx.version");
        final String javaVersion = System.getProperty("java.version");
        stage.setTitle(String.format("Consider App: [java:%s\tjavafx:%s]", javaVersion, javafxVersion));

        InputStream iconStream = getClass().getResourceAsStream("/assets/favicon.png");
        Image image = new Image(iconStream);
        stage.getIcons().add(image);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/scene.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/general.css");

        // DataSourceFactory.createProcedure();
        // DataSourceFactory.createProcedure2();
        // DataSourceFactory.createProcedure3();

        stage.setScene(scene);
        stage.show();
    }

    public static void main(final String[] args) {
        launch(args);
    }
}