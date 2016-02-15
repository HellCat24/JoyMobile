package y2k.joyreactor;

import kotlin.Unit;

import org.jetbrains.annotations.NotNull;
import org.ocpsoft.prettytime.PrettyTime;
import org.robovm.apple.foundation.NSIndexPath;
import org.robovm.apple.uikit.*;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.IBOutlet;
import y2k.joyreactor.common.BaseUIViewController;
import y2k.joyreactor.common.ServiceLocator;
import y2k.joyreactor.platform.ImageRequest;
import y2k.joyreactor.presenters.PostListPresenter;

import java.util.List;

/**
 * Created by y2k on 9/26/15.
 */
@CustomClass("PostListViewController")
public class PostListViewController extends BaseUIViewController implements PostListPresenter.View {

    @IBOutlet
    UITableView list;
    @IBOutlet
    UIActivityIndicatorView progressView;
    @IBOutlet
    UIButton applyButton;
    UIRefreshControl refresher;

    PostListPresenter presenter;
    List<Post> posts;

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        list.setDataSource(new PostDataSource());
        list.setDelegate(new PostDelegate());

        new SideMenu(this, "Menu").attach();

        list.addSubview(refresher = new UIRefreshControl());

        getNavigationItem().getRightBarButtonItem().setOnClickListener(sender ->
            new MenuController(this)
                .addNavigation("Add tag", "AddTag")
                .addNavigation("Profile", "Profile")
                .addNavigation("Messages", "MessageThreads")
                .addCancel("Cancel")
                .present());

        progressView.stopAnimating();

        applyButton.addOnTouchUpInsideListener((sender, e) -> presenter.applyNew());

        presenter = ServiceLocator.INSTANCE.resolve(getLifeCycleService(), this);
    }

    // ==========================================
    // Implement View methods
    // ==========================================

    @Override
    public void setBusy(boolean isBusy) {
        if (isBusy) refresher.beginRefreshing();
        else refresher.endRefreshing();
    }

    @Override
    public void reloadPosts(List<Post> posts, Integer divider) {
        this.posts = posts;
        list.reloadData();
    }

    @Override
    public void setHasNewPosts(boolean hasNewPosts) {
        new BottomButton(applyButton).setHidden(!hasNewPosts);
    }

    @NotNull
    @Override
    public String getPostType() {
        return "";
    }

    class PostDataSource extends UITableViewDataSourceAdapter {

        @Override
        public long getNumberOfRowsInSection(UITableView tableView, long section) {
            int count = posts == null ? 0 : posts.size();
            return count == 0 ? 0 : count + 1;
        }

        @Override
        public UITableViewCell getCellForRow(UITableView tableView, NSIndexPath indexPath) {
            if (indexPath.getRow() == posts.size()) {
                LoadMoreCell cell = (LoadMoreCell) tableView.dequeueReusableCell("LoadMore");
                cell.presenter = presenter;
                return cell;
            } else {
                PostCell cell = (PostCell) tableView.dequeueReusableCell("Post");
                Post post = posts.get(indexPath.getRow());
                cell.update(presenter, post);

                ((UILabel) cell.getViewWithTag(3)).setText(post.getUserName());
                ((UILabel) cell.getViewWithTag(4)).setText(new PrettyTime().format(post.getCreated()));

                Image image = post.getImage();
                if (image == null) {
                    // TODO
                } else {
                    loadImage(post.getImage(), 300,
                        (int) (300 / image.getAspect()),
                        (UIImageView) cell.getViewWithTag(1));
                }

                UIImageView userImageView = (UIImageView) cell.getViewWithTag(2);
                loadImage(post.getUserImage2().toImage(), 50, 50, userImageView);
                userImageView.getLayer().setCornerRadius(userImageView.getFrame().getWidth() / 2);

                UIView root = cell.getViewWithTag(10);
                root.getLayer().setCornerRadius(10);
                root.getLayer().setMasksToBounds(true);
                root.getLayer().setBorderColor(UIColor.lightGray().getCGColor());
                root.getLayer().setBorderWidth(1);
                return cell;
            }
        }

        void loadImage(Image image, int width, int height, UIImageView iv) {
            iv.setAlpha(0);
            new ImageRequest()
                .setUrl(image)
                .setSize(width, height)
                .to(iv, data -> {
                    iv.setImage(data);
                    UIView.animate(0.3, () -> iv.setAlpha(1));
                    return Unit.INSTANCE;
                });
        }
    }

    class PostDelegate extends UITableViewDelegateAdapter {

        @Override
        public double getHeightForRow(UITableView tableView, NSIndexPath indexPath) {
            if (indexPath.getRow() == posts.size()) return -1;
            Post post = posts.get(indexPath.getRow());
            Image image = post.getImage();
            double imageHeight = image == null ? 0 : (tableView.getFrame().getWidth() - 16) / image.getAspect();
            return imageHeight + 66 + 16;
        }

        @Override
        public void didEndDecelerating(UIScrollView scrollView) {
            if (refresher.isRefreshing()) presenter.reloadFirstPage();
        }
    }
}