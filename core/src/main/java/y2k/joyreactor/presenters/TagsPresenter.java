package y2k.joyreactor.presenters;

import rx.Observable;
import y2k.joyreactor.*;
import y2k.joyreactor.common.Messages;
import y2k.joyreactor.requests.MyTagsRequest;
import y2k.joyreactor.requests.UsernameRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by y2k on 9/26/15.
 */
public class TagsPresenter extends Presenter {

    private final View view;

    public TagsPresenter(View view) {
        this.view = view;
    }

    @Override
    public void activate() {
        new UsernameRequest()
                .request()
                .flatMap(username -> username == null
                        ? new DefaultTagRequest().request()
                        : new MyTagsRequest(username).request())
                .subscribe(view::reloadData, Throwable::printStackTrace);
    }

    public void selectTag(Tag item) {
        new Messages.TagSelected(item).broadcast();
    }

    public interface View {

        void reloadData(List<Tag> tags);
    }

    private static class DefaultTagRequest {

        private List<Tag> tags = new ArrayList<>();

        DefaultTagRequest() {
            addTag("Anime", "2851");
            addTag("Красивые картинки", "31505");
            addTag("Игры", "753");

            addTag("Комиксы", "27");
            addTag("Гифки", "116");
            addTag("Песочница", "10891");
            addTag("Geek", "7");
            addTag("Котэ", "1481");
            addTag("Видео", "1243");
            addTag("Story", "227");
        }

        private void addTag(String title, String tagId) {
            Tag tag = new Tag();
            tag.title = title;
            tag.image = "http://img0.joyreactor.cc/pics/avatar/tag/" + tagId;
            tags.add(tag);
        }

        public Observable<List<Tag>> request() {
            return Observable.just(tags);
        }
    }
}