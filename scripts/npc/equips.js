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
var status = 0;
var leaf = 4000313;
var slots = new Array(-1, -2, -3, -4, -5, -6, -7, -8, -9, -10, -11, -12, -13, -15, -16, -50, -112, -113, -115, -116);
var slot = 0;
var scroll = 0;
var scrollslot = 0;
var amount = 0;
var slotcount = 0;
var power = 0;
var equip;
var equips;
var equiplist;
var options = 0;
var itemcount = 0;

function start() {
    status = 0;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 0) {
            cm.dispose();
        }
        if (mode == 1) {
            status++;
        } else {
            cm.dispose();
        }
        if (status == 1) {
            cm.sendSimple("Which inventory would you like to access?\r\n\#L7#Equipped Items#l\r\n\#L8#Inventory Equips#l", 2);
        } else if (status == 2) {
            if (selection == 7) {
                equiplist = cm.getPlayer().getEquippedItems();
            } else {
                equiplist = cm.getPlayer().getEquipItems();
            }
            if (!equiplist.isEmpty()) {
                var selStr = "";
                for (var i = 0; i < equiplist.size(); i++) {
                    var curEquip = equiplist.get(i);
                    if (curEquip != null) {
                        selStr += "#L" + i + "##i"+curEquip.getItemId()+"# " + cm.getItemName(curEquip.getItemId()) + " (Power: "+curEquip.getPowerLevel()+") #l\r\n\ ";
                    } else {
                        break;
                    }
                }
                cm.sendSimple("Which equip would you like to view?\r\n\ " + selStr, 2);
            } else {
                cm.sendOk("You currently do not have any equips to view.");
                cm.dispose();
            }
        } else if (status == 3) {
            equip = equiplist.get(selection);
            cm.sendOk( "#i"+equip.getItemId()+"# " + cm.getItemName(equip.getItemId()) + "\r\nPower: #b"+equip.getPowerLevel()+"#k\r\nStr: #b"+equip.getStr()+"#k\r\nDex: #b"+equip.getDex()+"#k\r\nInt: #b"+equip.getInt()+"#k\r\nLuk: #b"+equip.getLuk()+"#k\r\nWeapon Attack: #b"+equip.getWatk()+"#k\r\nMagic Attack: #b"+equip.getMatk()+"#k\r\nWeapon Defense: #b"+equip.getWdef()+"#k\r\nMagic Defense: #b"+equip.getMdef(), 2);
            cm.dispose();	
        }
    }
}
