package me.ccrama.redditslide.test;

import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import me.ccrama.redditslide.OpenRedditLink;
import me.ccrama.redditslide.OpenRedditLink.RedditLinkType;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;


@RunWith(RobolectricTestRunner.class)
public class OpenRedditLinkTest {

    // Less characters
    private String formatURL(String url) {
        Uri uri = OpenRedditLink.formatRedditUrl(url);

        if (uri == null) {
            return null;
        }

        return uri.toString();
    }

    private OpenRedditLink.RedditLinkType getType(String url) {
        Uri uri = OpenRedditLink.formatRedditUrl(url);

        return OpenRedditLink.getRedditLinkType(uri);
    }

    @Test
    public void detectsShortened() {
        assertThat(getType("https://redd.it/eorhm/"), is(RedditLinkType.SHORTENED));
    }

    @Test
    public void detectsWiki() {
        assertThat(getType("https://www.reddit.com/r/Android/wiki/index"), is(RedditLinkType.WIKI));
        assertThat(getType("https://www.reddit.com/r/Android/help"), is(RedditLinkType.WIKI));
        assertThat(getType("https://reddit.com/help"), is(RedditLinkType.WIKI));
    }

    @Test
    public void detectsComment() {
        assertThat(
                getType("https://www.reddit.com/r/announcements/comments/eorhm/reddit_30_less_typing/c19qk6j"),
                is(RedditLinkType.COMMENT_PERMALINK));
        assertThat(getType("https://www.reddit.com/r/announcements/comments/eorhm//c19qk6j"),
                is(RedditLinkType.COMMENT_PERMALINK));
    }

    @Test
    public void detectsSubmission() {
        assertThat(
                getType("https://www.reddit.com/r/announcements/comments/eorhm/reddit_30_less_typing/"),
                is(RedditLinkType.SUBMISSION));
    }

    @Test
    public void detectsSubmissionWithoutSub() {
        assertThat(getType("https://www.reddit.com/comments/eorhm/reddit_30_less_typing/"),
                is(RedditLinkType.SUBMISSION_WITHOUT_SUB));
    }

    @Test
    public void detectsSubreddit() {
        assertThat(getType("https://www.reddit.com/r/android"), is(RedditLinkType.SUBREDDIT));
    }

    @Test
    public void detectsSearch() {
//        assertThat(getType("https://www.reddit.com/search?q=test"),
//                is(RedditLinkType.SEARCH));
        assertThat(
                getType("https://www.reddit.com/r/Android/search?q=test&restrict_sr=on&sort=relevance&t=all"),
                is(RedditLinkType.SEARCH));
    }

    @Test
    public void detectsUser() {
        assertThat(getType("https://www.reddit.com/u/l3d00m"), is(RedditLinkType.USER));
    }

    @Test
    public void detectsHome() {
        assertThat(getType("https://www.reddit.com/"), is(RedditLinkType.HOME));
    }

    @Test
    public void detectsOther() {
        assertThat(getType("https://www.reddit.com/r/pics/about/moderators"),
                is(RedditLinkType.OTHER));
        assertThat(getType("https://www.reddit.com/live/x9gf3donjlkq/discussions"),
                is(RedditLinkType.OTHER));
        assertThat(getType("https://www.reddit.com/live/x9gf3donjlkq/contributors"),
                is(RedditLinkType.OTHER));
    }

    @Test
    public void detectsLive() {
        assertThat(getType("https://www.reddit.com/live/x9gf3donjlkq"), is(RedditLinkType.LIVE));
    }

    @Test
    public void formatsBasic() {
        assertThat(formatURL("https://www.reddit.com/live/wbjbjba8zrl6"),
                is("https://reddit.com/live/wbjbjba8zrl6"));
    }

    @Test
    public void formatsNp() {
        assertThat(formatURL("https://np.reddit.com/live/wbjbjba8zrl6"),
                is("https://npreddit.com/live/wbjbjba8zrl6"));
    }

    @Test
    public void formatsSubdomains() {
        assertNull(formatURL("https://beta.reddit.com/"));
        assertNull(formatURL("https://blog.reddit.com/"));
        assertNull(formatURL("https://code.reddit.com/"));
        // https://www.reddit.com/r/modnews/comments/4z2nic/upcoming_change_updates_to_modredditcom/
        assertNull(formatURL("https://mod.reddit.com/"));
        // https://www.reddit.com/r/changelog/comments/49jjb7/reddit_change_click_events_on_outbound_links/
        assertNull(formatURL("https://out.reddit.com/"));
        assertNull(formatURL("https://store.reddit.com/"));
        assertThat(formatURL("https://pay.reddit.com/"), is("https://reddit.com/"));
        assertThat(formatURL("https://ssl.reddit.com/"), is("https://reddit.com/"));
        assertThat(formatURL("https://en-gb.reddit.com/"), is("https://reddit.com/"));
        assertThat(formatURL("https://us.reddit.com/"), is("https://reddit.com/"));
    }

    @Test
    public void formatsSubreddit() {
        assertThat(formatURL("/r/android"), is("https://reddit.com/r/android"));
        assertThat(formatURL("https://android.reddit.com"), is("https://reddit.com/r/android"));
    }

    @Test
    public void formatsWiki() {
        assertThat(formatURL("https://reddit.com/help"),
                is("https://reddit.com/r/reddit.com/wiki"));
        assertThat(formatURL("https://reddit.com/help/registration"),
                is("https://reddit.com/r/reddit.com/wiki/registration"));
        assertThat(formatURL("https://www.reddit.com/r/android/wiki/index"),
                is("https://reddit.com/r/android/wiki/index"));
    }

    @Test
    public void formatsProtocol() {
        assertThat(formatURL("http://reddit.com"), is("https://reddit.com"));
        assertThat(formatURL("Https://reddit.com"), is("https://reddit.com"));
        assertThat(formatURL("https://reddit.com"), is("https://reddit.com"));
    }
}
