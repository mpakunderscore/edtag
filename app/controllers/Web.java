package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.WebData;
import models.UserData;
import play.cache.Cache;
import play.db.ebean.Model;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 20/03/14.
 */
public class Web extends Controller {

    public static Result pages() {

        List<UserData> userDataList = Ebean.find(UserData.class).where().eq("user_id", 0).setMaxRows(80).findList();

        //TODO move and cache
        List<Long> ids = new ArrayList<Long>();
        for (UserData userData : userDataList)
            ids.add(userData.getWebDataId());

        List<WebData> webDataList = Ebean.find(WebData.class).where().idIn(ids).findList();
        //

        Map<String, JsonNode> out = new HashMap<String, JsonNode>();
        out.put("userDataList", toJson(userDataList));
        out.put("webDataList", toJson(webDataList));

        return ok(toJson(out));
    }
}
