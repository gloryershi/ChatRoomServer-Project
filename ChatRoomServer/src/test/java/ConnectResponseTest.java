import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConnectResponseTest {

  @Test
  public void testEquals() {
    ConnectResponse r1 = new ConnectResponse(true, "Connected");
    ConnectResponse r2 = new ConnectResponse(true, "Connected");
    ConnectResponse r3 = new ConnectResponse(true, "Connected");
    ConnectResponse r4 = new ConnectResponse(false, "Failed");
    ConnectResponse r5 = new ConnectResponse(true, "Different");

    Assertions.assertEquals(r1, r1);

    Assertions.assertEquals(r1, r2);
    Assertions.assertEquals(r2, r1);

    Assertions.assertEquals(r2, r3);
    Assertions.assertEquals(r1, r3);

    Assertions.assertNotEquals(r1, null);

    Assertions.assertNotEquals(r1, "some string");

    Assertions.assertNotEquals(r1, r4);
    Assertions.assertNotEquals(r1, r5);
  }

  @Test
  public void testHashCode() {
    ConnectResponse r1 = new ConnectResponse(true, "Connected");
    ConnectResponse r2 = new ConnectResponse(true, "Connected");
    ConnectResponse r3 = new ConnectResponse(false, "Failed");

    Assertions.assertEquals(r1.hashCode(), r2.hashCode());

    Assertions.assertNotEquals(r1.hashCode(), r3.hashCode());
  }

  @Test
  public void testToString() {
    ConnectResponse r1 = new ConnectResponse(true, "Connected");
    String str = r1.toString();
    Assertions.assertNotNull(str);
    Assertions.assertTrue(str.contains("ConnectResponse"));
    Assertions.assertTrue(str.contains("success=true"));
    Assertions.assertTrue(str.contains("message='Connected'"));
  }
}
