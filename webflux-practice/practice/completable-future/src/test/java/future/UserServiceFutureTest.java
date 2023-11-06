package future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.blocking.UserBlockingService;
import com.example.blocking.repository.ArticleRepository;
import com.example.blocking.repository.FollowRepository;
import com.example.blocking.repository.ImageRepository;
import com.example.blocking.repository.UserRepository;
import com.example.common.User;
import com.example.future.UserFutureService;
import com.example.future.repository.ArticleFutureRepository;
import com.example.future.repository.FollowFutureRepository;
import com.example.future.repository.ImageFutureRepository;
import com.example.future.repository.UserFutureRepository;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceFutureTest {
  UserFutureService userFutureService;
  UserFutureRepository userFutureRepository;
  ArticleFutureRepository articleFutureRepository;
  ImageFutureRepository imageFutureRepository;
  FollowFutureRepository followFutureRepository;

  @BeforeEach
  void setUp() {
    userFutureRepository = new UserFutureRepository();
    articleFutureRepository = new ArticleFutureRepository();
    imageFutureRepository = new ImageFutureRepository();
    followFutureRepository = new FollowFutureRepository();

    userFutureService = new UserFutureService(
        userFutureRepository,
        articleFutureRepository,
        imageFutureRepository,
        followFutureRepository
    );
  }

  @Test
  void getUserEmptyIfInvalidUserIdIsGiven() throws ExecutionException, InterruptedException {
    // given
    String userId = "invalid_user_id";

    // when
    Optional<User> user = userFutureService.getUserById(userId).get();

    // then
    assertTrue(user.isEmpty());
  }

  @Test
  void testGetUser() throws ExecutionException, InterruptedException {
    // given
    String userId = "1234";

    // when
    Optional<User> optionalUser = userFutureService.getUserById(userId).get();

    // then
    assertFalse(optionalUser.isEmpty());
    var user = optionalUser.get();
    assertEquals(user.getName(), "taewoo");
    assertEquals(user.getAge(), 32);

    assertFalse(user.getProfileImage().isEmpty());
    var image = user.getProfileImage().get();
    assertEquals(image.getId(), "image#1000");
    assertEquals(image.getName(), "profileImage");
    assertEquals(image.getUrl(), "https://dailyone.com/images/1000");

    assertEquals(2, user.getArticleList().size());

    assertEquals(1000, user.getFollowCount());
  }
}
