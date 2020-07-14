package com.districtmeps.bot.commands.district;

import java.util.List;
import java.util.Map;

import com.districtmeps.bot.Constants;
import com.districtmeps.bot.objects.APIHelper;
import com.districtmeps.bot.objects.ICommand;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class GetInstaCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        //Dist and Test
        if(!event.getGuild().getId().equals("296844096813793281") && (!event.getGuild().getId().equals("374386835327287306"))){
            event.getChannel().sendMessage("This command can only be used in the District Discord Server").queue();
            return;
        }

        //Just Dist
        // if(!event.getGuild().getId().equals("296844096813793281")){
        //     event.getChannel().sendMessage("This command can only be used in the District Discord Server").queue();
        //     return;
        // }

        String userId = null;
        if(args.size() > 0){
            if(event.getMessage().getMentionedUsers().size() != 1){
                event.getChannel().sendMessage("Incorrect arguments\nPlease check `" + Constants.PREFIX + "help " + getInvoke() + "`\nTip: Only mention one person. No mention needed for youself").queue();
                return;
            } else {
                userId = event.getMessage().getMentionedUsers().get(0).getId();
            }
        } else {
            userId = event.getAuthor().getId();
        }

        Map<String, String> userInstaInfo = APIHelper.getInstaInfo(userId);
        String instaName = userInstaInfo.get("insta");
        if(instaName.equals("null")){
            if(args.size() == 1){
                event.getChannel().sendMessage("This user hasn't saved their Instagram account. They will need to use `" + Constants.PREFIX + (new UpdateInstaComand()).getInvoke() + "`").queue();;
                return;
            } else {
                event.getChannel().sendMessage("You haven't saved your Instagram account. You will need to use `" + Constants.PREFIX + (new UpdateInstaComand()).getInvoke() + "`\nSee `" + Constants.PREFIX + "help " + (new UpdateInstaComand()).getInvoke() + "` for more info").queue();;
                return;
            }
        }


        EmbedBuilder builder = EmbedUtils.defaultEmbed();
            builder.setTitle("Instagram <:insta:732342758173573152>", "https://instagram.com/" + instaName)
            .setDescription("Instagram account for " + event.getJDA().getUserById(userId).getAsMention())
            .addField("Insta @", "@" + instaName, false)
            .setColor(event.getMember().getColor())
            .setThumbnail((new UpdateInstaComand()).getInstaPfp(instaName));

        event.getChannel().sendMessage(builder.build()).queue();

    }

    @Override
    public String getHelp() {
        return "Send an embed of your/mentioned user's saved instagram (No need to mention yourself)\n" + 
                "Usage: `" + Constants.PREFIX + getInvoke() + " [Instagram Name]`";
    }

    @Override
    public String getInvoke() {
        return "insta";
    }

    @Override
    public int getType() {
        return 9;
    }

    
    
}