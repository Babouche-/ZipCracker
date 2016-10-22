package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import net.lingala.zip4j.core.ZipFile;
import sample.task.Timer;

import java.io.File;
import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Controller {
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public final String dictionary = "1234";

    private ZipFile selectedFile;

    public TextField fileTextBox;

    public Button fileChooserButton;
    public Button startButton;

    public TitledPane filePanel;
    public TitledPane statisticsPanel;

    public Label currentPassword;
    public Label testedPassword;
    public Label period;
    public Label speedLabel;

    public ProgressIndicator progress;

    @FXML
    private void initialize() {
        startButton.setGraphic(new ImageView(new Image("data/images/run.png")) {{
            setFitHeight(20);
            setFitWidth(20);
        }});
        System.out.println(factorielleGrandNombre(4));
    }
    public static BigInteger factorielleGrandNombre(int n) {
        BigInteger produit = BigInteger.ONE;
        BigInteger mul = BigInteger.ONE;
        for (int i = 1; i <= n; i++ , mul = mul.add(BigInteger.ONE))
            produit = produit.multiply(mul);
        return produit;
    }
    public void openFileChooserDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Zip  archive", "*.zip"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        try {
            this.selectedFile = new ZipFile(fileChooser.showOpenDialog(null));
            if (this.selectedFile != null)
                if (selectedFile.isEncrypted()) {
                    fileTextBox.setText(selectedFile.getFile().getPath());
                    this.filePanel.setText("File : " + selectedFile.getFile().getName());
                } else
                    generateAlert(Alert.AlertType.INFORMATION, "No password required");
        } catch (Exception e) {
            e.printStackTrace();
            generateAlert(Alert.AlertType.INFORMATION, "Invalid file");
            this.selectedFile = null;
        }

    }

    public void start() {
        if (selectedFile == null) {
            generateAlert(Alert.AlertType.ERROR, "You must choose file");
            openFileChooserDialog();
        } else
            tryOpen();
    }

    private void tryOpen() {
        BruteForce bruteForce = new BruteForce(dictionary,selectedFile,this);
        Timer timer = new Timer();

        currentPassword.textProperty().bind(bruteForce.titleProperty());
        testedPassword.textProperty().bind(bruteForce.messageProperty());
        speedLabel.textProperty().bind(bruteForce.valueProperty());
        progress.progressProperty().bind(bruteForce.progressProperty());

        period.textProperty().bind(timer.titleProperty());

        executor.execute(bruteForce);
        executor.execute(timer);
    }

    public void generateAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("");
        alert.setHeaderText("");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void jobComplete() {
        currentPassword.textProperty().unbind();
        testedPassword.textProperty().unbind();

        currentPassword.setTextFill(Color.GREEN);
        generateAlert(Alert.AlertType.CONFIRMATION,"");
    }


}

