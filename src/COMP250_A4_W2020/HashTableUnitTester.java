package COMP250_A4_W2020;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class HashTableUnitTester {
    MyHashTable<String, Tweet> ht;
    RandomTweets rand;
    private JPanel mainWindow;
    private JTextArea Output;
    private JButton button1;

    public HashTableUnitTester() {
        ht = new MyHashTable<String, Tweet>(3);
        rand = new RandomTweets();
        addListeners();
        createUIComponents();
    }

    public HashTableUnitTester(RandomTweets rand) {
        ht = new MyHashTable<String, Tweet>(3);
        this.rand = rand;
        addListeners();
        createUIComponents();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("HashTableUnitTester");
        frame.setContentPane(new HashTableUnitTester().mainWindow);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setVisible(true);
    }

    private void addListeners() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Output.setText(runTests());
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String runTests() {
        StringBuilder sb = new StringBuilder(iteratorTest());
        sb.append("\n").append(keysTest());
        sb.append("\n" + valuesTest());
        sb.append("\n").append(sortTest());
        return sb.toString();
    }

    public String iteratorTest() {
        int fails = 0;
        Tweet[] tweetSet = rand.nextTweets(50, true);
        StringBuilder output = new StringBuilder("[ITERATOR TEST] Commencing test: Starting with " + tweetSet.length + " tweets being added.\n");
        try {
            ht = new MyHashTable<String, Tweet>(5);
            for (Tweet t : tweetSet) {
                ht.put(t.getDateAndTime(), t);
            }
            if (ht.size() != tweetSet.length) {
                output.append("[ITERATOR TEST] Failed put. Tried to add 15 tweets, but .size() reports a size of ").append(ht.size()).append("\n");
                fails++;
            }

            ArrayList<Tweet> iterOutput = new ArrayList<>();

            for (HashPair<String, Tweet> pair : ht) {
                iterOutput.add(pair.getValue());
            }

            if (!iterOutput.containsAll(Arrays.asList(tweetSet))) {
                output.append("[ITERATOR TEST] Failed because output of iterator does not contain all of the elements from the original.\n");
                fails++;
            }
        } catch (Exception e) {
            fails++;
            output.append("[ITERATOR TEST] Caught an exception!").append(e.getMessage()).append("\n");
        }

        output.append("[ITERATOR TEST] Failed ").append(fails).append(" times!\n");
        return output.toString();
    }

    public String keysTest() {
        int fails = 0;
        Tweet[] tweetSet = rand.nextTweets(50, true);
        StringBuilder output = new StringBuilder("[KEYS TEST] Commencing test: Starting with " + tweetSet.length + " tweets being added.\n");
        try {
            ht = new MyHashTable<String, Tweet>(5);
            for (Tweet t : tweetSet) {
                ht.put(t.getDateAndTime(), t);
            }
            if (ht.size() != tweetSet.length) {
                output.append("[KEYS TEST] Failed put. Tried to add 50 tweets, but .size() reports a size of ").append(ht.size()).append("\n");
                fails++;
            }

            ArrayList<String> iterOutput = ht.keys();

            if (iterOutput.size() != tweetSet.length) {
                output.append("[KEYS TEST] Resultant array size differes from size of input").append(iterOutput.size()).append("\n");
                fails++;
            }

            ArrayList<String> trueKeys = new ArrayList<>();

            for (Tweet t : tweetSet) {
                trueKeys.add(t.getDateAndTime());
            }

            if (!iterOutput.containsAll(trueKeys)) {
                output.append("[KEYS TEST] Resultant array content differs from ideal output").append("\n");
                fails++;
            }


        } catch (Exception e) {
            fails++;
            output.append("[KEYS TEST] Caught an exception!").append(e.getMessage()).append("\n");
        }

        output.append("[KEYS TEST] Failed ").append(fails).append(" times!\n");
        return output.toString();
    }

    public String valuesTest() {
        int fails = 0;
        Tweet[] tweetSet = rand.nextTweets(50, true);
        StringBuilder output = new StringBuilder("[VALUES TEST] Commencing test: Starting with " + tweetSet.length + " tweets being added.\n");
        try {
            ht = new MyHashTable<String, Tweet>(5);
            for (Tweet t : tweetSet) {
                ht.put(t.getDateAndTime(), t);
            }
            if (ht.size() != tweetSet.length) {
                output.append("[VALUES TEST] Failed put. Tried to add 50 tweets, but .size() reports a size of ").append(ht.size()).append("\n");
                fails++;
            }

            ArrayList<Tweet> iterOutput = ht.values();

            if (iterOutput.size() != tweetSet.length) {
                output.append("[VALUES TEST] Resultant array size differes from size of input").append(iterOutput.size()).append("\n");
                fails++;
            }

            ArrayList<Tweet> trueVals = new ArrayList<>(Arrays.asList(tweetSet));

            if (!iterOutput.containsAll(trueVals)) {
                output.append("[VALUES TEST] Resultant array content differs from ideal output").append("\n");
                fails++;
            } else {
                output.append("[VALUES TEST] Auto tests passed. Here are the tweets from Values()\n");
                for (Tweet t : iterOutput) {
                    output.append("                                                       ").append(t.toString()).append("\n");
                }
            }


        } catch (Exception e) {
            fails++;
            output.append("[VALUES TEST] Caught an exception!").append(e.getMessage()).append("\n");
        }

        output.append("[VALUES TEST] Failed ").append(fails).append(" times!\n");
        return output.toString();
    }

    public String sortTest() {
        int fails = 0;
        Tweet[] tweetSet = rand.nextTweets(50, true);
        StringBuilder output = new StringBuilder("[SORT TEST] Commencing test: Starting with " + tweetSet.length + " tweets being added.\n");
        try {
            ht = new MyHashTable<String, Tweet>(5);
            for (Tweet t : tweetSet) {
                ht.put(t.getDateAndTime(), t);
            }
            if (ht.size() != tweetSet.length) {
                output.append("[SORT TEST] Failed put. Tried to add 50 tweets, but .size() reports a size of ").append(ht.size()).append("\n");
                fails++;
            }

            ArrayList<String> resultOfSlow = MyHashTable.slowSort(ht);
            ArrayList<String> resultOfFast = MyHashTable.fastSort(ht);
            if (resultOfFast.size() != resultOfSlow.size()) {
                fails++;
                output.append("[SORT TEST] Failed sort by size of sorted array. Should be 50, is ").append(resultOfFast.size()).append("\n");
            }
            boolean matches = true;
            for (int i = 0; i < resultOfSlow.size(); i++) {
                if (!resultOfFast.get(i).equals(resultOfSlow.get(i))) {
                    matches = false;
                    output.append("[SORT TEST] Difference between slow and fast outputs at index ").append(i).append(" which contains data (fast) ").append(resultOfFast.get(i)).append(" and (slow) ").append(resultOfSlow.get(i)).append("\n");
                    fails++;
                }
            }

            if (!matches) {
                output.append("[SORT TEST] FAILED SORT DUE TO DIFFERENCE BETWEEN SLOW AND FAST OUTPUT\n");
                output.append("IDX     SLOW                                           FAST\n");
                for (int i = 0; i < resultOfFast.size(); i++) {
                    String s = resultOfFast.get(i);
                    output.append(i).append("         ").append(s).append("                   ").append(resultOfSlow.get(i)).append("\n");
                }
                fails++;
            }

        } catch (Exception e) {
            fails++;
            output.append("[SORT TEST] Caught an exception!").append(e.getMessage()).append("\n");
        }

        output.append("[SORT TEST] Failed ").append(fails).append(" times!\n");
        return output.toString();
    }

}
