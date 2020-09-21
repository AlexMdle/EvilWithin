package champ.stances;

import champ.ChampChar;
import champ.powers.CounterPower;
import champ.powers.ResolvePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class UltimateStance extends AbstractChampStance {

    public static final String STANCE_ID = "champ:UltimateStance";
    private static long sfxId = -1L;

    public UltimateStance() {
        this.ID = STANCE_ID;// 21
        this.name = ChampChar.characterStrings.TEXT[12];
        this.updateDescription();// 23
    }// 24

    int timeLeft = 2;

    @Override
    public void onEnterStance() {
        super.onEnterStance();
        timeLeft = 2;
    }

    @Override
    public void atStartOfTurn() {
        timeLeft--;
        if (timeLeft == 0) {
            AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction("Neutral"));
        }
    }

    @Override
    public void onExitStance() {
        super.onExitStance();
    }

    @Override
    public void updateDescription() {
        this.description = ChampChar.characterStrings.TEXT[13] + timeLeft + ChampChar.characterStrings.TEXT[14];
    }

    @Override
    public void technique() {
        AbstractDungeon.actionManager.addToBottom(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, 4));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ResolvePower(4), 4));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new CounterPower(6), 6));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(1));
    }

    @Override
    public void finisher() {
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                if (AbstractDungeon.player.hasPower(ResolvePower.POWER_ID)) {
                    int x = AbstractDungeon.player.getPower(ResolvePower.POWER_ID).amount;
                    if (x > 0) {
                        addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, x));
                        addToTop(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, AbstractDungeon.player.getPower(ResolvePower.POWER_ID)));
                    }
                }
            }
        });
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, 12));
    }
}
