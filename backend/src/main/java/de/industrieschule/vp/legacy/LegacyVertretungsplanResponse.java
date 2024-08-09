package de.industrieschule.vp.legacy;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.morihofi.iscvplan.vertretungsplan.utlity.HashGeneratorHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LegacyVertretungsplanResponse {

    private final ArrayList<VertretungsplanDay> days = new ArrayList<>();

    public void addDay(VertretungsplanDay day) {
        days.add(day);
    }

    public JsonObject generateVertretungsplan() {
        JsonObject vertretungsplan = new JsonObject();

        for (VertretungsplanDay day : days) {
            JsonObject objDayContent = new JsonObject();
            objDayContent.addProperty("Header", day.getHeader());
            objDayContent.addProperty("Footer", day.getFooter());
            objDayContent.addProperty("Block", day.getBlock());
            objDayContent.addProperty("Update", HashGeneratorHelper.formatDate("yyyy-MM-dd H:m:s", day.getUpdate()));

            JsonArray lessonArr = getJsonElements(day);
            objDayContent.add("Inhalt", lessonArr);

            vertretungsplan.add(day.getDate(), objDayContent);
        }

        return vertretungsplan;
    }

    private static @NotNull JsonArray getJsonElements(VertretungsplanDay day) {
        JsonArray lessonArr = new JsonArray();
        for (VertretungsplanLesson lesson : day.getLessons()) {
            JsonObject lessonObj = new JsonObject();
            lessonObj.addProperty("Stunde", lesson.getStunde());
            lessonObj.addProperty("Massnahme", lesson.getMassnahme());
            lessonObj.addProperty("Verantwortlicher", lesson.getVerantwortlicher());
            lessonObj.addProperty("Klasse", lesson.getKlasse());

            lessonArr.add(lessonObj);
        }
        return lessonArr;
    }

    public static class VertretungsplanDay {
        private final ArrayList<VertretungsplanLesson> lessons = new ArrayList<>();
        private final String header;
        private final String footer;
        private final String block;
        private final String date;
        private final Date update;

        public VertretungsplanDay(String header, String footer, String block, String date, Date update) {
            this.header = header;
            this.footer = footer;
            this.block = block;
            this.date = date;
            this.update = update;
        }

        public ArrayList<VertretungsplanLesson> getLessons() {
            return lessons;
        }

        public String getHeader() {
            return header;
        }

        public String getFooter() {
            return footer;
        }

        public String getBlock() {
            return block;
        }

        public String getDate() {
            return date;
        }

        public Date getUpdate() {
            return update;
        }

        public void addLesson(VertretungsplanLesson lesson) {
            lessons.add(lesson);
        }
    }

    public static class VertretungsplanLesson {
        private final String stunde;
        private final String massnahme;
        private final String verantwortlicher;
        private final String klasse;

        public VertretungsplanLesson(String stunde, String massnahme, String verantwortlicher, String klasse) {
            this.stunde = stunde;
            this.massnahme = massnahme;
            this.verantwortlicher = verantwortlicher;
            this.klasse = klasse;
        }

        public String getStunde() {
            return stunde;
        }

        public String getMassnahme() {
            return massnahme;
        }

        public String getVerantwortlicher() {
            return verantwortlicher;
        }

        public String getKlasse() {
            return klasse;
        }
    }
}
