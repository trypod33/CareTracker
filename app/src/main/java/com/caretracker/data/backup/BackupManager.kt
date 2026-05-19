package com.caretracker.data.backup

import android.content.Context
import android.os.Environment
import com.caretracker.data.entities.*
import com.caretracker.data.repository.CareTrackerRepository
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object BackupManager {

    suspend fun exportBackup(
        context: Context,
        repository: CareTrackerRepository,
        userId: Long
    ): Result<File> = runCatching {
        val user         = repository.getUserById(userId) ?: error("User not found")
        val habits       = repository.getAllHabitsForUserOnce(userId)
        val habitLogs    = repository.getAllHabitLogsForUserOnce(userId)
        val meds         = repository.getMedicationsForUserOnce(userId)
        val medLogs      = repository.getAllMedLogsForUserOnce(userId)
        val tasks        = repository.getAllTasksForUserOnce(userId)
        val health       = repository.getAllHealthEntriesForUserOnce(userId)
        val vitals       = repository.getAllVitalLogsForUserOnce(userId)
        val mood         = repository.getAllMoodEntriesForUserOnce(userId)
        val calendar     = repository.getAllEventsForUserOnce(userId)

        val json = JSONObject().apply {
            put("version",       1)
            put("exportedAt",    System.currentTimeMillis())
            put("user",          userToJson(user))
            put("habits",        toArray(habits,    ::habitToJson))
            put("habitLogs",     toArray(habitLogs, ::habitLogToJson))
            put("medications",   toArray(meds,      ::medToJson))
            put("medLogs",       toArray(medLogs,   ::medLogToJson))
            put("tasks",         toArray(tasks,     ::taskToJson))
            put("healthEntries", toArray(health,    ::healthToJson))
            put("vitalLogs",     toArray(vitals,    ::vitalToJson))
            put("moodEntries",   toArray(mood,      ::moodToJson))
            put("calendarEvents",toArray(calendar,  ::calendarToJson))
        }

        val stamp = SimpleDateFormat("yyyy-MM-dd_HH-mm", Locale.getDefault()).format(Date())
        val dir   = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file  = File(dir, "CareTracker-backup-$stamp.json")
        file.writeText(json.toString(2))
        file
    }

    suspend fun restoreBackup(
        file: File,
        repository: CareTrackerRepository
    ): Result<String> = runCatching {
        val json    = JSONObject(file.readText())
        val version = json.optInt("version", 1)
        require(version == 1) { "Unsupported backup version: $version" }

        val userJson     = json.getJSONObject("user")
        val backupUser   = jsonToUser(userJson)
        val existing     = repository.getUserByUsername(backupUser.username)
        val targetId: Long = if (existing != null) {
            existing.id
        } else {
            repository.insertUser(backupUser)
        }

        // Wipe existing data before restore
        repository.deleteUserData(targetId)

        fun arr(key: String) = json.optJSONArray(key) ?: JSONArray()

        var habitCount = 0; var medCount = 0; var taskCount = 0
        val habitsArr = arr("habits"); for (i in 0 until habitsArr.length()) { repository.insertHabit(jsonToHabit(habitsArr.getJSONObject(i)).copy(id = 0, userId = targetId)); habitCount++ }
        val habitLogsArr = arr("habitLogs"); for (i in 0 until habitLogsArr.length()) { repository.insertHabitLog(jsonToHabitLog(habitLogsArr.getJSONObject(i)).copy(id = 0)) }
        val medsArr = arr("medications"); for (i in 0 until medsArr.length()) { repository.insertMedication(jsonToMed(medsArr.getJSONObject(i)).copy(id = 0, userId = targetId)); medCount++ }
        val medLogsArr = arr("medLogs"); for (i in 0 until medLogsArr.length()) { repository.insertMedLog(jsonToMedLog(medLogsArr.getJSONObject(i)).copy(id = 0)) }
        val tasksArr = arr("tasks"); for (i in 0 until tasksArr.length()) { repository.insertTask(jsonToTask(tasksArr.getJSONObject(i)).copy(id = 0, userId = targetId)); taskCount++ }
        val healthArr = arr("healthEntries"); for (i in 0 until healthArr.length()) { repository.insertHealthEntry(jsonToHealth(healthArr.getJSONObject(i)).copy(id = 0, userId = targetId)) }
        val vitalsArr = arr("vitalLogs"); for (i in 0 until vitalsArr.length()) { repository.insertVitalLog(jsonToVital(vitalsArr.getJSONObject(i)).copy(id = 0, userId = targetId)) }
        val moodArr = arr("moodEntries"); for (i in 0 until moodArr.length()) { repository.insertMoodEntry(jsonToMood(moodArr.getJSONObject(i)).copy(id = 0, userId = targetId)) }
        val calArr = arr("calendarEvents"); for (i in 0 until calArr.length()) { repository.insertEvent(jsonToCalendar(calArr.getJSONObject(i)).copy(id = 0, userId = targetId)) }

        "$habitCount habits, $medCount medications, $taskCount tasks restored"
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private fun JSONArray.forEach(block: (JSONObject) -> Unit) {
        for (i in 0 until length()) block(getJSONObject(i))
    }

    private fun <T> toArray(list: List<T>, fn: (T) -> JSONObject): JSONArray {
        val arr = JSONArray(); list.forEach { arr.put(fn(it)) }; return arr
    }

    private fun String?.orNull(): String? = if (isNullOrEmpty()) null else this

    // ── Serializers ────────────────────────────────────────────────────────────

    private fun userToJson(u: UserEntity) = JSONObject().apply {
        put("username", u.username); put("email", u.email)
        put("passwordHash", u.passwordHash); put("displayName", u.displayName)
        put("avatarColor", u.avatarColor); put("role", u.role)
        put("isActive", u.isActive); put("createdAt", u.createdAt)
    }
    private fun jsonToUser(j: JSONObject) = UserEntity(
        username    = j.getString("username"),
        email       = j.getString("email"),
        passwordHash= j.getString("passwordHash"),
        displayName = j.optString("displayName").orNull(),
        avatarColor = j.optString("avatarColor", "#4f9cf9"),
        role        = j.optString("role", "user"),
        isActive    = j.optBoolean("isActive", true),
        createdAt   = j.optLong("createdAt", System.currentTimeMillis())
    )

    private fun habitToJson(h: HabitEntity) = JSONObject().apply {
        put("userId", h.userId); put("name", h.name); put("description", h.description)
        put("category", h.category); put("color", h.color); put("icon", h.icon)
        put("frequency", h.frequency); put("targetCount", h.targetCount)
        put("sortOrder", h.sortOrder); put("isActive", h.isActive); put("createdAt", h.createdAt)
    }
    private fun jsonToHabit(j: JSONObject) = HabitEntity(
        userId      = j.getLong("userId"),
        name        = j.getString("name"),
        description = j.optString("description").orNull(),
        category    = j.optString("category", "general"),
        color       = j.optString("color", "#4f9cf9"),
        icon        = j.optString("icon", "⭐"),
        frequency   = j.optString("frequency", "daily"),
        targetCount = j.optInt("targetCount", 1),
        sortOrder   = j.optInt("sortOrder", 0),
        isActive    = j.optBoolean("isActive", true),
        createdAt   = j.optLong("createdAt", System.currentTimeMillis())
    )

    private fun habitLogToJson(h: HabitLogEntity) = JSONObject().apply {
        put("habitId", h.habitId); put("loggedDate", h.loggedDate)
        put("count", h.count); put("note", h.note); put("loggedAt", h.loggedAt)
    }
    private fun jsonToHabitLog(j: JSONObject) = HabitLogEntity(
        habitId    = j.getLong("habitId"),
        loggedDate = j.getString("loggedDate"),
        count      = j.optInt("count", 1),
        note       = j.optString("note").orNull(),
        loggedAt   = j.optLong("loggedAt", System.currentTimeMillis())
    )

    private fun medToJson(m: MedicationEntity) = JSONObject().apply {
        put("userId", m.userId); put("name", m.name); put("genericName", m.genericName)
        put("dosage", m.dosage); put("dosageUnit", m.dosageUnit); put("form", m.form)
        put("frequency", m.frequency); put("timesPerDay", m.timesPerDay)
        put("scheduledTimes", m.scheduledTimes); put("withFood", m.withFood)
        put("instructions", m.instructions); put("prescriber", m.prescriber)
        put("color", m.color); put("currentCount", m.currentCount)
        put("refillReminderAt", m.refillReminderAt)
        put("sortOrder", m.sortOrder); put("isActive", m.isActive); put("createdAt", m.createdAt)
    }
    private fun jsonToMed(j: JSONObject) = MedicationEntity(
        userId          = j.getLong("userId"),
        name            = j.getString("name"),
        genericName     = j.optString("genericName").orNull(),
        dosage          = j.optString("dosage").orNull(),
        dosageUnit      = j.optString("dosageUnit", "mg"),
        form            = j.optString("form", "tablet"),
        frequency       = j.optString("frequency").orNull(),
        timesPerDay     = j.optInt("timesPerDay", 1),
        scheduledTimes  = j.optString("scheduledTimes").orNull(),
        withFood        = j.optBoolean("withFood", false),
        instructions    = j.optString("instructions").orNull(),
        prescriber      = j.optString("prescriber").orNull(),
        color           = j.optString("color", "#4f9cf9"),
        currentCount    = j.optInt("currentCount", 0),
        refillReminderAt= j.optInt("refillReminderAt", 7),
        sortOrder       = j.optInt("sortOrder", 0),
        isActive        = j.optBoolean("isActive", true),
        createdAt       = j.optLong("createdAt", System.currentTimeMillis())
    )

    private fun medLogToJson(m: MedLogEntity) = JSONObject().apply {
        put("medicationId", m.medicationId); put("takenAt", m.takenAt)
        put("takenDate", m.takenDate); put("scheduledTime", m.scheduledTime)
        put("status", m.status); put("note", m.note); put("doseTaken", m.doseTaken)
    }
    private fun jsonToMedLog(j: JSONObject) = MedLogEntity(
        medicationId  = j.getLong("medicationId"),
        takenAt       = j.optLong("takenAt", System.currentTimeMillis()),
        takenDate     = j.getString("takenDate"),
        scheduledTime = j.optString("scheduledTime").orNull(),
        status        = j.optString("status", "taken"),
        note          = j.optString("note").orNull(),
        doseTaken     = j.optString("doseTaken").orNull()
    )

    private fun taskToJson(t: TaskEntity) = JSONObject().apply {
        put("userId", t.userId); put("title", t.title); put("description", t.description)
        put("priority", t.priority); put("status", t.status); put("category", t.category)
        put("dueDate", t.dueDate); put("dueTime", t.dueTime); put("sortOrder", t.sortOrder)
        put("completedAt", t.completedAt); put("createdAt", t.createdAt)
    }
    private fun jsonToTask(j: JSONObject) = TaskEntity(
        userId      = j.getLong("userId"),
        title       = j.getString("title"),
        description = j.optString("description").orNull(),
        priority    = j.optString("priority", "medium"),
        status      = j.optString("status", "todo"),
        category    = j.optString("category", "personal"),
        dueDate     = j.optString("dueDate").orNull(),
        dueTime     = j.optString("dueTime").orNull(),
        sortOrder   = j.optInt("sortOrder", 0),
        completedAt = if (j.isNull("completedAt")) null else j.getLong("completedAt"),
        createdAt   = j.optLong("createdAt", System.currentTimeMillis())
    )

    private fun healthToJson(h: HealthEntryEntity) = JSONObject().apply {
        put("userId", h.userId); put("entryDate", h.entryDate)
        put("weight", h.weight); put("weightUnit", h.weightUnit)
        put("heartRate", h.heartRate)
        put("bloodPressureSystolic", h.bloodPressureSystolic)
        put("bloodPressureDiastolic", h.bloodPressureDiastolic)
        put("bloodSugar", h.bloodSugar); put("sleepHours", h.sleepHours)
        put("sleepQuality", h.sleepQuality); put("mood", h.mood)
        put("moodScore", h.moodScore); put("energy", h.energy)
        put("steps", h.steps); put("exerciseMinutes", h.exerciseMinutes)
        put("waterOz", h.waterOz); put("calories", h.calories); put("notes", h.notes)
    }
    private fun jsonToHealth(j: JSONObject) = HealthEntryEntity(
        userId                 = j.getLong("userId"),
        entryDate              = j.getString("entryDate"),
        weight                 = if (j.isNull("weight")) null else j.getDouble("weight"),
        weightUnit             = j.optString("weightUnit", "lbs").orNull(),
        heartRate              = if (j.isNull("heartRate")) null else j.getInt("heartRate"),
        bloodPressureSystolic  = if (j.isNull("bloodPressureSystolic")) null else j.getInt("bloodPressureSystolic"),
        bloodPressureDiastolic = if (j.isNull("bloodPressureDiastolic")) null else j.getInt("bloodPressureDiastolic"),
        bloodSugar             = if (j.isNull("bloodSugar")) null else j.getDouble("bloodSugar").toFloat(),
        sleepHours             = if (j.isNull("sleepHours")) null else j.getDouble("sleepHours").toFloat(),
        sleepQuality           = if (j.isNull("sleepQuality")) null else j.getInt("sleepQuality"),
        mood                   = if (j.isNull("mood")) null else j.getInt("mood"),
        moodScore              = if (j.isNull("moodScore")) null else j.getInt("moodScore"),
        energy                 = if (j.isNull("energy")) null else j.getInt("energy"),
        steps                  = if (j.isNull("steps")) null else j.getInt("steps"),
        exerciseMinutes        = if (j.isNull("exerciseMinutes")) null else j.getInt("exerciseMinutes"),
        waterOz                = if (j.isNull("waterOz")) null else j.getDouble("waterOz").toFloat(),
        calories               = if (j.isNull("calories")) null else j.getInt("calories"),
        notes                  = j.optString("notes").orNull()
    )

    private fun vitalToJson(v: VitalLogEntity) = JSONObject().apply {
        put("userId", v.userId); put("entryDate", v.entryDate)
        put("recordedAt", v.recordedAt); put("type", v.type)
        put("value", v.value); put("value2", v.value2)
        put("unit", v.unit); put("notes", v.notes)
    }
    private fun jsonToVital(j: JSONObject) = VitalLogEntity(
        userId     = j.getLong("userId"),
        entryDate  = j.getString("entryDate"),
        recordedAt = j.optLong("recordedAt", System.currentTimeMillis()),
        type       = j.getString("type"),
        value      = if (j.isNull("value")) null else j.getDouble("value").toFloat(),
        value2     = if (j.isNull("value2")) null else j.getDouble("value2").toFloat(),
        unit       = j.optString("unit", "mg/dL"),
        notes      = j.optString("notes").orNull()
    )

    private fun moodToJson(m: MoodJournalEntity) = JSONObject().apply {
        put("userId", m.userId); put("entryDate", m.entryDate)
        put("moodScore", m.moodScore); put("content", m.content)
        put("tags", m.tags); put("createdAt", m.createdAt)
    }
    private fun jsonToMood(j: JSONObject) = MoodJournalEntity(
        userId    = j.getLong("userId"),
        entryDate = j.getString("entryDate"),
        moodScore = if (j.isNull("moodScore")) null else j.getInt("moodScore"),
        content   = j.optString("content").orNull(),
        tags      = j.optString("tags").orNull(),
        createdAt = j.optLong("createdAt", System.currentTimeMillis())
    )

    private fun calendarToJson(c: CalendarEventEntity) = JSONObject().apply {
        put("userId", c.userId); put("title", c.title); put("description", c.description)
        put("category", c.category); put("color", c.color)
        put("startDatetime", c.startDatetime); put("endDatetime", c.endDatetime)
        put("allDay", c.allDay); put("location", c.location)
        put("reminderMinutes", c.reminderMinutes); put("recurrence", c.recurrence)
        put("isCompleted", c.isCompleted); put("createdAt", c.createdAt)
    }
    private fun jsonToCalendar(j: JSONObject) = CalendarEventEntity(
        userId          = j.getLong("userId"),
        title           = j.getString("title"),
        description     = j.optString("description").orNull(),
        category        = j.optString("category", "personal"),
        color           = j.optString("color", "#4f9cf9"),
        startDatetime   = j.getLong("startDatetime"),
        endDatetime     = if (j.isNull("endDatetime")) null else j.getLong("endDatetime"),
        allDay          = j.optBoolean("allDay", false),
        location        = j.optString("location").orNull(),
        reminderMinutes = j.optInt("reminderMinutes", 30),
        recurrence      = j.optString("recurrence", "none"),
        isCompleted     = j.optBoolean("isCompleted", false),
        createdAt       = j.optLong("createdAt", System.currentTimeMillis())
    )
}
