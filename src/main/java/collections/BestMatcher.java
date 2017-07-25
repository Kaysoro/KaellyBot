package collections;

import org.apache.commons.lang3.tuple.Pair;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve on 29/06/2017.
 */
public class BestMatcher {

    private String base;
    private String[] pattern;
    private List<Pair<String, String>> bestMatches;
    private int bestPoint;

    public BestMatcher(String base){
        this.base = Normalizer.normalize(base.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();
        this.pattern = this.base.split("\\s+");
        bestMatches = new ArrayList<>();
        bestPoint = 0;
    }

    public void evaluate(Pair<String, String> proposal){
        int points = 0;
        String key = Normalizer.normalize(proposal.getKey().trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();

        if (! key.equals(base)) {
            for (String base : pattern)
                if (key.contains(base))
                    points++;

            if (points > bestPoint) {
                bestMatches.clear();
                bestMatches.add(proposal);
                bestPoint = points;
            } else if (points == bestPoint)
                bestMatches.add(proposal);
        }
        else {
            bestMatches.clear();
            bestMatches.add(proposal);
            bestPoint = Integer.MAX_VALUE;
        }
    }

    public void evaluateAll(List<Pair<String, String>> proposals){
        for(Pair pair : proposals)
            evaluate(pair);
    }

    public boolean isUnique(){
        return bestMatches.size() == 1;
    }

    public boolean isEmpty(){
        return bestMatches.isEmpty();
    }

    public List<Pair<String, String>> getBests(){
        return bestMatches;
    }


    public Pair<String, String> getBest(){
        if (isUnique())
            return bestMatches.get(0);
        else
            return null;
    }
}
