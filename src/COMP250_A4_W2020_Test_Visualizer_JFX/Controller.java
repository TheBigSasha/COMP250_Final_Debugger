package COMP250_A4_W2020_Test_Visualizer_JFX;

import COMP250_A4_W2020.HashTableBenchmark;
import COMP250_A4_W2020.HashTableUnitTester;
import COMP250_A4_W2020.TwitterBenchmark;
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

//TODO: FUCKING GENIUS PLAN:
//Allow users to plot the methods of their choice all at once
//ALLOW USERS TO SUBTRACT AND ADD THE RUNTIME OF CERTAIN METHODS FROM EACH OTHER TO SEE
//THE EFFECTS OF METHOD CALLS ON RUNNING TIME (IE. SUBTRACT RUNTIME OF SORT FROM SMT ELSE)
//Add plots for built in methods for timing reference
//REVISE DRAWING USING AN ARRAYLIST OF SERIES AND USE CHECK BOXES TO SELECT WHAT GETS GRAPHED
//FIX WINDOW SIZING WHILE I AM AT IT.
public class Controller implements Initializable {
    private HashTableBenchmark BM;
    private TwitterBenchmark tBM;
    private static final String musicFile = "src/COMP250_A4_W2020_Test_Visualizer_JFX/minecraft_damage.mp3";
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
            menu_removeBM, menu_sortBM, menu_putBM, menu_getBM,
            menu_twitConstBM, menu_twitAddBM, menu_twitDateBM,
            menu_twitAuthBM, menu_twitTrendBM;
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
    private MenuItem UT_RunAll, UT_RunBasicTwitter;
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
        BM = new HashTableBenchmark();
        tBM = new TwitterBenchmark(BM.getRand());
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
        //initalizeGraph(0);
    }

    @FXML
    protected void OpenUnitTesting() {
        System.out.println("Switching to Unit Testing View");
        Benchmarking.setVisible(false);
        //scheduledExecutorService.shutdownNow();
        BM_Title.setOpacity(0.5);
        UT_Title.setOpacity(1);
        UnitTesting.setVisible(true);
    }

    private void addListeners() {
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
        menu_twitAddBM.setOnAction(e -> initalizeGraph(10));
        menu_twitDateBM.setOnAction(e -> initalizeGraph(11));
        menu_twitAuthBM.setOnAction(e -> initalizeGraph(12));
        menu_twitTrendBM.setOnAction(e -> initalizeGraph(13));
        menu_twitConstBM.setOnAction(e -> initalizeGraph(14));
        dark_theme_switch.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            playOof();
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
        UT_RunBasicTwitter.setOnAction(e -> runBasicTwitterTest());
    }

    private void playOof() {
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    private void runUnitTests() {
        HashTableUnitTester UT = new HashTableUnitTester(BM.getRand());
        UnitTestTextArea.setText(UT.runTests());
    }

    private void runBasicTwitterTest() {
        try {
            UnitTestTextArea.appendText("\n\n\n" + tBM.basicTwitterTest());
        } catch (NullPointerException e) {
            tBM = new TwitterBenchmark();
            try {
                UnitTestTextArea.appendText("\n\n\n" + tBM.basicTwitterTest());
            } catch (Exception f) {
                f.printStackTrace();
                UnitTestTextArea.appendText("\n\n\n FAILED TO RUN BASIC TWITTER TEST");
            }
        } catch (Exception e) {
            UnitTestTextArea.appendText("\n\n\n FAILED TO RUN BASIC TWITTER TEST");
        }
    }

    private void runAllTests() {
        UnitTestTextArea.setEditable(false);
        UnitTestTextArea.setWrapText(true);
        //UnitTestTextArea.setFont(javafx.scene.text.Font.font(Font.SERIF));
        UnitTestTextArea.setText("Running tests. This will take a while.");
        runUnitTests();
        /*try {
            HashTableStressTester.main(new String[0]);
            UnitTestTextArea.appendText("\n \n \nRan prof supplied tests. Check console for results.");
        } catch (Exception e) {
            UnitTestTextArea.appendText("\n\n\nAttempted to run prof supplied test, but it threw exception " + e.getMessage());
        }*/
        runBasicTwitterTest();
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
                dualGraph = true;
                lineChart.setTitle("Runtime Efficiency of Get");
                break;
            case 2:
                dualGraph = true;
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
                dualGraph = true;
                lineChart.setTitle("Runtime Efficiency of values");
                break;
            case 8:
                dualGraph = true;
                lineChart.setTitle("Runtime Efficiency of keys");
                break;
            case 9:
                dualGraph = true;
                lineChart.setTitle("Runtime Efficiency of remove");
                break;
            case 10:
                lineChart.setTitle("Runtime Efficiency of Twitter.add");
                break;
            case 11:
                lineChart.setTitle("Runtime Efficiency of Twitter.byDate");
                break;
            case 12:
                lineChart.setTitle("Runtime Efficiency of Twitter.byAuth");
                break;
            case 13:
                lineChart.setTitle("Runtime Efficiency of Twitter.trend");
                break;
            case 14:
                lineChart.setTitle("Runtime Efficiency of Twitter constructor");
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
                    value = BM.timedSort(counter.get());
                    slowValue = BM.timedSlowSort(counter.get());
                    break;
                case 1:
                    value = BM.timedGetAtSize(counter.get());
                    slowValue = BM.timedGetAtSizeRefernce(counter.get());
                    break;
                case 2:
                    value = BM.timedPut(counter.get());
                    slowValue = BM.timedPutReference(counter.get());
                    break;
                case 3:
                    value = BM.timedIteratorNext(counter.get());
                    slowValue = 0L;
                    break;
                case 4:
                    value = BM.timedIteratorHasNext(counter.get());
                    slowValue = 0L;
                    break;
                case 5:
                    value = BM.timedInterator(counter.get());
                    slowValue = 0L;
                    break;
                case 6:
                    value = BM.timedRehash(counter.get());
                    slowValue = 0L;
                    break;
                case 7:
                    value = BM.timedValues(counter.get());
                    slowValue = BM.timedValuesReference(counter.get());
                    break;
                case 8:
                    value = BM.timedKeys(counter.get());
                    slowValue = BM.timedKeysReference(counter.get());
                    break;
                case 9:
                    value = BM.timedRemove(counter.get());
                    slowValue = BM.timedRemoveReference(counter.get());
                    break;
                case 10:        //TODO: Twitter.add
                    value = tBM.timedTwitterAdd(counter.get());
                    slowValue = BM.timedPut(counter.get());
                    break;
                case 11:        //TODO: Twitter.Date
                    value = tBM.timedTwitterByDate(counter.get());
                    slowValue = 0L;
                    break;
                case 12:        //TODO: Twitter.Auth
                    value = tBM.timedTwitterByAuth(counter.get());
                    slowValue = 0L;
                    break;
                case 13:        //TODO: Twitter.trend
                    value = tBM.timedTwitterTrending(counter.get(), counter.get() / 3);
                    slowValue = 0L;
                    break;
                case 14:        //TODO: Twitter()
                    value = tBM.timedTwitterConstructor(counter.get(), counter.get());        //TODO: better solution for ratio of tweets to stopwords?
                    //TODO: Separate graph for increasing stopwords?
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
