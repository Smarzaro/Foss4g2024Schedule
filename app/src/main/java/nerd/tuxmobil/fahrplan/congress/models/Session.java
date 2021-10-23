package nerd.tuxmobil.fahrplan.congress.models;

import static java.util.Collections.emptyList;
import static info.metadude.android.eventfahrplan.commons.temporal.Moment.MILLISECONDS_OF_ONE_MINUTE;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.ObjectsCompat;

import org.threeten.bp.ZoneOffset;

import java.util.List;

import info.metadude.android.eventfahrplan.commons.temporal.Moment;
import info.metadude.android.eventfahrplan.network.serialization.FahrplanParser;
import info.metadude.android.eventfahrplan.network.temporal.DateParser;
import nerd.tuxmobil.fahrplan.congress.R;
import nerd.tuxmobil.fahrplan.congress.repositories.SessionsTransformer;

/**
 * Application model representing a lecture, a workshop or any similar time-framed happening.
 */
public class Session {

    public String title;
    public String subtitle;
    public String url;
    public int day;
    public String date;                 // YYYY-MM-DD
    public long dateUTC;                // milliseconds
    @Nullable
    public ZoneOffset timeZoneOffset;
    public int startTime;               // minutes since day start
    public int relStartTime;            // minutes since conference start
    public int duration;                // minutes

    public String room;

    /**
     * The value of this field is generated by {@link FahrplanParser} when parsing the schedule. It contributes to how
     * the rooms are sorted in the user interface, see {@link SessionsTransformer}. But it should not be used by any
     * other code!
     */
    @Deprecated
    public int roomIndex;

    public List<String> speakers;
    public String track;
    public String sessionId;
    public String type;
    public String lang;
    public String slug;
    public String abstractt;
    public String description;

    /**
     * Comma separated Markdown formatted links, see ParserTask#parseFahrplan.
     */
    public String links;

    public boolean highlight;
    public boolean hasAlarm;

    public String recordingLicense;
    public boolean recordingOptOut;

    public boolean changedTitle;
    public boolean changedSubtitle;
    public boolean changedRoom;
    public boolean changedDay;
    public boolean changedTime;
    public boolean changedDuration;
    public boolean changedSpeakers;
    public boolean changedRecordingOptOut;
    public boolean changedLanguage;
    public boolean changedTrack;
    public boolean changedIsNew;
    public boolean changedIsCanceled;

    private static final boolean RECORDING_OPTOUT_OFF = false;

    public Session(String sessionId) {
        title = "";
        subtitle = "";
        day = 0;
        room = "";
        slug = "";
        startTime = 0;
        duration = 0;
        speakers = emptyList();
        track = "";
        type = "";
        lang = "";
        abstractt = "";
        description = "";
        relStartTime = 0;
        links = "";
        date = "";
        this.sessionId = sessionId;
        highlight = false;
        hasAlarm = false;
        dateUTC = 0;
        timeZoneOffset = null;
        roomIndex = 0;
        recordingLicense = "";
        recordingOptOut = RECORDING_OPTOUT_OFF;
        changedTitle = false;
        changedSubtitle = false;
        changedRoom = false;
        changedDay = false;
        changedSpeakers = false;
        changedRecordingOptOut = false;
        changedLanguage = false;
        changedTrack = false;
        changedIsNew = false;
        changedTime = false;
        changedDuration = false;
        changedIsCanceled = false;
    }

    public Session(@NonNull Session session) {
        this.title = session.title;
        this.subtitle = session.subtitle;
        this.url = session.url;
        this.day = session.day;
        this.date = session.date;
        this.dateUTC = session.dateUTC;
        this.timeZoneOffset = session.timeZoneOffset;
        this.startTime = session.startTime;
        this.relStartTime = session.relStartTime;
        this.duration = session.duration;
        this.room = session.room;
        this.roomIndex = session.roomIndex;
        this.speakers = session.speakers;
        this.track = session.track;
        this.sessionId = session.sessionId;
        this.type = session.type;
        this.lang = session.lang;
        this.slug = session.slug;
        this.abstractt = session.abstractt;
        this.description = session.description;
        this.links = session.links;
        this.highlight = session.highlight;
        this.hasAlarm = session.hasAlarm;
        this.recordingLicense = session.recordingLicense;
        this.recordingOptOut = session.recordingOptOut;

        this.changedTitle = session.changedTitle;
        this.changedSubtitle = session.changedSubtitle;
        this.changedRoom = session.changedRoom;
        this.changedDay = session.changedDay;
        this.changedTime = session.changedTime;
        this.changedDuration = session.changedDuration;
        this.changedSpeakers = session.changedSpeakers;
        this.changedRecordingOptOut = session.changedRecordingOptOut;
        this.changedLanguage = session.changedLanguage;
        this.changedTrack = session.changedTrack;
        this.changedIsNew = session.changedIsNew;
        this.changedIsCanceled = session.changedIsCanceled;
    }

    @NonNull
    public String getLinks() {
        return links == null ? "" : links;
    }

    public Moment getStartTimeMoment() {
        long startOfDayTimestamp = DateParser.getDateTime(date);
        return Moment.ofEpochMilli(startOfDayTimestamp).plusMinutes(relStartTime);
    }

    /**
     * Returns the start time in milliseconds.
     * <p>
     * The {@link #dateUTC} is given precedence if its value is bigger then `0`.
     * Otherwise the start time is determined based on {@link #getStartTimeMoment}.
     */
    public long getStartTimeMilliseconds() {
        return (dateUTC > 0) ? dateUTC : getStartTimeMoment().toMilliseconds();
    }

    /**
     * Returns the end time since day start in minutes.
     */
    public int getEndsAtTime() {
        return startTime + duration;
    }

    /**
     * Returns the end date and time in milliseconds.
     */
    public long getEndsAtDateUtc() {
        return dateUTC + (long) duration * MILLISECONDS_OF_ONE_MINUTE;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        if (day != session.day) return false;
        if (duration != session.duration) return false;
        if (recordingOptOut != session.recordingOptOut) return false;
        if (startTime != session.startTime) return false;
        if (!ObjectsCompat.equals(date, session.date)) return false;
        if (!ObjectsCompat.equals(lang, session.lang)) return false;
        if (!sessionId.equals(session.sessionId)) return false;
        if (!ObjectsCompat.equals(recordingLicense, session.recordingLicense)) return false;
        if (!ObjectsCompat.equals(room, session.room)) return false;
        if (!ObjectsCompat.equals(speakers, session.speakers)) return false;
        if (!ObjectsCompat.equals(subtitle, session.subtitle)) return false;
        if (!title.equals(session.title)) return false;
        if (!ObjectsCompat.equals(track, session.track)) return false;
        if (!ObjectsCompat.equals(type, session.type)) return false;
        if (dateUTC != session.dateUTC) return false;
        if (!ObjectsCompat.equals(timeZoneOffset, session.timeZoneOffset)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + ObjectsCompat.hashCode(subtitle);
        result = 31 * result + day;
        result = 31 * result + ObjectsCompat.hashCode(room);
        result = 31 * result + startTime;
        result = 31 * result + duration;
        result = 31 * result + ObjectsCompat.hashCode(speakers);
        result = 31 * result + ObjectsCompat.hashCode(track);
        result = 31 * result + sessionId.hashCode();
        result = 31 * result + ObjectsCompat.hashCode(type);
        result = 31 * result + ObjectsCompat.hashCode(lang);
        result = 31 * result + ObjectsCompat.hashCode(date);
        result = 31 * result + ObjectsCompat.hashCode(recordingLicense);
        result = 31 * result + (recordingOptOut ? 1 : 0);
        result = 31 * result + (int) dateUTC;
        result = 31 * result + ObjectsCompat.hashCode(timeZoneOffset);
        return result;
    }

    public void cancel() {
        changedIsCanceled = true;
        changedTitle = false;
        changedSubtitle = false;
        changedRoom = false;
        changedDay = false;
        changedSpeakers = false;
        changedRecordingOptOut = false;
        changedLanguage = false;
        changedTrack = false;
        changedIsNew = false;
        changedTime = false;
        changedDuration = false;
    }

    public String getChangedStateString() {
        return "Session{" +
                "changedTitle=" + changedTitle +
                ", changedSubtitle=" + changedSubtitle +
                ", changedRoom=" + changedRoom +
                ", changedDay=" + changedDay +
                ", changedTime=" + changedTime +
                ", changedDuration=" + changedDuration +
                ", changedSpeakers=" + changedSpeakers +
                ", changedRecordingOptOut=" + changedRecordingOptOut +
                ", changedLanguage=" + changedLanguage +
                ", changedTrack=" + changedTrack +
                ", changedIsNew=" + changedIsNew +
                ", changedIsCanceled=" + changedIsCanceled +
                '}';
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isChanged() {
        return changedDay || changedDuration ||
                changedLanguage || changedRecordingOptOut ||
                changedRoom || changedSpeakers || changedSubtitle ||
                changedTime || changedTitle || changedTrack;
    }

    @NonNull
    public static String getDurationContentDescription(@NonNull Context context, int duration) {
        return context.getString(R.string.session_list_item_duration_content_description, duration);
    }

    @NonNull
    public String getFormattedSpeakers() {
        return speakers == null ? "" : TextUtils.join(", ", speakers);
    }

    public String getFormattedTrackLanguageText() {
        StringBuilder builder = new StringBuilder();
        builder.append(track);
        if (!TextUtils.isEmpty(lang)) {
            String language = lang.replace("-formal", "");
            builder.append(" [").append(language).append("]");
        }
        return builder.toString();
    }

    @NonNull
    public static String getRoomNameContentDescription(@NonNull Context context, @NonNull String roomName) {
        return context.getString(R.string.session_list_item_room_content_description, roomName);
    }

    @NonNull
    public static String getSpeakersContentDescription(@NonNull Context context, int speakersCount, @NonNull String formattedSpeakerNames) {
        return context.getResources().getQuantityString(R.plurals.session_list_item_speakers_content_description, speakersCount, formattedSpeakerNames);
    }

    @NonNull
    public static String getFormattedTrackContentDescription(@NonNull Context context, @NonNull String trackName, @NonNull String languageCode) {
        StringBuilder builder = new StringBuilder();
        builder.append(context.getString(R.string.session_list_item_track_content_description, trackName));
        if (!TextUtils.isEmpty(languageCode)) {
            builder.append("; ").append(getLanguageContentDescription(context, languageCode));
        }
        return builder.toString();
    }

    @NonNull
    public static String getLanguageContentDescription(@NonNull Context context, @NonNull String languageCode) {
        if (TextUtils.isEmpty(languageCode)) {
            return context.getString(R.string.session_list_item_language_unknown_content_description);
        }
        String languageName;
        switch (languageCode) {
            case "en":
                languageName = context.getString(R.string.session_list_item_language_english_content_description);
                break;
            case "de":
                languageName = context.getString(R.string.session_list_item_language_german_content_description);
                break;
            case "pt":
                languageName = context.getString(R.string.session_list_item_language_portuguese_content_description);
                break;
            default:
                languageName = languageCode;
        }
        return context.getString(R.string.session_list_item_language_content_description, languageName);
    }

    @NonNull
    public static String getStartTimeContentDescription(@NonNull Context context, @NonNull String startTimeText) {
        return context.getString(R.string.session_list_item_start_time_content_description, startTimeText);
    }

    public void shiftRoomIndexBy(int amount) {
        roomIndex += amount;
    }

}
