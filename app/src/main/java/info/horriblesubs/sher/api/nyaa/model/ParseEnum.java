package info.horriblesubs.sher.api.nyaa.model;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

abstract class ParseEnum {

    Date parseDate(String date) {
        try {
            String serverDateTime = "yyyy-MM-dd HH:mm Z";
            return new SimpleDateFormat(serverDateTime, Locale.US).parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    String parseDateTimeView(String date) {
        try {
            String viewDateTime = "dd-MM-yyyy HH:mm";
            return new SimpleDateFormat(viewDateTime, Locale.US).format(parseDate(date));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    String parseTimeView(String date) {
        try {
            String viewTime = "HH:mm";
            return new SimpleDateFormat(viewTime, Locale.US).format(parseDate(date));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    TorrentCategory parseCategory(@NotNull String id) {
        switch (id) {
            case "0_0":
                return TorrentCategory.All;
            case "1_0":
                return TorrentCategory.Anime;
            case "1_1":
                return TorrentCategory.Anime_AMV;
            case "1_2":
                return TorrentCategory.Anime_English;
            case "1_3":
                return TorrentCategory.Anime_Non_English;
            case "1_4":
                return TorrentCategory.Anime_Raw;
            case "2_0":
                return TorrentCategory.Audio;
            case "2_1":
                return TorrentCategory.Audio_Lossless;
            case "2_2":
                return TorrentCategory.Audio_Lossy;
            case "3_0":
                return TorrentCategory.Literature;
            case "3_1":
                return TorrentCategory.Literature_English;
            case "3_2":
                return TorrentCategory.Literature_Non_English;
            case "3_3":
                return TorrentCategory.Literature_Raw;
            case "4_0":
                return TorrentCategory.Live_Action;
            case "4_1":
                return TorrentCategory.Live_Action_English;
            case "4_2":
                return TorrentCategory.Live_Action_PV;
            case "4_3":
                return TorrentCategory.Live_Action_Non_English;
            case "4_4":
                return TorrentCategory.Live_Action_Raw;
            case "5_0":
                return TorrentCategory.Pictures;
            case "5_1":
                return TorrentCategory.Pictures_Graphics;
            case "5_2":
                return TorrentCategory.Pictures_Photos;
            case "6_0":
                return TorrentCategory.Software;
            case "6_1":
                return TorrentCategory.Software_Applications;
            case "6_2":
                return TorrentCategory.Software_Games;
        }
        return TorrentCategory.All;
    }

    TorrentState parseState(int id) {
        switch (id) {
            case -1:
                return TorrentState.REMAKE;
            case 1:
                return TorrentState.TRUSTED;
            case 0:
                return TorrentState.NONE;
        }
        return TorrentState.NONE;
    }

    DataSize parseSize(String s) {
        DataSize.DataUnit dataUnit = DataSize.DataUnit.Bytes;
        String[] strings = s.split(" ");
        switch (strings[1]) {
            case "Bytes":
                dataUnit = DataSize.DataUnit.Bytes;
                break;
            case "KiB":
                dataUnit = DataSize.DataUnit.KiB;
                break;
            case "MiB":
                dataUnit = DataSize.DataUnit.MiB;
                break;
            case "GiB":
                dataUnit = DataSize.DataUnit.GiB;
                break;
            case "TiB":
                dataUnit = DataSize.DataUnit.TiB;
                break;
        }
        return new DataSize(strings[0], dataUnit);
    }
}