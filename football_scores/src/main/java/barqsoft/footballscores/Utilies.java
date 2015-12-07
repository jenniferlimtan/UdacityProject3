package barqsoft.footballscores;

import android.content.Context;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilies
{
    public static final int SERIE_A = 357;
    public static final int PREMIER_LEGAUE = 354;
    public static final int CHAMPIONS_LEAGUE = 362;
    public static final int PRIMERA_DIVISION = 358;
    public static final int BUNDESLIGA = 351;
    public static String getLeague(int league_num, Context ctx)
    {
        switch (league_num)
        {
            case SERIE_A : return ctx.getString(R.string.seriaa);
            case PREMIER_LEGAUE : return ctx.getString(R.string.premierleague);
            case CHAMPIONS_LEAGUE : return ctx.getString(R.string.champions_league);
            case PRIMERA_DIVISION : return ctx.getString(R.string.primeradivison);
            case BUNDESLIGA : return ctx.getString(R.string.bundesliga);
            default: return  ctx.getString(R.string.unknown);
        }
    }
    public static String getMatchDay(int match_day,int league_num, Context ctx)
    {
        if(league_num == CHAMPIONS_LEAGUE)
        {
            if (match_day <= 6)
            {
                return ctx.getString(R.string.group_stage_text) + ", " + ctx.getString(R.string.matchday_text)+" : " + match_day;
            }
            else if(match_day == 7 || match_day == 8)
            {
                return ctx.getString(R.string.first_knockout_round);
            }
            else if(match_day == 9 || match_day == 10)
            {
                return ctx.getString(R.string.quarter_final);
            }
            else if(match_day == 11 || match_day == 12)
            {
                return ctx.getString(R.string.semi_final);
            }
            else
            {
                return ctx.getString(R.string.final_text);
            }
        }
        else
        {
            return ctx.getString(R.string.matchday_text) + " : " + String.valueOf(match_day);
        }
    }

    public static String getScores(int home_goals,int awaygoals)
    {
        if(home_goals < 0 || awaygoals < 0)
        {
            return " - ";
        }
        else
        {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName (String teamname, Context ctx)
    {
        if (teamname==null){return R.drawable.no_icon;}
        //This is the set of icons that are currently in the app. Feel free to find and add more
        //as you go.
        if(ctx.getString(R.string.team01).equals(teamname))
            return R.drawable.arsenal;
        else if(ctx.getString(R.string.team02).equals(teamname))
            return R.drawable.manchester_united;
        else if(ctx.getString(R.string.team03).equals(teamname))
            return R.drawable.swansea_city_afc;
        else if(ctx.getString(R.string.team04).equals(teamname))
            return R.drawable.leicester_city_fc_hd_logo;
        else if(ctx.getString(R.string.team05).equals(teamname))
            return R.drawable.everton_fc_logo1;
        else if(ctx.getString(R.string.team06).equals(teamname))
            return R.drawable.west_ham;
        else if(ctx.getString(R.string.team07).equals(teamname))
            return R.drawable.tottenham_hotspur;
        else if(ctx.getString(R.string.team08).equals(teamname))
            return R.drawable.west_bromwich_albion_hd_logo;
        else if(ctx.getString(R.string.team09).equals(teamname))
            return R.drawable.sunderland;
        else if(ctx.getString(R.string.team10).equals(teamname))
            return R.drawable.stoke_city;
        else
            return R.drawable.no_icon;
    }
}
