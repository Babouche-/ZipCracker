package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.getIcons().add(new Image("data/icon/icon.png"));
        primaryStage.setTitle("Zip cracker");
        Parent parent = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Group root = new Group(parent.getChildrenUnmodifiable());
        root.getChildren().add(generateImage());
        primaryStage.setScene(new Scene(root, 508, 628));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private ImageView generateImage() {
        ImageView view = new ImageView();
        view.setFitWidth(338);
        view.setFitHeight(175);
        view.setLayoutX(93);
        view.setLayoutY(14);
        view.setImage(new Image("data/images/graviton.png"));
        return view;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
