package util;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve on 29/06/2017.
 */
public class BestMatcher {

    private String base;
    private String[] pattern;
    private List<Requestable> bestMatches;
    private int bestPoint;

    public BestMatcher(String base){
        this.base = Normalizer.normalize(base.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();
        this.pattern = this.base.split("\\s+");
        bestMatches = new ArrayList<>();
        bestPoint = 0;
    }

    public void evaluate(Requestable proposal){
        int points = 0;
        String key = Normalizer.normalize(proposal.getName().trim(), Normalizer.Form.NFD)
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

    public void evaluateAll(List<Requestable> proposals){
        for(Requestable proposal : proposals)
            evaluate(proposal);
    }

    public void evaluateAll(Requestable... proposals){
        for(Requestable proposal : proposals)
            evaluate(proposal);
    }

    public boolean isUnique(){
        return bestMatches.size() == 1;
    }

    public boolean isEmpty(){
        return bestMatches.isEmpty();
    }

    public List<Requestable> getBests(){
        return bestMatches;
    }


    public Requestable getBest(){
        if (isUnique())
            return bestMatches.get(0);
        else
            return null;
    }
}
