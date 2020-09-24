package me.erichards.bedwars.game.team;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by Ethan Richards
 * September 09, 2020
 */
public class TeamManager {

    private List<Team> teams = new ArrayList<>();

    public List<Team> getTeams() {
        return teams;
    }

    public void clearTeams() {
        teams.clear();
    }

    public void addTeam(Team team) {
        teams.add(team);
    }

    public void removeTeam(Team team) {
        teams.remove(team);
    }

    public Team getTeamByName(String name) {
        for(Team team : teams) {
            if(team.getName().equalsIgnoreCase(name)) {
                return team;
            }
        }
        return null;
    }

    public Team getTeamByColor(ChatColor color) {
        for(Team team : teams) {
            if(team.getColor() == color) {
                return team;
            }
        }
        return null;
    }
}
