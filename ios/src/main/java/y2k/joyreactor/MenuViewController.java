package y2k.joyreactor;

import kotlin.Unit;
import org.jetbrains.annotations.NotNull;
import org.robovm.apple.foundation.NSIndexPath;
import org.robovm.apple.uikit.*;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.IBOutlet;
import y2k.joyreactor.common.BaseUIViewController;
import y2k.joyreactor.common.ServiceLocator;
import y2k.joyreactor.platform.ImageRequest;
import y2k.joyreactor.presenters.TagListPresenter;

import java.util.List;

/**
 * Created by y2k on 9/26/15.
 */
@CustomClass("MenuViewController")
public class MenuViewController extends BaseUIViewController {

    UITableView list;
    List<? extends Tag> tags;
    private TagListPresenter presenter;

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();

        presenter = ServiceLocator.INSTANCE.resolve(getLifeCycleService(), new TagListPresenter.View() {

            @Override
            public void reloadData(@NotNull List<Tag> tags) {
                MenuViewController.this.tags = tags;
                list.reloadData();
            }
        });

        list.setDataSource(new TagDataSource());
        list.setDelegate(new TagDelegate());
    }

    @IBOutlet
    void setList(UITableView list) {
        this.list = list;
    }

    class TagDataSource extends UITableViewDataSourceAdapter {

        @Override
        public long getNumberOfRowsInSection(UITableView tableView, long section) {
            return (tags == null ? 0 : tags.size()) + 1;
        }

        @Override
        public UITableViewCell getCellForRow(UITableView tableView, NSIndexPath indexPath) {
            if (indexPath.getRow() == 0) {
                MenuHeaderCell cell = (MenuHeaderCell) tableView.dequeueReusableCell("Header");
                cell.setPresenter(presenter);
                return cell;
            } else {
                UITableViewCell cell = tableView.dequeueReusableCell("Tag");
                Tag i = tags.get(indexPath.getRow() - 1);

                UIImageView iv = (UIImageView) cell.getViewWithTag(1);
                iv.getLayer().setCornerRadius(iv.getFrame().getWidth() / 2);
                new ImageRequest()
                    .setUrl(i.getImage())
                    .setSize(40, 40)
                    .to(iv, uiImage -> {
                        iv.setImage(uiImage);
                        return Unit.INSTANCE;
                    });
                ((UILabel) cell.getViewWithTag(2)).setText(i.getTitle());
                return cell;
            }
        }
    }

    class TagDelegate extends UITableViewDelegateAdapter {

        @Override
        public double getHeightForRow(UITableView tableView, NSIndexPath indexPath) {
            return indexPath.getRow() == 0 ? 136 : 50;
        }

        @Override
        public void didSelectRow(UITableView tableView, NSIndexPath indexPath) {
            presenter.selectTag(tags.get(indexPath.getRow() - 1));
            tableView.deselectRow(indexPath, true);
        }
    }
}