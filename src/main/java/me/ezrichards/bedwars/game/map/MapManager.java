package me.ezrichards.bedwars.game.map;

import me.ezrichards.bedwars.game.generator.Generator;
import me.ezrichards.bedwars.game.generator.GeneratorType;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by Ethan Richards
 * September 11, 2020
 */
public class MapManager {

    private List<GameMap> maps = new ArrayList<>();

    public List<GameMap> getMaps() {
        return maps;
    }

    public void clearMaps() {
        maps.clear();
    }

    public void addMap(GameMap map) {
        maps.add(map);
    }

    public void removeMap(GameMap map) {
        maps.remove(map);
    }

    public GameMap getMapByName(String name) {
        for(GameMap map : maps) {
            if(map.getName().equals(name)) {
                return map;
            }
        }
        return null;
    }

    public List<Generator> getGeneratorsByType(GameMap map, GeneratorType type) {
        List<Generator> generators = new ArrayList<>();

        for(Generator generator : map.getGenerators()) {
            if(generator.getType() == type) {
                generators.add(generator);
            }
        }
        return generators;
    }
}
