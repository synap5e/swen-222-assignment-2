package space.network;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import space.network.message.MessageTests;

@RunWith(Suite.class)
@SuiteClasses({ ConnectionTests.class, MessageTests.class })
public class NetworkTests {

}
