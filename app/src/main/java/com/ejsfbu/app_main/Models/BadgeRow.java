package com.ejsfbu.app_main.Models;

import com.ejsfbu.app_main.Adapters.BadgeRowAdapter;

import java.util.List;

public class BadgeRow {

    private Reward badge1;
    private Reward badge2;
    private Reward badge3;
    private Reward badge4;
    private Reward badge5;

    public Reward getBadge1() {
        return badge1;
    }

    public void setBadge1(Reward badge1) {
        this.badge1 = badge1;
    }

    public Reward getBadge2() {
        return badge2;
    }

    public void setBadge2(Reward badge2) {
        this.badge2 = badge2;
    }

    public Reward getBadge3() {
        return badge3;
    }

    public void setBadge3(Reward badge3) {
        this.badge3 = badge3;
    }

    public Reward getBadge4() {
        return badge4;
    }

    public void setBadge4(Reward badge4) {
        this.badge4 = badge4;
    }

    public Reward getBadge5() {
        return badge5;
    }

    public void setBadge5(Reward badge5) {
        this.badge5 = badge5;
    }

    public static void makeBadgeRows(List<Reward> rewards, BadgeRowAdapter badgeRowAdapter,
                               List<BadgeRow> badgeRows) {
        for (int i = 0; i < rewards.size() - (rewards.size() % 5); i += 5) {
            BadgeRow badgeRow = new BadgeRow();
            badgeRow.setBadge1(rewards.get(i));
            badgeRow.setBadge2(rewards.get(i + 1));
            badgeRow.setBadge3(rewards.get(i + 2));
            badgeRow.setBadge4(rewards.get(i + 3));
            badgeRow.setBadge5(rewards.get(i + 4));
            badgeRows.add(badgeRow);
            badgeRowAdapter.notifyItemInserted(badgeRows.size() - 1);
        }
        if (rewards.size() % 5 == 1) {
            BadgeRow badgeRow = new BadgeRow();
            badgeRow.setBadge1(rewards.get(rewards.size() - 1));
            badgeRows.add(badgeRow);
            badgeRowAdapter.notifyItemInserted(badgeRows.size() - 1);
        }
        if (rewards.size() % 5 == 2) {
            BadgeRow badgeRow = new BadgeRow();
            badgeRow.setBadge1(rewards.get(rewards.size() - 2));
            badgeRow.setBadge2(rewards.get(rewards.size() - 1));
            badgeRows.add(badgeRow);
            badgeRowAdapter.notifyItemInserted(badgeRows.size() - 1);
        }
        if (rewards.size() % 5 == 3) {
            BadgeRow badgeRow = new BadgeRow();
            badgeRow.setBadge1(rewards.get(rewards.size() - 3));
            badgeRow.setBadge2(rewards.get(rewards.size() - 2));
            badgeRow.setBadge3(rewards.get(rewards.size() - 1));
            badgeRows.add(badgeRow);
            badgeRowAdapter.notifyItemInserted(badgeRows.size() - 1);
        }
        if (rewards.size() % 5 == 4) {
            BadgeRow badgeRow = new BadgeRow();
            badgeRow.setBadge1(rewards.get(rewards.size() - 4));
            badgeRow.setBadge2(rewards.get(rewards.size() - 3));
            badgeRow.setBadge3(rewards.get(rewards.size() - 2));
            badgeRow.setBadge4(rewards.get(rewards.size() - 1));
            badgeRows.add(badgeRow);
            badgeRowAdapter.notifyItemInserted(badgeRows.size() - 1);
        }
    }
}
