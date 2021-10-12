package net.ryanland.colossus.sys.message.interactions.menu.action;

import net.dv8tion.jda.api.interactions.components.Button;
import net.ryanland.colossus.sys.message.interactions.ButtonClickContainer;

public record ActionButton(Button button, ButtonClickContainer onClick) {

}
