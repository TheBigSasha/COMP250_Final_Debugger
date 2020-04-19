package COMP250_A4_W2020_Test_Visualizer_JFX;

import COMP250_A4_W2020.HashTableBenchmark;
import COMP250_A4_W2020.HashTableUnitTester;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Controller implements Initializable {
    private HashTableBenchmark bm;
    private static final String musicFile = "src/COMP250_A4_W2020_Test_Visualizer_JFX/Minecraft Alpha Damage - Sound Effect.mp3";
    private ScheduledExecutorService scheduledExecutorService;
    //BENCHMARKING
    @FXML
    private Pane Benchmarking;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Pane paneView;
    @FXML
    private MenuItem menu_iterBM, menu_hasNextBM, menu_nextBM,
            menu_rehashBM, menu_keysBM, menu_valuesBM,
            menu_removeBM, menu_sortBM, menu_putBM, menu_getBM;
    @FXML
    private Slider sizeSlider;
    @FXML
    private CheckBox dark_theme_switch;
    @FXML
    private Hyperlink sashaPhotoLink;

    //UNIT TESTING
    @FXML
    private Pane UnitTesting;
    @FXML
    private MenuItem UT_RunAll;
    @FXML
    private Button UT_RunBtn;
    @FXML
    private TextArea UnitTestTextArea;

    //BOTH
    @FXML
    private Label BM_Title, UT_Title;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
        bm = new HashTableBenchmark();
        //playOof();
        initalizeGraph(0);
    }

    @FXML
    protected void OpenBenchmarking() {
        System.out.println("Switching to Benchmarking View");
        UnitTesting.setVisible(false);
        UT_Title.setOpacity(0.5);
        BM_Title.setOpacity(1);
        Benchmarking.setVisible(true);
        initalizeGraph(0);
    }

    @FXML
    protected void OpenUnitTesting() {
        System.out.println("Switching to Unit Testing View");
        Benchmarking.setVisible(false);
        scheduledExecutorService.shutdownNow();
        BM_Title.setOpacity(0.5);
        UT_Title.setOpacity(1);
        UnitTesting.setVisible(true);
    }

    private void addListeners() {
        /*  sortBM.setOnAction(e -> initalizeGraph(0));
        getBM.setOnAction(e -> initalizeGraph(1));
        putBM.setOnAction(e -> initalizeGraph(2));
        nextBM.setOnAction(e -> initalizeGraph(3));
        hasNextBM.setOnAction(e -> initalizeGraph(4));
        iterBM.setOnAction(e -> initalizeGraph(5));
        rehashBM.setOnAction(e -> initalizeGraph(6));
        valuesBM.setOnAction(e -> initalizeGraph(7));
        keysBM.setOnAction(e -> initalizeGraph(8));
        removeBM.setOnAction(e -> initalizeGraph(9));*/
        menu_sortBM.setOnAction(e -> initalizeGraph(0));
        menu_getBM.setOnAction(e -> initalizeGraph(1));
        menu_putBM.setOnAction(e -> initalizeGraph(2));
        menu_nextBM.setOnAction(e -> initalizeGraph(3));
        menu_hasNextBM.setOnAction(e -> initalizeGraph(4));
        menu_iterBM.setOnAction(e -> initalizeGraph(5));
        menu_rehashBM.setOnAction(e -> initalizeGraph(6));
        menu_valuesBM.setOnAction(e -> initalizeGraph(7));
        menu_keysBM.setOnAction(e -> initalizeGraph(8));
        menu_removeBM.setOnAction(e -> initalizeGraph(9));
        dark_theme_switch.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                enableDarkTheme();
            } else {
                disableDarkTheme();
            }
        });
        sashaPhotoLink.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("https://sashaphoto.ca/"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });
        UT_RunBtn.setOnAction(e -> runAllTests());
        UT_RunAll.setOnAction(e -> runUnitTests());
    }

    private void playOof() {
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    private void runUnitTests() {
        HashTableUnitTester UT = new HashTableUnitTester(bm.getRand());
        UnitTestTextArea.setText(UT.runTests());
    }

    private void runAllTests() {
        HashTableUnitTester UT = new HashTableUnitTester(bm.getRand());
        UnitTestTextArea.setText(UT.runTests());
        UnitTestTextArea.appendText("\n \n \nMore tests coming soon <3");
    }

    private void initalizeGraph(int input) {
        int WINDOW_SIZE = (int) Math.round(sizeSlider.getValue());
        try {
            scheduledExecutorService.shutdownNow();
        } catch (NullPointerException e) {
            System.out.println("Starting plot");
        }
        paneView.getChildren().clear();
        NumberAxis yAxis = new NumberAxis();
        CategoryAxis xAxis = new CategoryAxis();

        //primaryStage.setTitle("Runtime Efficiency Visualizer");

        // defining the axes
        xAxis.setLabel("Size of HashTable");
        xAxis.setAnimated(true);
        yAxis.setLabel("Runtime (nano)");
        yAxis.setAnimated(false);

        // creating the line chart with two axis created above
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        boolean dualGraph = false;
        switch (input) {
            case 0:
                dualGraph = true;
                lineChart.setTitle("Runtime Efficiency of FastSort");
                break;
            case 1:
                lineChart.setTitle("Runtime Efficiency of Get");
                break;
            case 2:
                lineChart.setTitle("Runtime Efficiency of Put");
                break;
            case 3:
                lineChart.setTitle("Runtime Efficiency of Iterator.next");
                break;
            case 4:
                lineChart.setTitle("Runtime Efficiency of Iterator.hasNext");
                break;
            case 5:
                lineChart.setTitle("Runtime Efficiency of Iterator constructor");
                break;
            case 6:
                lineChart.setTitle("Runtime Efficiency of rehash");
                break;
            case 7:
                lineChart.setTitle("Runtime Efficiency of values");
                break;
            case 8:
                lineChart.setTitle("Runtime Efficiency of keys");
                break;
            case 9:
                lineChart.setTitle("Runtime Efficiency of remove");
                break;
            default:
                dualGraph = false;
                break;
        }
        lineChart.setAnimated(true); // disable animations

        // defining a series to display data
        XYChart.Series<String, Number> fastSortSeries = new XYChart.Series<>();
        fastSortSeries.setName("User Coded Algorithm");

        //defining a series to display slow sort
        XYChart.Series<String, Number> slowSortSeries = new XYChart.Series<>();
        slowSortSeries.setName("Benchmark Algorithm");


        // add series to chart
        lineChart.getData().add(fastSortSeries);
        if (dualGraph) {
            lineChart.getData().add(slowSortSeries);
        }

        // setup scene
        //Group g = new Group(lineChart1);
        //Scene scene = new Scene(g, 800, 600);
        //primaryStage.setScene(scene);

        // show the stage
        //primaryStage.show();
        //lineChart.setMinSize(499,499);
        lineChart.setPrefSize(900, 500);
        paneView.getChildren().add(lineChart);

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        AtomicInteger counter = new AtomicInteger();
        boolean finalDualGraph = dualGraph;
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            counter.getAndIncrement();
            Long value;
            Long slowValue;
            switch (input) {
                case 0:
                    value = bm.timedSort(counter.get());
                    slowValue = bm.timedSlowSort(counter.get());
                    break;
                case 1:
                    value = bm.timedGetAtSize(counter.get());
                    System.out.println("[Grapher] Value on Y axis is: " + value);
                    slowValue = 0L;
                    break;
                case 2:
                    value = bm.timedPut(counter.get());
                    System.out.println("[Grapher] Value on Y axis is: " + value);
                    slowValue = 0L;
                    break;
                case 3:
                    value = bm.timedIteratorNext(counter.get());
                    System.out.println("[Grapher] Value on Y axis is: " + value);
                    slowValue = 0L;
                    break;
                case 4:
                    value = bm.timedIteratorHasNext(counter.get());
                    System.out.println("[Grapher] Value on Y axis is: " + value);
                    slowValue = 0L;
                    break;
                case 5:
                    value = bm.timedInterator(counter.get());
                    System.out.println("[Grapher] Value on Y axis is: " + value);
                    slowValue = 0L;
                    break;
                case 6:
                    value = bm.timedRehash(counter.get());
                    System.out.println("[Grapher] Value on Y axis is: " + value);
                    slowValue = 0L;
                    break;
                case 7:
                    value = bm.timedValues(counter.get());
                    System.out.println("[Grapher] Value on Y axis is: " + value);
                    slowValue = 0L;
                    break;
                case 8:
                    value = bm.timedKeys(counter.get());
                    System.out.println("[Grapher] Value on Y axis is: " + value);
                    slowValue = 0L;
                    break;
                case 9:
                    value = bm.timedRemove(counter.get());
                    System.out.println("[Grapher] Value on Y axis is: " + value);
                    slowValue = 0L;
                    break;
                default:
                    value = (long) 0;
                    slowValue = 0L;
                    break;
            }

            //System.out.println("[Grapher] Data updated.");

            // Update the chart
            Platform.runLater(() -> {
                // get current time
                Date now = new Date();
                // put random number with current time
                if (finalDualGraph) {
                    slowSortSeries.getData().add(
                            new XYChart.Data<>(Integer.toString(counter.get()), slowValue));
                }
                fastSortSeries.getData().add(
                        new XYChart.Data<>(Integer.toString(counter.get()), value));

                if (fastSortSeries.getData().size() > WINDOW_SIZE)
                    fastSortSeries.getData().remove(0);

                if (slowSortSeries.getData().size() > WINDOW_SIZE)
                    slowSortSeries.getData().remove(0);

                if (fastSortSeries.getData().size() > 75) {
                    lineChart.setVerticalGridLinesVisible(false);
                    lineChart.setHorizontalGridLinesVisible(false);
                    lineChart.setCreateSymbols(false);
                }
            });
        }, 0, 250, TimeUnit.MILLISECONDS);

    }

    private void enableDarkTheme() {
        anchorPane.getStylesheets().add(" /COMP250_A4_W2020_Test_Visualizer_JFX/mondea_dark.css");
    }

    private void disableDarkTheme() {
        anchorPane.getStylesheets().remove(" /COMP250_A4_W2020_Test_Visualizer_JFX/mondea_dark.css");
    }
}
