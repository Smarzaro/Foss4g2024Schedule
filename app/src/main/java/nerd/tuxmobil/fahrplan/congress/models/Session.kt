package nerd.tuxmobil.fahrplan.congress.models

import info.metadude.android.eventfahrplan.commons.temporal.Moment
import info.metadude.android.eventfahrplan.commons.temporal.Moment.Companion.MILLISECONDS_OF_ONE_MINUTE
import nerd.tuxmobil.fahrplan.congress.schedule.Conference
import org.threeten.bp.ZoneOffset

/**
 * Application model representing a lecture, a workshop or any similar time-framed happening.
 */
data class Session(
    val sessionId: String,
    val title: String = "",
    val subtitle: String = "",
    val abstractt: String = "",
    val description: String = "",
    val feedbackUrl: String? = null, // URL to Frab/Pretalx feedback system, e.g. feedbackUrl = "https://talks.event.net/2023/talk/V8LUNA/feedback"
    val links: String = "", // Comma separated Markdown formatted links, see ParserTask#parseFahrplan.
    val url: String = "",
    @Deprecated(
        "The value of this field is generated by {@link FahrplanParser} " +
                "when parsing the schedule. It contributes to how the rooms are sorted in " +
                "the user interface, see {@link SessionsTransformer}. But it should not be " +
                "used by any other code!"
    )
    val dayIndex: Int = 0, // XML values start with 1
    val dateText: String = "", // YYYY-MM-DD
    val dateUTC: Long = 0, // milliseconds
    val timeZoneOffset: ZoneOffset? = null,
    val startTime: Int = 0, // minutes since day start
    val relativeStartTime: Int = 0, // minutes since conference start
    val duration: Int = 0, // minutes
    val roomName: String = "",
    val roomIdentifier: String = "", // Unique identifier of a room, e.g. "bccb6a5b-b26b-4f17-90b9-b5966f5e34d8"
    val roomIndex: Int = 0,
    val speakers: List<String> = emptyList(),
    val track: String = "",
    val type: String = "",
    val language: String = "",
    val slug: String = "",
    val recordingLicense: String = "",
    val recordingOptOut: Boolean = false,
    val isHighlight: Boolean = false,
    val hasAlarm: Boolean = false,

    val changedTitle: Boolean = false,
    val changedSubtitle: Boolean = false,
    val changedRoomName: Boolean = false,
    val changedDayIndex: Boolean = false,
    val changedStartTime: Boolean = false,
    val changedDuration: Boolean = false,
    val changedSpeakers: Boolean = false,
    val changedLanguage: Boolean = false,
    val changedRecordingOptOut: Boolean = false,
    val changedTrack: Boolean = false,

    val changedIsNew: Boolean = false,
    val changedIsCanceled: Boolean = false,
) {

    override fun equals(other: Any?) =
        other is Session && EssentialSession(this) == EssentialSession(other)

    override fun hashCode() = EssentialSession(this)
        .hashCode()

    override fun toString() = EssentialSession(this)
        .toString()
        .replaceFirst("EssentialSession", "Session")

    /**
     * Returns a moment based on the start time milliseconds.
     *
     * Don't use in [Conference.ofSessions] as long as [relativeStartTime] is supported.
     * See: [5a402](https://github.com/EventFahrplan/EventFahrplan/commit/5a4022b00434700274a824cc63f6d54a18b06fac)
     */
    val startsAt: Moment
        get() {
            check(dateUTC > 0) { "Field 'dateUTC' must be more than 0." }
            return Moment.ofEpochMilli(dateUTC)
        }

    /**
     * Returns a moment based on summing up the start time milliseconds and the duration.
     */
    val endsAt: Moment
        get() = Moment.ofEpochMilli(dateUTC + duration.toLong() * MILLISECONDS_OF_ONE_MINUTE)

    /**
     * Keep in sync with [info.metadude.android.eventfahrplan.database.models.Session.isChanged].
     */
    val isChanged: Boolean
        get() = changedTitle ||
                changedSubtitle ||
                changedRoomName ||
                changedDayIndex ||
                changedStartTime ||
                changedDuration ||
                changedSpeakers ||
                changedLanguage ||
                changedRecordingOptOut ||
                changedTrack

}

private data class EssentialSession(
    val sessionId: String,
    val title: String,
    val subtitle: String,
    val feedbackUrl: String?,
    val dayIndex: Int,
    val dateText: String,
    val dateUTC: Long,
    val timeZoneOffset: ZoneOffset?,
    val startTime: Int,
    val duration: Int,
    val roomName: String,
    val roomIdentifier: String,
    val speakers: List<String>,
    val track: String,
    val type: String,
    val language: String,
    val recordingLicense: String,
    val recordingOptOut: Boolean,
) {
    constructor(session: Session) : this(
        sessionId = session.sessionId,
        title = session.title,
        subtitle = session.subtitle,
        feedbackUrl = session.feedbackUrl,
        dayIndex = session.dayIndex,
        dateText = session.dateText,
        dateUTC = session.dateUTC,
        timeZoneOffset = session.timeZoneOffset,
        startTime = session.startTime,
        duration = session.duration,
        roomName = session.roomName,
        roomIdentifier = session.roomIdentifier,
        speakers = session.speakers,
        track = session.track,
        type = session.type,
        language = session.language,
        recordingLicense = session.recordingLicense,
        recordingOptOut = session.recordingOptOut,
    )
}
