/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
Matthias Butz <matze@odinms.de>
Jan Christian Meyer <vimes@odinms.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation version 3 as published by
the Free Software Foundation. You may not use, modify or distribute
this program under any other version of the GNU Affero General Public
License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package server.life;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import client.MapleCharacter;
import client.MapleDisease;
import client.status.MonsterStatus;
import constants.GameConstants;
import constants.skills.Bishop;
import java.util.LinkedList;
import java.util.Map;
import tools.Randomizer;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.maps.MapleMist;
import tools.ArrayMap;

/**
 *
 * @author Danny (Leifde)
 */
public class MobSkill {

    private int skillId, skillLevel, mpCon;
    private List<Integer> toSummon = new ArrayList<Integer>();
    private int spawnEffect, hp, x, y;
    private long duration, cooltime;
    private float prop;
    private Point lt, rb;
    private int limit;

    public MobSkill(int skillId, int level) {
        this.skillId = skillId;
        this.skillLevel = level;
    }

    public void setMpCon(int mpCon) {
        this.mpCon = mpCon;
    }

    public void addSummons(List<Integer> toSummon) {
        for (Integer summon : toSummon) {
            this.toSummon.add(summon);
        }
    }

    public void setSpawnEffect(int spawnEffect) {
        this.spawnEffect = spawnEffect;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setCoolTime(long cooltime) {
        this.cooltime = cooltime;
    }

    public void setProp(float prop) {
        this.prop = prop;
    }

    public void setLtRb(Point lt, Point rb) {
        this.lt = lt;
        this.rb = rb;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void applyDelayedEffect(final MapleCharacter player, final MapleMonster monster, final boolean skill, int animationTime) {
        Runnable toRun = new Runnable() {

            @Override
            public void run() {
                if (monster.isAlive()) {
                    applyEffect(player, monster, skill, null);
                }
            }
        };

        monster.getMap().getChannelServer().registerOverallAction(monster.getMap().getId(), toRun, animationTime);
    }

    public void applyEffect(MapleCharacter player, MapleMonster monster, boolean skill, List<MapleCharacter> banishPlayers) {
        MapleDisease disease = null;
        Map<MonsterStatus, Integer> stats = new ArrayMap<MonsterStatus, Integer>();
        List<Integer> reflection = new LinkedList<Integer>();
        switch (skillId) {
            case 199:
                int random = Randomizer.random(0, 10);
                switch (random) {
                    case 0:
                        player.dropMessage("You have been sealed");
                        disease = MapleDisease.SEAL;
                        break;
                    case 1:
                        player.dropMessage("You have been darkend");
                        disease = MapleDisease.DARKNESS;
                        break;
                    case 2:
                        player.dropMessage("You have been weakened");
                        disease = MapleDisease.WEAKEN;
                        break;
                    case 3:
                        player.dropMessage("You have been stunned");
                        disease = MapleDisease.STUN;
                        break;
                    case 4:
                        player.dropMessage("You have been Cursed");
                        disease = MapleDisease.CURSE;
                        break;
                    case 5:
                        player.dropMessage("You have been poisoned");
                        disease = MapleDisease.POISON;
                        break;
                    case 6:
                        player.dropMessage("You have been slowed");
                        disease = MapleDisease.SLOW;
                        break;
                    case 7:
                        player.dropMessage("You have been seduced");
                        disease = MapleDisease.SEDUCE;
                        break;
                    case 8:
                        player.dropMessage("You have been confused");
                        disease = MapleDisease.CONFUSE;
                        break;
                    case 9:
                        player.dropMessage("You have been zombied");
                        disease = MapleDisease.ZOMBIFY;
                        break;
                    case 10:
                        player.dropMessage("You evaded his after effect");
                        break;
                }
                break;
            case 100:
            case 110:
            case 150:
                stats.put(MonsterStatus.WEAPON_ATTACK_UP, Integer.valueOf(x));
                break;
            case 101:
            case 111:
            case 151:
                stats.put(MonsterStatus.MAGIC_ATTACK_UP, Integer.valueOf(x));
                break;
            case 102:
            case 112:
            case 152:
                stats.put(MonsterStatus.WEAPON_DEFENSE_UP, Integer.valueOf(x));
                break;
            case 103:
            case 113:
            case 153:
                stats.put(MonsterStatus.MAGIC_DEFENSE_UP, Integer.valueOf(x));
                break;
            case 114:
                if (lt != null && rb != null && skill) {
                    List<MapleMapObject> objects = getObjectsInRange(monster, MapleMapObjectType.MONSTER);
                    for (MapleMapObject mons : objects) {
                        MapleMonster mob = ((MapleMonster) mons);
                        long hp = mob.getMaxHp() / 10;
                        int hps = (int) Randomizer.MaxLong(hp, Integer.MAX_VALUE);
                        mob.heal(hps, getY());
                    }
                } else {
                    monster.heal(getX(), getY());
                }
                break;
            case 120:
            case 172:
            case 178:
                disease = MapleDisease.SEAL;
                break;
            case 121:
                disease = MapleDisease.DARKNESS;
                break;
            case 122:
                disease = MapleDisease.WEAKEN;
                break;
            case 123:
            case 129: // banished replaced with seduce
            case 135: // Seduce + stun
                disease = MapleDisease.STUN;
                break;
            case 124:
            case 138:
                disease = MapleDisease.CURSE;
                break;
            case 125:
            case 177:
            case 182:
                disease = MapleDisease.POISON;
                break;
            case 126: // Slow
            case 181:
                disease = MapleDisease.SLOW;
                break;
            case 127:// dispel
                if (lt != null && rb != null && skill) {
                    for (MapleCharacter character : getPlayersInRange(monster)) {
                        character.dispel();
                    }
                } else {
                    player.dispel();
                    //player.megaDispel();
                }
                break;
            case 128: // Seduce
            case 174:
                disease = MapleDisease.SEDUCE;
                break;
            case 131: // Mist
                monster.getMap().spawnMist(new MapleMist(calculateBoundingBox(monster.getPosition()), monster, this), x * 100, false, false, false);
                break;
            case 132:
            case 136:
                disease = MapleDisease.CONFUSE;
                break;
            case 133: // zombify
            case 134: // zombify
                disease = MapleDisease.ZOMBIFY;
                break;
            case 140:
                if (makeChanceResult() && !monster.isBuffed(MonsterStatus.MAGIC_IMMUNITY)) {
                    stats.put(MonsterStatus.WEAPON_IMMUNITY, Integer.valueOf(x));
                }
                break;
            case 141:
                if (makeChanceResult() && !monster.isBuffed(MonsterStatus.WEAPON_IMMUNITY)) {
                    stats.put(MonsterStatus.MAGIC_IMMUNITY, Integer.valueOf(x));
                }
                break;
            case 142: // Weapon / Magic reflect weaker
                stats.put(MonsterStatus.WEAPON_IMMUNITY, Integer.valueOf(x));
                stats.put(MonsterStatus.MAGIC_IMMUNITY, Integer.valueOf(x));
                break;
            case 143: // Weapon Reflect
                stats.put(MonsterStatus.WEAPON_REFLECT, 10);
                stats.put(MonsterStatus.WEAPON_IMMUNITY, 10);
                reflection.add((int) (player.getMaxHp() * 0.01));
                break;
            case 144: // Magic Reflect
                stats.put(MonsterStatus.MAGIC_REFLECT, 10);
                stats.put(MonsterStatus.MAGIC_IMMUNITY, 10);
                reflection.add((int) (player.getMaxHp() * 0.01));
                break;
            case 145: // Weapon / Magic reflect
            case 146: //shield
                stats.put(MonsterStatus.WEAPON_REFLECT, 10);
                stats.put(MonsterStatus.MAGIC_REFLECT, 10);
                stats.put(MonsterStatus.WEAPON_IMMUNITY, 10);
                stats.put(MonsterStatus.MAGIC_IMMUNITY, 10);
                reflection.add((int) (player.getMaxHp() * 0.01));
                break;
            case 154:
                stats.put(MonsterStatus.ACC, Integer.valueOf(x));
                break;
            case 155:
                stats.put(MonsterStatus.AVOID, Integer.valueOf(x));
                break;
            case 156:
                stats.put(MonsterStatus.SPEED, Integer.valueOf(x));
                break;
            case 176://arks final attack
                for (MapleCharacter character : getPlayersInRange(monster)) {
                    character.updateHpMp(1, 1);
                }
            case 200: // summon
            case 201: // summon
                int skillLimit = this.getLimit();
                MapleMap map = monster.getMap();

                if (map.isDojoMap()) {  // spawns in dojo should be unlimited
                    skillLimit = Integer.MAX_VALUE;
                }

                if (map.getSpawnedMonstersOnMap() < 80) {
                    List<Integer> summons = getSummons();
                    int summonLimit = monster.countAvailableMobSummons(summons.size(), skillLimit);
                    if (summonLimit >= 1) {
                        boolean bossRushMap = GameConstants.isBossRush(map.getId());

                        Collections.shuffle(summons);
                        for (Integer mobId : summons.subList(0, summonLimit)) {
                            MapleMonster toSpawn = MapleLifeFactory.getMonster(mobId, monster.getChannel());
                            if (toSpawn != null) {
                                if (bossRushMap) {
                                    toSpawn.disableDrops();  // no littering on BRPQ pls
                                }
                                toSpawn.setPosition(monster.getPosition());
                                int ypos, xpos;
                                xpos = (int) monster.getPosition().getX();
                                ypos = (int) monster.getPosition().getY();
                                switch (mobId) {
                                    case 8500003: // Pap bomb high
                                        toSpawn.setFh((int) Math.ceil(Math.random() * 19.0));
                                        ypos = -590;
                                        break;
                                    case 8500004: // Pap bomb
                                        xpos = (int) (monster.getPosition().getX() + Randomizer.nextInt(1000) - 500);
                                        if (ypos != -590) {
                                            ypos = (int) monster.getPosition().getY();
                                        }
                                        break;
                                    case 8510100: //Pianus bomb
                                        if (Math.ceil(Math.random() * 5) == 1) {
                                            ypos = 78;
                                            xpos = (int) Randomizer.nextInt(5) + (Randomizer.nextInt(2) == 1 ? 180 : 0);
                                        } else {
                                            xpos = (int) (monster.getPosition().getX() + Randomizer.nextInt(1000) - 500);
                                        }
                                        break;
                                }
                                switch (map.getId()) {
                                    case 220080001: //Pap map
                                        if (xpos < -890) {
                                            xpos = (int) (Math.ceil(Math.random() * 150) - 890);
                                        } else if (xpos > 230) {
                                            xpos = (int) (230 - Math.ceil(Math.random() * 150));
                                        }
                                        break;
                                    case 230040420: // Pianus map
                                        if (xpos < -239) {
                                            xpos = (int) (Math.ceil(Math.random() * 150) - 239);
                                        } else if (xpos > 371) {
                                            xpos = (int) (371 - Math.ceil(Math.random() * 150));
                                        }
                                        break;
                                }
                                toSpawn.setPosition(new Point(xpos, ypos));
                                if (toSpawn.getId() == 8500004) {
                                    map.spawnFakeMonster(toSpawn);
                                } else {
                                    map.spawnMonsterWithEffect(toSpawn, getSpawnEffect(), toSpawn.getPosition());
                                }
                                monster.addSummonedMob(toSpawn);
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }
        if (stats.size() > 0) {
            if (lt != null && rb != null && skill) {
                for (MapleMapObject mons : getObjectsInRange(monster, MapleMapObjectType.MONSTER)) {
                    ((MapleMonster) mons).applyMonsterBuff(stats, getX(), getSkillId(), getDuration(), this, reflection);
                }
            } else {
                monster.applyMonsterBuff(stats, getX(), getSkillId(), getDuration(), this, reflection);
            }
        }
        if (disease != null) {
            if (lt != null && rb != null && skill) {
                int i = 0;
                for (MapleCharacter character : getPlayersInRange(monster)) {
                    if (!character.isActiveBuffedValue(2321005)) {  // holy shield
                        if (disease.equals(MapleDisease.SEDUCE)) {
                            if (i < 10) {
                                character.giveDebuff(MapleDisease.SEDUCE, this);
                                i++;
                            }
                        } else {
                            character.giveDebuff(disease, this);
                        }
                    }
                }
            } else {
                player.giveDebuff(disease, this);
            }
        }
    }

    private List<MapleCharacter> getPlayersInRange(MapleMonster monster) {
        return monster.getMap().getPlayersInRange(calculateBoundingBox(monster.getPosition()));
    }

    public int getSkillId() {
        return skillId;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public int getMpCon() {
        return mpCon;
    }

    public List<Integer> getSummons() {
        return new ArrayList<>(toSummon);
    }

    public int getSpawnEffect() {
        return spawnEffect;
    }

    public int getHP() {
        return hp;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public long getDuration() {
        return duration;
    }

    public long getCoolTime() {
        return cooltime;
    }

    public Point getLt() {
        return lt;
    }

    public Point getRb() {
        return rb;
    }

    public int getLimit() {
        return limit;
    }

    public boolean makeChanceResult() {
        return prop == 1.0 || Math.random() < prop;
    }

    private Rectangle calculateBoundingBox(Point posFrom) {
        Point mylt = new Point(lt.x + posFrom.x, lt.y + posFrom.y);
        Point myrb = new Point(rb.x + posFrom.x, rb.y + posFrom.y);

        Rectangle bounds = new Rectangle(mylt.x, mylt.y, myrb.x - mylt.x, myrb.y - mylt.y);
        return bounds;
    }

    private List<MapleMapObject> getObjectsInRange(MapleMonster monster, MapleMapObjectType objectType) {
        return monster.getMap().getMapObjectsInBox(calculateBoundingBox(monster.getPosition()), Collections.singletonList(objectType));
    }
}