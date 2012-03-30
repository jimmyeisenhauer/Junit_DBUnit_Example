package cvTests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.assertion.DiffCollectingFailureHandler;
import org.dbunit.assertion.Difference;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


@RunWith(Parameterized.class)
public class CeeVeeTest 
{
	private IDatabaseTester connection ;
	private String testCaseName;
	private String claimNumber;
	
	@SuppressWarnings("rawtypes")
	@Parameters
    public static Collection data() {
        return Arrays.asList(new Object[][] { { "CeeVee1", "001" }, { "CeeVee2", "002" },{ "CeeVee3", "003" }, });
    }
	
	
	public CeeVeeTest(String testCaseName, String claimNumber) {
        super();
        this.testCaseName = testCaseName;
        this.claimNumber = claimNumber;
        
    }
	
	
	
	
	@Before
	public void setUp() throws Exception {
			
	connection = 	new JdbcDatabaseTester("com.mysql.jdbc.Driver",
	            "jdbc:mysql://localhost/ceevee", "root", "Happy#11");

	}

	@After
	public void tearDown() throws Exception {
		connection.onTearDown();
		
	}

	
	@Test
	public void testClaimContents() throws Exception {
	String compareResults = testCaseName + "_" + claimNumber + "-ERROR: ";
    String fileSrc = "src/test/java/cvTests/cvExpected/";
    String claimTableName = "claims";
    String claimQuery = "select MICRO_ID, STATUS_REASON_CODE, STATUS_REASON, TOTAL_CHARGES, DEDUCTABLE, TOTAL_PATIENT_LIABILITY from claims where CLAIM_ID=";
	
    
		ITable actualClaim = (connection.getConnection()).createQueryTable("RESULTNAME",claimQuery + claimNumber );
		
		// Load expected data from an XML dataset
        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File(fileSrc + testCaseName + ".xml"));
        ITable expectedClaim = expectedDataSet.getTable(claimTableName);

        
        DiffCollectingFailureHandler myHandler = new DiffCollectingFailureHandler();

        try {
        Assertion.assertEquals(expectedClaim, actualClaim,myHandler);
        }
        catch (AssertionError ex){
        	compareResults += ex;
        	fail(compareResults);
        }
       
        
        if (myHandler.getDiffList().size() > 0) {
        	
        	@SuppressWarnings("unchecked")
    		List<Difference> diffList = myHandler.getDiffList();              
            for (Difference difference : diffList) {
            	compareResults += ( difference.getColumnName() + " Expected:" + difference.getExpectedValue() + " Actual:" + difference.getActualValue()  + " | ");             
            	
            } 	
        	
        	fail(compareResults);
        	
        }
        
        
	
	}

}




