import com.ddz.demo.core.model.data.UserDemo;
import com.ddz.demo.core.model.data.UserDemoFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DemoTests {

    @Test
    public void test() {

        List<UserDemo> users = UserDemoFactory.buildUser(10);

        users.forEach(user -> System.out.println(user));



    }
}
