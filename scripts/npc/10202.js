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
var status = -1;

function start() {
    cm.sendNext("Warriors possess an enormous power with stamina to back it up, and they shine the brightest in melee combat situation. Regular attacks are powerful to begin with, and armed with complex skills, the job is perfect for explosive attacks.");
}

function action(mode, type, selection) {
    status++;
    if (mode != 1) {
        if (mode == 0)
            cm.sendNext("If you wish to experience what it's like to be a Warrior, come see me again.");
        cm.dispose();
        return;
    }
    if (status == 0) {
        cm.sendYesNo("Would you like to experience what it's like to be a Warrior?");
    } else if (status == 1) {
        if (cm.getJobId() == 0) {
            cm.resetStats();
            cm.changeJobById(100);
            cm.getPlayer().gainMeso(25000);
            cm.getPlayer().maxSlots();
            if (cm.getPlayer().getSkillLevel(12) > 0) {
                cm.getPlayer().gainBonusAP(cm.getPlayer().getSkillLevel(12) * 25);
            }
            cm.gainItem(1142249, 1);
            cm.warp(102000000, 0);
            cm.getPlayer().gainlevel(10 - cm.getPlayer().getLevel());
        } else {
            cm.sendNext("If you wish to experience what it's like to be a Warrior, come see me again when your level 10.");
        }
        cm.dispose();
    }
}