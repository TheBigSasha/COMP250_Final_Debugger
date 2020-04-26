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
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Controller implements Initializable {
    private static final String musicFile = "src/COMP250_A4_W2020_Test_Visualizer_JFX/minecraft_damage.mp3";
    ArrayList<CheckBox> toggles;
    private HashTableBenchmark BM;
    private TwitterBenchmark tBM;
    private ScheduledExecutorService scheduledExecutorService;
    //BENCHMARKING
    @FXML
    private Pane Benchmarking;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Pane paneView;
    @FXML
    private CheckBox GC_FastSort, GC_Remove, GC_Rehash, GC_Values, GC_Constructor, GC_Keys, GC_Get, GC_Put,
            GC_ArrayListMergeSort, GC_ProfSlowSort, GC_Iter, GC_hasNext, GC_Next, GC_J_Constructor,
            GC_J_Put, GC_J_Get, GC_J_Remove, GC_J_Values, GC_J_Keys;
    @FXML
    private CheckBox GC_Twit_Trending, GC_Twit_Constructor, GC_Twit_ByDate, GC_Twit_ByAuth, GC_Twit_Add,
            GC_Twit_ConstructorII, GC_Twit_TrendingII, GC_TurboMode;
    @FXML
    private Slider GC_StopWordFactor, GC_StopWordFactorII, GC_TurboFactor;
    @FXML
    private Button GC_Reset, GC_Help, GC_Refresh;

    //UNIT TESTING
    @FXML
    private Pane UnitTesting;
    @FXML
    private MenuItem UT_RunAll, UT_RunBasicTwitter;
    @FXML
    private Button UT_RunBtn;
    @FXML
    private TextArea UnitTestTextArea;

    //FUN DEMOS
    @FXML
    private Pane FunDemos;

    //ALL
    @FXML
    private Label BM_Title, UT_Title, FUN_Title;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toggles = new ArrayList<>(Arrays.asList(GC_FastSort, GC_Remove, GC_Rehash, GC_Values, GC_Constructor, GC_Keys, GC_Get, GC_Put,
                GC_ArrayListMergeSort, GC_ProfSlowSort, GC_Iter, GC_hasNext, GC_Next, GC_J_Constructor,
                GC_J_Put, GC_J_Get, GC_J_Remove, GC_J_Values, GC_J_Keys, GC_Twit_Trending, GC_Twit_Constructor, GC_Twit_ByDate, GC_Twit_ByAuth, GC_Twit_Add,
                GC_Twit_ConstructorII, GC_Twit_TrendingII));
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
        FunDemos.setVisible(false);
        UT_Title.setOpacity(0.5);
        FUN_Title.setOpacity(0.5);
        BM_Title.setOpacity(1);
        Benchmarking.setVisible(true);
        //initalizeGraph(0);
    }

    @FXML
    protected void OpenUnitTesting() {
        System.out.println("Switching to Unit Testing View");
        Benchmarking.setVisible(false);
        FunDemos.setVisible(false);
        //scheduledExecutorService.shutdownNow();
        BM_Title.setOpacity(0.5);
        FUN_Title.setOpacity(0.5);
        UT_Title.setOpacity(1);
        UnitTesting.setVisible(true);
    }

    @FXML
    protected void OpenFun() {
        System.out.println("Switching to fun view");
        BM_Title.setOpacity(0.5);
        FUN_Title.setOpacity(1);
        UT_Title.setOpacity(0.5);
        UnitTesting.setVisible(false);
        Benchmarking.setVisible(false);
        FunDemos.setVisible(true);

    }

    private void addListeners() {
        UT_RunBtn.setOnAction(e -> runAllTests());
        UT_RunAll.setOnAction(e -> runUnitTests());
        UT_RunBasicTwitter.setOnAction(e -> runBasicTwitterTest());
        GC_Reset.setOnAction(e -> resetButtons());
        GC_Refresh.setOnAction(e -> initalizeGraph(0));
        GC_Help.setOnAction(e -> openHelpPage());
        for (CheckBox box : toggles) {
            box.setOnMouseClicked(e -> initalizeGraph(0));
        }
    }

    private void playOof() {
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    private void openHelpPage() {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("https://sashaphoto.ca/COMP250FinalDebuggerHelp/"));
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void resetButtons() {
        for (CheckBox box : toggles) {
            box.setSelected(false);
        }
        initalizeGraph(0);
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
        int WINDOW_SIZE = 2000;
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
        lineChart.setTitle("Runtime Efficiency");
        lineChart.setAnimated(true); // disable animations
        ArrayList<XYChart.Series<String, Number>> plots = new ArrayList<XYChart.Series<String, Number>>();
        HashMap<XYChart.Series<String, Number>, Long> plotsRunTime = new HashMap<XYChart.Series<String, Number>, Long>();


        for (CheckBox box : toggles) {
            if (box.isSelected()) {
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName(box.getText());  //TODO: Better naming
                plotsRunTime.put(series, 0L);
            }
        }


        // add series to chart
        lineChart.getData().addAll(plotsRunTime.keySet());

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
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            counter.getAndIncrement();
            Long value;
            Long slowValue;
            for (Map.Entry<XYChart.Series<String, Number>, Long> entry : plotsRunTime.entrySet()) {
                entry.setValue(ComputeRuntime(entry.getKey().getName(), counter.get()));
            }


            //System.out.println("[Grapher] Data updated.");

            // Update the chart
            Platform.runLater(() -> {
                for (Map.Entry<XYChart.Series<String, Number>, Long> entry : plotsRunTime.entrySet()) {
                    entry.getKey().getData().add(new XYChart.Data<String, Number>(Integer.toString(counter.get()), entry.getValue()));
                    if (entry.getKey().getData().size() > 75) {
                        lineChart.setVerticalGridLinesVisible(false);
                        lineChart.setHorizontalGridLinesVisible(false);
                        lineChart.setCreateSymbols(false);
                    }
                }
            });
        }, 0, 250, TimeUnit.MILLISECONDS);

    }

    private long ComputeRuntime(String input, int count) {
        if (GC_TurboMode.isSelected()) {
            count *= GC_TurboFactor.getValue();
        }
        if (input.equals(GC_FastSort.getText())) {
            return BM.timedSort(count);
        } else if (input.equals(GC_Remove.getText())) {
            return BM.timedRemove(count);
        } else if (input.equals(GC_Rehash.getText())) {
            return BM.timedRehash(count);
        } else if (input.equals(GC_Values.getText())) {
            return BM.timedValues(count);
        } else if (input.equals(GC_Constructor.getText())) {
            return BM.timedMyHashTable(count);
        } else if (input.equals(GC_Keys.getText())) {
            return BM.timedKeys(count);
        } else if (input.equals(GC_Get.getText())) {
            return BM.timedGetAtSize(count);
        } else if (input.equals(GC_Put.getText())) {
            return BM.timedPutAtSize(count);
        } else if (input.equals(GC_ArrayListMergeSort.getText())) {
            return BM.timedSortReference(count);
        } else if (input.equals(GC_ProfSlowSort.getText())) {
            return BM.timedSlowSort(count);
        } else if (input.equals(GC_Iter.getText())) {
            return BM.timedInterator(count);
        } else if (input.equals(GC_Next.getText())) {
            return BM.timedIteratorNext(count);
        } else if (input.equals(GC_J_Constructor.getText())) {
            return BM.timedMyHashTableReference(count);
        } else if (input.equals(GC_J_Put.getText())) {
            return BM.timedPutReference(count);
        } else if (input.equals(GC_J_Get.getText())) {
            return BM.timedGetAtSizeRefernce(count);
        } else if (input.equals(GC_J_Remove.getText())) {
            return BM.timedRemoveReference(count);
        } else if (input.equals(GC_J_Values.getText())) {
            return BM.timedValuesReference(count);
        } else if (input.equals(GC_J_Keys.getText())) {
            return BM.timedKeysReference(count);
        } else if (input.equals(GC_Twit_Trending.getText())) {
            return tBM.timedTwitterTrending(count, (int) (count * GC_StopWordFactor.getValue()));
        } else if (input.equals(GC_Twit_Constructor.getText())) {
            return tBM.timedTwitterConstructor(count, (int) (count * GC_StopWordFactor.getValue()));
        } else if (input.equals(GC_Twit_ByDate.getText())) {
            return tBM.timedTwitterByDate(count);
        } else if (input.equals(GC_Twit_ByAuth.getText())) {
            return tBM.timedTwitterByAuth(count);
        } else if (input.equals(GC_Twit_Add.getText())) {
            return tBM.timedTwitterAdd(count);
        } else if (input.equals(GC_Twit_ConstructorII.getText())) {
            return tBM.timedTwitterConstructor(count, (int) (count * GC_StopWordFactorII.getValue()));
        } else if (input.equals(GC_Twit_TrendingII.getText())) {
            return tBM.timedTwitterTrending(count, (int) (count * GC_StopWordFactorII.getValue()));
        } else {
            return 0L;
        }
    }

    private void enableDarkTheme() {
        anchorPane.getStylesheets().add(" /COMP250_A4_W2020_Test_Visualizer_JFX/mondea_dark.css");
    }

    private void disableDarkTheme() {
        anchorPane.getStylesheets().remove(" /COMP250_A4_W2020_Test_Visualizer_JFX/mondea_dark.css");
    }
}
