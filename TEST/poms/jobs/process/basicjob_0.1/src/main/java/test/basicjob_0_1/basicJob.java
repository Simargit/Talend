
package test.basicjob_0_1;

import routines.DataOperation;
import routines.TalendDataGenerator;
import routines.DataQuality;
import routines.Relational;
import routines.DataQualityDependencies;
import routines.Mathematical;
import routines.SQLike;
import routines.Numeric;
import routines.TalendStringUtil;
import routines.TalendString;
import routines.DQTechnical;
import routines.StringHandling;
import routines.DataMasking;
import routines.TalendDate;
import routines.DqStringHandling;
import routines.system.*;
import routines.system.api.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Comparator;
 





@SuppressWarnings("unused")

/**
 * Job: basicJob Purpose: readDataFromCSV<br>
 * Description:  <br>
 * @author simarpreet.singh@intellinum.com
 * @version 7.1.1.20181026_1147
 * @status 
 */
public class basicJob implements TalendJob {
	static {System.setProperty("TalendJob.log", "basicJob.log");}
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(basicJob.class);

protected static void logIgnoredError(String message, Throwable cause) {
       log.error(message, cause);

}


	public final Object obj = new Object();

	// for transmiting parameters purpose
	private Object valueObject = null;

	public Object getValueObject() {
		return this.valueObject;
	}

	public void setValueObject(Object valueObject) {
		this.valueObject = valueObject;
	}
	
	private final static String defaultCharset = java.nio.charset.Charset.defaultCharset().name();

	
	private final static String utf8Charset = "UTF-8";
	//contains type for every context property
	public class PropertiesWithType extends java.util.Properties {
		private static final long serialVersionUID = 1L;
		private java.util.Map<String,String> propertyTypes = new java.util.HashMap<>();
		
		public PropertiesWithType(java.util.Properties properties){
			super(properties);
		}
		public PropertiesWithType(){
			super();
		}
		
		public void setContextType(String key, String type) {
			propertyTypes.put(key,type);
		}
	
		public String getContextType(String key) {
			return propertyTypes.get(key);
		}
	}
	
	// create and load default properties
	private java.util.Properties defaultProps = new java.util.Properties();
	// create application properties with default
	public class ContextProperties extends PropertiesWithType {

		private static final long serialVersionUID = 1L;

		public ContextProperties(java.util.Properties properties){
			super(properties);
		}
		public ContextProperties(){
			super();
		}

		public void synchronizeContext(){
			
		}

	}
	protected ContextProperties context = new ContextProperties(); // will be instanciated by MS.
	public ContextProperties getContext() {
		return this.context;
	}
	private final String jobVersion = "0.1";
	private final String jobName = "basicJob";
	private final String projectName = "TEST";
	public Integer errorCode = null;
	private String currentComponent = "";
	
		private final java.util.Map<String, Object> globalMap = new java.util.HashMap<String, Object>();
        private final static java.util.Map<String, Object> junitGlobalMap = new java.util.HashMap<String, Object>();
	
		private final java.util.Map<String, Long> start_Hash = new java.util.HashMap<String, Long>();
		private final java.util.Map<String, Long> end_Hash = new java.util.HashMap<String, Long>();
		private final java.util.Map<String, Boolean> ok_Hash = new java.util.HashMap<String, Boolean>();
		public  final java.util.List<String[]> globalBuffer = new java.util.ArrayList<String[]>();
	

private RunStat runStat = new RunStat();

	// OSGi DataSource
	private final static String KEY_DB_DATASOURCES = "KEY_DB_DATASOURCES";
	
	private final static String KEY_DB_DATASOURCES_RAW = "KEY_DB_DATASOURCES_RAW";

	public void setDataSources(java.util.Map<String, javax.sql.DataSource> dataSources) {
		java.util.Map<String, routines.system.TalendDataSource> talendDataSources = new java.util.HashMap<String, routines.system.TalendDataSource>();
		for (java.util.Map.Entry<String, javax.sql.DataSource> dataSourceEntry : dataSources.entrySet()) {
			talendDataSources.put(dataSourceEntry.getKey(), new routines.system.TalendDataSource(dataSourceEntry.getValue()));
		}
		globalMap.put(KEY_DB_DATASOURCES, talendDataSources);
		globalMap.put(KEY_DB_DATASOURCES_RAW, new java.util.HashMap<String, javax.sql.DataSource>(dataSources));
	}


private final java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
private final java.io.PrintStream errorMessagePS = new java.io.PrintStream(new java.io.BufferedOutputStream(baos));

public String getExceptionStackTrace() {
	if ("failure".equals(this.getStatus())) {
		errorMessagePS.flush();
		return baos.toString();
	}
	return null;
}

private Exception exception;

public Exception getException() {
	if ("failure".equals(this.getStatus())) {
		return this.exception;
	}
	return null;
}

private class TalendException extends Exception {

	private static final long serialVersionUID = 1L;

	private java.util.Map<String, Object> globalMap = null;
	private Exception e = null;
	private String currentComponent = null;
	private String virtualComponentName = null;
	
	public void setVirtualComponentName (String virtualComponentName){
		this.virtualComponentName = virtualComponentName;
	}

	private TalendException(Exception e, String errorComponent, final java.util.Map<String, Object> globalMap) {
		this.currentComponent= errorComponent;
		this.globalMap = globalMap;
		this.e = e;
	}

	public Exception getException() {
		return this.e;
	}

	public String getCurrentComponent() {
		return this.currentComponent;
	}

	
    public String getExceptionCauseMessage(Exception e){
        Throwable cause = e;
        String message = null;
        int i = 10;
        while (null != cause && 0 < i--) {
            message = cause.getMessage();
            if (null == message) {
                cause = cause.getCause();
            } else {
                break;          
            }
        }
        if (null == message) {
            message = e.getClass().getName();
        }   
        return message;
    }

	@Override
	public void printStackTrace() {
		if (!(e instanceof TalendException || e instanceof TDieException)) {
			if(virtualComponentName!=null && currentComponent.indexOf(virtualComponentName+"_")==0){
				globalMap.put(virtualComponentName+"_ERROR_MESSAGE",getExceptionCauseMessage(e));
			}
			globalMap.put(currentComponent+"_ERROR_MESSAGE",getExceptionCauseMessage(e));
			System.err.println("Exception in component " + currentComponent + " (" + jobName + ")");
		}
		if (!(e instanceof TDieException)) {
			if(e instanceof TalendException){
				e.printStackTrace();
			} else {
				e.printStackTrace();
				e.printStackTrace(errorMessagePS);
				basicJob.this.exception = e;
			}
		}
		if (!(e instanceof TalendException)) {
		try {
			for (java.lang.reflect.Method m : this.getClass().getEnclosingClass().getMethods()) {
				if (m.getName().compareTo(currentComponent + "_error") == 0) {
					m.invoke(basicJob.this, new Object[] { e , currentComponent, globalMap});
					break;
				}
			}

			if(!(e instanceof TDieException)){
			}
		} catch (Exception e) {
			this.e.printStackTrace();
		}
		}
	}
}

			public void tFileInputExcel_1_error(Exception exception, String errorComponent, final java.util.Map<String, Object> globalMap) throws TalendException {
				
				end_Hash.put(errorComponent, System.currentTimeMillis());
				
				status = "failure";
				
					tFileInputExcel_1_onSubJobError(exception, errorComponent, globalMap);
			}
			
			public void tMap_1_error(Exception exception, String errorComponent, final java.util.Map<String, Object> globalMap) throws TalendException {
				
				end_Hash.put(errorComponent, System.currentTimeMillis());
				
				status = "failure";
				
					tFileInputExcel_1_onSubJobError(exception, errorComponent, globalMap);
			}
			
			public void tDBCDCOutput_1_error(Exception exception, String errorComponent, final java.util.Map<String, Object> globalMap) throws TalendException {
				
				end_Hash.put(errorComponent, System.currentTimeMillis());
				
				status = "failure";
				
					tFileInputExcel_1_onSubJobError(exception, errorComponent, globalMap);
			}
			
			public void tFileInputExcel_1_onSubJobError(Exception exception, String errorComponent, final java.util.Map<String, Object> globalMap) throws TalendException {

resumeUtil.addLog("SYSTEM_LOG", "NODE:"+ errorComponent, "", Thread.currentThread().getId()+ "", "FATAL", "", exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception),"");

			}
	




	



public static class sendDataStruct implements routines.system.IPersistableRow<sendDataStruct> {
    final static byte[] commonByteArrayLock_TEST_basicJob = new byte[0];
    static byte[] commonByteArray_TEST_basicJob = new byte[0];
	protected static final int DEFAULT_HASHCODE = 1;
    protected static final int PRIME = 31;
    protected int hashCode = DEFAULT_HASHCODE;
    public boolean hashCodeDirty = true;

    public String loopKey;



	
			    public String PO_NUMBER;

				public String getPO_NUMBER () {
					return this.PO_NUMBER;
				}
				
			    public String SUP_ID;

				public String getSUP_ID () {
					return this.SUP_ID;
				}
				
			    public String SHIP_TO;

				public String getSHIP_TO () {
					return this.SHIP_TO;
				}
				
			    public String BILL_TO;

				public String getBILL_TO () {
					return this.BILL_TO;
				}
				
			    public String VENDOR;

				public String getVENDOR () {
					return this.VENDOR;
				}
				
			    public String ORD_DATE;

				public String getORD_DATE () {
					return this.ORD_DATE;
				}
				
			    public String SHIP_DATE;

				public String getSHIP_DATE () {
					return this.SHIP_DATE;
				}
				
			    public String LINE_NO;

				public String getLINE_NO () {
					return this.LINE_NO;
				}
				
			    public String ITEM_CODE;

				public String getITEM_CODE () {
					return this.ITEM_CODE;
				}
				
			    public String ORD_QTY;

				public String getORD_QTY () {
					return this.ORD_QTY;
				}
				


	@Override
	public int hashCode() {
		if (this.hashCodeDirty) {
			final int prime = PRIME;
			int result = DEFAULT_HASHCODE;
	
						result = prime * result + ((this.PO_NUMBER == null) ? 0 : this.PO_NUMBER.hashCode());
					
    		this.hashCode = result;
    		this.hashCodeDirty = false;
		}
		return this.hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final sendDataStruct other = (sendDataStruct) obj;
		
						if (this.PO_NUMBER == null) {
							if (other.PO_NUMBER != null)
								return false;
						
						} else if (!this.PO_NUMBER.equals(other.PO_NUMBER))
						
							return false;
					

		return true;
    }

	public void copyDataTo(sendDataStruct other) {

		other.PO_NUMBER = this.PO_NUMBER;
	            other.SUP_ID = this.SUP_ID;
	            other.SHIP_TO = this.SHIP_TO;
	            other.BILL_TO = this.BILL_TO;
	            other.VENDOR = this.VENDOR;
	            other.ORD_DATE = this.ORD_DATE;
	            other.SHIP_DATE = this.SHIP_DATE;
	            other.LINE_NO = this.LINE_NO;
	            other.ITEM_CODE = this.ITEM_CODE;
	            other.ORD_QTY = this.ORD_QTY;
	            
	}

	public void copyKeysDataTo(sendDataStruct other) {

		other.PO_NUMBER = this.PO_NUMBER;
	            	
	}




	private String readString(ObjectInputStream dis) throws IOException{
		String strReturn = null;
		int length = 0;
        length = dis.readInt();
		if (length == -1) {
			strReturn = null;
		} else {
			if(length > commonByteArray_TEST_basicJob.length) {
				if(length < 1024 && commonByteArray_TEST_basicJob.length == 0) {
   					commonByteArray_TEST_basicJob = new byte[1024];
				} else {
   					commonByteArray_TEST_basicJob = new byte[2 * length];
   				}
			}
			dis.readFully(commonByteArray_TEST_basicJob, 0, length);
			strReturn = new String(commonByteArray_TEST_basicJob, 0, length, utf8Charset);
		}
		return strReturn;
	}

    private void writeString(String str, ObjectOutputStream dos) throws IOException{
		if(str == null) {
            dos.writeInt(-1);
		} else {
            byte[] byteArray = str.getBytes(utf8Charset);
	    	dos.writeInt(byteArray.length);
			dos.write(byteArray);
    	}
    }

    public void readData(ObjectInputStream dis) {

		synchronized(commonByteArrayLock_TEST_basicJob) {

        	try {

        		int length = 0;
		
					this.PO_NUMBER = readString(dis);
					
					this.SUP_ID = readString(dis);
					
					this.SHIP_TO = readString(dis);
					
					this.BILL_TO = readString(dis);
					
					this.VENDOR = readString(dis);
					
					this.ORD_DATE = readString(dis);
					
					this.SHIP_DATE = readString(dis);
					
					this.LINE_NO = readString(dis);
					
					this.ITEM_CODE = readString(dis);
					
					this.ORD_QTY = readString(dis);
					
        	} catch (IOException e) {
	            throw new RuntimeException(e);

		

        }

		

      }


    }

    public void writeData(ObjectOutputStream dos) {
        try {

		
					// String
				
						writeString(this.PO_NUMBER,dos);
					
					// String
				
						writeString(this.SUP_ID,dos);
					
					// String
				
						writeString(this.SHIP_TO,dos);
					
					// String
				
						writeString(this.BILL_TO,dos);
					
					// String
				
						writeString(this.VENDOR,dos);
					
					// String
				
						writeString(this.ORD_DATE,dos);
					
					// String
				
						writeString(this.SHIP_DATE,dos);
					
					// String
				
						writeString(this.LINE_NO,dos);
					
					// String
				
						writeString(this.ITEM_CODE,dos);
					
					// String
				
						writeString(this.ORD_QTY,dos);
					
        	} catch (IOException e) {
	            throw new RuntimeException(e);
        }


    }


    public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("[");
		sb.append("PO_NUMBER="+PO_NUMBER);
		sb.append(",SUP_ID="+SUP_ID);
		sb.append(",SHIP_TO="+SHIP_TO);
		sb.append(",BILL_TO="+BILL_TO);
		sb.append(",VENDOR="+VENDOR);
		sb.append(",ORD_DATE="+ORD_DATE);
		sb.append(",SHIP_DATE="+SHIP_DATE);
		sb.append(",LINE_NO="+LINE_NO);
		sb.append(",ITEM_CODE="+ITEM_CODE);
		sb.append(",ORD_QTY="+ORD_QTY);
	    sb.append("]");

	    return sb.toString();
    }
        public String toLogString(){
        	StringBuilder sb = new StringBuilder();
        	
        				if(PO_NUMBER == null){
        					sb.append("<null>");
        				}else{
            				sb.append(PO_NUMBER);
            			}
            		
        			sb.append("|");
        		
        				if(SUP_ID == null){
        					sb.append("<null>");
        				}else{
            				sb.append(SUP_ID);
            			}
            		
        			sb.append("|");
        		
        				if(SHIP_TO == null){
        					sb.append("<null>");
        				}else{
            				sb.append(SHIP_TO);
            			}
            		
        			sb.append("|");
        		
        				if(BILL_TO == null){
        					sb.append("<null>");
        				}else{
            				sb.append(BILL_TO);
            			}
            		
        			sb.append("|");
        		
        				if(VENDOR == null){
        					sb.append("<null>");
        				}else{
            				sb.append(VENDOR);
            			}
            		
        			sb.append("|");
        		
        				if(ORD_DATE == null){
        					sb.append("<null>");
        				}else{
            				sb.append(ORD_DATE);
            			}
            		
        			sb.append("|");
        		
        				if(SHIP_DATE == null){
        					sb.append("<null>");
        				}else{
            				sb.append(SHIP_DATE);
            			}
            		
        			sb.append("|");
        		
        				if(LINE_NO == null){
        					sb.append("<null>");
        				}else{
            				sb.append(LINE_NO);
            			}
            		
        			sb.append("|");
        		
        				if(ITEM_CODE == null){
        					sb.append("<null>");
        				}else{
            				sb.append(ITEM_CODE);
            			}
            		
        			sb.append("|");
        		
        				if(ORD_QTY == null){
        					sb.append("<null>");
        				}else{
            				sb.append(ORD_QTY);
            			}
            		
        			sb.append("|");
        		
        	return sb.toString();
        }

    /**
     * Compare keys
     */
    public int compareTo(sendDataStruct other) {

		int returnValue = -1;
		
						returnValue = checkNullsAndCompare(this.PO_NUMBER, other.PO_NUMBER);
						if(returnValue != 0) {
							return returnValue;
						}

					
	    return returnValue;
    }


    private int checkNullsAndCompare(Object object1, Object object2) {
        int returnValue = 0;
		if (object1 instanceof Comparable && object2 instanceof Comparable) {
            returnValue = ((Comparable) object1).compareTo(object2);
        } else if (object1 != null && object2 != null) {
            returnValue = compareStrings(object1.toString(), object2.toString());
        } else if (object1 == null && object2 != null) {
            returnValue = 1;
        } else if (object1 != null && object2 == null) {
            returnValue = -1;
        } else {
            returnValue = 0;
        }

        return returnValue;
    }

    private int compareStrings(String string1, String string2) {
        return string1.compareTo(string2);
    }


}

public static class row1Struct implements routines.system.IPersistableRow<row1Struct> {
    final static byte[] commonByteArrayLock_TEST_basicJob = new byte[0];
    static byte[] commonByteArray_TEST_basicJob = new byte[0];

	
			    public String po_no;

				public String getPo_no () {
					return this.po_no;
				}
				
			    public Integer sup_id;

				public Integer getSup_id () {
					return this.sup_id;
				}
				
			    public String ship_to;

				public String getShip_to () {
					return this.ship_to;
				}
				
			    public String bill_to;

				public String getBill_to () {
					return this.bill_to;
				}
				
			    public String vendor_code;

				public String getVendor_code () {
					return this.vendor_code;
				}
				
			    public String ord_date;

				public String getOrd_date () {
					return this.ord_date;
				}
				
			    public String ship_date;

				public String getShip_date () {
					return this.ship_date;
				}
				
			    public Integer line_no;

				public Integer getLine_no () {
					return this.line_no;
				}
				
			    public String item_code;

				public String getItem_code () {
					return this.item_code;
				}
				
			    public Integer ord_qty;

				public Integer getOrd_qty () {
					return this.ord_qty;
				}
				



	private String readString(ObjectInputStream dis) throws IOException{
		String strReturn = null;
		int length = 0;
        length = dis.readInt();
		if (length == -1) {
			strReturn = null;
		} else {
			if(length > commonByteArray_TEST_basicJob.length) {
				if(length < 1024 && commonByteArray_TEST_basicJob.length == 0) {
   					commonByteArray_TEST_basicJob = new byte[1024];
				} else {
   					commonByteArray_TEST_basicJob = new byte[2 * length];
   				}
			}
			dis.readFully(commonByteArray_TEST_basicJob, 0, length);
			strReturn = new String(commonByteArray_TEST_basicJob, 0, length, utf8Charset);
		}
		return strReturn;
	}

    private void writeString(String str, ObjectOutputStream dos) throws IOException{
		if(str == null) {
            dos.writeInt(-1);
		} else {
            byte[] byteArray = str.getBytes(utf8Charset);
	    	dos.writeInt(byteArray.length);
			dos.write(byteArray);
    	}
    }
	private Integer readInteger(ObjectInputStream dis) throws IOException{
		Integer intReturn;
        int length = 0;
        length = dis.readByte();
		if (length == -1) {
			intReturn = null;
		} else {
	    	intReturn = dis.readInt();
		}
		return intReturn;
	}

	private void writeInteger(Integer intNum, ObjectOutputStream dos) throws IOException{
		if(intNum == null) {
            dos.writeByte(-1);
		} else {
			dos.writeByte(0);
	    	dos.writeInt(intNum);
    	}
	}

    public void readData(ObjectInputStream dis) {

		synchronized(commonByteArrayLock_TEST_basicJob) {

        	try {

        		int length = 0;
		
					this.po_no = readString(dis);
					
						this.sup_id = readInteger(dis);
					
					this.ship_to = readString(dis);
					
					this.bill_to = readString(dis);
					
					this.vendor_code = readString(dis);
					
					this.ord_date = readString(dis);
					
					this.ship_date = readString(dis);
					
						this.line_no = readInteger(dis);
					
					this.item_code = readString(dis);
					
						this.ord_qty = readInteger(dis);
					
        	} catch (IOException e) {
	            throw new RuntimeException(e);

		

        }

		

      }


    }

    public void writeData(ObjectOutputStream dos) {
        try {

		
					// String
				
						writeString(this.po_no,dos);
					
					// Integer
				
						writeInteger(this.sup_id,dos);
					
					// String
				
						writeString(this.ship_to,dos);
					
					// String
				
						writeString(this.bill_to,dos);
					
					// String
				
						writeString(this.vendor_code,dos);
					
					// String
				
						writeString(this.ord_date,dos);
					
					// String
				
						writeString(this.ship_date,dos);
					
					// Integer
				
						writeInteger(this.line_no,dos);
					
					// String
				
						writeString(this.item_code,dos);
					
					// Integer
				
						writeInteger(this.ord_qty,dos);
					
        	} catch (IOException e) {
	            throw new RuntimeException(e);
        }


    }


    public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("[");
		sb.append("po_no="+po_no);
		sb.append(",sup_id="+String.valueOf(sup_id));
		sb.append(",ship_to="+ship_to);
		sb.append(",bill_to="+bill_to);
		sb.append(",vendor_code="+vendor_code);
		sb.append(",ord_date="+ord_date);
		sb.append(",ship_date="+ship_date);
		sb.append(",line_no="+String.valueOf(line_no));
		sb.append(",item_code="+item_code);
		sb.append(",ord_qty="+String.valueOf(ord_qty));
	    sb.append("]");

	    return sb.toString();
    }
        public String toLogString(){
        	StringBuilder sb = new StringBuilder();
        	
        				if(po_no == null){
        					sb.append("<null>");
        				}else{
            				sb.append(po_no);
            			}
            		
        			sb.append("|");
        		
        				if(sup_id == null){
        					sb.append("<null>");
        				}else{
            				sb.append(sup_id);
            			}
            		
        			sb.append("|");
        		
        				if(ship_to == null){
        					sb.append("<null>");
        				}else{
            				sb.append(ship_to);
            			}
            		
        			sb.append("|");
        		
        				if(bill_to == null){
        					sb.append("<null>");
        				}else{
            				sb.append(bill_to);
            			}
            		
        			sb.append("|");
        		
        				if(vendor_code == null){
        					sb.append("<null>");
        				}else{
            				sb.append(vendor_code);
            			}
            		
        			sb.append("|");
        		
        				if(ord_date == null){
        					sb.append("<null>");
        				}else{
            				sb.append(ord_date);
            			}
            		
        			sb.append("|");
        		
        				if(ship_date == null){
        					sb.append("<null>");
        				}else{
            				sb.append(ship_date);
            			}
            		
        			sb.append("|");
        		
        				if(line_no == null){
        					sb.append("<null>");
        				}else{
            				sb.append(line_no);
            			}
            		
        			sb.append("|");
        		
        				if(item_code == null){
        					sb.append("<null>");
        				}else{
            				sb.append(item_code);
            			}
            		
        			sb.append("|");
        		
        				if(ord_qty == null){
        					sb.append("<null>");
        				}else{
            				sb.append(ord_qty);
            			}
            		
        			sb.append("|");
        		
        	return sb.toString();
        }

    /**
     * Compare keys
     */
    public int compareTo(row1Struct other) {

		int returnValue = -1;
		
	    return returnValue;
    }


    private int checkNullsAndCompare(Object object1, Object object2) {
        int returnValue = 0;
		if (object1 instanceof Comparable && object2 instanceof Comparable) {
            returnValue = ((Comparable) object1).compareTo(object2);
        } else if (object1 != null && object2 != null) {
            returnValue = compareStrings(object1.toString(), object2.toString());
        } else if (object1 == null && object2 != null) {
            returnValue = 1;
        } else if (object1 != null && object2 == null) {
            returnValue = -1;
        } else {
            returnValue = 0;
        }

        return returnValue;
    }

    private int compareStrings(String string1, String string2) {
        return string1.compareTo(string2);
    }


}
public void tFileInputExcel_1Process(final java.util.Map<String, Object> globalMap) throws TalendException {
	globalMap.put("tFileInputExcel_1_SUBPROCESS_STATE", 0);

 final boolean execStat = this.execStat;
	
		String iterateId = "";
	
	
	String currentComponent = "";
	java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

	try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { //start the resume
				globalResumeTicket = true;



		row1Struct row1 = new row1Struct();
sendDataStruct sendData = new sendDataStruct();





	
	/**
	 * [tDBCDCOutput_1 begin ] start
	 */

	

	
		
		ok_Hash.put("tDBCDCOutput_1", false);
		start_Hash.put("tDBCDCOutput_1", System.currentTimeMillis());
		
	
	currentComponent="tDBCDCOutput_1";

	
			if (execStat) {
				if(resourceMap.get("inIterateVComp") == null){
					
						runStat.updateStatOnConnection("sendData" + iterateId, 0, 0);
					
				}
			} 

		
		int tos_count_tDBCDCOutput_1 = 0;
		

			int nb_line_tDBCDCOutput_1 = 0;
			java.sql.Connection conn_tDBCDCOutput_1 = null;
			

			    

			        String driverClass_tDBCDCOutput_1 = "oracle.jdbc.OracleDriver";
			    
				java.lang.Class.forName(driverClass_tDBCDCOutput_1);	
            	
            	String url_tDBCDCOutput_1 = "jdbc:oracle:oci8:@" + "VISION";
            	
            	String dbUser_tDBCDCOutput_1 = "APPS";
            	
                 
	final String decryptedPassword_tDBCDCOutput_1 = routines.system.PasswordEncryptUtil.decryptPassword("43ae1c9f8db8943ef4f7aba1746784ea");
              	
            	
            	String dbPwd_tDBCDCOutput_1 = decryptedPassword_tDBCDCOutput_1;
            	
	    		log.debug("tDBCDCOutput_1 - Driver ClassName: "+driverClass_tDBCDCOutput_1+".");
			
	    		log.debug("tDBCDCOutput_1 - Connection attempt to '" + url_tDBCDCOutput_1 + "' with the username '" + dbUser_tDBCDCOutput_1 + "'.");
			
			conn_tDBCDCOutput_1 = java.sql.DriverManager.getConnection(url_tDBCDCOutput_1, dbUser_tDBCDCOutput_1, dbPwd_tDBCDCOutput_1);
			
	    		log.debug("tDBCDCOutput_1 - Connection to '" + url_tDBCDCOutput_1 + "' has succeeded.");
			
				//////////////////////////////////////////////////
				the column:PO_NUMBER should be a Object type or a Document type.
				//////////////////////////////////////////////////
			
        	class XStreanUtil_tDBCDCOutput_1 {
				public void printHex(byte[] b) {
					for (int i = 0; i < b.length; ++i) {
						System.out.print(Integer.toHexString((b[i] & 0xFF) | 0x100)
								.substring(1, 3));
					}
				}
			
				public byte[] encode2bytes(int transaction, int rank) {
					byte[] mybyte = new byte[5];
					mybyte[0] = (byte) (transaction >> 24);
					mybyte[1] = (byte) ((transaction << 8) >> 24);
					mybyte[2] = (byte) ((transaction << 16) >> 24);
					mybyte[3] = (byte) ((transaction << 24) >> 24);
					mybyte[4] = (byte) (rank);
					return mybyte;
				}
			
				public int getTransaction(byte[] mybyte) {
					int i = 0;
					int pos = 0;
					i += ((int) mybyte[pos++] & 0xFF) << 24;
					i += ((int) mybyte[pos++] & 0xFF) << 16;
					i += ((int) mybyte[pos++] & 0xFF) << 8;
					i += ((int) mybyte[pos] & 0xFF);
					return i;
				}
			
				public int getRank(byte[] mybyte) {
					int foo;
					foo = ((int) mybyte[4] & 0xFF);
					return foo;
				}
			}
        	XStreanUtil_tDBCDCOutput_1 xstreanUtil_tDBCDCOutput_1 = new XStreanUtil_tDBCDCOutput_1();
        	byte[] lastPosition_tDBCDCOutput_1 = null;
        	oracle.streams.XStreamIn xStreamIn_tDBCDCOutput_1 = null;
        	try{
        		
	    		log.info("tDBCDCOutput_1 - Try to attach to inbound server:"+""+"");
			
	        	xStreamIn_tDBCDCOutput_1=oracle.streams.XStreamIn.attach((oracle.jdbc.OracleConnection) conn_tDBCDCOutput_1 , "",
					"tDBCDCOutput_1",30,oracle.streams.XStreamIn.DEFAULT_MODE);
				
	    		log.info("tDBCDCOutput_1 - Attached successfully.");
			
			} catch (oracle.streams.StreamsException e_tDBCDCOutput_1) {
				System.err.println("Cannot attach to the inbound server: " + "");
				
				
	    		log.error("tDBCDCOutput_1 - Cannot attach to the inbound server:  "+""+"");
			
				throw e_tDBCDCOutput_1;
			}
			lastPosition_tDBCDCOutput_1 = xStreamIn_tDBCDCOutput_1.getLastPosition();
			int transaction_tDBCDCOutput_1 = 0;
			int rank_tDBCDCOutput_1 = 0;
			if (lastPosition_tDBCDCOutput_1 == null) {
				transaction_tDBCDCOutput_1 = 1;
				rank_tDBCDCOutput_1 = 1;
			} else {
				transaction_tDBCDCOutput_1 = xstreanUtil_tDBCDCOutput_1.getTransaction(lastPosition_tDBCDCOutput_1);
				rank_tDBCDCOutput_1 = xstreanUtil_tDBCDCOutput_1.getRank(lastPosition_tDBCDCOutput_1);
				if (rank_tDBCDCOutput_1 == 1)
					rank_tDBCDCOutput_1 = 2;
				else {
					rank_tDBCDCOutput_1 = 1;
					transaction_tDBCDCOutput_1++;
				}
			}
			oracle.streams.LCR inLCR_tDBCDCOutput_1 = null;
			oracle.streams.LCR commitLCR_tDBCDCOutput_1 = null;
			String trancationID_tDBCDCOutput_1=null;
		


 



/**
 * [tDBCDCOutput_1 begin ] stop
 */



	
	/**
	 * [tMap_1 begin ] start
	 */

	

	
		
		ok_Hash.put("tMap_1", false);
		start_Hash.put("tMap_1", System.currentTimeMillis());
		
	
	currentComponent="tMap_1";

	
			if (execStat) {
				if(resourceMap.get("inIterateVComp") == null){
					
						runStat.updateStatOnConnection("row1" + iterateId, 0, 0);
					
				}
			} 

		
		int tos_count_tMap_1 = 0;
		
                if(log.isDebugEnabled())
            log.debug("tMap_1 - "  + ("Start to work.") );
            if (log.isDebugEnabled()) {
                class BytesLimit65535_tMap_1{
                    public void limitLog4jByte() throws Exception{
                    StringBuilder log4jParamters_tMap_1 = new StringBuilder();
                    log4jParamters_tMap_1.append("Parameters:");
                            log4jParamters_tMap_1.append("LINK_STYLE" + " = " + "LINE");
                        log4jParamters_tMap_1.append(" | ");
                            log4jParamters_tMap_1.append("TEMPORARY_DATA_DIRECTORY" + " = " + "");
                        log4jParamters_tMap_1.append(" | ");
                            log4jParamters_tMap_1.append("ROWS_BUFFER_SIZE" + " = " + "2000000");
                        log4jParamters_tMap_1.append(" | ");
                            log4jParamters_tMap_1.append("CHANGE_HASH_AND_EQUALS_FOR_BIGDECIMAL" + " = " + "true");
                        log4jParamters_tMap_1.append(" | ");
                if(log.isDebugEnabled())
            log.debug("tMap_1 - "  + (log4jParamters_tMap_1) );
                    } 
                } 
            new BytesLimit65535_tMap_1().limitLog4jByte();
            }




// ###############################
// # Lookup's keys initialization
		int count_row1_tMap_1 = 0;
		
// ###############################        

// ###############################
// # Vars initialization
class  Var__tMap_1__Struct  {
}
Var__tMap_1__Struct Var__tMap_1 = new Var__tMap_1__Struct();
// ###############################

// ###############################
// # Outputs initialization
				int count_sendData_tMap_1 = 0;
				
sendDataStruct sendData_tmp = new sendDataStruct();
// ###############################

        
        



        









 



/**
 * [tMap_1 begin ] stop
 */



	
	/**
	 * [tFileInputExcel_1 begin ] start
	 */

	

	
		
		ok_Hash.put("tFileInputExcel_1", false);
		start_Hash.put("tFileInputExcel_1", System.currentTimeMillis());
		
	
	currentComponent="tFileInputExcel_1";

	
		int tos_count_tFileInputExcel_1 = 0;
		
                if(log.isDebugEnabled())
            log.debug("tFileInputExcel_1 - "  + ("Start to work.") );
            if (log.isDebugEnabled()) {
                class BytesLimit65535_tFileInputExcel_1{
                    public void limitLog4jByte() throws Exception{
                    StringBuilder log4jParamters_tFileInputExcel_1 = new StringBuilder();
                    log4jParamters_tFileInputExcel_1.append("Parameters:");
                            log4jParamters_tFileInputExcel_1.append("VERSION_2007" + " = " + "true");
                        log4jParamters_tFileInputExcel_1.append(" | ");
                            log4jParamters_tFileInputExcel_1.append("FILENAME" + " = " + "\"C:/WorkSpace/talend/20181204182740_test_supplier1.xlsx\"");
                        log4jParamters_tFileInputExcel_1.append(" | ");
                            log4jParamters_tFileInputExcel_1.append("ALL_SHEETS" + " = " + "true");
                        log4jParamters_tFileInputExcel_1.append(" | ");
                            log4jParamters_tFileInputExcel_1.append("HEADER" + " = " + "1");
                        log4jParamters_tFileInputExcel_1.append(" | ");
                            log4jParamters_tFileInputExcel_1.append("FOOTER" + " = " + "0");
                        log4jParamters_tFileInputExcel_1.append(" | ");
                            log4jParamters_tFileInputExcel_1.append("LIMIT" + " = " + "");
                        log4jParamters_tFileInputExcel_1.append(" | ");
                            log4jParamters_tFileInputExcel_1.append("AFFECT_EACH_SHEET" + " = " + "false");
                        log4jParamters_tFileInputExcel_1.append(" | ");
                            log4jParamters_tFileInputExcel_1.append("FIRST_COLUMN" + " = " + "1");
                        log4jParamters_tFileInputExcel_1.append(" | ");
                            log4jParamters_tFileInputExcel_1.append("LAST_COLUMN" + " = " + "");
                        log4jParamters_tFileInputExcel_1.append(" | ");
                            log4jParamters_tFileInputExcel_1.append("DIE_ON_ERROR" + " = " + "false");
                        log4jParamters_tFileInputExcel_1.append(" | ");
                            log4jParamters_tFileInputExcel_1.append("ADVANCED_SEPARATOR" + " = " + "false");
                        log4jParamters_tFileInputExcel_1.append(" | ");
                            log4jParamters_tFileInputExcel_1.append("TRIMALL" + " = " + "false");
                        log4jParamters_tFileInputExcel_1.append(" | ");
                            log4jParamters_tFileInputExcel_1.append("TRIMSELECT" + " = " + "[{TRIM="+("true")+", SCHEMA_COLUMN="+("po_no")+"}, {TRIM="+("false")+", SCHEMA_COLUMN="+("sup_id")+"}, {TRIM="+("false")+", SCHEMA_COLUMN="+("ship_to")+"}, {TRIM="+("false")+", SCHEMA_COLUMN="+("bill_to")+"}, {TRIM="+("false")+", SCHEMA_COLUMN="+("vendor_code")+"}, {TRIM="+("false")+", SCHEMA_COLUMN="+("ord_date")+"}, {TRIM="+("false")+", SCHEMA_COLUMN="+("ship_date")+"}, {TRIM="+("false")+", SCHEMA_COLUMN="+("line_no")+"}, {TRIM="+("false")+", SCHEMA_COLUMN="+("item_code")+"}, {TRIM="+("false")+", SCHEMA_COLUMN="+("ord_qty")+"}]");
                        log4jParamters_tFileInputExcel_1.append(" | ");
                            log4jParamters_tFileInputExcel_1.append("CONVERTDATETOSTRING" + " = " + "false");
                        log4jParamters_tFileInputExcel_1.append(" | ");
                            log4jParamters_tFileInputExcel_1.append("ENCODING" + " = " + "\"ISO-8859-15\"");
                        log4jParamters_tFileInputExcel_1.append(" | ");
                            log4jParamters_tFileInputExcel_1.append("STOPREAD_ON_EMPTYROW" + " = " + "false");
                        log4jParamters_tFileInputExcel_1.append(" | ");
                            log4jParamters_tFileInputExcel_1.append("GENERATION_MODE" + " = " + "USER_MODE");
                        log4jParamters_tFileInputExcel_1.append(" | ");
                if(log.isDebugEnabled())
            log.debug("tFileInputExcel_1 - "  + (log4jParamters_tFileInputExcel_1) );
                    } 
                } 
            new BytesLimit65535_tFileInputExcel_1().limitLog4jByte();
            }


			class RegexUtil_tFileInputExcel_1 {

		    	public java.util.List<org.apache.poi.xssf.usermodel.XSSFSheet> getSheets(org.apache.poi.xssf.usermodel.XSSFWorkbook workbook, String oneSheetName, boolean useRegex) {

			        java.util.List<org.apache.poi.xssf.usermodel.XSSFSheet> list = new java.util.ArrayList<org.apache.poi.xssf.usermodel.XSSFSheet>();

			        if(useRegex){//this part process the regex issue

				        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(oneSheetName);
				        for (org.apache.poi.ss.usermodel.Sheet sheet : workbook) {
				            String sheetName = sheet.getSheetName();
				            java.util.regex.Matcher matcher = pattern.matcher(sheetName);
				            if (matcher.matches()) {
				            	if(sheet != null){
				                	list.add((org.apache.poi.xssf.usermodel.XSSFSheet) sheet);
				                }
				            }
				        }

			        }else{
			        	org.apache.poi.xssf.usermodel.XSSFSheet sheet = (org.apache.poi.xssf.usermodel.XSSFSheet) workbook.getSheet(oneSheetName);
		            	if(sheet != null){
		                	list.add(sheet);
		                }

			        }

			        return list;
			    }

			    public java.util.List<org.apache.poi.xssf.usermodel.XSSFSheet> getSheets(org.apache.poi.xssf.usermodel.XSSFWorkbook workbook, int index, boolean useRegex) {
			    	java.util.List<org.apache.poi.xssf.usermodel.XSSFSheet> list =  new java.util.ArrayList<org.apache.poi.xssf.usermodel.XSSFSheet>();
			    	org.apache.poi.xssf.usermodel.XSSFSheet sheet = (org.apache.poi.xssf.usermodel.XSSFSheet) workbook.getSheetAt(index);
	            	if(sheet != null){
	                	list.add(sheet);
	                }
			    	return list;
			    }

			}
		RegexUtil_tFileInputExcel_1 regexUtil_tFileInputExcel_1 = new RegexUtil_tFileInputExcel_1();

		Object source_tFileInputExcel_1 = "C:/WorkSpace/talend/20181204182740_test_supplier1.xlsx";
		org.apache.poi.xssf.usermodel.XSSFWorkbook workbook_tFileInputExcel_1 = null;

		if(source_tFileInputExcel_1 instanceof String){
			workbook_tFileInputExcel_1 = new org.apache.poi.xssf.usermodel.XSSFWorkbook((String)source_tFileInputExcel_1);
		} else if(source_tFileInputExcel_1 instanceof java.io.InputStream) {
     		workbook_tFileInputExcel_1 = new org.apache.poi.xssf.usermodel.XSSFWorkbook((java.io.InputStream)source_tFileInputExcel_1);
		} else{
			workbook_tFileInputExcel_1 = null;
			throw new java.lang.Exception("The data source should be specified as Inputstream or File Path!");
		}
		try {

    	java.util.List<org.apache.poi.xssf.usermodel.XSSFSheet> sheetList_tFileInputExcel_1 = new java.util.ArrayList<org.apache.poi.xssf.usermodel.XSSFSheet>();
    	for(org.apache.poi.ss.usermodel.Sheet sheet_tFileInputExcel_1 : workbook_tFileInputExcel_1){
   			sheetList_tFileInputExcel_1.add((org.apache.poi.xssf.usermodel.XSSFSheet) sheet_tFileInputExcel_1);
    	}
    	if(sheetList_tFileInputExcel_1.size() <= 0){
            throw new RuntimeException("Special sheets not exist!");
        }

		java.util.List<org.apache.poi.xssf.usermodel.XSSFSheet> sheetList_FilterNull_tFileInputExcel_1 = new java.util.ArrayList<org.apache.poi.xssf.usermodel.XSSFSheet>();
		for (org.apache.poi.xssf.usermodel.XSSFSheet sheet_FilterNull_tFileInputExcel_1 : sheetList_tFileInputExcel_1) {
			if(sheet_FilterNull_tFileInputExcel_1!=null && sheetList_FilterNull_tFileInputExcel_1.iterator()!=null && sheet_FilterNull_tFileInputExcel_1.iterator().hasNext()){
				sheetList_FilterNull_tFileInputExcel_1.add(sheet_FilterNull_tFileInputExcel_1);
			}
		}
		sheetList_tFileInputExcel_1 = sheetList_FilterNull_tFileInputExcel_1;
	if(sheetList_tFileInputExcel_1.size()>0){
		int nb_line_tFileInputExcel_1 = 0;

        int begin_line_tFileInputExcel_1 = 1;

        int footer_input_tFileInputExcel_1 = 0;

        int end_line_tFileInputExcel_1=0;
        for(org.apache.poi.xssf.usermodel.XSSFSheet sheet_tFileInputExcel_1:sheetList_tFileInputExcel_1){
			end_line_tFileInputExcel_1+=(sheet_tFileInputExcel_1.getLastRowNum()+1);
        }
        end_line_tFileInputExcel_1 -= footer_input_tFileInputExcel_1;
        int limit_tFileInputExcel_1 = -1;
        int start_column_tFileInputExcel_1 = 1-1;
        int end_column_tFileInputExcel_1 = -1;

        org.apache.poi.xssf.usermodel.XSSFRow row_tFileInputExcel_1 = null;
        org.apache.poi.xssf.usermodel.XSSFSheet sheet_tFileInputExcel_1 = sheetList_tFileInputExcel_1.get(0);
        int rowCount_tFileInputExcel_1 = 0;
        int sheetIndex_tFileInputExcel_1 = 0;
        int currentRows_tFileInputExcel_1 = (sheetList_tFileInputExcel_1.get(0).getLastRowNum()+1);

		//for the number format
        java.text.DecimalFormat df_tFileInputExcel_1 = new java.text.DecimalFormat("#.####################################");
        char decimalChar_tFileInputExcel_1 = df_tFileInputExcel_1.getDecimalFormatSymbols().getDecimalSeparator();
						log.debug("tFileInputExcel_1 - Retrieving records from the datasource.");
			
        for(int i_tFileInputExcel_1 = begin_line_tFileInputExcel_1; i_tFileInputExcel_1 < end_line_tFileInputExcel_1; i_tFileInputExcel_1++){

        	int emptyColumnCount_tFileInputExcel_1 = 0;

        	if (limit_tFileInputExcel_1 != -1 && nb_line_tFileInputExcel_1 >= limit_tFileInputExcel_1) {
        		break;
        	}

            while (i_tFileInputExcel_1 >= rowCount_tFileInputExcel_1 + currentRows_tFileInputExcel_1) {
                rowCount_tFileInputExcel_1 += currentRows_tFileInputExcel_1;
                sheet_tFileInputExcel_1 = sheetList_tFileInputExcel_1.get(++sheetIndex_tFileInputExcel_1);
                currentRows_tFileInputExcel_1 = (sheet_tFileInputExcel_1.getLastRowNum()+1);
            }
            globalMap.put("tFileInputExcel_1_CURRENT_SHEET",sheet_tFileInputExcel_1.getSheetName());
            if (rowCount_tFileInputExcel_1 <= i_tFileInputExcel_1) {
                row_tFileInputExcel_1 = sheet_tFileInputExcel_1.getRow(i_tFileInputExcel_1 - rowCount_tFileInputExcel_1);
            }
		    row1 = null;
					int tempRowLength_tFileInputExcel_1 = 10;
				
				int columnIndex_tFileInputExcel_1 = 0;
			
			String[] temp_row_tFileInputExcel_1 = new String[tempRowLength_tFileInputExcel_1];
			int excel_end_column_tFileInputExcel_1;
			if(row_tFileInputExcel_1==null){
				excel_end_column_tFileInputExcel_1=0;
			}else{
				excel_end_column_tFileInputExcel_1=row_tFileInputExcel_1.getLastCellNum();
			}
			int actual_end_column_tFileInputExcel_1;
			if(end_column_tFileInputExcel_1 == -1){
				actual_end_column_tFileInputExcel_1 = excel_end_column_tFileInputExcel_1;
			}
			else{
				actual_end_column_tFileInputExcel_1 = end_column_tFileInputExcel_1 >	excel_end_column_tFileInputExcel_1 ? excel_end_column_tFileInputExcel_1 : end_column_tFileInputExcel_1;
			}
			org.apache.poi.ss.formula.eval.NumberEval ne_tFileInputExcel_1 = null;
			for(int i=0;i<tempRowLength_tFileInputExcel_1;i++){
				if(i + start_column_tFileInputExcel_1 < actual_end_column_tFileInputExcel_1){
					org.apache.poi.ss.usermodel.Cell cell_tFileInputExcel_1 = row_tFileInputExcel_1.getCell(i + start_column_tFileInputExcel_1);
					if(cell_tFileInputExcel_1!=null){
					switch (cell_tFileInputExcel_1.getCellType()) {
                        case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
                            temp_row_tFileInputExcel_1[i] = cell_tFileInputExcel_1.getRichStringCellValue().getString();
                            break;
                        case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
                            if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell_tFileInputExcel_1)) {
									temp_row_tFileInputExcel_1[i] =cell_tFileInputExcel_1.getDateCellValue().toString();
                            } else {
                                temp_row_tFileInputExcel_1[i] = df_tFileInputExcel_1.format(cell_tFileInputExcel_1.getNumericCellValue());
                            }
                            break;
                        case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN:
                            temp_row_tFileInputExcel_1[i] =String.valueOf(cell_tFileInputExcel_1.getBooleanCellValue());
                            break;
                        case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA:
        					switch (cell_tFileInputExcel_1.getCachedFormulaResultType()) {
                                case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
                                    temp_row_tFileInputExcel_1[i] = cell_tFileInputExcel_1.getRichStringCellValue().getString();
                                    break;
                                case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
                                    if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell_tFileInputExcel_1)) {
											temp_row_tFileInputExcel_1[i] =cell_tFileInputExcel_1.getDateCellValue().toString();
                                    } else {
	                                    ne_tFileInputExcel_1 = new org.apache.poi.ss.formula.eval.NumberEval(cell_tFileInputExcel_1.getNumericCellValue());
										temp_row_tFileInputExcel_1[i] = ne_tFileInputExcel_1.getStringValue();
                                    }
                                    break;
                                case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN:
                                    temp_row_tFileInputExcel_1[i] =String.valueOf(cell_tFileInputExcel_1.getBooleanCellValue());
                                    break;
                                default:
                            		temp_row_tFileInputExcel_1[i] = "";
                            }
                            break;
                        default:
                            temp_row_tFileInputExcel_1[i] = "";
                        }
                	}
                	else{
                		temp_row_tFileInputExcel_1[i]="";
                	}

				}else{
					temp_row_tFileInputExcel_1[i]="";
				}
			}
			boolean whetherReject_tFileInputExcel_1 = false;
			row1 = new row1Struct();
			int curColNum_tFileInputExcel_1 = -1;
			String curColName_tFileInputExcel_1 = "";
			try{
							columnIndex_tFileInputExcel_1 = 0;
						
			if( temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1].trim().length() > 0) {
				curColNum_tFileInputExcel_1=columnIndex_tFileInputExcel_1 + start_column_tFileInputExcel_1 + 1;
				curColName_tFileInputExcel_1 = "po_no";

				row1.po_no = temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1].trim();
			}else{
				row1.po_no = null;
				emptyColumnCount_tFileInputExcel_1++;
			}
							columnIndex_tFileInputExcel_1 = 1;
						
			if( temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1].length() > 0) {
				curColNum_tFileInputExcel_1=columnIndex_tFileInputExcel_1 + start_column_tFileInputExcel_1 + 1;
				curColName_tFileInputExcel_1 = "sup_id";

				row1.sup_id = ParserUtils.parseTo_Integer(ParserUtils.parseTo_Number(temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1], null, '.'==decimalChar_tFileInputExcel_1 ? null : decimalChar_tFileInputExcel_1));
			}else{
				row1.sup_id = null;
				emptyColumnCount_tFileInputExcel_1++;
			}
							columnIndex_tFileInputExcel_1 = 2;
						
			if( temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1].length() > 0) {
				curColNum_tFileInputExcel_1=columnIndex_tFileInputExcel_1 + start_column_tFileInputExcel_1 + 1;
				curColName_tFileInputExcel_1 = "ship_to";

				row1.ship_to = temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1];
			}else{
				row1.ship_to = null;
				emptyColumnCount_tFileInputExcel_1++;
			}
							columnIndex_tFileInputExcel_1 = 3;
						
			if( temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1].length() > 0) {
				curColNum_tFileInputExcel_1=columnIndex_tFileInputExcel_1 + start_column_tFileInputExcel_1 + 1;
				curColName_tFileInputExcel_1 = "bill_to";

				row1.bill_to = temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1];
			}else{
				row1.bill_to = null;
				emptyColumnCount_tFileInputExcel_1++;
			}
							columnIndex_tFileInputExcel_1 = 4;
						
			if( temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1].length() > 0) {
				curColNum_tFileInputExcel_1=columnIndex_tFileInputExcel_1 + start_column_tFileInputExcel_1 + 1;
				curColName_tFileInputExcel_1 = "vendor_code";

				row1.vendor_code = temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1];
			}else{
				row1.vendor_code = null;
				emptyColumnCount_tFileInputExcel_1++;
			}
							columnIndex_tFileInputExcel_1 = 5;
						
			if( temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1].length() > 0) {
				curColNum_tFileInputExcel_1=columnIndex_tFileInputExcel_1 + start_column_tFileInputExcel_1 + 1;
				curColName_tFileInputExcel_1 = "ord_date";

				row1.ord_date = temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1];
			}else{
				row1.ord_date = null;
				emptyColumnCount_tFileInputExcel_1++;
			}
							columnIndex_tFileInputExcel_1 = 6;
						
			if( temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1].length() > 0) {
				curColNum_tFileInputExcel_1=columnIndex_tFileInputExcel_1 + start_column_tFileInputExcel_1 + 1;
				curColName_tFileInputExcel_1 = "ship_date";

				row1.ship_date = temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1];
			}else{
				row1.ship_date = null;
				emptyColumnCount_tFileInputExcel_1++;
			}
							columnIndex_tFileInputExcel_1 = 7;
						
			if( temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1].length() > 0) {
				curColNum_tFileInputExcel_1=columnIndex_tFileInputExcel_1 + start_column_tFileInputExcel_1 + 1;
				curColName_tFileInputExcel_1 = "line_no";

				row1.line_no = ParserUtils.parseTo_Integer(ParserUtils.parseTo_Number(temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1], null, '.'==decimalChar_tFileInputExcel_1 ? null : decimalChar_tFileInputExcel_1));
			}else{
				row1.line_no = null;
				emptyColumnCount_tFileInputExcel_1++;
			}
							columnIndex_tFileInputExcel_1 = 8;
						
			if( temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1].length() > 0) {
				curColNum_tFileInputExcel_1=columnIndex_tFileInputExcel_1 + start_column_tFileInputExcel_1 + 1;
				curColName_tFileInputExcel_1 = "item_code";

				row1.item_code = temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1];
			}else{
				row1.item_code = null;
				emptyColumnCount_tFileInputExcel_1++;
			}
							columnIndex_tFileInputExcel_1 = 9;
						
			if( temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1].length() > 0) {
				curColNum_tFileInputExcel_1=columnIndex_tFileInputExcel_1 + start_column_tFileInputExcel_1 + 1;
				curColName_tFileInputExcel_1 = "ord_qty";

				row1.ord_qty = ParserUtils.parseTo_Integer(ParserUtils.parseTo_Number(temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1], null, '.'==decimalChar_tFileInputExcel_1 ? null : decimalChar_tFileInputExcel_1));
			}else{
				row1.ord_qty = null;
				emptyColumnCount_tFileInputExcel_1++;
			}

				nb_line_tFileInputExcel_1++;
				
				log.debug("tFileInputExcel_1 - Retrieving the record " + (nb_line_tFileInputExcel_1) + ".");
			
			}catch(java.lang.Exception e){
			whetherReject_tFileInputExcel_1 = true;
						log.error("tFileInputExcel_1 - " + e.getMessage());
					
					 System.err.println(e.getMessage());
					 row1 = null;
			}


		



 



/**
 * [tFileInputExcel_1 begin ] stop
 */
	
	/**
	 * [tFileInputExcel_1 main ] start
	 */

	

	
	
	currentComponent="tFileInputExcel_1";

	

 


	tos_count_tFileInputExcel_1++;

/**
 * [tFileInputExcel_1 main ] stop
 */
	
	/**
	 * [tFileInputExcel_1 process_data_begin ] start
	 */

	

	
	
	currentComponent="tFileInputExcel_1";

	

 



/**
 * [tFileInputExcel_1 process_data_begin ] stop
 */
// Start of branch "row1"
if(row1 != null) { 



	
	/**
	 * [tMap_1 main ] start
	 */

	

	
	
	currentComponent="tMap_1";

	

			//row1
			//row1


			
				if(execStat){
					runStat.updateStatOnConnection("row1"+iterateId,1, 1);
				} 
			

		
    			if(log.isTraceEnabled()){
    				log.trace("row1 - " + (row1==null? "": row1.toLogString()));
    			}
    		

		
		
		boolean hasCasePrimitiveKeyWithNull_tMap_1 = false;
		
        // ###############################
        // # Input tables (lookups)
		  boolean rejectedInnerJoin_tMap_1 = false;
		  boolean mainRowRejected_tMap_1 = false;
            				    								  
		// ###############################
        { // start of Var scope
        
	        // ###############################
        	// # Vars tables
        
Var__tMap_1__Struct Var = Var__tMap_1;// ###############################
        // ###############################
        // # Output tables

sendData = null;


// # Output table : 'sendData'
count_sendData_tMap_1++;

sendData_tmp.PO_NUMBER = row1.po_no ;
sendData_tmp.SUP_ID = row1.sup_id ;
sendData_tmp.SHIP_TO = row1.ship_to ;
sendData_tmp.BILL_TO = row1.bill_to ;
sendData_tmp.VENDOR = row1.vendor_code ;
sendData_tmp.ORD_DATE = row1.ord_date ;
sendData_tmp.SHIP_DATE = row1.ship_date ;
sendData_tmp.LINE_NO = row1.line_no ;
sendData_tmp.ITEM_CODE = row1.item_code ;
sendData_tmp.ORD_QTY = row1.ord_qty ;
sendData = sendData_tmp;
log.debug("tMap_1 - Outputting the record " + count_sendData_tMap_1 + " of the output table 'sendData'.");

// ###############################

} // end of Var scope

rejectedInnerJoin_tMap_1 = false;










 


	tos_count_tMap_1++;

/**
 * [tMap_1 main ] stop
 */
	
	/**
	 * [tMap_1 process_data_begin ] start
	 */

	

	
	
	currentComponent="tMap_1";

	

 



/**
 * [tMap_1 process_data_begin ] stop
 */
// Start of branch "sendData"
if(sendData != null) { 



	
	/**
	 * [tDBCDCOutput_1 main ] start
	 */

	

	
	
	currentComponent="tDBCDCOutput_1";

	

			//sendData
			//sendData


			
				if(execStat){
					runStat.updateStatOnConnection("sendData"+iterateId,1, 1);
				} 
			

		
    			if(log.isTraceEnabled()){
    				log.trace("sendData - " + (sendData==null? "": sendData.toLogString()));
    			}
    		

			     			if(sendData.PO_NUMBER!=null){
			     				java.util.List<oracle.streams.ChunkColumnValue> chunks_tDBCDCOutput_1=null;
				     			
		    						//////////////////////////////////////////////////"
									the column:PO_NUMBER should be a Object type or a Document type.
									//////////////////////////////////////////////////
	    							


 


	tos_count_tDBCDCOutput_1++;

/**
 * [tDBCDCOutput_1 main ] stop
 */
	
	/**
	 * [tDBCDCOutput_1 process_data_begin ] start
	 */

	

	
	
	currentComponent="tDBCDCOutput_1";

	

 



/**
 * [tDBCDCOutput_1 process_data_begin ] stop
 */
	
	/**
	 * [tDBCDCOutput_1 process_data_end ] start
	 */

	

	
	
	currentComponent="tDBCDCOutput_1";

	

 



/**
 * [tDBCDCOutput_1 process_data_end ] stop
 */

} // End of branch "sendData"




	
	/**
	 * [tMap_1 process_data_end ] start
	 */

	

	
	
	currentComponent="tMap_1";

	

 



/**
 * [tMap_1 process_data_end ] stop
 */

} // End of branch "row1"




	
	/**
	 * [tFileInputExcel_1 process_data_end ] start
	 */

	

	
	
	currentComponent="tFileInputExcel_1";

	

 



/**
 * [tFileInputExcel_1 process_data_end ] stop
 */
	
	/**
	 * [tFileInputExcel_1 end ] start
	 */

	

	
	
	currentComponent="tFileInputExcel_1";

	

			}
			
			
				log.debug("tFileInputExcel_1 - Retrieved records count: "+ nb_line_tFileInputExcel_1 + " .");
			
			
			globalMap.put("tFileInputExcel_1_NB_LINE",nb_line_tFileInputExcel_1);
			
				}
			
		} finally { 
				
  				if(!(source_tFileInputExcel_1 instanceof java.io.InputStream)){
  					workbook_tFileInputExcel_1.getPackage().revert();
  				}
				
		}	
		
 
                if(log.isDebugEnabled())
            log.debug("tFileInputExcel_1 - "  + ("Done.") );

ok_Hash.put("tFileInputExcel_1", true);
end_Hash.put("tFileInputExcel_1", System.currentTimeMillis());




/**
 * [tFileInputExcel_1 end ] stop
 */

	
	/**
	 * [tMap_1 end ] start
	 */

	

	
	
	currentComponent="tMap_1";

	


// ###############################
// # Lookup hashes releasing
// ###############################      
				log.debug("tMap_1 - Written records count in the table 'sendData': " + count_sendData_tMap_1 + ".");





			if(execStat){
				if(resourceMap.get("inIterateVComp") == null || !((Boolean)resourceMap.get("inIterateVComp"))){
			 		runStat.updateStatOnConnection("row1"+iterateId,2, 0); 
			 	}
			}
		
 
                if(log.isDebugEnabled())
            log.debug("tMap_1 - "  + ("Done.") );

ok_Hash.put("tMap_1", true);
end_Hash.put("tMap_1", System.currentTimeMillis());




/**
 * [tMap_1 end ] stop
 */

	
	/**
	 * [tDBCDCOutput_1 end ] start
	 */

	

	
	
	currentComponent="tDBCDCOutput_1";

	

	globalMap.put("tDBCDCOutput_1_NB_LINE",nb_line_tDBCDCOutput_1);
	try {
		
	    		log.info("tDBCDCOutput_1 - Try to detach from inbound server:"+""+"");
			
		xStreamIn_tDBCDCOutput_1.detach(oracle.streams.XStreamIn.DEFAULT_MODE);
		
	    		log.info("tDBCDCOutput_1 - Detached successfully.");
			
	} catch (oracle.streams.StreamsException e_tDBCDCOutput_1) {
		System.err.println("Cannot detach from the inbound server: "+ "");
		
	    		log.error("tDBCDCOutput_1 - Cannot detach from the inbound server: "+""+"");
			
		throw e_tDBCDCOutput_1;
	}
	
	    		log.debug("tDBCDCOutput_1 - Closing the connection to the database.");
			
			conn_tDBCDCOutput_1.close();
			
	    		log.debug("tDBCDCOutput_1 - Connection to the database closed.");
			
	    		log.debug("tDBCDCOutput_1 - Retrieved records count: "+nb_line_tDBCDCOutput_1 + " .");
			
			if(execStat){
				if(resourceMap.get("inIterateVComp") == null || !((Boolean)resourceMap.get("inIterateVComp"))){
			 		runStat.updateStatOnConnection("sendData"+iterateId,2, 0); 
			 	}
			}
		
 

ok_Hash.put("tDBCDCOutput_1", true);
end_Hash.put("tDBCDCOutput_1", System.currentTimeMillis());




/**
 * [tDBCDCOutput_1 end ] stop
 */






				}//end the resume

				



	
			}catch(java.lang.Exception e){	
				
				    if(!(e instanceof TalendException)){
					   log.fatal(currentComponent + " " + e.getMessage(),e);
					}
				
				TalendException te = new TalendException(e, currentComponent, globalMap);
				
				throw te;
			}catch(java.lang.Error error){	
				
					runStat.stopThreadStat();
				
				throw error;
			}finally{
				
				try{
					
	
	/**
	 * [tFileInputExcel_1 finally ] start
	 */

	

	
	
	currentComponent="tFileInputExcel_1";

	

 



/**
 * [tFileInputExcel_1 finally ] stop
 */

	
	/**
	 * [tMap_1 finally ] start
	 */

	

	
	
	currentComponent="tMap_1";

	

 



/**
 * [tMap_1 finally ] stop
 */

	
	/**
	 * [tDBCDCOutput_1 finally ] start
	 */

	

	
	
	currentComponent="tDBCDCOutput_1";

	

 



/**
 * [tDBCDCOutput_1 finally ] stop
 */






				}catch(java.lang.Exception e){	
					//ignore
				}catch(java.lang.Error error){
					//ignore
				}
				resourceMap = null;
			}
		

		globalMap.put("tFileInputExcel_1_SUBPROCESS_STATE", 1);
	}
	
    public String resuming_logs_dir_path = null;
    public String resuming_checkpoint_path = null;
    public String parent_part_launcher = null;
    private String resumeEntryMethodName = null;
    private boolean globalResumeTicket = false;

    public boolean watch = false;
    // portStats is null, it means don't execute the statistics
    public Integer portStats = null;
    public int portTraces = 4334;
    public String clientHost;
    public String defaultClientHost = "localhost";
    public String contextStr = "Default";
    public boolean isDefaultContext = true;
    public String pid = "0";
    public String rootPid = null;
    public String fatherPid = null;
    public String fatherNode = null;
    public long startTime = 0;
    public boolean isChildJob = false;
    public String log4jLevel = "";

    private boolean execStat = true;

    private ThreadLocal<java.util.Map<String, String>> threadLocal = new ThreadLocal<java.util.Map<String, String>>() {
        protected java.util.Map<String, String> initialValue() {
            java.util.Map<String,String> threadRunResultMap = new java.util.HashMap<String, String>();
            threadRunResultMap.put("errorCode", null);
            threadRunResultMap.put("status", "");
            return threadRunResultMap;
        };
    };


    private PropertiesWithType context_param = new PropertiesWithType();
    public java.util.Map<String, Object> parentContextMap = new java.util.HashMap<String, Object>();

    public String status= "";
    

    public static void main(String[] args){
        final basicJob basicJobClass = new basicJob();

        int exitCode = basicJobClass.runJobInTOS(args);
	        if(exitCode==0){
		        log.info("TalendJob: 'basicJob' - Done.");
	        }

        System.exit(exitCode);
    }


    public String[][] runJob(String[] args) {

        int exitCode = runJobInTOS(args);
        String[][] bufferValue = new String[][] { { Integer.toString(exitCode) } };

        return bufferValue;
    }

    public boolean hastBufferOutputComponent() {
		boolean hastBufferOutput = false;
    	
        return hastBufferOutput;
    }

    public int runJobInTOS(String[] args) {
	   	// reset status
	   	status = "";

        String lastStr = "";
        for (String arg : args) {
            if (arg.equalsIgnoreCase("--context_param")) {
                lastStr = arg;
            } else if (lastStr.equals("")) {
                evalParam(arg);
            } else {
                evalParam(lastStr + " " + arg);
                lastStr = "";
            }
        }

	        if(!"".equals(log4jLevel)){
				if("trace".equalsIgnoreCase(log4jLevel)){
					log.setLevel(org.apache.log4j.Level.TRACE);
				}else if("debug".equalsIgnoreCase(log4jLevel)){
					log.setLevel(org.apache.log4j.Level.DEBUG);
				}else if("info".equalsIgnoreCase(log4jLevel)){
					log.setLevel(org.apache.log4j.Level.INFO);
				}else if("warn".equalsIgnoreCase(log4jLevel)){
					log.setLevel(org.apache.log4j.Level.WARN);
				}else if("error".equalsIgnoreCase(log4jLevel)){
					log.setLevel(org.apache.log4j.Level.ERROR);
				}else if("fatal".equalsIgnoreCase(log4jLevel)){
					log.setLevel(org.apache.log4j.Level.FATAL);
				}else if ("off".equalsIgnoreCase(log4jLevel)){
					log.setLevel(org.apache.log4j.Level.OFF);
				}
				org.apache.log4j.Logger.getRootLogger().setLevel(log.getLevel());
    	    }
        	log.info("TalendJob: 'basicJob' - Start.");
    	

        if(clientHost == null) {
            clientHost = defaultClientHost;
        }

        if(pid == null || "0".equals(pid)) {
            pid = TalendString.getAsciiRandomString(6);
        }

        if (rootPid==null) {
            rootPid = pid;
        }
        if (fatherPid==null) {
            fatherPid = pid;
        }else{
            isChildJob = true;
        }

        if (portStats != null) {
            // portStats = -1; //for testing
            if (portStats < 0 || portStats > 65535) {
                // issue:10869, the portStats is invalid, so this client socket can't open
                System.err.println("The statistics socket port " + portStats + " is invalid.");
                execStat = false;
            }
        } else {
            execStat = false;
        }

        try {
            //call job/subjob with an existing context, like: --context=production. if without this parameter, there will use the default context instead.
            java.io.InputStream inContext = basicJob.class.getClassLoader().getResourceAsStream("test/basicjob_0_1/contexts/" + contextStr + ".properties");
            if (inContext == null) {
                inContext = basicJob.class.getClassLoader().getResourceAsStream("config/contexts/" + contextStr + ".properties");
            }
            if (inContext != null && context != null && context.isEmpty()) {
                //defaultProps is in order to keep the original context value
                defaultProps.load(inContext);
                inContext.close();
                context = new ContextProperties(defaultProps);
            } else if (!isDefaultContext) {
                //print info and job continue to run, for case: context_param is not empty.
                System.err.println("Could not find the context " + contextStr);
            }

            if(!context_param.isEmpty()) {
                context.putAll(context_param);
				//set types for params from parentJobs
				for (Object key: context_param.keySet()){
					String context_key = key.toString();
					String context_type = context_param.getContextType(context_key);
					context.setContextType(context_key, context_type);

				}
            }
        } catch (java.io.IOException ie) {
            System.err.println("Could not load context "+contextStr);
            ie.printStackTrace();
        }


        // get context value from parent directly
        if (parentContextMap != null && !parentContextMap.isEmpty()) {
        }

        //Resume: init the resumeUtil
        resumeEntryMethodName = ResumeUtil.getResumeEntryMethodName(resuming_checkpoint_path);
        resumeUtil = new ResumeUtil(resuming_logs_dir_path, isChildJob, rootPid);
        resumeUtil.initCommonInfo(pid, rootPid, fatherPid, projectName, jobName, contextStr, jobVersion);

		List<String> parametersToEncrypt = new java.util.ArrayList<String>();
        //Resume: jobStart
        resumeUtil.addLog("JOB_STARTED", "JOB:" + jobName, parent_part_launcher, Thread.currentThread().getId() + "", "","","","",resumeUtil.convertToJsonText(context,parametersToEncrypt));

if(execStat) {
    try {
        runStat.openSocket(!isChildJob);
        runStat.setAllPID(rootPid, fatherPid, pid, jobName);
        runStat.startThreadStat(clientHost, portStats);
        runStat.updateStatOnJob(RunStat.JOBSTART, fatherNode);
    } catch (java.io.IOException ioException) {
        ioException.printStackTrace();
    }
}



	
	    java.util.concurrent.ConcurrentHashMap<Object, Object> concurrentHashMap = new java.util.concurrent.ConcurrentHashMap<Object, Object>();
	    globalMap.put("concurrentHashMap", concurrentHashMap);
	

    long startUsedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    long endUsedMemory = 0;
    long end = 0;

    startTime = System.currentTimeMillis();




this.globalResumeTicket = true;//to run tPreJob




this.globalResumeTicket = false;//to run others jobs

try {
errorCode = null;tFileInputExcel_1Process(globalMap);
if(!"failure".equals(status)) { status = "end"; }
}catch (TalendException e_tFileInputExcel_1) {
globalMap.put("tFileInputExcel_1_SUBPROCESS_STATE", -1);

e_tFileInputExcel_1.printStackTrace();

}

this.globalResumeTicket = true;//to run tPostJob




        end = System.currentTimeMillis();

        if (watch) {
            System.out.println((end-startTime)+" milliseconds");
        }

        endUsedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        if (false) {
            System.out.println((endUsedMemory - startUsedMemory) + " bytes memory increase when running : basicJob");
        }



if (execStat) {
    runStat.updateStatOnJob(RunStat.JOBEND, fatherNode);
    runStat.stopThreadStat();
}
    int returnCode = 0;
    if(errorCode == null) {
         returnCode = status != null && status.equals("failure") ? 1 : 0;
    } else {
         returnCode = errorCode.intValue();
    }
    resumeUtil.addLog("JOB_ENDED", "JOB:" + jobName, parent_part_launcher, Thread.currentThread().getId() + "", "","" + returnCode,"","","");

    return returnCode;

  }

    // only for OSGi env
    public void destroy() {


    }














    private java.util.Map<String, Object> getSharedConnections4REST() {
        java.util.Map<String, Object> connections = new java.util.HashMap<String, Object>();







        return connections;
    }

    private void evalParam(String arg) {
        if (arg.startsWith("--resuming_logs_dir_path")) {
            resuming_logs_dir_path = arg.substring(25);
        } else if (arg.startsWith("--resuming_checkpoint_path")) {
            resuming_checkpoint_path = arg.substring(27);
        } else if (arg.startsWith("--parent_part_launcher")) {
            parent_part_launcher = arg.substring(23);
        } else if (arg.startsWith("--watch")) {
            watch = true;
        } else if (arg.startsWith("--stat_port=")) {
            String portStatsStr = arg.substring(12);
            if (portStatsStr != null && !portStatsStr.equals("null")) {
                portStats = Integer.parseInt(portStatsStr);
            }
        } else if (arg.startsWith("--trace_port=")) {
            portTraces = Integer.parseInt(arg.substring(13));
        } else if (arg.startsWith("--client_host=")) {
            clientHost = arg.substring(14);
        } else if (arg.startsWith("--context=")) {
            contextStr = arg.substring(10);
            isDefaultContext = false;
        } else if (arg.startsWith("--father_pid=")) {
            fatherPid = arg.substring(13);
        } else if (arg.startsWith("--root_pid=")) {
            rootPid = arg.substring(11);
        } else if (arg.startsWith("--father_node=")) {
            fatherNode = arg.substring(14);
        } else if (arg.startsWith("--pid=")) {
            pid = arg.substring(6);
        } else if (arg.startsWith("--context_type")) {
            String keyValue = arg.substring(15);
			int index = -1;
            if (keyValue != null && (index = keyValue.indexOf('=')) > -1) {
                if (fatherPid==null) {
                    context_param.setContextType(keyValue.substring(0, index), replaceEscapeChars(keyValue.substring(index + 1)));
                } else { // the subjob won't escape the especial chars
                    context_param.setContextType(keyValue.substring(0, index), keyValue.substring(index + 1) );
                }

            }

		} else if (arg.startsWith("--context_param")) {
            String keyValue = arg.substring(16);
            int index = -1;
            if (keyValue != null && (index = keyValue.indexOf('=')) > -1) {
                if (fatherPid==null) {
                    context_param.put(keyValue.substring(0, index), replaceEscapeChars(keyValue.substring(index + 1)));
                } else { // the subjob won't escape the especial chars
                    context_param.put(keyValue.substring(0, index), keyValue.substring(index + 1) );
                }
            }
        }else if (arg.startsWith("--log4jLevel=")) {
            log4jLevel = arg.substring(13);
		}

    }
    
    private static final String NULL_VALUE_EXPRESSION_IN_COMMAND_STRING_FOR_CHILD_JOB_ONLY = "<TALEND_NULL>";

    private final String[][] escapeChars = {
        {"\\\\","\\"},{"\\n","\n"},{"\\'","\'"},{"\\r","\r"},
        {"\\f","\f"},{"\\b","\b"},{"\\t","\t"}
        };
    private String replaceEscapeChars (String keyValue) {

		if (keyValue == null || ("").equals(keyValue.trim())) {
			return keyValue;
		}

		StringBuilder result = new StringBuilder();
		int currIndex = 0;
		while (currIndex < keyValue.length()) {
			int index = -1;
			// judege if the left string includes escape chars
			for (String[] strArray : escapeChars) {
				index = keyValue.indexOf(strArray[0],currIndex);
				if (index>=0) {

					result.append(keyValue.substring(currIndex, index + strArray[0].length()).replace(strArray[0], strArray[1]));
					currIndex = index + strArray[0].length();
					break;
				}
			}
			// if the left string doesn't include escape chars, append the left into the result
			if (index < 0) {
				result.append(keyValue.substring(currIndex));
				currIndex = currIndex + keyValue.length();
			}
		}

		return result.toString();
    }

    public Integer getErrorCode() {
        return errorCode;
    }


    public String getStatus() {
        return status;
    }

    ResumeUtil resumeUtil = null;
}
/************************************************************************************************
 *     76094 characters generated by Talend Cloud Data Management Platform 
 *     on the 12 December, 2018 6:07:38 PM IST
 ************************************************************************************************/