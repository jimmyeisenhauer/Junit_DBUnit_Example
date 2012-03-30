package cvTests;

import static org.junit.Assert.*;

import java.io.File;
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

public class CeeVee1Test 
{
	private IDatabaseTester connection ;
	
	


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
	public void testCeeVee1() throws Exception {
	String compareResults = "ERROR: ";
	
		
		
		ITable actualClaim = (connection.getConnection()).createQueryTable("RESULTNAME", "select MICRO_ID, STATUS_REASON_CODE, STATUS_REASON, TOTAL_CHARGES, DEDUCTABLE, TOTAL_PATIENT_LIABILITY from claims where CLAIM_ID='001'");
		
		// Load expected data from an XML dataset
        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/test/java/cvTests/cvExpected/CeeVee1.xml"));
        ITable expectedClaim = expectedDataSet.getTable("claims");

        
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
