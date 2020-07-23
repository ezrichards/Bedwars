package me.erichards.bedwars.game.team;

/**
 * Made by Ethan Richards
 * June 01, 2020
 */
public class Team {

    private TeamType teamType;
    private boolean bedBroken;

    public Team(TeamType teamType, boolean bedBroken) {
        this.teamType = teamType;
        this.bedBroken = bedBroken;
    }

    public TeamType getTeamType() {
        return teamType;
    }

    public boolean isBedBroken() {
        return bedBroken;
    }
}
