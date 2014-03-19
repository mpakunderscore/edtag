package controllers;

import com.avaje.ebean.Ebean;
import models.Domain;
import play.mvc.Controller;
import play.mvc.Result;
import static play.libs.Json.toJson;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by ya.surin on 3/19/2014.
 */
public class DomainController extends Controller {

    public static Result getApproved() {
        if (session("connected") == null) return ok();

        List<Domain> domains = Ebean.find(Domain.class).where().eq("isApproved", true).findList();

        List<String> result = new ArrayList<String>();
        for (Domain domain: domains) {
            result.add(domain.getDomain());
        }

        return ok(toJson(result));
    }
}
