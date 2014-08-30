package utils;

import controllers.*;
import controllers.parsers.Watcher;
import models.WebData;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import play.Logger;
import play.mvc.Result;

import java.io.IOException;
import java.util.*;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 21/08/14.
 */
public class Research {

    public static void scan(String[] urls) throws Exception {

        for (String url : urls) {
            API.add(url, 0);
        }
    }

    public static Set<String> getLinksFromUrl(String url, String class_) {

        Set<String> urls = new HashSet<String>();

//        String urlDomain = WebData.getDomainString(url);

        Document doc;

        Connection connection = Jsoup.connect(url);

        try {

            doc = connection.userAgent(Watcher.USER_AGENT).followRedirects(true).get();

        } catch (IOException exception) { //TODO

            return null;
        }

        if (class_ == null || class_.length() == 0) {

            urls.addAll(getLinksFromElement(doc.body()));

        } else {

            Elements elementsByClass = doc.body().select("." + class_);

            for (org.jsoup.nodes.Element element : elementsByClass) {

                urls.addAll(getLinksFromElement(element));
            }
        }

        Logger.debug("Found urls: " + url.length());
        return getCorrectLinks(url, urls);
    }

    public static Set<String> getLinksFromElement(org.jsoup.nodes.Element element) {

        Set<String> urls = new HashSet<String>();
        Elements linksElements = element.select("a");

        for (org.jsoup.nodes.Element linkElement : linksElements) {

            String href = linkElement.attr("href");

            urls.add(href);
            Logger.debug("URL found: " + href);
        }

        return urls;
    }

    public static Set<String> getCorrectLinks(String url, Set<String> urls) {

        Set<String> links = new HashSet<String>();

        String domainString = WebData.getDomainString(url);

        String protocol = url.split("://")[0];

        String domainStringWithProtocol =  protocol + "://" + domainString; //TODO !!!

        for (String href : urls) {

            String link = "";
            if (href.startsWith("//"))
                link = protocol + ":" + href;

            else if (href.startsWith("/"))
                link = domainStringWithProtocol + href;

            else if (href.startsWith("http"))
                link = href;

            else if (href.startsWith("#"))
                link = url + href;

            else
                link = domainStringWithProtocol + "/" + href;

            links.add(link);
        }

        return links;
    }

    static String[] mediumUrls = {"https://medium.com/comedy-corner/10-tricks-to-appear-smart-during-meetings-27b489a39d1a",
            "https://medium.com/matter/my-life-with-piper-from-big-house-to-small-screen-592b35f5af94",
            "https://medium.com/@manicho/how-a-password-changed-my-life-7af5d5f28038",
            "https://medium.com/the-nib/trigger-warning-breakfast-c6cdeec070e6",
            "https://medium.com/teaching-learning/an-open-letter-to-my-sons-kindergarten-teacher-ed1f90239ae7",
            "https://medium.com/cycling-in-the-city/why-bikes-make-smart-people-say-dumb-things-9316abbd5735",
            "https://medium.com/@hollyseymour/10-things-no-one-tells-women-about-turning-40-8d7c582ef795",
            "https://medium.com/things-ive-written/thirty-things-ive-learned-482765ee3503",
            "https://medium.com/matter/speaking-up-every-fucking-time-a61a24aa7629",
            "https://medium.com/@increment/the-ambush-at-sheridan-springs-3a29d07f6836",
            "https://medium.com/matter/the-ping-pong-theory-of-tech-world-sexism-c2053c10c06c",
            "https://medium.com/@basicallystacey/the-top-3-reasons-why-filipinos-boycott-sofitel-manila-f7405c9cde3b",
            "https://medium.com/code-adventures/farewell-node-js-4ba9e7f3e52b",
            "https://medium.com/@guvenindahouse/turk-milleti-ve-sikinin-ucuyla-i-s-yapmak-gerce-i-e7b170699056",
            "https://medium.com/race-class/daquan-is-a-white-girl-9ba9de9fba95",
            "https://medium.com/@nikkidurkin99/my-startup-failed-and-this-is-what-it-feels-like-c5d64b3ae96b",
            "https://medium.com/message/the-secret-of-minecraft-97dfacb05a3c",
            "https://medium.com/@cammipham/7-things-you-need-to-stop-doing-to-be-more-productive-backed-by-science-a988c17383a6",
            "https://medium.com/human-parts/a-gentlemens-guide-to-rape-culture-7fc86c50dc4c",
            "https://medium.com/@Rafalogo/el-mundo-de-los-hombres-gira-en-torno-a-los-culos-b6f7255f399b",
            "https://medium.com/war-is-boring/fd-how-the-u-s-and-its-allies-got-stuck-with-the-worlds-worst-new-warplane-5c95d45f86a5",
            "https://medium.com/@RickWebb/the-economics-of-star-trek-29bab88d50",
            "https://medium.com/matter/the-martyrdom-of-weev-9e72da8a133d",
            "https://medium.com/the-archipelago/i-left-facebook-and-you-can-too-1e9a1886c4e4",
            "https://medium.com/@sgehrman/banned-for-life-c62f2404f66",
            "https://medium.com/message/you-are-not-late-b3d76f963142",
            "https://medium.com/@MittRomney/a-family-tradition-f18c5d60c0e3",
            "https://medium.com/message/the-american-room-3fce9b2b98c5",
            "https://medium.com/@nkkl/ride-like-a-girl-1d5524e25d3a",
            "https://medium.com/@olivermorales/wake-up-now-fraude-o-fantasia-105c5cfc092a",
            "https://medium.com/starts-with-a-bang/ask-ethan-45-how-deep-does-the-multiverse-go-70820b852ee8",
            "https://medium.com/@aaln/how-to-find-your-uber-passenger-rating-4aa1d9cc927f",
            "https://medium.com/gwot-weather-report/10-tricks-to-appear-smart-during-development-meetings-4194f030d6ab",
            "https://medium.com/matter/the-untold-and-insanely-weird-story-of-a-rods-doping-habits-e888f08e012a",
            "https://medium.com/message/lets-fly-d566ecd35678",
            "https://medium.com/message/how-to-always-be-right-on-the-internet-delete-your-mistakes-519a595da2f5",
            "https://medium.com/@joaomilho/festina-lente-e29070811b84",
            "https://medium.com/the-nib/orientation-police-fccd8118b6f9",
            "https://medium.com/@sofauxboho/pack-like-a-nerd-2157aa39738f",
            "https://medium.com/@jonkrakauer/greg-mortenson-disgraced-author-of-three-cups-of-tea-believes-he-will-have-the-last-laugh-760949b1f964",
            "https://medium.com/@GuusterBeek/redesigning-the-world-cup-2014-brazil-5f3471869815",
            "https://medium.com/@richroll/slaying-the-protein-myth-edf53585e778",
            "https://medium.com/@directordanic/the-problem-with-false-feminism-7c0bbc7252ef",
            "https://medium.com/life-hacks-for-business/12-lessons-of-waking-up-at-4-30-a-m-for-21-days-90d1053c3634",
            "https://medium.com/@thechriskiehl/the-great-white-space-debate-3633cba8b5c1",
            "https://medium.com/@publicanthro/academia-and-the-people-without-jobs-c7e503f3bbc3",
            "https://medium.com/@luckyshirt/dear-guy-who-just-made-my-burrito-fd08c0babb57",
            "https://medium.com/opinionated-angularjs/techniques-for-authentication-in-angularjs-applications-7bbf0346acec",
            "https://medium.com/message/what-is-public-f33b16d780f9",
            "https://medium.com/@tom_watson/something-terrible-could-be-happening-in-parliament-on-monday-and-i-need-your-urgent-attention-22c3136de17c",
            "https://medium.com/bits-and-pieces-of-my-mind/membedah-keanehan-membedah-keanehan-website-kawalpemilu-org-bagian1-927406a3e478",
            "https://medium.com/the-nib/the-laughing-muggers-80b1b36e4448",
            "https://medium.com/@cambronero/el-profe-pinta-d7811529e3d9",
            "https://medium.com/@dickeyxxx/best-practices-for-building-angular-js-apps-266c1a4a6917",
            "https://medium.com/@gomattymo/how-i-sold-1m-of-art-884bfbf3e2ae",
            "https://medium.com/re-form/welcome-to-dataland-d8c06a5f3bc6",
            "https://medium.com/matter/did-big-pharma-test-your-meds-on-homeless-people-a6d8d3fc7dfe",
            "https://medium.com/starts-with-a-bang/the-two-faces-of-the-moon-398fbc85840d",
            "https://medium.com/war-is-boring/your-periodic-reminder-that-the-v-22-is-a-piece-of-junk-db72a8a23ccf",
            "https://medium.com/message/everything-is-broken-81e5f33a24e1",
            "https://medium.com/raymmars-reads/7-reasons-why-you-will-never-do-anything-amazing-with-your-life-2a1841f1335d",
            "https://medium.com/@tikbalang/the-other-ateneo-dc329c89f65a",
            "https://medium.com/race-class/mh-17-left-amsterdam-fcef850b1fec",
            "https://medium.com/@_marcos_otero/the-real-10-algorithms-that-dominate-our-world-e95fa9f16c04",
            "https://medium.com/@cammipham/cruel-intentions-how-i-hacked-tinder-and-got-2015-matches-in-under-17-hours-dd51cdd5d7fe",
            "https://medium.com/message/how-tinder-co-founder-whitney-wolfe-hacked-metcalfes-law-f607dddbde66",
            "https://medium.com/@maebert/9-things-i-learned-as-a-software-engineer-c2c9f76c9266",
            "https://medium.com/human-parts/i-took-shrooms-and-nude-modeled-10d5a461a65b",
            "https://medium.com/matter/the-124-421-man-56e3b84a321",
            "https://medium.com/matter/the-larry-spin-off-is-going-to-be-awesome-de067bd2d53",
            "https://medium.com/matter/miss-american-dream-31c823ad0e5a",
            "https://medium.com/bad-words/the-bullshit-machine-df95646d0383",
            "https://medium.com/race-class/maybe-black-women-dont-need-friends-like-you-65ecbf502f17",
            "https://medium.com/the-cauldron/the-man-who-sold-the-world-3df16d8b754a",
            "https://medium.com/matter/how-to-ignore-a-plague-14ea08694cc",
            "https://medium.com/@waresarah/the-real-lives-of-new-jersey-the-heroin-capital-that-kills-10-things-you-need-to-know-c77e763bcc12",
            "https://medium.com/@PR_Doktor/hort-endlich-auf-euch-fur-eure-eigene-ruckstandigkeit-auch-noch-zu-feiern-7d47cb7d406e",
            "https://medium.com/the-cauldron/the-best-player-since-jordan-3da47f9ca3e1",
            "https://medium.com/@jgvandehey/this-is-your-brain-on-mobile-15308056cfae",
            "https://medium.com/prezi-engineering/how-and-why-prezi-turned-to-javascript-56e0ca57d135",
            "https://medium.com/@dweekly/dear-foursquare-c7c441fdf25e",
            "https://medium.com/@chase_reeves/perspectives-1-year-after-my-sons-birth-death-f3348475bb28",
            "https://medium.com/@joshbocanegra/how-to-win-a-debate-against-an-atheist-for-the-existence-of-god-93efa31a0df4",
            "https://medium.com/@adambutler/why-i-connected-my-coffee-machine-to-the-internet-bd0c1546c03c",
            "https://medium.com/@ajt/how-we-got-the-com-for-our-startup-b48fd6c5511",
            "https://medium.com/message/doomed-to-repeat-it-fb03757dfcca",
            "https://medium.com/matter/franciss-holy-war-70a382606c0d",
            "https://medium.com/message/what-does-the-facebook-experiment-teach-us-c858c08e287f",
            "https://medium.com/@mbrianorme/3-things-never-to-say-at-a-funeral-164267bc91f",
            "https://medium.com/human-parts/when-your-mother-says-shes-fat-bf5111e68cc1",
            "https://medium.com/war-is-boring/everyone-relax-the-armys-native-american-helicopter-names-are-not-racist-d21beb55d782",
            "https://medium.com/the-cauldron/carlos-boozer-is-the-worst-a41cef8f2887",
            "https://medium.com/message/learning-from-natesilver538s-omg-wrong-bra-vs-ger-prediction-9d47fa0611e",
            "https://medium.com/war-is-boring/now-we-know-what-the-navys-next-submarine-will-look-like-781439bbd214",
            "https://medium.com/@jan.curn/how-bug-in-dropbox-permanently-deleted-my-8000-photos-cb7dcf13647b",
            "https://medium.com/the-physics-arxiv-blog/the-astounding-link-between-the-p-np-problem-and-the-quantum-nature-of-universe-7ef5eea6fd7a",
            "https://medium.com/the-cauldron/all-you-need-is-love-4a89d785e32e",
            "https://medium.com/the-archipelago/guess-ill-go-eat-worms-5ba9711905ec",
            "https://medium.com/the-nib/something-from-beyond-the-grave-dd0509092b2a",
            "https://medium.com/message/failing-the-third-machine-age-1883e647ba74"};
}
