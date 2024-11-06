# Colossus
A high-level, robust, customizable framework making Discord bot development easy using the [JDA](https://github.com/DV8FromTheWorld/JDA) library

## Wiki
See the [wiki](https://github.com/RyanLandDev/Colossus/wiki) for various guides and more information to get started

## Features

* Slash and text (prefixed) commands
    * Automatically registered using classpath scanning
    * May be generic, which means a command can be a slash and text command simultaneously while only writing the code for it once
    * You could add more features of your own using OOP
    * Extensive **argument system**
        * Automatic **parsing**
        * Automatic **exception handling**
        * Possibility to create **custom arguments**
        * **Optional arguments** with custom fallback functions
        * **Multi-step arguments** using `CompletableFuture<T>`
        * Supports all Discord option features, such as autocomplete
    * Automatic **exception handling**; throwing a `CommandException` in the middle of your command execution code will automatically send an error message, same for regular exceptions
      ```java
      @Override
      public void run(SlashCommandEvent event) throws CommandException {
          if (!event.getUserEntity().isStaff()) {
              // This will send the user an ephemeral error embed with the provided description
              throw new CommandException("You are not allowed to close a ticket.");
          }

          event.reply("Ticket closed.", true);
          TicketHandler.deleteTicket(event.getChannel());
      }
      ```
    * **Subcommands** and nested subcommands
    * **Categories**
    * **Permission system**, also allows for custom permissions (e.g. bot tester)
      * Discord's default slash command permissions are also supported
    * **Cooldowns**, with the option to create your own `CooldownManager` to handle cooldowns in a custom way
      * Comes with `MemoryCooldownManager` and `DatabaseCooldownManager` by default
    * **Inhibitors** - custom conditions that should be checked before any command is executed
      * Colossus also uses inhibitors internally for checks like cooldowns and permissions
    * **Finalizers** - custom code that is run after a command was successfully executed without exceptions
    * **Command localization** - add localizations to slash commands
    * **Help command** - provides a default extensive help command, can be disabled
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
      * **Static Buttons** - for static buttons without consumers
          ```java
        @ButtonListener("button_id") // this method can live anywhere, it is registered automatically
        private static void listen(ButtonClickEvent event) {
            event.reply("Clicked!", true);
            // ... do stuff
        }
          ```
      * **Select Menus**
        ```java
        SelectMenu menu = //... jda select menu
        BaseSelectMenu selectMenu = BaseSelectMenu.user(userId, menu,
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
* `PresetBuilder` - a helper class to create rich embed messages with action rows
    * **Preset types** - these can be used to create PresetBuilders with default values. Error messages use a (customizable) `PresetType` for example.
    * Easily add components using the previously mentioned custom component system
    * Ephemeral messages
* Customizable **configuration system**
  * Allows custom configuration setups implemented using `ConfigSupplier` (e.g. for a web dashboard or Minecraft plugin config)
  * Uses the pre-built `JsonConfig` by default, supports grouping
  * Easily add custom key entries
    ```java
    ColossusBuilder builder = new ColossusBuilder(".")
        .registerConfigEntry("tickets.log_channel", "");
    // ...
    String channelId = Config.getString("tickets.log_channel");
    ```
* **Database system** to easily perform create, read, update and delete actions to a database
    * Uses Hibernate as base
    * Create your own `UserEntity`, `MemberEntity` or `GuildEntity` to store data mapped to snowflakes
    * **Update and read** database values easily using Colossus entities, automatically integrated
      ```java
      // Coins is a custom property here
      event.getUserEntity().getCoins();
      event.getUserEntity().setCoins(10);
      event.getUserEntity().save();
      ```
    * All other features that Hibernate provides
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

If you need help using the framework, please contact me on Discord at `ryandev.` or join our [Discord server](https://discord.gg/j7fmJYxPKf).
