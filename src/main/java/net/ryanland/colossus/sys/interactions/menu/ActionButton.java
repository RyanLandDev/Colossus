package net.ryanland.colossus.sys.interactions.menu;

import net.dv8tion.jda.api.interactions.components.Button;
import net.ryanland.colossus.sys.interactions.ButtonClickContainer;

public record ActionButton(Button button, ButtonClickContainer onClick) {

}
