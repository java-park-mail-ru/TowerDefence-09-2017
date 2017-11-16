package com.td.game.services;

import com.td.game.GameSession;
import com.td.game.domain.GameMap;
import com.td.game.domain.Player;
import com.td.game.domain.PlayerClass;
import com.td.game.gameobjects.Tower;
import com.td.game.resource.ResourceFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TowerManager {

    @NotNull
    private final ResourceFactory resourceFactory;

    public TowerManager(@NotNull ResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
    }

    public void processOrder(@NotNull GameSession session, @NotNull TowerOrder order) {
        Map<Long, PlayerClass> playersClasses = session.getPlayersClasses();
        PlayerClass playerClass = playersClasses.get(order.getPlayerId());
        if (playerClass
                .getAvailableTowers()
                .stream()
                .anyMatch(towerClass -> towerClass.equals(order.getTowerClass()))) {

            Tower.TowerResource resource = resourceFactory
                    .loadResource(order.getTowerClass() + ".json", Tower.TowerResource.class);
            Player player = null;
            for (Player sessionPlayer : session.getPlayers()) {
                if (sessionPlayer.getId().equals(order.playerId)) {
                    player = sessionPlayer;
                    break;
                }
            }
            GameMap map = session.getGameMap();
            if (player != null && player.getMoney() >= resource.getCost() && map.isTitleFree(order.xcoord, order.ycoord)) {
                Tower tower = new Tower(resource);
                tower.setTitlePosition(order.xcoord, order.ycoord);
                session.getGameMap().placeObject(order.xcoord, order.ycoord, tower);
                session.getTowers().add(tower);
                session.addArea(tower.getRangeArea());
                player.setMoney(player.getMoney() - resource.getCost());
            }
        }

    }


    public static class TowerOrder {
        private long xcoord;
        private long ycoord;
        private String towerClass;
        private Long playerId;

        public TowerOrder(long xcoord, long ycoord, String towerClass, Long playerId) {
            this.xcoord = xcoord;
            this.ycoord = ycoord;
            this.towerClass = towerClass;
            this.playerId = playerId;
        }

        public Long getPlayerId() {
            return playerId;
        }

        public String getTowerClass() {
            return towerClass;
        }

        public long getXcoord() {
            return xcoord;
        }

        public long getYcoord() {
            return ycoord;
        }
    }
}
