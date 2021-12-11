package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;


public class LeadBuffCooldown extends FlavourBuff {

    public int icon() {
        return BuffIndicator.TIME;
    }

    public void tintIcon(Image icon) {
        icon.hardlight(0.0f, 0.0f, 0.2f);
    }

    public float iconFadePercent() {
        return Math.max(0, visualcooldown() / 5);
    }

    public String toString() {
        return Messages.get(this, "name");
    }

    public String desc() { return Messages.get(this, "desc", dispTurns(visualcooldown())); }
}
