package COMP250_A4_W2020;

import java.util.*;

public class HashTableBenchmark {
    private final MyHashTable<String, Tweet> userTable;
    protected RandomTweets rand;
    private Hashtable<String, Tweet> javaTable;

    public HashTableBenchmark() {
        userTable = new MyHashTable<String, Tweet>(10);
        rand = new RandomTweets(10);
    }

    public HashTableBenchmark(RandomTweets rand) {
        userTable = new MyHashTable<String, Tweet>(10);
        this.rand = rand;
    }

    public RandomTweets getRand() {
        return rand;
    }

    public static void main(String[] args) {
        HashTableBenchmark bm = new HashTableBenchmark();
        bm.printPutBenchMark(19);
        bm.printPutBenchMark(100);
        bm.printGetBenchMark(100);
        bm.printSortBenchmark(200);
    }

    public void printPutBenchMark(int length) {
        System.out.println(Arrays.toString(putBenchmark(length)));
    }

    public void printGetBenchMark(int length) {
        System.out.println(Arrays.toString(getBenchmark(length)));
    }

    public void printSortBenchmark(int length) {
        System.out.println(Arrays.toString(sortBenchmark(length)));
    }

    public long timedPutReference(Tweet input) {
        long startTime = System.nanoTime();
        javaTable.put(input.getDateAndTime(), input);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public long timedPut(Tweet input) {
        long startTime = System.nanoTime();
        userTable.put(input.getDateAndTime(), input);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public long timedPut(int howMany) {
        Tweet[] tweets = rand.nextTweets(howMany, true);
        MyHashTable<String, Tweet> userTable = new MyHashTable<String, Tweet>(10);
        for (int i = 0; i < tweets.length - 1; i++) {
            userTable.put(tweets[i].getDateAndTime(), tweets[i]);
        }
        long startTime = System.nanoTime();
        userTable.put(tweets[tweets.length - 1].getDateAndTime(), tweets[tweets.length - 1]);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public long timedPutReference(int howMany) {
        Tweet[] tweets = rand.nextTweets(howMany, true);
        Hashtable<String, Tweet> javaTable = new Hashtable<>(10);
        for (int i = 0; i < tweets.length - 1; i++) {
            javaTable.put(tweets[i].getDateAndTime(), tweets[i]);
        }
        long startTime = System.nanoTime();
        javaTable.put(tweets[tweets.length - 1].getDateAndTime(), tweets[tweets.length - 1]);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public long[] timedPut(Tweet[] input) {
        long[] output = new long[input.length];
        for (int i = 0; i < output.length; i++) {
            output[i] = timedPut(input[i]);
        }
        return output;
    }

    public long timedPutAtSize(int size) {
        long[] result = putBenchmark(size);
        return result[result.length - 1];
    }

    public long timedGetAtSize(int size) {
        long[] result = getBenchmark(size);
        return result[result.length - 1];
    }

    public long timedGetAtSizeRefernce(int size) {
        long[] result = getBenchmarkReference(size);
        return result[result.length - 1];
    }

    public long timedGet(String key) {
        long startTime = System.nanoTime();
        userTable.get(key);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public long timedSlowSort(int length) {
        MyHashTable<String, Tweet> testSubject = new MyHashTable<String, Tweet>(10);
        for (int i = 0; i < length; i++) {
            Tweet toAdd = rand.nextTweet(true);
            testSubject.put(toAdd.getDateAndTime(), toAdd);
        }
        Tweet a = rand.nextTweet(true);
        System.out.println("sorting table of size: " + testSubject.size());
        long startTime = System.nanoTime();
        MyHashTable.slowSort(testSubject);  //TODO: is it myHastTable. or testSubject.
        long endTime = System.nanoTime();
        System.out.println("runtime was " + (endTime - startTime));
        return endTime - startTime;
    }

    public long timedSort(int length) {
        MyHashTable<String, Tweet> testSubject = new MyHashTable<String, Tweet>(10);
        for (int i = 0; i < length; i++) {
            Tweet toAdd = rand.nextTweet(true);
            testSubject.put(toAdd.getDateAndTime(), toAdd);
        }
        Tweet a = rand.nextTweet(true);
        System.out.println("sorting table of size: " + testSubject.size());
        long startTime = System.nanoTime();
        MyHashTable.fastSort(testSubject);  //TODO: is it myHastTable. or testSubject.
        long endTime = System.nanoTime();
        System.out.println("runtime was " + (endTime - startTime));
        return endTime - startTime;
    }

    protected long[] timedGet(String[] input) {
        long[] output = new long[input.length];
        for (int i = 0; i < output.length; i++) {
            output[i] = timedGet(input[i]);
        }
        return output;
    }

    public long summedTimedGet(String[] input) {
        return sum(timedGet(input));
    }

    public long summedTimedPut(Tweet[] input) {
        return sum(timedPut(input));
    }

    public long autoSummedTimedPut(int length) {
        System.out.println("putting to table of size: " + length);
        return summedTimedPut(rand.nextTweets(length, true));
    }

    public long autoSummedTimedGet(int length) {
        System.out.println("getting from table of size: " + length);
        MyHashTable<String, Tweet> table = new MyHashTable<String, Tweet>(10);
        Tweet[] tweets = rand.nextTweets(length, true);
        for (Tweet t : tweets) {
            table.put(t.getDateAndTime(), t);
        }
        String[] keys = new String[tweets.length];
        for (int i = 0; i < tweets.length; i++) {
            keys[i] = tweets[i].getDateAndTime();
        }
        return summedTimedGet(keys);
    }

    public long timedInterator(int size) {
        MyHashTable<String, Tweet> table = new MyHashTable<String, Tweet>(10);
        for (int i = 0; i < size; i++) {
            Tweet toAdd = rand.nextTweet();
            table.put(toAdd.getDateAndTime(), toAdd);
        }
        long startTime = System.nanoTime();
        Object dummy = table.iterator();
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public long timedIteratorNext(int size) {
        MyHashTable<String, Tweet> table = new MyHashTable<String, Tweet>(10);
        for (int i = 0; i < size; i++) {
            Tweet toAdd = rand.nextTweet();
            table.put(toAdd.getDateAndTime(), toAdd);
        }
        Iterator iter = table.iterator();
        for (int i = 0; i < size - 1; i++) {
            iter.next();
        }
        long startTime = System.nanoTime();
        Object dummy = iter.next();
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public long timedIteratorHasNext(int size) {
        MyHashTable<String, Tweet> table = new MyHashTable<String, Tweet>(10);
        for (int i = 0; i < size; i++) {
            Tweet toAdd = rand.nextTweet();
            table.put(toAdd.getDateAndTime(), toAdd);
        }
        Iterator iter = table.iterator();
       /* for(int i = 0; i < size - 1; i++) {
            iter.next();
        }*/
        long startTime = System.nanoTime();
        Object dummy = iter.hasNext();
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public long timedValues(int size) {
        MyHashTable<String, Tweet> table = new MyHashTable<String, Tweet>(10);
        for (int i = 0; i < size; i++) {
            Tweet toAdd = rand.nextTweet();
            table.put(toAdd.getDateAndTime(), toAdd);
        }
        long startTime = System.nanoTime();
        ArrayList<Tweet> dummy = table.values();
        long endTime = System.nanoTime();
        System.out.println(dummy.toString());
        return endTime - startTime;
    }

    public long timedValuesReference(int size) {
        Hashtable<String, Tweet> table = new Hashtable<>(10);
        for (int i = 0; i < size; i++) {
            Tweet toAdd = rand.nextTweet();
            table.put(toAdd.getDateAndTime(), toAdd);
        }
        long startTime = System.nanoTime();
        Collection<Tweet> dummy = table.values();
        long endTime = System.nanoTime();
        System.out.println(dummy.toString());
        return endTime - startTime;
    }

    public long timedKeys(int size) {
        MyHashTable<String, Tweet> table = new MyHashTable<String, Tweet>(10);
        for (int i = 0; i < size; i++) {
            Tweet toAdd = rand.nextTweet();
            table.put(toAdd.getDateAndTime(), toAdd);
        }
        long startTime = System.nanoTime();
        Object dummy = table.keys();
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public long timedKeysReference(int size) {
        Hashtable<String, Tweet> table = new Hashtable<>(10);
        for (int i = 0; i < size; i++) {
            Tweet toAdd = rand.nextTweet();
            table.put(toAdd.getDateAndTime(), toAdd);
        }
        long startTime = System.nanoTime();
        Object dummy = table.keys();
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public long timedRehash(int size) {
        MyHashTable<String, Tweet> table = new MyHashTable<String, Tweet>(10);
        for (int i = 0; i < size; i++) {
            Tweet toAdd = rand.nextTweet();
            table.put(toAdd.getDateAndTime(), toAdd);
        }
        long startTime = System.nanoTime();
        table.rehash();
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public long timedRemove(int size) {
        MyHashTable<String, Tweet> table = new MyHashTable<String, Tweet>(10);
        ArrayList<Tweet> added = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Tweet toAdd = rand.nextTweet();
            added.add(toAdd);
            table.put(toAdd.getDateAndTime(), toAdd);
        }
        String toRemove;
        if (added.size() > 1) {
            toRemove = added.get(rand.nextInt(added.size() - 1)).getDateAndTime();
        } else {
            toRemove = added.get(0).getDateAndTime();
        }
        System.out.println("Removing " + toRemove);
        long startTime = System.nanoTime();
        table.remove(toRemove);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public long timedRemoveReference(int size) {
        Hashtable<String, Tweet> table = new Hashtable<>(10);
        ArrayList<Tweet> added = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Tweet toAdd = rand.nextTweet();
            added.add(toAdd);
            table.put(toAdd.getDateAndTime(), toAdd);
        }
        String toRemove;
        if (added.size() > 1) {
            toRemove = added.get(rand.nextInt(added.size() - 1)).getDateAndTime();
        } else {
            toRemove = added.get(0).getDateAndTime();
        }
        System.out.println("Removing " + toRemove);
        long startTime = System.nanoTime();
        table.remove(toRemove);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    private long sum(long[] input) {
        long output = 0;
        for (long l : input) {
            output += l;
        }
        return output;
    }

    public long[] getBenchmarkReference(int length) {
        Hashtable<String, Tweet> table = new Hashtable<>(10);
        Tweet[] tweets = rand.nextTweets(length, true);
        for (Tweet t : tweets) {
            table.put(t.getDateAndTime(), t);
        }
        String[] keys = new String[tweets.length];
        for (int i = 0; i < tweets.length; i++) {
            keys[i] = tweets[i].getDateAndTime();
        }
        long[] output = new long[keys.length];
        for (int i = 0; i < output.length; i++) {
            long startTime = System.nanoTime();
            table.get(keys[i]);
            long endTime = System.nanoTime();
            output[i] = endTime - startTime;
        }
        return output;
    }

    public long[] getBenchmark(int length) {
        MyHashTable<String, Tweet> table = new MyHashTable<String, Tweet>(10);
        Tweet[] tweets = rand.nextTweets(length, true);
        for (Tweet t : tweets) {
            table.put(t.getDateAndTime(), t);
        }
        String[] keys = new String[tweets.length];
        for (int i = 0; i < tweets.length; i++) {
            keys[i] = tweets[i].getDateAndTime();
        }
        long[] output = new long[keys.length];
        for (int i = 0; i < output.length; i++) {
            long startTime = System.nanoTime();
            table.get(keys[i]);
            long endTime = System.nanoTime();
            output[i] = endTime - startTime;
        }
        return output;
    }

    public long[] putBenchmark(int length){
        return timedPut(rand.nextTweets(length, true));
    }

    public long[] sortBenchmark(int iterations) {
        long[] output = new long[iterations];
        for (int i = 1; i < iterations; i++) {
            output[i] = this.timedSort(i);
        }
        return output;
    }

    public long timedMyHashTable(int count) {
        long startTime = System.nanoTime();
        MyHashTable<MyHashTable<String, String>, MyHashTable<String, String>> ht = new MyHashTable<MyHashTable<String, String>, MyHashTable<String, String>>(count);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public long timedSortReference(int count) {
        ArrayList<String> testSubject = new ArrayList<>(rand.nextStopWords(count));
        long startTime = System.nanoTime();
        testSubject.sort(String::compareToIgnoreCase);
        long endTime = System.nanoTime();
        return endTime - startTime;

    }

    public long timedMyHashTableReference(int count) {
        long startTime = System.nanoTime();
        Hashtable<Hashtable<String, String>, Hashtable<String, String>> ht = new Hashtable<Hashtable<String, String>, Hashtable<String, String>>(count);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }
}
