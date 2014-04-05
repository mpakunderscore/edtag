package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.Domain;
import models.WebData;
import models.UserData;
import play.cache.Cache;
import play.db.ebean.Model;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.*;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 20/03/14.
 */
public class Web extends Controller {

    //TODO Cache
    public static Result pages() {

        int pageSetSize = 80;

        //get userData (lastUpdate and count)
        List<UserData> userDataList = Ebean.find(UserData.class).where().eq("user_id", 0).setMaxRows(pageSetSize).findList();

        List<Long> webDataIds = new ArrayList<Long>();
        for (UserData userData : userDataList)
            webDataIds.add(userData.getWebDataId());

        //get webData (title and tags)
        List<WebData> webDataList = Ebean.find(WebData.class).where().idIn(webDataIds).findList();

        //get domains and sort (favIcons and )
        Map<String, Integer> domains = new HashMap<String, Integer>();

        //TODO look at getDomain(), it can be moved inside client for optimization. depend on pageSetSize, client should work fast and server results can be cached
        for (WebData webData : webDataList) {

            String domain = webData.getDomain();
            int count = domains.containsKey(domain) ? domains.get(domain) : 0;
            domains.put(domain, count + 1);
        }

        List<Domain> domainList = Ebean.find(Domain.class).where().idIn(Arrays.asList(domains.keySet())).findList();

        Map<String, JsonNode> out = new HashMap<String, JsonNode>();
        out.put("webDataList", toJson(webDataList));
        out.put("userDataList", toJson(userDataList));
        out.put("domains", toJson(domainList));

        return ok(toJson(out));
    }
}