package thePackmaster.actions.sneckopack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thePackmaster.util.Wiz;

import java.util.ArrayList;

public class SheddingAction extends AbstractGameAction {

    public SheddingAction(int amount) {
        this.amount = amount;
    }

    @Override
    public void update() {
        ArrayList<AbstractCard> toRandomize = new ArrayList<>();
        ArrayList<AbstractCard> tempHand = new ArrayList<>(AbstractDungeon.player.hand.group);
        while (toRandomize.size() < amount && !tempHand.isEmpty()) {
            //identify highest cost cards
            int maxCost = -1;
            ArrayList<AbstractCard> maxCostCards = new ArrayList<>();
            for (AbstractCard c : tempHand) {
                if (c.costForTurn > maxCost) {
                    maxCost = c.costForTurn;
                    maxCostCards.clear();
                    maxCostCards.add(c);
                } else if (c.costForTurn == maxCost) {
                    maxCostCards.add(c);
                }
            }

            //Stop if no randomizable cards are left
            if (maxCost < 0) break;

            //choose which are randomized, remove them from tempHand
            while (!maxCostCards.isEmpty() && toRandomize.size() < amount) {
                AbstractCard card = Wiz.getRandomItem(maxCostCards);
                toRandomize.add(card);
                tempHand.remove(card);
                maxCostCards.remove(card);
            }
        }
        if (!toRandomize.isEmpty()) {
            addToTop(new RandomizeCostAction(toRandomize.toArray(new AbstractCard[0])));
        }
        isDone = true;
    }
}
