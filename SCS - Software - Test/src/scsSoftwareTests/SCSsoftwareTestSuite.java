package scsSoftwareTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({CheckoutTests.class, ItemAdderTests.class, PlacingItemTest.class, TestPayWithBankNote.class, ReceiptTest.class, CoinPaymentTest.class})
public class SCSsoftwareTestSuite {}

