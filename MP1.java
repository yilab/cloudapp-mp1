import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    public String[] process() throws Exception {
        List<String> stopWordsList = Arrays.asList(stopWordsArray);
        Map<Integer, String> linesMap = new HashMap<Integer, String>();
        String line;
        // populate linesMap
        try (
                InputStream fis = new FileInputStream(inputFileName);
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(isr)
        ){
            Integer i = 0;
            while ((line = br.readLine()) != null){
                linesMap.put(i++,line);
            }
        }
        // populate countMap
        Integer[] indexes = getIndexes();
        Map<String, Integer> countMap = new HashMap<String, Integer>();
        for(Integer j=0; j < indexes.length; j++){
            line = linesMap.get(indexes[j]);
//            System.out.println(line);
            StringTokenizer st = new StringTokenizer(line, delimiters);
            while (st.hasMoreTokens()){
                String word = st.nextToken().trim().toLowerCase();
                if(!stopWordsList.contains(word)){
                    if(countMap.containsKey(word)){
                        countMap.put(word, countMap.get(word) + 1);
                    }else{
                        countMap.put(word, 0);
                    }
//                    System.out.println(word);
//                    System.out.println(countMap.get(word));
                }
            }
        }
        // sort
        List<Map.Entry<String, Integer>> countList = new ArrayList<Map.Entry<String, Integer>>(countMap.entrySet());
        Collections.sort(countList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> t0, Map.Entry<String, Integer> t1) {
                if(t0.getValue() > t1.getValue()) return -1;
                if(t0.getValue() < t1.getValue()) return 1;
                return 0;
            }
        });
//        System.out.println(countList);
        String[] ret = new String[20];
        for( Integer k = 0; k < 20; k++){
            ret[k] = countList.get(k).getKey();
        }
        return ret;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
}
