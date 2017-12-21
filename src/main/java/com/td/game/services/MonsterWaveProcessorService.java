package com.td.game.services;

import com.td.game.GameSession;
import com.td.game.domain.Wave;
import com.td.game.gameobjects.Monster;
import com.td.game.gameobjects.Path;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

@Service
public class MonsterWaveProcessorService {

    @NotNull
    private final MonsterWaveGenerator waveGenerator;

    private final Map<Wave.WaveStatus, BiConsumer<Wave, Long>> waveStatusHandlers = new HashMap<>();

    public MonsterWaveProcessorService(@NotNull MonsterWaveGenerator waveGenerator) {
        this.waveGenerator = waveGenerator;
    }

    @PostConstruct
    public void init() {
        waveStatusHandlers.put(Wave.WaveStatus.PENDING, Wave::updatePendingTime);
        waveStatusHandlers.put(Wave.WaveStatus.STARTED, (wave, delta) -> {
            wave.pollMonster(delta);
            moveMonsters(wave, delta);
        });
        waveStatusHandlers.put(Wave.WaveStatus.RUNNING, this::moveMonsters);
        waveStatusHandlers.put(Wave.WaveStatus.FINISHED, (wave, delta) -> {

        });
    }

    public void processWave(@NotNull GameSession session, long delta) {
        Wave wave = session.getCurrentWave();
        if (wave.getStatus() == Wave.WaveStatus.FINISHED) {
            session.setWaveNumber(session.getWaveNumber() + 1);
            session.setCurrentWave(waveGenerator.generateWave(session.getWaveNumber(), session.getPaths()));
            return;
        }
        wave.tryToFinishWave();
        waveStatusHandlers.get(wave.getStatus()).accept(wave, delta);

    }

    private void moveMonsters(@NotNull Wave wave, Long delta) {
        Map<Path, List<Monster>> pathBindings = wave.getPathBindings();
        Set<Monster> running = wave.getRunning();
        pathBindings.forEach((path, monsters) ->
                monsters.stream().filter(running::contains).forEach(monster -> {
                    Integer next = monster.getCoord().getIndex() + 1;
                    monster.move(delta, path.getPathPoints().get(next));
                }));
    }

    public void processCleanup(@NotNull GameSession session) {
        Wave wave = session.getCurrentWave();
        wave.passDeadMonsters();
    }

    public void processPassedMonsters(@NotNull GameSession session) {
        Wave wave = session.getCurrentWave();
        if (wave.getStatus() != Wave.WaveStatus.RUNNING
                && wave.getStatus() != Wave.WaveStatus.STARTED) {
            return;
        }
        Map<Path, List<Monster>> pathBindings = wave.getPathBindings();
        Set<Monster> running = wave.getRunning();
        pathBindings.forEach((path, monsters) ->
                monsters.stream().filter(monster -> path.getLastPoint().equals(monster.getCoord()))
                        .filter(running::contains)
                        .forEach(monster -> {
                            session.setHp(session.getHp() - monster.getWeight());
                            wave.passMonster(monster);
                        }));
    }

}
