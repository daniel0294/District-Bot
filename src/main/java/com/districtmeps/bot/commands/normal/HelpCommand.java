/**
 *      Copyright 2020 Daniel Sanchez
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.districtmeps.bot.commands.normal;

import java.util.ArrayList;
import java.util.List;

import com.districtmeps.bot.CommandManager;
import com.districtmeps.bot.Constants;
import com.districtmeps.bot.objects.ICommand;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * HelpCommand
 */
public class HelpCommand implements ICommand {

    private final CommandManager manager;
	private String ownerDesc = "";
    private String modDesc = "";
    private String musicDesc = "";
    private String normDesc = "";
    private String funDesc = "";
    private String nsfwDesc = "";
    private String holoDesc = "";
    private String moneyDesc = "";
    private String distDesc = "";

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        ownerDesc = "";
        modDesc = "";
        musicDesc = "";
        normDesc = "";
        funDesc = "";
        nsfwDesc = "";
        holoDesc = "";
        moneyDesc = "";
        distDesc = "";
        

        if (args.isEmpty()) {
            generateAndSendEmbed(event);
            return;
        }

        String joined = String.join("", args);

        ICommand command = manager.getCommand(joined);
        if (command == null) {
            event.getChannel().sendMessage("The command `" + joined + "` does not exist\n" + "Use: `" + Constants.PREFIX
                    + getInvoke() + "` for a list of commands").queue();
            return;
        }

        String message = "Command help for `" + command.getInvoke() + "`\n" + command.getHelp();
        event.getChannel().sendMessage(message).queue();
    }

    private void generateAndSendEmbed(GuildMessageReceivedEvent event) {

        EmbedBuilder builder = EmbedUtils.defaultEmbed().setTitle("A list of all my commands:");

        //StringBuilder descriptionBuilder = builder.getDescriptionBuilder();

        List<ICommand> ownerCommands = new ArrayList<>();
        List<ICommand> modCommands = new ArrayList<>();
        List<ICommand> normCommands = new ArrayList<>();
        List<ICommand> musicCommands = new ArrayList<>();
        List<ICommand> funCommands = new ArrayList<>();
        List<ICommand> nsfwCommands = new ArrayList<>();
        List<ICommand> holoCommands = new ArrayList<>();
        List<ICommand> moneyCommands = new ArrayList<>();
        List<ICommand> distCommands = new ArrayList<>();
        

        

        manager.getCommands().forEach((command) -> {
            //descriptionBuilder.append('`').append(command.getInvoke()).append("`\n");

            int type = command.getType();

            switch (type) {
                case 1:
                    ownerCommands.add(command);
                    break;
                case 2:
                    modCommands.add(command);
                    break;
                case 3:
                    normCommands.add(command);
                    break;
                case 4:
                    musicCommands.add(command);
                    break;
                case 5:
                    funCommands.add(command);
                    break;
                case 6:
                    nsfwCommands.add(command);
                    break;
                case 7:
                    holoCommands.add(command);
                    break;
                case 8:
                    moneyCommands.add(command);
                    break;
                case 9:
                    distCommands.add(command);
                    break;
                default:
                    normCommands.add(command);
                    break;
            }

        });

        ownerCommands.forEach((command)->{
            this.ownerDesc += "`" + command.getInvoke() + "`\n";
        });
        modCommands.forEach((command)->{
            this.modDesc += "`" + command.getInvoke() + "`\n";
        });
        normCommands.forEach((command)->{
            this.normDesc += "`" + command.getInvoke() + "`\n";
        });
        musicCommands.forEach((command)->{
            this.musicDesc += "`" + command.getInvoke() + "`\n";
        });
        funCommands.forEach((command)->{
            this.funDesc += "`" + command.getInvoke() + "`\n";
        });
        nsfwCommands.forEach((command)->{
            this.nsfwDesc += "`" + command.getInvoke() + "`\n";
        });
        holoCommands.forEach((command)->{
            this.holoDesc += "`" + command.getInvoke() + "`\n";
        });
        moneyCommands.forEach((command)->{
            this.moneyDesc += "`" + command.getInvoke() + "`\n";
        });
        distCommands.forEach((command) ->{
            this.distDesc += "`" + command.getInvoke() + "`\n";
        });

        

        builder.addField("Prefix", Constants.PREFIX, false);

        builder.addField("Owner Commands", ownerDesc, false);
        builder.addField("Mod Commands", modDesc, true);
        builder.addField("District Commands", distDesc, true);
        builder.addField("Normal Commands", normDesc, true);
        builder.addField("Money Commands", moneyDesc, true);
        builder.addField("Music Commands", musicDesc, true);
        builder.addField("Fun Commands", funDesc, true);
        builder.addField("Neko.life Hentai Commands", nsfwDesc, true);
        builder.addField("Holo Commands", holoDesc, true);
        builder.addField("Extra", "Shoutout to neko.life for neko/nsfw/holo api calls", false);

        String avatar = event.getJDA().getSelfUser().getAvatarUrl();
        if(avatar == null){
            avatar = event.getJDA().getSelfUser().getDefaultAvatarUrl();
        }
        
        builder.setThumbnail(avatar);

        // TODO: Make a permission check to see if the bot can send embeds if not, just
        // plain text
        event.getChannel().sendMessage(builder.build()).queue();

    }

    @Override
    public String getHelp() {
        return "Shows a list of all the commands\n" + "Usage: `" + Constants.PREFIX + getInvoke() + " [command]`";
    }

    @Override
    public String getInvoke() {
        return "help";
    }

    @Override
    public int getType() {
        return 3;
    }

}
