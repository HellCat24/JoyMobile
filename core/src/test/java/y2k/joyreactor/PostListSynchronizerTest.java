package y2k.joyreactor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import rx.Observable;
import y2k.joyreactor.common.PostGenerator;
import y2k.joyreactor.requests.PostsForTagRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by y2k on 03/11/15.
 */
public class PostListSynchronizerTest {

    @Mock
    private Repository<Post> repository;

    @Mock
    private PostsForTagRequest.Factory requestFactory;
    @Mock
    private PostsForTagRequest request;

//    @Captor
//    private ArgumentCaptor<ArrayList<Post>> captor;

    private PostListSynchronizer synchronizer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(requestFactory.make(anyString(), anyString())).thenReturn(request);

        synchronizer = new PostListSynchronizer(repository, requestFactory);
    }

    @Test
    public void testPreloadNewPosts() throws Exception {
        createInitState();

        boolean actual = synchronizer.preloadNewPosts().toBlocking().last();

        assertNull(synchronizer.getDivider());
        verify(request).requestAsync();
        assertFalse(actual);
    }

    @Test
    public void testApplyNew() throws Exception {
        createInitState();
        synchronizer.preloadNewPosts();

        when(repository.replaceAllAsync(anyListOf(Post.class)))
                .thenReturn(Observable.just(null));
        synchronizer.applyNew().toBlocking().last();

        repository.replaceAllAsync(PostGenerator.getPage(0));

        verify(repository).replaceAllAsync(argThat(new ListArgumentMatcher(PostGenerator.getPage(0))));
        assertEquals(10, (int) synchronizer.getDivider());
    }

    private void createInitState() {
        when(request.getPosts()).thenReturn(PostGenerator.getPage(0));
        when(request.requestAsync()).thenReturn(Observable.just(null));
        when(repository.queryAsync()).thenReturn(Observable.just(Collections.emptyList()));
    }

    private static class ListArgumentMatcher extends ArgumentMatcher<List<Post>> {

        private List<Post> expected;

        private ListArgumentMatcher(List<Post> expected) {
            this.expected = expected;
        }

        @Override
        public boolean matches(Object argument) {
            List<Post> actual = (List<Post>) argument;
            if (expected.size() != actual.size()) return false;
            for (int i = 0; i < actual.size(); i++)
                if (!Objects.equals(expected.get(i).id, actual.get(i).id)) return false;
            return true;
        }
    }
}