# Colossus
A high-level, robust, customizable framework making Discord bot development easy using the [JDA](https://github.com/DV8FromTheWorld/JDA) library

## Wiki
See the [wiki](https://github.com/RyanLandDev/Colossus/wiki) for various guides and more information to get started

## Features

* Slash and text (prefixed) commands
    * May be generic, which means a command can be a slash and text command simultaneously while only writing the code for it once
    * You could add more features of your own using OOP
    * Extensive **argument system**
        * Automatic **parsing**
        * Automatic **exception handling**
        * Possibility to create **custom arguments**
        * **Optional arguments** with custom fallback functions
        * **Multi-step arguments** using `CompleteableFuture<T>`
        * Supports all Discord option features, such as autocomplete
    * Automatic **exception handling**; throwing a `CommandException` in the middle of your command execution code will automatically send an error message, same for regular exceptions
    * **Subcommands** and nested subcommands
    * **Categories**
    * **Permission system**, also allows for custom permissions (e.g. bot tester)
    * **Cooldowns**, with the option to create your own `CooldownManager` to handle cooldowns in a custom way
    * **Inhibitors** - custom conditions that should be checked before any command is executed
    * **Finalizers** - custom code that is run after a command was successfully executed
    * **Disabled command handler** - handles the enabling/disabling of commands and offers a default disable/enable command (can be removed)
    * **Command localization** - add localizations to slash commands
    * **Help command** - provides a default help command, can be disabled
* User/message **context commands**
* Custom **interactions/components system**
    * **Automatic event handling** - provide a condition and consumer beforehand
    * **Buttons**
      ```java
      BaseButton button = BaseButton.user(userId,
          Button.primary("click", "Click me!"),
          clickEvent -> clickEvent.reply("Clicked!"));
      presetBuilder.addButtons(button);
      event.reply(presetBuilder);
      ```
    * **Select Menus**
      ```java
      SelectMenu menu = //... jda select menu
      aseSelectMenu selectMenu = BaseSelectMenu.user(userId, menu,
          submitEvent -> submitEvent.reply("Submitted!"));
      presetBuilder.addComponentRows(selectMenu);
      event.reply(presetBuilder);
      ```
    * **Modals**
      ```java
      Modal modal = // ... jda modal
      event.reply(modal,
          submitEvent -> submitEvent.reply("Submitted!"));
      ```
* Custom **interaction menus**; you can either create your own or use one of the pre-made ones:
    * `ConfirmMenu` - a menu to either cancel or confirm an action
    * `TabMenu` - a menu to browse custom defined pages and (nested) subpages using tabs. The default help command uses this menu
    * `SelectRowMenu` - a menu to switch between pages using a select menu, useful for e.g. a settings command
    * `ScrollPageMenu` - a menu to browse pages using buttons
* `PresetBuilder` - a helper class to create rich embed messages
    * **Preset types** - these can be used to create PresetBuilders with default values. Error messages use a (customizable) `PresetType` for example.
    * Easily add components using the previously mentioned custom component system
    * **Ephemeral** messages
* Automatic JSON **config system**; allowing for extra custom key entries and grouping
* **Database driver system** to easily manage, write and read a database
    * Either create your own or use the pre-made `SQLDatabaseDriver`, `JsonDatabaseDriver` or `MongoDatabaseDriver`
    * Automatically takes care of **caching**, see the `DatabaseDriver` javadoc for specifics
    * **Update and read** database values easily using a `Supply` and Colossus entities
      ```java
      event.getUser().modifyValue("coins", coins -> coins * 10); // multiply coins by 10
      event.getUser().increaseValue("coins", 10); // add 10 coins
      ```
* `LocalFile` - extended File class, with useful methods such as `getContent()` or `parseJson()`, also comes with a helpful `LocalFileBuilder`
* An **event waiter** to await events with ease
    * **Conditions**; the waiter will only proceed if the condition is met
    * **Timeout**; optionally wait a specific amount of time before the waiter stops listening and runs a timeout action
    * **Run once**; whether the waiter should stop listening after it has been successfully run once or not
    * Returns an `EventWaiterListener` so you can optionally stop listening for the event earlier
    * Example:
      ```java
      /* Await a MessageReceivedEvent.
       * Condition: The message must contain 'cookie'
       * Once the condition is met the bot replies with 'nom nom'
       * The waiter does not run once, it will listen until the timeout is over
       * The waiter will time out after 5 minutes, when it will say 'I got all the cookies!'
       */
       EventWaiter.awaitEvent(MessageReceivedEvent.class,
           msgEvent -> msgEvent.getMessage().getContent().contains("cookie"),
           (msgEvent, listener) -> msgEvent.reply("nom nom").queue(),
           false, 5, TimeUnit.MINUTES, () -> cmdEvent.reply("I got all cookies!"));
       ```
* `RepliableEvent` interface implemented by all Colossus events to easily reply to events with strings, messages, embeds, PresetBuilders, modals or InteractionMenus. Some examples can be seen throughout this feature list
* `WebUtil` utility class for easily retrieving data from web APIs
  ```java
  WebUtil.requestJson("https://some-random-api.ml/facts/dog").get("fact").getAsString();
  ```
* `ExecutorUtil` utility class for scheduling and cancelling tasks

## Prerequisites
* Java 17
* Project with Gradle/Maven
* An IDE (IntelliJ IDEA recommended)

## Installation
### Gradle
 ```gradle 
 repositories {
     maven { url 'https://jitpack.io' }
 }

 dependencies {
     implementation 'com.github.RyanLandDev:Colossus:VERSION'
 }
 ``` 
### Maven
 ```xml 
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.RyanLandDev</groupId>
    <artifactId>Colossus</artifactId>
    <version>VERSION</version>
</dependency>
 ```

## Support

If you need help using the framework, please join our [Discord server](https://discord.gg/j7fmJYxPKf).
