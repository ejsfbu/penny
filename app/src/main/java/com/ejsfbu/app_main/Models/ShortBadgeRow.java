package com.ejsfbu.app_main.Models;

import com.ejsfbu.app_main.Adapters.ShortBadgeRowAdapter;

import java.util.List;

public class ShortBadgeRow {

    private Reward badge1;
    private Reward badge2;
    private Reward badge3;

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

    public static void makeShortBadgeRows(List<Reward> rewards,
                                          ShortBadgeRowAdapter shortBadgeRowAdapter,
                                          List<ShortBadgeRow> shortBadgeRows) {
        for (int i = 0; i < rewards.size() - (rewards.size() % 3); i += 3) {
            ShortBadgeRow shortBadgeRow = new ShortBadgeRow();
            shortBadgeRow.setBadge1(rewards.get(i));
            shortBadgeRow.setBadge2(rewards.get(i + 1));
            shortBadgeRow.setBadge3(rewards.get(i + 2));
            shortBadgeRows.add(shortBadgeRow);
            shortBadgeRowAdapter.notifyItemInserted(shortBadgeRows.size() - 1);
        }
        if (rewards.size() % 3 == 1) {
            ShortBadgeRow shortBadgeRow = new ShortBadgeRow();
            shortBadgeRow.setBadge1(rewards.get(rewards.size() - 1));
            shortBadgeRows.add(shortBadgeRow);
            shortBadgeRowAdapter.notifyItemInserted(shortBadgeRows.size() - 1);
        }
        if (rewards.size() % 3 == 2) {
            ShortBadgeRow shortBadgeRow = new ShortBadgeRow();
            shortBadgeRow.setBadge1(rewards.get(rewards.size() - 2));
            shortBadgeRow.setBadge2(rewards.get(rewards.size() - 1));
            shortBadgeRows.add(shortBadgeRow);
            shortBadgeRowAdapter.notifyItemInserted(shortBadgeRows.size() - 1);
        }
    }
}
