package com.td.resources;

import com.td.game.domain.Adventurer;
import com.td.game.domain.GameMap;
import com.td.game.domain.PlayerClass;
import com.td.game.domain.Point;
import com.td.game.gameobjects.Monster;
import com.td.game.resource.ResourceFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class ResourceSystemTest {

    @Autowired
    private ResourceFactory resourceFactory;

    @Test
    public void testGameMapLoading() {
        GameMap.GameMapResource res = resourceFactory.loadResource("GameMap.json", GameMap.GameMapResource.class);
        GameMap map = new GameMap(res);
        assertNotNull(res);
        assertEquals(map.getTitle(new Point<>(10L, 1L)).getTitleType(), new Long(0));
    }

    @Test
    public void testMonsterLoading() {
        Monster.MonsterResource mr = resourceFactory.loadResource("RedMonster.json", Monster.MonsterResource.class);
        assertNotNull(mr);
        Monster monster = new Monster(mr);
        assertEquals(monster.getHp(), 100);
    }

    @Test
    public void testPlayerClassLoading() {
        PlayerClass pc = resourceFactory.loadResource("Adventurer.json", Adventurer.class);
        assertNotNull(pc);
        assertEquals(3, pc.getAvailableTowers().size());
    }
}
