package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LeadBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LeadBuffCooldown;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Sheath extends Artifact {

    {
        image = ItemSpriteSheet.ARTIFACT_SHEATH;
        defaultAction = AC_LEAD;

        bones = false;
    }

    public static final String AC_LEAD = "LEAD";

    public int getLvl() {
        return this.level();
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped( hero ) && !cursed)
            actions.add(AC_LEAD);
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        super.execute(hero, action);

        if (action.equals(AC_LEAD)){
            if (hero.buff(LeadBuffCooldown.class) != null) {
                GLog.i( Messages.get(Artifact.class, "need_to_cooldown") );
            } else {
                if (hero.buff(LeadBuff.class) == null) {
                    if (!isEquipped( hero )) GLog.i( Messages.get(Artifact.class, "need_to_equip") );
                    else {
                        hero.spend( 1f );
                        hero.busy();
                        Buff.prolong(hero, LeadBuff.class, 5f+level());
                        Talent.onArtifactUsed(Dungeon.hero);
                        updateQuickslot();
                    }
                } else {
                    hero.buff(LeadBuff.class).detach();
                    if (hero.buff(ArtifactRecharge.class) == null) {
                        Buff.prolong(hero, LeadBuffCooldown.class, 5f);
                    }
                }
            }
        }
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new Sheath.sheathUpgrade();
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc");
    }

    public class sheathUpgrade extends ArtifactBuff{

        public void gainExp( float levelPortion ) {
            if (cursed || levelPortion == 0) return;

            exp += Math.round(levelPortion*100);

            if (exp > 100+level()*100 && level() < levelCap){
                exp -= 100+level()*100;
                GLog.p( Messages.get(this, "levelup") );
                upgrade();
            }
        }
    }
}