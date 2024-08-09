package de.industrieschule.vp.legacy;

import com.google.gson.JsonObject;
import de.morihofi.iscvplan.vertretungsplan.utlity.HashGenerator;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class LegacyVertretungsplanEndpoint implements Handler {
    @Override
    public void handle(@NotNull Context context) throws Exception {

        context.header("Content-Type", "application/json");

        String anz = context.queryParam("ANZ");
        String sec = context.queryParam("SEC"); // (Secure?) Hash
        String pw = context.queryParam("PW"); // Hashed password

        String secExpectedHash = HashGenerator.generateSecureHash();
        String pwExpectedHash = HashGenerator.generatePasswordHash("legacy"); //TODO: Change Password

        if (anz == null) {
            sendErrorResponse(context, "NO_ANS (inofficial error code)");
            return;
        }

        if (!sec.equals(secExpectedHash)) {
            sendErrorResponse(context, "WRONG_SECUREHASH");
            return;
        }

        if (!pw.equals(pwExpectedHash)) {
            sendErrorResponse(context, "WRONG_PASSWORD");
            return;
        }

        // If all checks pass, create and return the VertretungsplanResponse
        LegacyVertretungsplanResponse vp = new LegacyVertretungsplanResponse();

        LegacyVertretungsplanResponse.VertretungsplanDay day = new LegacyVertretungsplanResponse.VertretungsplanDay(
                "Legacy Vertretungsplan - It works!",
                "Reverse engineered with ♥ by morihofi",
                "unbekannt",
                "01.01.",
                new Date()
        );

        day.addLesson(new LegacyVertretungsplanResponse.VertretungsplanLesson(
                "Ganzer Tag",
                "Ganztägiger Stundenausfall, keine Vertretung",
                "INFO für ALLE",
                "ALLE"
        ));

        vp.addDay(day);

        context.contentType("application/json");
        context.result(vp.generateVertretungsplan().toString());
    }

    private void sendErrorResponse(Context context, String errorMessage) {
        JsonObject retObj = new JsonObject();
        retObj.addProperty("ERROR", errorMessage);
        context.json(retObj);
    }

}
