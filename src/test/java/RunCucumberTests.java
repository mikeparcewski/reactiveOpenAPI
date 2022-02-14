import com.wickedagile.apis.reference.reactoropenapi.OpenAPI2SpringBoot;
import com.wickedagile.apis.reference.reactoropenapi.event.EventsConfig;
import com.accenture.testing.bdd.BDDForAll;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/***
 * Executes the BDD tests while hooking into the transaction events
 * for extra validation
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = { OpenAPI2SpringBoot.class, EventsConfig.class })
public class RunCucumberTests {

  @Value("${test.expected.deletes}")
  int expectedDeletes;

  @Value("${test.expected.updates}")
  int expectedUpdates;

  @Value("${test.expected.inserts}")
  int expectedInserts;

  @Value("${test.expected.reads}")
  int expectedReads;

  /**
   * executes BDD tests, for reporting purposes
   * we grab all the events as a factory
   */
  @TestFactory
  Stream<DynamicTest> testFeatures() {

    // run the test
    BDDForAll bddForAll = new BDDForAll();
    bddForAll.run();

    // we'll start by adding all the cucumber tests
    List<DynamicTest> tests = bddForAll.getEventListener()
    .getTestCaseEventHandler()
    .getFinishedItems()
    .stream().map(item -> {
      return DynamicTest.dynamicTest(item.getTestCase().getName(),
          () -> Assertions.assertTrue(item.getResult().getStatus().isOk(), item.getTestCase().getName()));
        }).collect(Collectors.toList());

    // now let's validate the transactions for all the tests
    Arrays.stream(getInfos()).forEach(info -> {
      tests.add(
        DynamicTest.dynamicTest(
            info.getMessage(),
            () -> Assertions.assertEquals(info.expected, info.actual, info.getMessage())
        )
      );
    });

    return tests.stream();
  }

  /**
   * get the array of transaction information
   * @return the array of transaction information
   */
  public TransactionInfo[] getInfos() {
    return new TransactionInfo[] {
        new TransactionInfo("deletes", expectedDeletes, EventsConfig.deletes),
        new TransactionInfo("inserts", expectedInserts, EventsConfig.inserts),
        new TransactionInfo("updates", expectedUpdates, EventsConfig.updates),
        new TransactionInfo("reads", expectedReads, EventsConfig.reads)
    };
  }

  /**
   * simple container for the transaction stats
   * @param name the name (e.g. insert, update, etc...)
   * @param expected the expected count
   * @param actual the actual count
   * @return a transactioninfo object for your enjoyment
   */
  public TransactionInfo getTransactionInfo(String name, int expected, int actual) {
    return new TransactionInfo(name, expected, actual);
  }

  /**
   * container for transaction info
   */
  @Data
  @AllArgsConstructor
  class TransactionInfo {

    String name;
    int expected;
    int actual;

    /**
     * readable error message
     * @return readable error message
     */
    public String getMessage() {
      return String.format("Expected %d %s from the tests and got %d", expected, name, actual);
    }

  }

}