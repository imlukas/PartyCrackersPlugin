package dev.imlukas.partycrackersplugin.registry;

import dev.imlukas.partycrackersplugin.impl.PartyCracker;

import java.util.*;

public class PartyCrackerRegistry {

    private final Map<String, PartyCracker> crackerMap = new HashMap<>();

    public void register(PartyCracker cracker) {
        crackerMap.put(cracker.getIdentifier(), cracker);
    }

    public PartyCracker getCracker(String name) {
        return crackerMap.get(name);
    }

    public List<PartyCracker> getCrackers() {
        return new ArrayList<>(crackerMap.values());
    }

    public Set<String> getCrackerIdentifiers() {
        return crackerMap.keySet();
    }

}
