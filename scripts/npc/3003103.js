var status = 0;
var leaf = 4006000;
var stage = 0;
var amount = 0;
var jobs = new Array(0, 112, 122, 132, 212, 222, 232, 312, 322, 412, 422, 512, 522);
var item = 4310001;

function start() {
    cm.sendGetText("Hello#b #h ##k, How many maxlevels do you want to buy?\r\n#kYou current level cap is " + cm.getPlayer().getLevelCap() + ".\r\n#kEach Level costs 5 MP.\r\n\r\n");
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
            status--;
        }
        if (status == 1) {
            amount = Number(cm.getText());
            if (amount > 0) {
                if (cm.getPlayer().getCashShop().getCash(amount * 5)) {
                    cm.sendYesNo("Do you confirm that you want to spend " + (amount * 5) + " MP to expand your Max level cap by " + amount + " levels?");
                } else {
                    cm.sendOk("Sorry, you do not own enough Meso Bags.");
                    cm.dispose();
                }
            } else {
                cm.sendOk("Sorry, you dont seem to know what your doing.");
                cm.dispose();
            }
        } else if (status == 2) {
            cm.getPlayer().getCashShop().gainCash(2, -(amount * 5));
            cm.getPlayer().addLevelCap(amount);
            cm.sendOk("Your level has been increased by " + amount + " levels. Your level cap is now set to " + cm.getPlayer().getLevelCap() + ".");
            cm.dispose();
        } else {
            cm.dispose();
        }
    }
}



    