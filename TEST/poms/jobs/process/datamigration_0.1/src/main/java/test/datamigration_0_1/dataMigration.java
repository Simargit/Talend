package test.datamigration_0_1;

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
 * Job: dataMigration Purpose: <br>
 * Description:  <br>
 * @author simarpreet.singh@intellinum.com
 * @version 7.1.1.20181026_1147
 * @status 
 */
public class dataMigration implements TalendJob {
	static {
		System.setProperty("TalendJob.log", "dataMigration.log");
	}
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(dataMigration.class);

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

	private final static String defaultCharset = java.nio.charset.Charset
			.defaultCharset().name();

	private final static String utf8Charset = "UTF-8";

	// contains type for every context property
	public class PropertiesWithType extends java.util.Properties {
		private static final long serialVersionUID = 1L;
		private java.util.Map<String, String> propertyTypes = new java.util.HashMap<>();

		public PropertiesWithType(java.util.Properties properties) {
			super(properties);
		}

		public PropertiesWithType() {
			super();
		}

		public void setContextType(String key, String type) {
			propertyTypes.put(key, type);
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

		public ContextProperties(java.util.Properties properties) {
			super(properties);
		}

		public ContextProperties() {
			super();
		}

		public void synchronizeContext() {

		}

	}

	protected ContextProperties context = new ContextProperties(); // will be
																	// instanciated
																	// by MS.

	public ContextProperties getContext() {
		return this.context;
	}

	private final String jobVersion = "0.1";
	private final String jobName = "dataMigration";
	private final String projectName = "TEST";
	public Integer errorCode = null;
	private String currentComponent = "";

	private final java.util.Map<String, Object> globalMap = new java.util.HashMap<String, Object>();
	private final static java.util.Map<String, Object> junitGlobalMap = new java.util.HashMap<String, Object>();

	private final java.util.Map<String, Long> start_Hash = new java.util.HashMap<String, Long>();
	private final java.util.Map<String, Long> end_Hash = new java.util.HashMap<String, Long>();
	private final java.util.Map<String, Boolean> ok_Hash = new java.util.HashMap<String, Boolean>();
	public final java.util.List<String[]> globalBuffer = new java.util.ArrayList<String[]>();

	private RunStat runStat = new RunStat();

	// OSGi DataSource
	private final static String KEY_DB_DATASOURCES = "KEY_DB_DATASOURCES";

	private final static String KEY_DB_DATASOURCES_RAW = "KEY_DB_DATASOURCES_RAW";

	public void setDataSources(
			java.util.Map<String, javax.sql.DataSource> dataSources) {
		java.util.Map<String, routines.system.TalendDataSource> talendDataSources = new java.util.HashMap<String, routines.system.TalendDataSource>();
		for (java.util.Map.Entry<String, javax.sql.DataSource> dataSourceEntry : dataSources
				.entrySet()) {
			talendDataSources.put(
					dataSourceEntry.getKey(),
					new routines.system.TalendDataSource(dataSourceEntry
							.getValue()));
		}
		globalMap.put(KEY_DB_DATASOURCES, talendDataSources);
		globalMap
				.put(KEY_DB_DATASOURCES_RAW,
						new java.util.HashMap<String, javax.sql.DataSource>(
								dataSources));
	}

	private final java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
	private final java.io.PrintStream errorMessagePS = new java.io.PrintStream(
			new java.io.BufferedOutputStream(baos));

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

		public void setVirtualComponentName(String virtualComponentName) {
			this.virtualComponentName = virtualComponentName;
		}

		private TalendException(Exception e, String errorComponent,
				final java.util.Map<String, Object> globalMap) {
			this.currentComponent = errorComponent;
			this.globalMap = globalMap;
			this.e = e;
		}

		public Exception getException() {
			return this.e;
		}

		public String getCurrentComponent() {
			return this.currentComponent;
		}

		public String getExceptionCauseMessage(Exception e) {
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
				if (virtualComponentName != null
						&& currentComponent.indexOf(virtualComponentName + "_") == 0) {
					globalMap.put(virtualComponentName + "_ERROR_MESSAGE",
							getExceptionCauseMessage(e));
				}
				globalMap.put(currentComponent + "_ERROR_MESSAGE",
						getExceptionCauseMessage(e));
				System.err.println("Exception in component " + currentComponent
						+ " (" + jobName + ")");
			}
			if (!(e instanceof TDieException)) {
				if (e instanceof TalendException) {
					e.printStackTrace();
				} else {
					e.printStackTrace();
					e.printStackTrace(errorMessagePS);
					dataMigration.this.exception = e;
				}
			}
			if (!(e instanceof TalendException)) {
				try {
					for (java.lang.reflect.Method m : this.getClass()
							.getEnclosingClass().getMethods()) {
						if (m.getName().compareTo(currentComponent + "_error") == 0) {
							m.invoke(dataMigration.this, new Object[] { e,
									currentComponent, globalMap });
							break;
						}
					}

					if (!(e instanceof TDieException)) {
					}
				} catch (Exception e) {
					this.e.printStackTrace();
				}
			}
		}
	}

	public void tFileInputExcel_1_error(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tFileInputExcel_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tDBOutput_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tFileInputExcel_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tHMap_1_THMAP_OUT_error(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		tHMap_1_THMAP_IN_error(exception, errorComponent, globalMap);

	}

	public void tHMap_1_THMAP_IN_error(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tFileInputExcel_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tFileInputExcel_1_onSubJobError(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread
				.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(),
				ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public static class finalinputStruct implements
			routines.system.IPersistableRow<finalinputStruct> {
		final static byte[] commonByteArrayLock_TEST_dataMigration = new byte[0];
		static byte[] commonByteArray_TEST_dataMigration = new byte[0];
		protected static final int DEFAULT_HASHCODE = 1;
		protected static final int PRIME = 31;
		protected int hashCode = DEFAULT_HASHCODE;
		public boolean hashCodeDirty = true;

		public String loopKey;

		public String po_number;

		public String getPo_number() {
			return this.po_number;
		}

		public Integer sup_id;

		public Integer getSup_id() {
			return this.sup_id;
		}

		public String ship_to;

		public String getShip_to() {
			return this.ship_to;
		}

		public String bill_to;

		public String getBill_to() {
			return this.bill_to;
		}

		public String vendor;

		public String getVendor() {
			return this.vendor;
		}

		public java.util.Date ord_date;

		public java.util.Date getOrd_date() {
			return this.ord_date;
		}

		public java.util.Date ship_date;

		public java.util.Date getShip_date() {
			return this.ship_date;
		}

		public Integer line_no;

		public Integer getLine_no() {
			return this.line_no;
		}

		public String item_code;

		public String getItem_code() {
			return this.item_code;
		}

		public Integer ord_qty;

		public Integer getOrd_qty() {
			return this.ord_qty;
		}

		@Override
		public int hashCode() {
			if (this.hashCodeDirty) {
				final int prime = PRIME;
				int result = DEFAULT_HASHCODE;

				result = prime
						* result
						+ ((this.po_number == null) ? 0 : this.po_number
								.hashCode());

				this.hashCode = result;
				this.hashCodeDirty = false;
			}
			return this.hashCode;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final finalinputStruct other = (finalinputStruct) obj;

			if (this.po_number == null) {
				if (other.po_number != null)
					return false;

			} else if (!this.po_number.equals(other.po_number))

				return false;

			return true;
		}

		public void copyDataTo(finalinputStruct other) {

			other.po_number = this.po_number;
			other.sup_id = this.sup_id;
			other.ship_to = this.ship_to;
			other.bill_to = this.bill_to;
			other.vendor = this.vendor;
			other.ord_date = this.ord_date;
			other.ship_date = this.ship_date;
			other.line_no = this.line_no;
			other.item_code = this.item_code;
			other.ord_qty = this.ord_qty;

		}

		public void copyKeysDataTo(finalinputStruct other) {

			other.po_number = this.po_number;

		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_TEST_dataMigration.length) {
					if (length < 1024
							&& commonByteArray_TEST_dataMigration.length == 0) {
						commonByteArray_TEST_dataMigration = new byte[1024];
					} else {
						commonByteArray_TEST_dataMigration = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_TEST_dataMigration, 0, length);
				strReturn = new String(commonByteArray_TEST_dataMigration, 0,
						length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos)
				throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private Integer readInteger(ObjectInputStream dis) throws IOException {
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

		private void writeInteger(Integer intNum, ObjectOutputStream dos)
				throws IOException {
			if (intNum == null) {
				dos.writeByte(-1);
			} else {
				dos.writeByte(0);
				dos.writeInt(intNum);
			}
		}

		private java.util.Date readDate(ObjectInputStream dis)
				throws IOException {
			java.util.Date dateReturn = null;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				dateReturn = null;
			} else {
				dateReturn = new Date(dis.readLong());
			}
			return dateReturn;
		}

		private void writeDate(java.util.Date date1, ObjectOutputStream dos)
				throws IOException {
			if (date1 == null) {
				dos.writeByte(-1);
			} else {
				dos.writeByte(0);
				dos.writeLong(date1.getTime());
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_TEST_dataMigration) {

				try {

					int length = 0;

					this.po_number = readString(dis);

					this.sup_id = readInteger(dis);

					this.ship_to = readString(dis);

					this.bill_to = readString(dis);

					this.vendor = readString(dis);

					this.ord_date = readDate(dis);

					this.ship_date = readDate(dis);

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

				writeString(this.po_number, dos);

				// Integer

				writeInteger(this.sup_id, dos);

				// String

				writeString(this.ship_to, dos);

				// String

				writeString(this.bill_to, dos);

				// String

				writeString(this.vendor, dos);

				// java.util.Date

				writeDate(this.ord_date, dos);

				// java.util.Date

				writeDate(this.ship_date, dos);

				// Integer

				writeInteger(this.line_no, dos);

				// String

				writeString(this.item_code, dos);

				// Integer

				writeInteger(this.ord_qty, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("po_number=" + po_number);
			sb.append(",sup_id=" + String.valueOf(sup_id));
			sb.append(",ship_to=" + ship_to);
			sb.append(",bill_to=" + bill_to);
			sb.append(",vendor=" + vendor);
			sb.append(",ord_date=" + String.valueOf(ord_date));
			sb.append(",ship_date=" + String.valueOf(ship_date));
			sb.append(",line_no=" + String.valueOf(line_no));
			sb.append(",item_code=" + item_code);
			sb.append(",ord_qty=" + String.valueOf(ord_qty));
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (po_number == null) {
				sb.append("<null>");
			} else {
				sb.append(po_number);
			}

			sb.append("|");

			if (sup_id == null) {
				sb.append("<null>");
			} else {
				sb.append(sup_id);
			}

			sb.append("|");

			if (ship_to == null) {
				sb.append("<null>");
			} else {
				sb.append(ship_to);
			}

			sb.append("|");

			if (bill_to == null) {
				sb.append("<null>");
			} else {
				sb.append(bill_to);
			}

			sb.append("|");

			if (vendor == null) {
				sb.append("<null>");
			} else {
				sb.append(vendor);
			}

			sb.append("|");

			if (ord_date == null) {
				sb.append("<null>");
			} else {
				sb.append(ord_date);
			}

			sb.append("|");

			if (ship_date == null) {
				sb.append("<null>");
			} else {
				sb.append(ship_date);
			}

			sb.append("|");

			if (line_no == null) {
				sb.append("<null>");
			} else {
				sb.append(line_no);
			}

			sb.append("|");

			if (item_code == null) {
				sb.append("<null>");
			} else {
				sb.append(item_code);
			}

			sb.append("|");

			if (ord_qty == null) {
				sb.append("<null>");
			} else {
				sb.append(ord_qty);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(finalinputStruct other) {

			int returnValue = -1;

			returnValue = checkNullsAndCompare(this.po_number, other.po_number);
			if (returnValue != 0) {
				return returnValue;
			}

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(),
						object2.toString());
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

	public static class row1Struct implements
			routines.system.IPersistableRow<row1Struct> {
		final static byte[] commonByteArrayLock_TEST_dataMigration = new byte[0];
		static byte[] commonByteArray_TEST_dataMigration = new byte[0];
		protected static final int DEFAULT_HASHCODE = 1;
		protected static final int PRIME = 31;
		protected int hashCode = DEFAULT_HASHCODE;
		public boolean hashCodeDirty = true;

		public String loopKey;

		public String po_no;

		public String getPo_no() {
			return this.po_no;
		}

		public Integer sup_id;

		public Integer getSup_id() {
			return this.sup_id;
		}

		public String ship_to;

		public String getShip_to() {
			return this.ship_to;
		}

		public String bill_to;

		public String getBill_to() {
			return this.bill_to;
		}

		public String vendor_code;

		public String getVendor_code() {
			return this.vendor_code;
		}

		public java.util.Date ord_date;

		public java.util.Date getOrd_date() {
			return this.ord_date;
		}

		public java.util.Date ship_date;

		public java.util.Date getShip_date() {
			return this.ship_date;
		}

		public Integer line_no;

		public Integer getLine_no() {
			return this.line_no;
		}

		public String item_code;

		public String getItem_code() {
			return this.item_code;
		}

		public Integer ord_qty;

		public Integer getOrd_qty() {
			return this.ord_qty;
		}

		@Override
		public int hashCode() {
			if (this.hashCodeDirty) {
				final int prime = PRIME;
				int result = DEFAULT_HASHCODE;

				result = prime * result
						+ ((this.po_no == null) ? 0 : this.po_no.hashCode());

				this.hashCode = result;
				this.hashCodeDirty = false;
			}
			return this.hashCode;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final row1Struct other = (row1Struct) obj;

			if (this.po_no == null) {
				if (other.po_no != null)
					return false;

			} else if (!this.po_no.equals(other.po_no))

				return false;

			return true;
		}

		public void copyDataTo(row1Struct other) {

			other.po_no = this.po_no;
			other.sup_id = this.sup_id;
			other.ship_to = this.ship_to;
			other.bill_to = this.bill_to;
			other.vendor_code = this.vendor_code;
			other.ord_date = this.ord_date;
			other.ship_date = this.ship_date;
			other.line_no = this.line_no;
			other.item_code = this.item_code;
			other.ord_qty = this.ord_qty;

		}

		public void copyKeysDataTo(row1Struct other) {

			other.po_no = this.po_no;

		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_TEST_dataMigration.length) {
					if (length < 1024
							&& commonByteArray_TEST_dataMigration.length == 0) {
						commonByteArray_TEST_dataMigration = new byte[1024];
					} else {
						commonByteArray_TEST_dataMigration = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_TEST_dataMigration, 0, length);
				strReturn = new String(commonByteArray_TEST_dataMigration, 0,
						length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos)
				throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private Integer readInteger(ObjectInputStream dis) throws IOException {
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

		private void writeInteger(Integer intNum, ObjectOutputStream dos)
				throws IOException {
			if (intNum == null) {
				dos.writeByte(-1);
			} else {
				dos.writeByte(0);
				dos.writeInt(intNum);
			}
		}

		private java.util.Date readDate(ObjectInputStream dis)
				throws IOException {
			java.util.Date dateReturn = null;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				dateReturn = null;
			} else {
				dateReturn = new Date(dis.readLong());
			}
			return dateReturn;
		}

		private void writeDate(java.util.Date date1, ObjectOutputStream dos)
				throws IOException {
			if (date1 == null) {
				dos.writeByte(-1);
			} else {
				dos.writeByte(0);
				dos.writeLong(date1.getTime());
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_TEST_dataMigration) {

				try {

					int length = 0;

					this.po_no = readString(dis);

					this.sup_id = readInteger(dis);

					this.ship_to = readString(dis);

					this.bill_to = readString(dis);

					this.vendor_code = readString(dis);

					this.ord_date = readDate(dis);

					this.ship_date = readDate(dis);

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

				writeString(this.po_no, dos);

				// Integer

				writeInteger(this.sup_id, dos);

				// String

				writeString(this.ship_to, dos);

				// String

				writeString(this.bill_to, dos);

				// String

				writeString(this.vendor_code, dos);

				// java.util.Date

				writeDate(this.ord_date, dos);

				// java.util.Date

				writeDate(this.ship_date, dos);

				// Integer

				writeInteger(this.line_no, dos);

				// String

				writeString(this.item_code, dos);

				// Integer

				writeInteger(this.ord_qty, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("po_no=" + po_no);
			sb.append(",sup_id=" + String.valueOf(sup_id));
			sb.append(",ship_to=" + ship_to);
			sb.append(",bill_to=" + bill_to);
			sb.append(",vendor_code=" + vendor_code);
			sb.append(",ord_date=" + String.valueOf(ord_date));
			sb.append(",ship_date=" + String.valueOf(ship_date));
			sb.append(",line_no=" + String.valueOf(line_no));
			sb.append(",item_code=" + item_code);
			sb.append(",ord_qty=" + String.valueOf(ord_qty));
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (po_no == null) {
				sb.append("<null>");
			} else {
				sb.append(po_no);
			}

			sb.append("|");

			if (sup_id == null) {
				sb.append("<null>");
			} else {
				sb.append(sup_id);
			}

			sb.append("|");

			if (ship_to == null) {
				sb.append("<null>");
			} else {
				sb.append(ship_to);
			}

			sb.append("|");

			if (bill_to == null) {
				sb.append("<null>");
			} else {
				sb.append(bill_to);
			}

			sb.append("|");

			if (vendor_code == null) {
				sb.append("<null>");
			} else {
				sb.append(vendor_code);
			}

			sb.append("|");

			if (ord_date == null) {
				sb.append("<null>");
			} else {
				sb.append(ord_date);
			}

			sb.append("|");

			if (ship_date == null) {
				sb.append("<null>");
			} else {
				sb.append(ship_date);
			}

			sb.append("|");

			if (line_no == null) {
				sb.append("<null>");
			} else {
				sb.append(line_no);
			}

			sb.append("|");

			if (item_code == null) {
				sb.append("<null>");
			} else {
				sb.append(item_code);
			}

			sb.append("|");

			if (ord_qty == null) {
				sb.append("<null>");
			} else {
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

			returnValue = checkNullsAndCompare(this.po_no, other.po_no);
			if (returnValue != 0) {
				return returnValue;
			}

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(),
						object2.toString());
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

	public void tFileInputExcel_1Process(
			final java.util.Map<String, Object> globalMap)
			throws TalendException {
		globalMap.put("tFileInputExcel_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;
		String currentVirtualComponent = null;

		String iterateId = "";

		String currentComponent = "";
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception()
						.getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				row1Struct row1 = new row1Struct();
				finalinputStruct finalinput = new finalinputStruct();

				/**
				 * [tHMap_1_THMAP_OUT begin ] start
				 */

				ok_Hash.put("tHMap_1_THMAP_OUT", false);
				start_Hash.put("tHMap_1_THMAP_OUT", System.currentTimeMillis());

				currentVirtualComponent = "tHMap_1";

				currentComponent = "tHMap_1_THMAP_OUT";

				if (execStat) {
					if (resourceMap.get("inIterateVComp") == null) {

						runStat.updateStatOnConnection("row1" + iterateId, 0, 0);

					}
				}

				int tos_count_tHMap_1_THMAP_OUT = 0;

				// THMAPOUT_BEGIN thMap: tHMap_1_THMAP_OUT
				int nb_line_tHMap_1 = 0;
				java.util.List<java.util.Map<String, Object>> list_tHMap_1 = new java.util.ArrayList<>();
				globalMap.put(Thread.currentThread().getId()
						+ "_list_result_tHMap_1", list_tHMap_1);

				// Keep tHMap input and output structure names available to the
				// job code
				globalMap.put("tHMap_1_source_struct_name", "row1");
				globalMap.put("tHMap_1_target_struct_name", "finalinput");

				/**
				 * [tHMap_1_THMAP_OUT begin ] stop
				 */

				/**
				 * [tFileInputExcel_1 begin ] start
				 */

				ok_Hash.put("tFileInputExcel_1", false);
				start_Hash.put("tFileInputExcel_1", System.currentTimeMillis());

				currentComponent = "tFileInputExcel_1";

				int tos_count_tFileInputExcel_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tFileInputExcel_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tFileInputExcel_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tFileInputExcel_1 = new StringBuilder();
							log4jParamters_tFileInputExcel_1
									.append("Parameters:");
							log4jParamters_tFileInputExcel_1
									.append("VERSION_2007" + " = " + "true");
							log4jParamters_tFileInputExcel_1.append(" | ");
							log4jParamters_tFileInputExcel_1
									.append("FILENAME"
											+ " = "
											+ "\"C:/WorkSpace/talend/20181204182740_test_supplier1.xlsx\"");
							log4jParamters_tFileInputExcel_1.append(" | ");
							log4jParamters_tFileInputExcel_1
									.append("ALL_SHEETS" + " = " + "true");
							log4jParamters_tFileInputExcel_1.append(" | ");
							log4jParamters_tFileInputExcel_1.append("HEADER"
									+ " = " + "1");
							log4jParamters_tFileInputExcel_1.append(" | ");
							log4jParamters_tFileInputExcel_1.append("FOOTER"
									+ " = " + "0");
							log4jParamters_tFileInputExcel_1.append(" | ");
							log4jParamters_tFileInputExcel_1.append("LIMIT"
									+ " = " + "");
							log4jParamters_tFileInputExcel_1.append(" | ");
							log4jParamters_tFileInputExcel_1
									.append("AFFECT_EACH_SHEET" + " = "
											+ "false");
							log4jParamters_tFileInputExcel_1.append(" | ");
							log4jParamters_tFileInputExcel_1
									.append("FIRST_COLUMN" + " = " + "1");
							log4jParamters_tFileInputExcel_1.append(" | ");
							log4jParamters_tFileInputExcel_1
									.append("LAST_COLUMN" + " = " + "");
							log4jParamters_tFileInputExcel_1.append(" | ");
							log4jParamters_tFileInputExcel_1
									.append("DIE_ON_ERROR" + " = " + "false");
							log4jParamters_tFileInputExcel_1.append(" | ");
							log4jParamters_tFileInputExcel_1
									.append("ADVANCED_SEPARATOR" + " = "
											+ "false");
							log4jParamters_tFileInputExcel_1.append(" | ");
							log4jParamters_tFileInputExcel_1.append("TRIMALL"
									+ " = " + "false");
							log4jParamters_tFileInputExcel_1.append(" | ");
							log4jParamters_tFileInputExcel_1
									.append("TRIMSELECT" + " = " + "[{TRIM="
											+ ("false") + ", SCHEMA_COLUMN="
											+ ("po_no") + "}, {TRIM="
											+ ("false") + ", SCHEMA_COLUMN="
											+ ("sup_id") + "}, {TRIM="
											+ ("false") + ", SCHEMA_COLUMN="
											+ ("ship_to") + "}, {TRIM="
											+ ("false") + ", SCHEMA_COLUMN="
											+ ("bill_to") + "}, {TRIM="
											+ ("false") + ", SCHEMA_COLUMN="
											+ ("vendor_code") + "}, {TRIM="
											+ ("false") + ", SCHEMA_COLUMN="
											+ ("ord_date") + "}, {TRIM="
											+ ("false") + ", SCHEMA_COLUMN="
											+ ("ship_date") + "}, {TRIM="
											+ ("false") + ", SCHEMA_COLUMN="
											+ ("line_no") + "}, {TRIM="
											+ ("false") + ", SCHEMA_COLUMN="
											+ ("item_code") + "}, {TRIM="
											+ ("false") + ", SCHEMA_COLUMN="
											+ ("ord_qty") + "}]");
							log4jParamters_tFileInputExcel_1.append(" | ");
							log4jParamters_tFileInputExcel_1
									.append("CONVERTDATETOSTRING" + " = "
											+ "false");
							log4jParamters_tFileInputExcel_1.append(" | ");
							log4jParamters_tFileInputExcel_1.append("ENCODING"
									+ " = " + "\"ISO-8859-15\"");
							log4jParamters_tFileInputExcel_1.append(" | ");
							log4jParamters_tFileInputExcel_1
									.append("STOPREAD_ON_EMPTYROW" + " = "
											+ "false");
							log4jParamters_tFileInputExcel_1.append(" | ");
							log4jParamters_tFileInputExcel_1
									.append("GENERATION_MODE" + " = "
											+ "USER_MODE");
							log4jParamters_tFileInputExcel_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tFileInputExcel_1 - "
										+ (log4jParamters_tFileInputExcel_1));
						}
					}
					new BytesLimit65535_tFileInputExcel_1().limitLog4jByte();
				}

				class RegexUtil_tFileInputExcel_1 {

					public java.util.List<org.apache.poi.xssf.usermodel.XSSFSheet> getSheets(
							org.apache.poi.xssf.usermodel.XSSFWorkbook workbook,
							String oneSheetName, boolean useRegex) {

						java.util.List<org.apache.poi.xssf.usermodel.XSSFSheet> list = new java.util.ArrayList<org.apache.poi.xssf.usermodel.XSSFSheet>();

						if (useRegex) {// this part process the regex issue

							java.util.regex.Pattern pattern = java.util.regex.Pattern
									.compile(oneSheetName);
							for (org.apache.poi.ss.usermodel.Sheet sheet : workbook) {
								String sheetName = sheet.getSheetName();
								java.util.regex.Matcher matcher = pattern
										.matcher(sheetName);
								if (matcher.matches()) {
									if (sheet != null) {
										list.add((org.apache.poi.xssf.usermodel.XSSFSheet) sheet);
									}
								}
							}

						} else {
							org.apache.poi.xssf.usermodel.XSSFSheet sheet = (org.apache.poi.xssf.usermodel.XSSFSheet) workbook
									.getSheet(oneSheetName);
							if (sheet != null) {
								list.add(sheet);
							}

						}

						return list;
					}

					public java.util.List<org.apache.poi.xssf.usermodel.XSSFSheet> getSheets(
							org.apache.poi.xssf.usermodel.XSSFWorkbook workbook,
							int index, boolean useRegex) {
						java.util.List<org.apache.poi.xssf.usermodel.XSSFSheet> list = new java.util.ArrayList<org.apache.poi.xssf.usermodel.XSSFSheet>();
						org.apache.poi.xssf.usermodel.XSSFSheet sheet = (org.apache.poi.xssf.usermodel.XSSFSheet) workbook
								.getSheetAt(index);
						if (sheet != null) {
							list.add(sheet);
						}
						return list;
					}

				}
				RegexUtil_tFileInputExcel_1 regexUtil_tFileInputExcel_1 = new RegexUtil_tFileInputExcel_1();

				Object source_tFileInputExcel_1 = "C:/WorkSpace/talend/20181204182740_test_supplier1.xlsx";
				org.apache.poi.xssf.usermodel.XSSFWorkbook workbook_tFileInputExcel_1 = null;

				if (source_tFileInputExcel_1 instanceof String) {
					workbook_tFileInputExcel_1 = new org.apache.poi.xssf.usermodel.XSSFWorkbook(
							(String) source_tFileInputExcel_1);
				} else if (source_tFileInputExcel_1 instanceof java.io.InputStream) {
					workbook_tFileInputExcel_1 = new org.apache.poi.xssf.usermodel.XSSFWorkbook(
							(java.io.InputStream) source_tFileInputExcel_1);
				} else {
					workbook_tFileInputExcel_1 = null;
					throw new java.lang.Exception(
							"The data source should be specified as Inputstream or File Path!");
				}
				try {

					java.util.List<org.apache.poi.xssf.usermodel.XSSFSheet> sheetList_tFileInputExcel_1 = new java.util.ArrayList<org.apache.poi.xssf.usermodel.XSSFSheet>();
					for (org.apache.poi.ss.usermodel.Sheet sheet_tFileInputExcel_1 : workbook_tFileInputExcel_1) {
						sheetList_tFileInputExcel_1
								.add((org.apache.poi.xssf.usermodel.XSSFSheet) sheet_tFileInputExcel_1);
					}
					if (sheetList_tFileInputExcel_1.size() <= 0) {
						throw new RuntimeException("Special sheets not exist!");
					}

					java.util.List<org.apache.poi.xssf.usermodel.XSSFSheet> sheetList_FilterNull_tFileInputExcel_1 = new java.util.ArrayList<org.apache.poi.xssf.usermodel.XSSFSheet>();
					for (org.apache.poi.xssf.usermodel.XSSFSheet sheet_FilterNull_tFileInputExcel_1 : sheetList_tFileInputExcel_1) {
						if (sheet_FilterNull_tFileInputExcel_1 != null
								&& sheetList_FilterNull_tFileInputExcel_1
										.iterator() != null
								&& sheet_FilterNull_tFileInputExcel_1
										.iterator().hasNext()) {
							sheetList_FilterNull_tFileInputExcel_1
									.add(sheet_FilterNull_tFileInputExcel_1);
						}
					}
					sheetList_tFileInputExcel_1 = sheetList_FilterNull_tFileInputExcel_1;
					if (sheetList_tFileInputExcel_1.size() > 0) {
						int nb_line_tFileInputExcel_1 = 0;

						int begin_line_tFileInputExcel_1 = 1;

						int footer_input_tFileInputExcel_1 = 0;

						int end_line_tFileInputExcel_1 = 0;
						for (org.apache.poi.xssf.usermodel.XSSFSheet sheet_tFileInputExcel_1 : sheetList_tFileInputExcel_1) {
							end_line_tFileInputExcel_1 += (sheet_tFileInputExcel_1
									.getLastRowNum() + 1);
						}
						end_line_tFileInputExcel_1 -= footer_input_tFileInputExcel_1;
						int limit_tFileInputExcel_1 = -1;
						int start_column_tFileInputExcel_1 = 1 - 1;
						int end_column_tFileInputExcel_1 = -1;

						org.apache.poi.xssf.usermodel.XSSFRow row_tFileInputExcel_1 = null;
						org.apache.poi.xssf.usermodel.XSSFSheet sheet_tFileInputExcel_1 = sheetList_tFileInputExcel_1
								.get(0);
						int rowCount_tFileInputExcel_1 = 0;
						int sheetIndex_tFileInputExcel_1 = 0;
						int currentRows_tFileInputExcel_1 = (sheetList_tFileInputExcel_1
								.get(0).getLastRowNum() + 1);

						// for the number format
						java.text.DecimalFormat df_tFileInputExcel_1 = new java.text.DecimalFormat(
								"#.####################################");
						char decimalChar_tFileInputExcel_1 = df_tFileInputExcel_1
								.getDecimalFormatSymbols()
								.getDecimalSeparator();
						log.debug("tFileInputExcel_1 - Retrieving records from the datasource.");

						for (int i_tFileInputExcel_1 = begin_line_tFileInputExcel_1; i_tFileInputExcel_1 < end_line_tFileInputExcel_1; i_tFileInputExcel_1++) {

							int emptyColumnCount_tFileInputExcel_1 = 0;

							if (limit_tFileInputExcel_1 != -1
									&& nb_line_tFileInputExcel_1 >= limit_tFileInputExcel_1) {
								break;
							}

							while (i_tFileInputExcel_1 >= rowCount_tFileInputExcel_1
									+ currentRows_tFileInputExcel_1) {
								rowCount_tFileInputExcel_1 += currentRows_tFileInputExcel_1;
								sheet_tFileInputExcel_1 = sheetList_tFileInputExcel_1
										.get(++sheetIndex_tFileInputExcel_1);
								currentRows_tFileInputExcel_1 = (sheet_tFileInputExcel_1
										.getLastRowNum() + 1);
							}
							globalMap.put("tFileInputExcel_1_CURRENT_SHEET",
									sheet_tFileInputExcel_1.getSheetName());
							if (rowCount_tFileInputExcel_1 <= i_tFileInputExcel_1) {
								row_tFileInputExcel_1 = sheet_tFileInputExcel_1
										.getRow(i_tFileInputExcel_1
												- rowCount_tFileInputExcel_1);
							}
							row1 = null;
							int tempRowLength_tFileInputExcel_1 = 10;

							int columnIndex_tFileInputExcel_1 = 0;

							String[] temp_row_tFileInputExcel_1 = new String[tempRowLength_tFileInputExcel_1];
							int excel_end_column_tFileInputExcel_1;
							if (row_tFileInputExcel_1 == null) {
								excel_end_column_tFileInputExcel_1 = 0;
							} else {
								excel_end_column_tFileInputExcel_1 = row_tFileInputExcel_1
										.getLastCellNum();
							}
							int actual_end_column_tFileInputExcel_1;
							if (end_column_tFileInputExcel_1 == -1) {
								actual_end_column_tFileInputExcel_1 = excel_end_column_tFileInputExcel_1;
							} else {
								actual_end_column_tFileInputExcel_1 = end_column_tFileInputExcel_1 > excel_end_column_tFileInputExcel_1 ? excel_end_column_tFileInputExcel_1
										: end_column_tFileInputExcel_1;
							}
							org.apache.poi.ss.formula.eval.NumberEval ne_tFileInputExcel_1 = null;
							for (int i = 0; i < tempRowLength_tFileInputExcel_1; i++) {
								if (i + start_column_tFileInputExcel_1 < actual_end_column_tFileInputExcel_1) {
									org.apache.poi.ss.usermodel.Cell cell_tFileInputExcel_1 = row_tFileInputExcel_1
											.getCell(i
													+ start_column_tFileInputExcel_1);
									if (cell_tFileInputExcel_1 != null) {
										switch (cell_tFileInputExcel_1
												.getCellType()) {
										case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
											temp_row_tFileInputExcel_1[i] = cell_tFileInputExcel_1
													.getRichStringCellValue()
													.getString();
											break;
										case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
											if (org.apache.poi.ss.usermodel.DateUtil
													.isCellDateFormatted(cell_tFileInputExcel_1)) {
												temp_row_tFileInputExcel_1[i] = cell_tFileInputExcel_1
														.getDateCellValue()
														.toString();
											} else {
												temp_row_tFileInputExcel_1[i] = df_tFileInputExcel_1
														.format(cell_tFileInputExcel_1
																.getNumericCellValue());
											}
											break;
										case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN:
											temp_row_tFileInputExcel_1[i] = String
													.valueOf(cell_tFileInputExcel_1
															.getBooleanCellValue());
											break;
										case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA:
											switch (cell_tFileInputExcel_1
													.getCachedFormulaResultType()) {
											case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
												temp_row_tFileInputExcel_1[i] = cell_tFileInputExcel_1
														.getRichStringCellValue()
														.getString();
												break;
											case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
												if (org.apache.poi.ss.usermodel.DateUtil
														.isCellDateFormatted(cell_tFileInputExcel_1)) {
													temp_row_tFileInputExcel_1[i] = cell_tFileInputExcel_1
															.getDateCellValue()
															.toString();
												} else {
													ne_tFileInputExcel_1 = new org.apache.poi.ss.formula.eval.NumberEval(
															cell_tFileInputExcel_1
																	.getNumericCellValue());
													temp_row_tFileInputExcel_1[i] = ne_tFileInputExcel_1
															.getStringValue();
												}
												break;
											case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN:
												temp_row_tFileInputExcel_1[i] = String
														.valueOf(cell_tFileInputExcel_1
																.getBooleanCellValue());
												break;
											default:
												temp_row_tFileInputExcel_1[i] = "";
											}
											break;
										default:
											temp_row_tFileInputExcel_1[i] = "";
										}
									} else {
										temp_row_tFileInputExcel_1[i] = "";
									}

								} else {
									temp_row_tFileInputExcel_1[i] = "";
								}
							}
							boolean whetherReject_tFileInputExcel_1 = false;
							row1 = new row1Struct();
							int curColNum_tFileInputExcel_1 = -1;
							String curColName_tFileInputExcel_1 = "";
							try {
								columnIndex_tFileInputExcel_1 = 0;

								if (temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1]
										.length() > 0) {
									curColNum_tFileInputExcel_1 = columnIndex_tFileInputExcel_1
											+ start_column_tFileInputExcel_1
											+ 1;
									curColName_tFileInputExcel_1 = "po_no";

									row1.po_no = temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1];
								} else {
									row1.po_no = null;
									emptyColumnCount_tFileInputExcel_1++;
								}
								columnIndex_tFileInputExcel_1 = 1;

								if (temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1]
										.length() > 0) {
									curColNum_tFileInputExcel_1 = columnIndex_tFileInputExcel_1
											+ start_column_tFileInputExcel_1
											+ 1;
									curColName_tFileInputExcel_1 = "sup_id";

									row1.sup_id = ParserUtils
											.parseTo_Integer(ParserUtils
													.parseTo_Number(
															temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1],
															null,
															'.' == decimalChar_tFileInputExcel_1 ? null
																	: decimalChar_tFileInputExcel_1));
								} else {
									row1.sup_id = null;
									emptyColumnCount_tFileInputExcel_1++;
								}
								columnIndex_tFileInputExcel_1 = 2;

								if (temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1]
										.length() > 0) {
									curColNum_tFileInputExcel_1 = columnIndex_tFileInputExcel_1
											+ start_column_tFileInputExcel_1
											+ 1;
									curColName_tFileInputExcel_1 = "ship_to";

									row1.ship_to = temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1];
								} else {
									row1.ship_to = null;
									emptyColumnCount_tFileInputExcel_1++;
								}
								columnIndex_tFileInputExcel_1 = 3;

								if (temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1]
										.length() > 0) {
									curColNum_tFileInputExcel_1 = columnIndex_tFileInputExcel_1
											+ start_column_tFileInputExcel_1
											+ 1;
									curColName_tFileInputExcel_1 = "bill_to";

									row1.bill_to = temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1];
								} else {
									row1.bill_to = null;
									emptyColumnCount_tFileInputExcel_1++;
								}
								columnIndex_tFileInputExcel_1 = 4;

								if (temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1]
										.length() > 0) {
									curColNum_tFileInputExcel_1 = columnIndex_tFileInputExcel_1
											+ start_column_tFileInputExcel_1
											+ 1;
									curColName_tFileInputExcel_1 = "vendor_code";

									row1.vendor_code = temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1];
								} else {
									row1.vendor_code = null;
									emptyColumnCount_tFileInputExcel_1++;
								}
								columnIndex_tFileInputExcel_1 = 5;

								if (temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1]
										.length() > 0) {
									curColNum_tFileInputExcel_1 = columnIndex_tFileInputExcel_1
											+ start_column_tFileInputExcel_1
											+ 1;
									curColName_tFileInputExcel_1 = "ord_date";

									if (5 < actual_end_column_tFileInputExcel_1) {
										try {
											if (row_tFileInputExcel_1
													.getCell(
															columnIndex_tFileInputExcel_1
																	+ start_column_tFileInputExcel_1)
													.getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC
													&& org.apache.poi.ss.usermodel.DateUtil
															.isCellDateFormatted(row_tFileInputExcel_1
																	.getCell(columnIndex_tFileInputExcel_1
																			+ start_column_tFileInputExcel_1))) {
												row1.ord_date = row_tFileInputExcel_1
														.getCell(
																columnIndex_tFileInputExcel_1
																		+ start_column_tFileInputExcel_1)
														.getDateCellValue();
											} else {
												java.util.Date tempDate_tFileInputExcel_1 = ParserUtils
														.parseTo_Date(
																temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1],
																"dd-MM-yyyy");
												if (tempDate_tFileInputExcel_1
														.after((new SimpleDateFormat(
																"yyyy/MM/dd hh:mm:ss.SSS"))
																.parse("9999/12/31 23:59:59.999"))
														|| tempDate_tFileInputExcel_1
																.before((new SimpleDateFormat(
																		"yyyy/MM/dd"))
																		.parse("1900/01/01"))) {
													throw new RuntimeException(
															"The cell format is not Date in ( Row. "
																	+ (nb_line_tFileInputExcel_1 + 1)
																	+ " and ColumnNum. "
																	+ curColNum_tFileInputExcel_1
																	+ " )");
												} else {
													row1.ord_date = tempDate_tFileInputExcel_1;
												}
											}
										} catch (java.lang.Exception e) {

											throw new RuntimeException(
													"The cell format is not Date in ( Row. "
															+ (nb_line_tFileInputExcel_1 + 1)
															+ " and ColumnNum. "
															+ curColNum_tFileInputExcel_1
															+ " )");
										}
									}

								} else {
									row1.ord_date = null;
									emptyColumnCount_tFileInputExcel_1++;
								}
								columnIndex_tFileInputExcel_1 = 6;

								if (temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1]
										.length() > 0) {
									curColNum_tFileInputExcel_1 = columnIndex_tFileInputExcel_1
											+ start_column_tFileInputExcel_1
											+ 1;
									curColName_tFileInputExcel_1 = "ship_date";

									if (6 < actual_end_column_tFileInputExcel_1) {
										try {
											if (row_tFileInputExcel_1
													.getCell(
															columnIndex_tFileInputExcel_1
																	+ start_column_tFileInputExcel_1)
													.getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC
													&& org.apache.poi.ss.usermodel.DateUtil
															.isCellDateFormatted(row_tFileInputExcel_1
																	.getCell(columnIndex_tFileInputExcel_1
																			+ start_column_tFileInputExcel_1))) {
												row1.ship_date = row_tFileInputExcel_1
														.getCell(
																columnIndex_tFileInputExcel_1
																		+ start_column_tFileInputExcel_1)
														.getDateCellValue();
											} else {
												java.util.Date tempDate_tFileInputExcel_1 = ParserUtils
														.parseTo_Date(
																temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1],
																"dd-MM-yyyy");
												if (tempDate_tFileInputExcel_1
														.after((new SimpleDateFormat(
																"yyyy/MM/dd hh:mm:ss.SSS"))
																.parse("9999/12/31 23:59:59.999"))
														|| tempDate_tFileInputExcel_1
																.before((new SimpleDateFormat(
																		"yyyy/MM/dd"))
																		.parse("1900/01/01"))) {
													throw new RuntimeException(
															"The cell format is not Date in ( Row. "
																	+ (nb_line_tFileInputExcel_1 + 1)
																	+ " and ColumnNum. "
																	+ curColNum_tFileInputExcel_1
																	+ " )");
												} else {
													row1.ship_date = tempDate_tFileInputExcel_1;
												}
											}
										} catch (java.lang.Exception e) {

											throw new RuntimeException(
													"The cell format is not Date in ( Row. "
															+ (nb_line_tFileInputExcel_1 + 1)
															+ " and ColumnNum. "
															+ curColNum_tFileInputExcel_1
															+ " )");
										}
									}

								} else {
									row1.ship_date = null;
									emptyColumnCount_tFileInputExcel_1++;
								}
								columnIndex_tFileInputExcel_1 = 7;

								if (temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1]
										.length() > 0) {
									curColNum_tFileInputExcel_1 = columnIndex_tFileInputExcel_1
											+ start_column_tFileInputExcel_1
											+ 1;
									curColName_tFileInputExcel_1 = "line_no";

									row1.line_no = ParserUtils
											.parseTo_Integer(ParserUtils
													.parseTo_Number(
															temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1],
															null,
															'.' == decimalChar_tFileInputExcel_1 ? null
																	: decimalChar_tFileInputExcel_1));
								} else {
									row1.line_no = null;
									emptyColumnCount_tFileInputExcel_1++;
								}
								columnIndex_tFileInputExcel_1 = 8;

								if (temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1]
										.length() > 0) {
									curColNum_tFileInputExcel_1 = columnIndex_tFileInputExcel_1
											+ start_column_tFileInputExcel_1
											+ 1;
									curColName_tFileInputExcel_1 = "item_code";

									row1.item_code = temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1];
								} else {
									row1.item_code = null;
									emptyColumnCount_tFileInputExcel_1++;
								}
								columnIndex_tFileInputExcel_1 = 9;

								if (temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1]
										.length() > 0) {
									curColNum_tFileInputExcel_1 = columnIndex_tFileInputExcel_1
											+ start_column_tFileInputExcel_1
											+ 1;
									curColName_tFileInputExcel_1 = "ord_qty";

									row1.ord_qty = ParserUtils
											.parseTo_Integer(ParserUtils
													.parseTo_Number(
															temp_row_tFileInputExcel_1[columnIndex_tFileInputExcel_1],
															null,
															'.' == decimalChar_tFileInputExcel_1 ? null
																	: decimalChar_tFileInputExcel_1));
								} else {
									row1.ord_qty = null;
									emptyColumnCount_tFileInputExcel_1++;
								}

								nb_line_tFileInputExcel_1++;

								log.debug("tFileInputExcel_1 - Retrieving the record "
										+ (nb_line_tFileInputExcel_1) + ".");

							} catch (java.lang.Exception e) {
								whetherReject_tFileInputExcel_1 = true;
								log.error("tFileInputExcel_1 - "
										+ e.getMessage());

								System.err.println(e.getMessage());
								row1 = null;
							}

							/**
							 * [tFileInputExcel_1 begin ] stop
							 */

							/**
							 * [tFileInputExcel_1 main ] start
							 */

							currentComponent = "tFileInputExcel_1";

							tos_count_tFileInputExcel_1++;

							/**
							 * [tFileInputExcel_1 main ] stop
							 */

							/**
							 * [tFileInputExcel_1 process_data_begin ] start
							 */

							currentComponent = "tFileInputExcel_1";

							/**
							 * [tFileInputExcel_1 process_data_begin ] stop
							 */
							// Start of branch "row1"
							if (row1 != null) {

								/**
								 * [tHMap_1_THMAP_OUT main ] start
								 */

								currentVirtualComponent = "tHMap_1";

								currentComponent = "tHMap_1_THMAP_OUT";

								// row1
								// row1

								if (execStat) {
									runStat.updateStatOnConnection("row1"
											+ iterateId, 1, 1);
								}

								if (log.isTraceEnabled()) {
									log.trace("row1 - "
											+ (row1 == null ? "" : row1
													.toLogString()));
								}

								// THMAPOUT_MAIN thMap: tHMap_1_THMAP_OUT
								// Main job's main incomingConn: row1
								java.util.List<java.util.Map<String, Object>> list_row1_tHMap_1 = list_tHMap_1;
								java.util.Map<String, Object> map_row1_tHMap_1 = new java.util.HashMap<>();
								map_row1_tHMap_1.put("po_no", row1.po_no);
								map_row1_tHMap_1.put("sup_id", row1.sup_id);
								map_row1_tHMap_1.put("ship_to", row1.ship_to);
								map_row1_tHMap_1.put("bill_to", row1.bill_to);
								map_row1_tHMap_1.put("vendor_code",
										row1.vendor_code);
								map_row1_tHMap_1.put("ord_date", row1.ord_date);
								map_row1_tHMap_1.put("ship_date",
										row1.ship_date);
								map_row1_tHMap_1.put("line_no", row1.line_no);
								map_row1_tHMap_1.put("item_code",
										row1.item_code);
								map_row1_tHMap_1.put("ord_qty", row1.ord_qty);
								list_row1_tHMap_1.add(map_row1_tHMap_1);
								nb_line_tHMap_1++;

								tos_count_tHMap_1_THMAP_OUT++;

								/**
								 * [tHMap_1_THMAP_OUT main ] stop
								 */

								/**
								 * [tHMap_1_THMAP_OUT process_data_begin ] start
								 */

								currentVirtualComponent = "tHMap_1";

								currentComponent = "tHMap_1_THMAP_OUT";

								/**
								 * [tHMap_1_THMAP_OUT process_data_begin ] stop
								 */

								/**
								 * [tHMap_1_THMAP_OUT process_data_end ] start
								 */

								currentVirtualComponent = "tHMap_1";

								currentComponent = "tHMap_1_THMAP_OUT";

								/**
								 * [tHMap_1_THMAP_OUT process_data_end ] stop
								 */

							} // End of branch "row1"

							/**
							 * [tFileInputExcel_1 process_data_end ] start
							 */

							currentComponent = "tFileInputExcel_1";

							/**
							 * [tFileInputExcel_1 process_data_end ] stop
							 */

							/**
							 * [tFileInputExcel_1 end ] start
							 */

							currentComponent = "tFileInputExcel_1";

						}

						log.debug("tFileInputExcel_1 - Retrieved records count: "
								+ nb_line_tFileInputExcel_1 + " .");

						globalMap.put("tFileInputExcel_1_NB_LINE",
								nb_line_tFileInputExcel_1);

					}

				} finally {

					if (!(source_tFileInputExcel_1 instanceof java.io.InputStream)) {
						workbook_tFileInputExcel_1.getPackage().revert();
					}

				}

				if (log.isDebugEnabled())
					log.debug("tFileInputExcel_1 - " + ("Done."));

				ok_Hash.put("tFileInputExcel_1", true);
				end_Hash.put("tFileInputExcel_1", System.currentTimeMillis());

				/**
				 * [tFileInputExcel_1 end ] stop
				 */

				/**
				 * [tHMap_1_THMAP_OUT end ] start
				 */

				currentVirtualComponent = "tHMap_1";

				currentComponent = "tHMap_1_THMAP_OUT";

				if (execStat) {
					if (resourceMap.get("inIterateVComp") == null
							|| !((Boolean) resourceMap.get("inIterateVComp"))) {
						runStat.updateStatOnConnection("row1" + iterateId, 2, 0);
					}
				}

				ok_Hash.put("tHMap_1_THMAP_OUT", true);
				end_Hash.put("tHMap_1_THMAP_OUT", System.currentTimeMillis());

				/**
				 * [tHMap_1_THMAP_OUT end ] stop
				 */

				/**
				 * [tDBOutput_1 begin ] start
				 */

				ok_Hash.put("tDBOutput_1", false);
				start_Hash.put("tDBOutput_1", System.currentTimeMillis());

				currentComponent = "tDBOutput_1";

				if (execStat) {
					if (resourceMap.get("inIterateVComp") == null) {

						runStat.updateStatOnConnection(
								"finalinput" + iterateId, 0, 0);

					}
				}

				int tos_count_tDBOutput_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tDBOutput_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tDBOutput_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tDBOutput_1 = new StringBuilder();
							log4jParamters_tDBOutput_1.append("Parameters:");
							log4jParamters_tDBOutput_1
									.append("USE_EXISTING_CONNECTION" + " = "
											+ "false");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1.append("CONNECTION_TYPE"
									+ " = " + "ORACLE_SERVICE_NAME");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1.append("DB_VERSION"
									+ " = " + "ORACLE_12");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1.append("HOST" + " = "
									+ "\"129.213.58.214\"");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1.append("PORT" + " = "
									+ "\"1521\"");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1.append("DBNAME" + " = "
									+ "\"VISION\"");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1.append("TABLESCHEMA"
									+ " = " + "\"APPS\"");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1.append("USER" + " = "
									+ "\"APPS\"");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1.append("PASS"
									+ " = "
									+ String.valueOf(
											"43ae1c9f8db8943ef4f7aba1746784ea")
											.substring(0, 4) + "...");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1.append("TABLE" + " = "
									+ "\"xxmynag_po_detls\"");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1.append("TABLE_ACTION"
									+ " = " + "CREATE_IF_NOT_EXISTS");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1.append("DATA_ACTION"
									+ " = " + "INSERT");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1
									.append("SPECIFY_DATASOURCE_ALIAS" + " = "
											+ "false");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1.append("DIE_ON_ERROR"
									+ " = " + "false");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1.append("PROPERTIES"
									+ " = " + "\"\"");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1.append("COMMIT_EVERY"
									+ " = " + "10000");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1.append("ADD_COLS"
									+ " = " + "[]");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1
									.append("USE_FIELD_OPTIONS" + " = "
											+ "false");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1
									.append("USE_HINT_OPTIONS" + " = "
											+ "false");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1
									.append("CONVERT_COLUMN_TABLE_TO_UPPERCASE"
											+ " = " + "false");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1
									.append("ENABLE_DEBUG_MODE" + " = "
											+ "false");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1.append("USE_BATCH_SIZE"
									+ " = " + "true");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1.append("BATCH_SIZE"
									+ " = " + "10000");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1
									.append("SUPPORT_NULL_WHERE" + " = "
											+ "false");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1
									.append("USE_TIMESTAMP_FOR_DATE_TYPE"
											+ " = " + "true");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1.append("TRIM_CHAR"
									+ " = " + "true");
							log4jParamters_tDBOutput_1.append(" | ");
							log4jParamters_tDBOutput_1
									.append("UNIFIED_COMPONENTS" + " = "
											+ "tOracleOutput");
							log4jParamters_tDBOutput_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tDBOutput_1 - "
										+ (log4jParamters_tDBOutput_1));
						}
					}
					new BytesLimit65535_tDBOutput_1().limitLog4jByte();
				}

				int nb_line_tDBOutput_1 = 0;
				int nb_line_update_tDBOutput_1 = 0;
				int nb_line_inserted_tDBOutput_1 = 0;
				int nb_line_deleted_tDBOutput_1 = 0;
				int nb_line_rejected_tDBOutput_1 = 0;

				int tmp_batchUpdateCount_tDBOutput_1 = 0;

				int deletedCount_tDBOutput_1 = 0;
				int updatedCount_tDBOutput_1 = 0;
				int insertedCount_tDBOutput_1 = 0;
				int rejectedCount_tDBOutput_1 = 0;

				boolean whetherReject_tDBOutput_1 = false;

				java.sql.Connection conn_tDBOutput_1 = null;

				// optional table
				String dbschema_tDBOutput_1 = null;
				String tableName_tDBOutput_1 = null;
				String driverClass_tDBOutput_1 = "oracle.jdbc.OracleDriver";

				if (log.isDebugEnabled())
					log.debug("tDBOutput_1 - " + ("Driver ClassName: ")
							+ (driverClass_tDBOutput_1) + ("."));

				java.lang.Class.forName(driverClass_tDBOutput_1);
				String url_tDBOutput_1 = null;
				url_tDBOutput_1 = "jdbc:oracle:thin:@(description=(address=(protocol=tcp)(host="
						+ "129.213.58.214"
						+ ")(port="
						+ "1521"
						+ "))(connect_data=(service_name=" + "VISION" + ")))";
				String dbUser_tDBOutput_1 = "APPS";

				final String decryptedPassword_tDBOutput_1 = routines.system.PasswordEncryptUtil
						.decryptPassword("43ae1c9f8db8943ef4f7aba1746784ea");

				String dbPwd_tDBOutput_1 = decryptedPassword_tDBOutput_1;
				dbschema_tDBOutput_1 = "APPS";

				if (log.isDebugEnabled())
					log.debug("tDBOutput_1 - " + ("Connection attempts to '")
							+ (url_tDBOutput_1) + ("' with the username '")
							+ (dbUser_tDBOutput_1) + ("'."));

				conn_tDBOutput_1 = java.sql.DriverManager.getConnection(
						url_tDBOutput_1, dbUser_tDBOutput_1, dbPwd_tDBOutput_1);
				if (log.isDebugEnabled())
					log.debug("tDBOutput_1 - " + ("Connection to '")
							+ (url_tDBOutput_1) + ("' has succeeded."));
				resourceMap.put("conn_tDBOutput_1", conn_tDBOutput_1);
				conn_tDBOutput_1.setAutoCommit(false);
				int commitEvery_tDBOutput_1 = 10000;
				int commitCounter_tDBOutput_1 = 0;
				if (log.isDebugEnabled())
					log.debug("tDBOutput_1 - "
							+ ("Connection is set auto commit to '")
							+ (conn_tDBOutput_1.getAutoCommit()) + ("'."));
				int batchSize_tDBOutput_1 = 10000;
				int batchSizeCounter_tDBOutput_1 = 0;
				int count_tDBOutput_1 = 0;

				if (dbschema_tDBOutput_1 == null
						|| dbschema_tDBOutput_1.trim().length() == 0) {
					tableName_tDBOutput_1 = ("xxmynag_po_detls");
				} else {
					tableName_tDBOutput_1 = dbschema_tDBOutput_1 + "."
							+ ("xxmynag_po_detls");
				}
				String tableNameForSearch_tDBOutput_1 = ""
						+ ((String) "xxmynag_po_detls") + "";
				String dbschemaForSearch_tDBOutput_1 = null;
				if (dbschema_tDBOutput_1 == null
						|| dbschema_tDBOutput_1.trim().length() == 0) {
					dbschemaForSearch_tDBOutput_1 = ((String) "APPS")
							.toUpperCase();
				} else {
					dbschemaForSearch_tDBOutput_1 = dbschema_tDBOutput_1
							.toUpperCase();
				}

				java.sql.DatabaseMetaData dbMetaData_tDBOutput_1 = conn_tDBOutput_1
						.getMetaData();
				if (tableNameForSearch_tDBOutput_1.indexOf("\"") == -1) {
					tableNameForSearch_tDBOutput_1 = tableNameForSearch_tDBOutput_1
							.toUpperCase();
				} else {
					tableNameForSearch_tDBOutput_1 = tableNameForSearch_tDBOutput_1
							.replaceAll("\"", "");
				}
				boolean whetherExist_tDBOutput_1 = false;
				try (java.sql.ResultSet rsTable_tDBOutput_1 = dbMetaData_tDBOutput_1
						.getTables(null, dbschemaForSearch_tDBOutput_1,
								tableNameForSearch_tDBOutput_1,
								new String[] { "TABLE" })) {
					if (rsTable_tDBOutput_1.next()) {
						whetherExist_tDBOutput_1 = true;
					}
				}

				if (!whetherExist_tDBOutput_1) {
					try (java.sql.Statement stmtCreate_tDBOutput_1 = conn_tDBOutput_1
							.createStatement()) {
						if (log.isDebugEnabled())
							log.debug("tDBOutput_1 - " + ("Creating")
									+ (" table '") + (tableName_tDBOutput_1)
									+ ("'."));
						stmtCreate_tDBOutput_1
								.execute("CREATE TABLE "
										+ tableName_tDBOutput_1
										+ "(po_number VARCHAR2(4)  ,sup_id INT ,ship_to VARCHAR2(4)  ,bill_to VARCHAR2(4)  ,vendor VARCHAR2(4)  ,ord_date DATE ,ship_date DATE ,line_no INT ,item_code VARCHAR2(4)  ,ord_qty INT ,primary key(po_number))");
						if (log.isDebugEnabled())
							log.debug("tDBOutput_1 - " + ("Create")
									+ (" table '") + (tableName_tDBOutput_1)
									+ ("' has succeeded."));
					}
				}
				String insert_tDBOutput_1 = "INSERT INTO "
						+ tableName_tDBOutput_1
						+ " (po_number,sup_id,ship_to,bill_to,vendor,ord_date,ship_date,line_no,item_code,ord_qty) VALUES (?,?,?,?,?,?,?,?,?,?)";

				java.sql.PreparedStatement pstmt_tDBOutput_1 = conn_tDBOutput_1
						.prepareStatement(insert_tDBOutput_1);
				resourceMap.put("pstmt_tDBOutput_1", pstmt_tDBOutput_1);

				/**
				 * [tDBOutput_1 begin ] stop
				 */

				/**
				 * [tHMap_1_THMAP_IN begin ] start
				 */

				ok_Hash.put("tHMap_1_THMAP_IN", false);
				start_Hash.put("tHMap_1_THMAP_IN", System.currentTimeMillis());

				currentVirtualComponent = "tHMap_1";

				currentComponent = "tHMap_1_THMAP_IN";

				int tos_count_tHMap_1_THMAP_IN = 0;

				// THMAPIN_BEGIN tHMap: tHMap_1_THMAP_IN
				String executorId_tHMap_1 = Thread.currentThread().getId()
						+ "_tHMap_1_" + "mapExecutor";
				org.talend.transform.runtime.common.MapExecutor mapExec_tHMap_1 = (org.talend.transform.runtime.common.MapExecutor) dataMigration.this.globalMap
						.get(executorId_tHMap_1);
				if (mapExec_tHMap_1 == null) {
					mapExec_tHMap_1 = org.talend.transform.runtime.common.MapExecutorFactory
							.get();
					dataMigration.this.globalMap.put(executorId_tHMap_1,
							mapExec_tHMap_1);

					mapExec_tHMap_1.setLoggingLevel("infrequent");
					mapExec_tHMap_1
							.setExceptionThreshold(org.talend.transform.runtime.common.SeverityLevel.HIGHEST);
				}

				java.util.Map<String, Object> ecProps_tHMap_1 = new java.util.HashMap<String, Object>();
				context.synchronizeContext();

				java.util.Enumeration<?> propertyNames_tHMap_1 = context
						.propertyNames();
				while (propertyNames_tHMap_1.hasMoreElements()) {
					String key_tHMap_1 = (String) propertyNames_tHMap_1
							.nextElement();
					Object value_tHMap_1 = (Object) context.get(key_tHMap_1);
					ecProps_tHMap_1
							.put("context." + key_tHMap_1, value_tHMap_1);
				}
				mapExec_tHMap_1.setExecutionProperties("tHMap_1",
						ecProps_tHMap_1);

				mapExec_tHMap_1.setUp("tHMap_1",
						new org.talend.transform.runtime.common.MapLocation(
								"TEST", "Jobs/dataMigration/tHMap_1"));
				java.util.Map<String, javax.xml.transform.Source> sources_tHMap_1 = new java.util.LinkedHashMap<>();
				// Setting one source only
				mapExec_tHMap_1.setObjectSource("tHMap_1", list_tHMap_1);

				javax.xml.transform.Result output_tHMap_1 = null;
				// Result for thMap Connection "finalinput" : ObjectResult
				java.util.List<java.util.Map<String, Object>> outList_tHMap_1_finalinput = new java.util.ArrayList<>();
				org.talend.transform.io.ObjectResult result_tHMap_1_finalinput = new org.talend.transform.io.ObjectResult(
						outList_tHMap_1_finalinput);
				// Set the single result
				output_tHMap_1 = result_tHMap_1_finalinput;
				mapExec_tHMap_1.setResult("tHMap_1", output_tHMap_1);
				dataMigration.this.globalMap.put(Thread.currentThread().getId()
						+ "_tHMap_1_" + "outputResult", output_tHMap_1);

				org.talend.transform.runtime.common.MapExecutionStatus results_tHMap_1 = mapExec_tHMap_1
						.execute("tHMap_1");
				mapExec_tHMap_1.freeExecutionResources("tHMap_1");
				dataMigration.this.globalMap.put("tHMap_1_"
						+ "EXECUTION_STATUS", results_tHMap_1);
				dataMigration.this.globalMap.put("tHMap_1_"
						+ "EXECUTION_SEVERITY", results_tHMap_1
						.getHighestSeverityLevel().getNumValue());
				if (results_tHMap_1.getHighestSeverityLevel()
						.isGreaterOrEqualsTo(64)) {
					throw new TalendException(new java.lang.Exception(
							String.valueOf(results_tHMap_1)), currentComponent,
							globalMap);
				}

				if (results_tHMap_1.getHighestSeverityLevel().isGreaterThan(
						org.talend.transform.runtime.common.SeverityLevel.INFO)) {
					System.err.println(results_tHMap_1);
				}

				/**
				 * [tHMap_1_THMAP_IN begin ] stop
				 */

				/**
				 * [tHMap_1_THMAP_IN main ] start
				 */

				currentVirtualComponent = "tHMap_1";

				currentComponent = "tHMap_1_THMAP_IN";

				// THMAPIN_MAIN tHMap: tHMap_1
				java.util.List<java.lang.Object> rows_tHMap_1 = new java.util.ArrayList<>();
				// Result for single col connection: finalinput
				javax.xml.transform.Result execResult_tHMap_1_finalinput = (javax.xml.transform.Result) dataMigration.this.globalMap
						.get(Thread.currentThread().getId() + "_tHMap_1_"
								+ "outputResult");
				// Connection finalinput with object result type
				org.talend.transform.io.ObjectResult objectResult_tHMap_1_finalinput = (org.talend.transform.io.ObjectResult) execResult_tHMap_1_finalinput;
				java.util.List<java.util.Map<String, Object>> outputList_tHMap_1_finalinput = (java.util.List) objectResult_tHMap_1_finalinput
						.getObject();
				if (outputList_tHMap_1_finalinput != null) {
					for (java.util.Map<String, Object> outMap_finalinput : outputList_tHMap_1_finalinput) {
						finalinput = new finalinputStruct();
						// Output as direct map column: finalinput.po_number
						if (outMap_finalinput.get("po_number") == null) {
							finalinput.po_number = null;
						} else if (outMap_finalinput.get("po_number") instanceof String) {
							finalinput.po_number = (String) outMap_finalinput
									.get("po_number");
						}
						// Output as direct map column: finalinput.sup_id
						if (outMap_finalinput.get("sup_id") == null) {
							finalinput.sup_id = 0;
						} else if (outMap_finalinput.get("sup_id") instanceof Integer) {
							finalinput.sup_id = ((Integer) outMap_finalinput
									.get("sup_id")).intValue();
						}
						// Output as direct map column: finalinput.ship_to
						if (outMap_finalinput.get("ship_to") == null) {
							finalinput.ship_to = null;
						} else if (outMap_finalinput.get("ship_to") instanceof String) {
							finalinput.ship_to = (String) outMap_finalinput
									.get("ship_to");
						}
						// Output as direct map column: finalinput.bill_to
						if (outMap_finalinput.get("bill_to") == null) {
							finalinput.bill_to = null;
						} else if (outMap_finalinput.get("bill_to") instanceof String) {
							finalinput.bill_to = (String) outMap_finalinput
									.get("bill_to");
						}
						// Output as direct map column: finalinput.vendor
						if (outMap_finalinput.get("vendor") == null) {
							finalinput.vendor = null;
						} else if (outMap_finalinput.get("vendor") instanceof String) {
							finalinput.vendor = (String) outMap_finalinput
									.get("vendor");
						}
						// Output as direct map column: finalinput.ord_date
						if (outMap_finalinput.get("ord_date") == null) {
							finalinput.ord_date = null;
						} else if (outMap_finalinput.get("ord_date") instanceof java.util.Date) {
							finalinput.ord_date = (java.util.Date) outMap_finalinput
									.get("ord_date");
						}
						// Output as direct map column: finalinput.ship_date
						if (outMap_finalinput.get("ship_date") == null) {
							finalinput.ship_date = null;
						} else if (outMap_finalinput.get("ship_date") instanceof java.util.Date) {
							finalinput.ship_date = (java.util.Date) outMap_finalinput
									.get("ship_date");
						}
						// Output as direct map column: finalinput.line_no
						if (outMap_finalinput.get("line_no") == null) {
							finalinput.line_no = 0;
						} else if (outMap_finalinput.get("line_no") instanceof Integer) {
							finalinput.line_no = ((Integer) outMap_finalinput
									.get("line_no")).intValue();
						}
						// Output as direct map column: finalinput.item_code
						if (outMap_finalinput.get("item_code") == null) {
							finalinput.item_code = null;
						} else if (outMap_finalinput.get("item_code") instanceof String) {
							finalinput.item_code = (String) outMap_finalinput
									.get("item_code");
						}
						// Output as direct map column: finalinput.ord_qty
						if (outMap_finalinput.get("ord_qty") == null) {
							finalinput.ord_qty = 0;
						} else if (outMap_finalinput.get("ord_qty") instanceof Integer) {
							finalinput.ord_qty = ((Integer) outMap_finalinput
									.get("ord_qty")).intValue();
						}
						rows_tHMap_1.add(finalinput);
					} // for (java.util.Map
				} // if (outputList_

				for (java.lang.Object row_tHMap_1 : rows_tHMap_1) {
					finalinput = null;
					if (row_tHMap_1 instanceof finalinputStruct) {
						finalinput = (finalinputStruct) row_tHMap_1;
					}

					tos_count_tHMap_1_THMAP_IN++;

					/**
					 * [tHMap_1_THMAP_IN main ] stop
					 */

					/**
					 * [tHMap_1_THMAP_IN process_data_begin ] start
					 */

					currentVirtualComponent = "tHMap_1";

					currentComponent = "tHMap_1_THMAP_IN";

					/**
					 * [tHMap_1_THMAP_IN process_data_begin ] stop
					 */
					// Start of branch "finalinput"
					if (finalinput != null) {

						/**
						 * [tDBOutput_1 main ] start
						 */

						currentComponent = "tDBOutput_1";

						// finalinput
						// finalinput

						if (execStat) {
							runStat.updateStatOnConnection("finalinput"
									+ iterateId, 1, 1);
						}

						if (log.isTraceEnabled()) {
							log.trace("finalinput - "
									+ (finalinput == null ? "" : finalinput
											.toLogString()));
						}

						whetherReject_tDBOutput_1 = false;
						if (finalinput.po_number == null) {
							pstmt_tDBOutput_1
									.setNull(1, java.sql.Types.VARCHAR);
						} else {
							pstmt_tDBOutput_1
									.setString(1, finalinput.po_number);
						}

						if (finalinput.sup_id == null) {
							pstmt_tDBOutput_1
									.setNull(2, java.sql.Types.INTEGER);
						} else {
							pstmt_tDBOutput_1.setInt(2, finalinput.sup_id);
						}

						if (finalinput.ship_to == null) {
							pstmt_tDBOutput_1
									.setNull(3, java.sql.Types.VARCHAR);
						} else {
							pstmt_tDBOutput_1.setString(3, finalinput.ship_to);
						}

						if (finalinput.bill_to == null) {
							pstmt_tDBOutput_1
									.setNull(4, java.sql.Types.VARCHAR);
						} else {
							pstmt_tDBOutput_1.setString(4, finalinput.bill_to);
						}

						if (finalinput.vendor == null) {
							pstmt_tDBOutput_1
									.setNull(5, java.sql.Types.VARCHAR);
						} else {
							pstmt_tDBOutput_1.setString(5, finalinput.vendor);
						}

						if (finalinput.ord_date != null) {
							pstmt_tDBOutput_1.setObject(
									6,
									new java.sql.Timestamp(finalinput.ord_date
											.getTime()), java.sql.Types.DATE);
						} else {
							pstmt_tDBOutput_1.setNull(6, java.sql.Types.DATE);
						}

						if (finalinput.ship_date != null) {
							pstmt_tDBOutput_1.setObject(
									7,
									new java.sql.Timestamp(finalinput.ship_date
											.getTime()), java.sql.Types.DATE);
						} else {
							pstmt_tDBOutput_1.setNull(7, java.sql.Types.DATE);
						}

						if (finalinput.line_no == null) {
							pstmt_tDBOutput_1
									.setNull(8, java.sql.Types.INTEGER);
						} else {
							pstmt_tDBOutput_1.setInt(8, finalinput.line_no);
						}

						if (finalinput.item_code == null) {
							pstmt_tDBOutput_1
									.setNull(9, java.sql.Types.VARCHAR);
						} else {
							pstmt_tDBOutput_1
									.setString(9, finalinput.item_code);
						}

						if (finalinput.ord_qty == null) {
							pstmt_tDBOutput_1.setNull(10,
									java.sql.Types.INTEGER);
						} else {
							pstmt_tDBOutput_1.setInt(10, finalinput.ord_qty);
						}

						pstmt_tDBOutput_1.addBatch();
						nb_line_tDBOutput_1++;
						if (log.isDebugEnabled())
							log.debug("tDBOutput_1 - " + ("Adding the record ")
									+ (nb_line_tDBOutput_1) + (" to the ")
									+ ("INSERT") + (" batch."));
						batchSizeCounter_tDBOutput_1++;
						if (batchSize_tDBOutput_1 > 0
								&& batchSize_tDBOutput_1 <= batchSizeCounter_tDBOutput_1) {
							try {
								if (log.isDebugEnabled())
									log.debug("tDBOutput_1 - "
											+ ("Executing the ") + ("INSERT")
											+ (" batch."));
								pstmt_tDBOutput_1.executeBatch();
								if (log.isDebugEnabled())
									log.debug("tDBOutput_1 - "
											+ ("The ")
											+ ("INSERT")
											+ (" batch execution has succeeded."));
							} catch (java.sql.BatchUpdateException e_tDBOutput_1) {
								java.sql.SQLException ne_tDBOutput_1 = e_tDBOutput_1
										.getNextException(), sqle_tDBOutput_1 = null;
								String errormessage_tDBOutput_1;
								if (ne_tDBOutput_1 != null) {
									// build new exception to provide the
									// original cause
									sqle_tDBOutput_1 = new java.sql.SQLException(
											e_tDBOutput_1.getMessage()
													+ "\ncaused by: "
													+ ne_tDBOutput_1
															.getMessage(),
											ne_tDBOutput_1.getSQLState(),
											ne_tDBOutput_1.getErrorCode(),
											ne_tDBOutput_1);
									errormessage_tDBOutput_1 = sqle_tDBOutput_1
											.getMessage();
								} else {
									errormessage_tDBOutput_1 = e_tDBOutput_1
											.getMessage();
								}

								log.error("tDBOutput_1 - "
										+ (errormessage_tDBOutput_1));
								System.err.println(errormessage_tDBOutput_1);

							}
							tmp_batchUpdateCount_tDBOutput_1 = pstmt_tDBOutput_1
									.getUpdateCount();
							insertedCount_tDBOutput_1 += (tmp_batchUpdateCount_tDBOutput_1 != -1 ? tmp_batchUpdateCount_tDBOutput_1
									: 0);
							batchSizeCounter_tDBOutput_1 = 0;
						}
						commitCounter_tDBOutput_1++;
						if (commitEvery_tDBOutput_1 <= commitCounter_tDBOutput_1) {

							try {
								if (log.isDebugEnabled())
									log.debug("tDBOutput_1 - "
											+ ("Executing the ") + ("INSERT")
											+ (" batch."));
								pstmt_tDBOutput_1.executeBatch();
								if (log.isDebugEnabled())
									log.debug("tDBOutput_1 - "
											+ ("The ")
											+ ("INSERT")
											+ (" batch execution has succeeded."));
							} catch (java.sql.BatchUpdateException e_tDBOutput_1) {
								java.sql.SQLException ne_tDBOutput_1 = e_tDBOutput_1
										.getNextException(), sqle_tDBOutput_1 = null;
								String errormessage_tDBOutput_1;
								if (ne_tDBOutput_1 != null) {
									// build new exception to provide the
									// original cause
									sqle_tDBOutput_1 = new java.sql.SQLException(
											e_tDBOutput_1.getMessage()
													+ "\ncaused by: "
													+ ne_tDBOutput_1
															.getMessage(),
											ne_tDBOutput_1.getSQLState(),
											ne_tDBOutput_1.getErrorCode(),
											ne_tDBOutput_1);
									errormessage_tDBOutput_1 = sqle_tDBOutput_1
											.getMessage();
								} else {
									errormessage_tDBOutput_1 = e_tDBOutput_1
											.getMessage();
								}

								log.error("tDBOutput_1 - "
										+ (errormessage_tDBOutput_1));
								System.err.println(errormessage_tDBOutput_1);

							}
							tmp_batchUpdateCount_tDBOutput_1 = pstmt_tDBOutput_1
									.getUpdateCount();
							insertedCount_tDBOutput_1 += (tmp_batchUpdateCount_tDBOutput_1 != -1 ? tmp_batchUpdateCount_tDBOutput_1
									: 0);
							if (log.isDebugEnabled())
								log.debug("tDBOutput_1 - "
										+ ("Connection starting to commit ")
										+ (commitCounter_tDBOutput_1)
										+ (" record(s)."));
							conn_tDBOutput_1.commit();
							if (log.isDebugEnabled())
								log.debug("tDBOutput_1 - "
										+ ("Connection commit has succeeded."));
							commitCounter_tDBOutput_1 = 0;
						}

						tos_count_tDBOutput_1++;

						/**
						 * [tDBOutput_1 main ] stop
						 */

						/**
						 * [tDBOutput_1 process_data_begin ] start
						 */

						currentComponent = "tDBOutput_1";

						/**
						 * [tDBOutput_1 process_data_begin ] stop
						 */

						/**
						 * [tDBOutput_1 process_data_end ] start
						 */

						currentComponent = "tDBOutput_1";

						/**
						 * [tDBOutput_1 process_data_end ] stop
						 */

					} // End of branch "finalinput"

					/**
					 * [tHMap_1_THMAP_IN process_data_end ] start
					 */

					currentVirtualComponent = "tHMap_1";

					currentComponent = "tHMap_1_THMAP_IN";

					/**
					 * [tHMap_1_THMAP_IN process_data_end ] stop
					 */

					/**
					 * [tHMap_1_THMAP_IN end ] start
					 */

					currentVirtualComponent = "tHMap_1";

					currentComponent = "tHMap_1_THMAP_IN";

					// THMAPIN_END thMap: tHMap_1_THMAP_IN
				} // end for (rows_)
				globalMap.put("tHMap_1_NB_LINE", nb_line_tHMap_1);

				ok_Hash.put("tHMap_1_THMAP_IN", true);
				end_Hash.put("tHMap_1_THMAP_IN", System.currentTimeMillis());

				/**
				 * [tHMap_1_THMAP_IN end ] stop
				 */

				/**
				 * [tDBOutput_1 end ] start
				 */

				currentComponent = "tDBOutput_1";

				try {
					if (pstmt_tDBOutput_1 != null) {

						if (log.isDebugEnabled())
							log.debug("tDBOutput_1 - " + ("Executing the ")
									+ ("INSERT") + (" batch."));
						pstmt_tDBOutput_1.executeBatch();

						if (log.isDebugEnabled())
							log.debug("tDBOutput_1 - " + ("The ") + ("INSERT")
									+ (" batch execution has succeeded."));
					}
				} catch (java.sql.BatchUpdateException e_tDBOutput_1) {
					java.sql.SQLException ne_tDBOutput_1 = e_tDBOutput_1
							.getNextException(), sqle_tDBOutput_1 = null;
					String errormessage_tDBOutput_1;
					if (ne_tDBOutput_1 != null) {
						// build new exception to provide the original cause
						sqle_tDBOutput_1 = new java.sql.SQLException(
								e_tDBOutput_1.getMessage() + "\ncaused by: "
										+ ne_tDBOutput_1.getMessage(),
								ne_tDBOutput_1.getSQLState(),
								ne_tDBOutput_1.getErrorCode(), ne_tDBOutput_1);
						errormessage_tDBOutput_1 = sqle_tDBOutput_1
								.getMessage();
					} else {
						errormessage_tDBOutput_1 = e_tDBOutput_1.getMessage();
					}

					log.error("tDBOutput_1 - " + (errormessage_tDBOutput_1));
					System.err.println(errormessage_tDBOutput_1);

				}
				if (pstmt_tDBOutput_1 != null) {
					tmp_batchUpdateCount_tDBOutput_1 = pstmt_tDBOutput_1
							.getUpdateCount();

					insertedCount_tDBOutput_1

					+= (tmp_batchUpdateCount_tDBOutput_1 != -1 ? tmp_batchUpdateCount_tDBOutput_1
							: 0);
				}
				if (pstmt_tDBOutput_1 != null) {

					pstmt_tDBOutput_1.close();
					resourceMap.remove("pstmt_tDBOutput_1");

				}
				resourceMap.put("statementClosed_tDBOutput_1", true);
				if (commitCounter_tDBOutput_1 > 0) {

					if (log.isDebugEnabled())
						log.debug("tDBOutput_1 - "
								+ ("Connection starting to commit ")
								+ (commitCounter_tDBOutput_1) + (" record(s)."));
					conn_tDBOutput_1.commit();

					if (log.isDebugEnabled())
						log.debug("tDBOutput_1 - "
								+ ("Connection commit has succeeded."));
				}

				if (log.isDebugEnabled())
					log.debug("tDBOutput_1 - "
							+ ("Closing the connection to the database."));
				conn_tDBOutput_1.close();

				if (log.isDebugEnabled())
					log.debug("tDBOutput_1 - "
							+ ("Connection to the database has closed."));
				resourceMap.put("finish_tDBOutput_1", true);

				nb_line_deleted_tDBOutput_1 = nb_line_deleted_tDBOutput_1
						+ deletedCount_tDBOutput_1;
				nb_line_update_tDBOutput_1 = nb_line_update_tDBOutput_1
						+ updatedCount_tDBOutput_1;
				nb_line_inserted_tDBOutput_1 = nb_line_inserted_tDBOutput_1
						+ insertedCount_tDBOutput_1;
				nb_line_rejected_tDBOutput_1 = nb_line_rejected_tDBOutput_1
						+ rejectedCount_tDBOutput_1;

				globalMap.put("tDBOutput_1_NB_LINE", nb_line_tDBOutput_1);
				globalMap.put("tDBOutput_1_NB_LINE_UPDATED",
						nb_line_update_tDBOutput_1);
				globalMap.put("tDBOutput_1_NB_LINE_INSERTED",
						nb_line_inserted_tDBOutput_1);
				globalMap.put("tDBOutput_1_NB_LINE_DELETED",
						nb_line_deleted_tDBOutput_1);
				globalMap.put("tDBOutput_1_NB_LINE_REJECTED",
						nb_line_rejected_tDBOutput_1);

				if (log.isDebugEnabled())
					log.debug("tDBOutput_1 - " + ("Has ") + ("inserted")
							+ (" ") + (nb_line_inserted_tDBOutput_1)
							+ (" record(s)."));

				if (execStat) {
					if (resourceMap.get("inIterateVComp") == null
							|| !((Boolean) resourceMap.get("inIterateVComp"))) {
						runStat.updateStatOnConnection(
								"finalinput" + iterateId, 2, 0);
					}
				}

				if (log.isDebugEnabled())
					log.debug("tDBOutput_1 - " + ("Done."));

				ok_Hash.put("tDBOutput_1", true);
				end_Hash.put("tDBOutput_1", System.currentTimeMillis());

				/**
				 * [tDBOutput_1 end ] stop
				 */

			}// end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent,
					globalMap);

			te.setVirtualComponentName(currentVirtualComponent);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tFileInputExcel_1 finally ] start
				 */

				currentComponent = "tFileInputExcel_1";

				/**
				 * [tFileInputExcel_1 finally ] stop
				 */

				/**
				 * [tHMap_1_THMAP_OUT finally ] start
				 */

				currentVirtualComponent = "tHMap_1";

				currentComponent = "tHMap_1_THMAP_OUT";

				/**
				 * [tHMap_1_THMAP_OUT finally ] stop
				 */

				/**
				 * [tHMap_1_THMAP_IN finally ] start
				 */

				currentVirtualComponent = "tHMap_1";

				currentComponent = "tHMap_1_THMAP_IN";

				/**
				 * [tHMap_1_THMAP_IN finally ] stop
				 */

				/**
				 * [tDBOutput_1 finally ] start
				 */

				currentComponent = "tDBOutput_1";

				try {
					if (resourceMap.get("statementClosed_tDBOutput_1") == null) {
						java.sql.PreparedStatement pstmtToClose_tDBOutput_1 = null;
						if ((pstmtToClose_tDBOutput_1 = (java.sql.PreparedStatement) resourceMap
								.remove("pstmt_tDBOutput_1")) != null) {
							pstmtToClose_tDBOutput_1.close();
						}
					}
				} finally {
					if (resourceMap.get("finish_tDBOutput_1") == null) {
						java.sql.Connection ctn_tDBOutput_1 = null;
						if ((ctn_tDBOutput_1 = (java.sql.Connection) resourceMap
								.get("conn_tDBOutput_1")) != null) {
							try {
								if (log.isDebugEnabled())
									log.debug("tDBOutput_1 - "
											+ ("Closing the connection to the database."));
								ctn_tDBOutput_1.close();
								if (log.isDebugEnabled())
									log.debug("tDBOutput_1 - "
											+ ("Connection to the database has closed."));
							} catch (java.sql.SQLException sqlEx_tDBOutput_1) {
								String errorMessage_tDBOutput_1 = "failed to close the connection in tDBOutput_1 :"
										+ sqlEx_tDBOutput_1.getMessage();
								log.error("tDBOutput_1 - "
										+ (errorMessage_tDBOutput_1));
								System.err.println(errorMessage_tDBOutput_1);
							}
						}
					}
				}

				/**
				 * [tDBOutput_1 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
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
			java.util.Map<String, String> threadRunResultMap = new java.util.HashMap<String, String>();
			threadRunResultMap.put("errorCode", null);
			threadRunResultMap.put("status", "");
			return threadRunResultMap;
		};
	};

	private PropertiesWithType context_param = new PropertiesWithType();
	public java.util.Map<String, Object> parentContextMap = new java.util.HashMap<String, Object>();

	public String status = "";

	public static void main(String[] args) {
		final dataMigration dataMigrationClass = new dataMigration();

		int exitCode = dataMigrationClass.runJobInTOS(args);
		if (exitCode == 0) {
			log.info("TalendJob: 'dataMigration' - Done.");
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

		if (!"".equals(log4jLevel)) {
			if ("trace".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.TRACE);
			} else if ("debug".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.DEBUG);
			} else if ("info".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.INFO);
			} else if ("warn".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.WARN);
			} else if ("error".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.ERROR);
			} else if ("fatal".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.FATAL);
			} else if ("off".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.OFF);
			}
			org.apache.log4j.Logger.getRootLogger().setLevel(log.getLevel());
		}
		log.info("TalendJob: 'dataMigration' - Start.");

		if (clientHost == null) {
			clientHost = defaultClientHost;
		}

		if (pid == null || "0".equals(pid)) {
			pid = TalendString.getAsciiRandomString(6);
		}

		if (rootPid == null) {
			rootPid = pid;
		}
		if (fatherPid == null) {
			fatherPid = pid;
		} else {
			isChildJob = true;
		}

		if (portStats != null) {
			// portStats = -1; //for testing
			if (portStats < 0 || portStats > 65535) {
				// issue:10869, the portStats is invalid, so this client socket
				// can't open
				System.err.println("The statistics socket port " + portStats
						+ " is invalid.");
				execStat = false;
			}
		} else {
			execStat = false;
		}

		try {
			// call job/subjob with an existing context, like:
			// --context=production. if without this parameter, there will use
			// the default context instead.
			java.io.InputStream inContext = dataMigration.class
					.getClassLoader().getResourceAsStream(
							"test/datamigration_0_1/contexts/" + contextStr
									+ ".properties");
			if (inContext == null) {
				inContext = dataMigration.class
						.getClassLoader()
						.getResourceAsStream(
								"config/contexts/" + contextStr + ".properties");
			}
			if (inContext != null && context != null && context.isEmpty()) {
				// defaultProps is in order to keep the original context value
				defaultProps.load(inContext);
				inContext.close();
				context = new ContextProperties(defaultProps);
			} else if (!isDefaultContext) {
				// print info and job continue to run, for case: context_param
				// is not empty.
				System.err.println("Could not find the context " + contextStr);
			}

			if (!context_param.isEmpty()) {
				context.putAll(context_param);
				// set types for params from parentJobs
				for (Object key : context_param.keySet()) {
					String context_key = key.toString();
					String context_type = context_param
							.getContextType(context_key);
					context.setContextType(context_key, context_type);

				}
			}
		} catch (java.io.IOException ie) {
			System.err.println("Could not load context " + contextStr);
			ie.printStackTrace();
		}

		// get context value from parent directly
		if (parentContextMap != null && !parentContextMap.isEmpty()) {
		}

		// Resume: init the resumeUtil
		resumeEntryMethodName = ResumeUtil
				.getResumeEntryMethodName(resuming_checkpoint_path);
		resumeUtil = new ResumeUtil(resuming_logs_dir_path, isChildJob, rootPid);
		resumeUtil.initCommonInfo(pid, rootPid, fatherPid, projectName,
				jobName, contextStr, jobVersion);

		List<String> parametersToEncrypt = new java.util.ArrayList<String>();
		// Resume: jobStart
		resumeUtil.addLog("JOB_STARTED", "JOB:" + jobName,
				parent_part_launcher, Thread.currentThread().getId() + "", "",
				"", "", "",
				resumeUtil.convertToJsonText(context, parametersToEncrypt));

		if (execStat) {
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

		long startUsedMemory = Runtime.getRuntime().totalMemory()
				- Runtime.getRuntime().freeMemory();
		long endUsedMemory = 0;
		long end = 0;

		startTime = System.currentTimeMillis();

		this.globalResumeTicket = true;// to run tPreJob

		this.globalResumeTicket = false;// to run others jobs

		try {
			errorCode = null;
			tFileInputExcel_1Process(globalMap);
			if (!"failure".equals(status)) {
				status = "end";
			}
		} catch (TalendException e_tFileInputExcel_1) {
			globalMap.put("tFileInputExcel_1_SUBPROCESS_STATE", -1);

			e_tFileInputExcel_1.printStackTrace();

		}

		this.globalResumeTicket = true;// to run tPostJob

		end = System.currentTimeMillis();

		if (watch) {
			System.out.println((end - startTime) + " milliseconds");
		}

		endUsedMemory = Runtime.getRuntime().totalMemory()
				- Runtime.getRuntime().freeMemory();
		if (false) {
			System.out.println((endUsedMemory - startUsedMemory)
					+ " bytes memory increase when running : dataMigration");
		}

		if (execStat) {
			runStat.updateStatOnJob(RunStat.JOBEND, fatherNode);
			runStat.stopThreadStat();
		}
		int returnCode = 0;
		if (errorCode == null) {
			returnCode = status != null && status.equals("failure") ? 1 : 0;
		} else {
			returnCode = errorCode.intValue();
		}
		resumeUtil.addLog("JOB_ENDED", "JOB:" + jobName, parent_part_launcher,
				Thread.currentThread().getId() + "", "", "" + returnCode, "",
				"", "");

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
				if (fatherPid == null) {
					context_param.setContextType(keyValue.substring(0, index),
							replaceEscapeChars(keyValue.substring(index + 1)));
				} else { // the subjob won't escape the especial chars
					context_param.setContextType(keyValue.substring(0, index),
							keyValue.substring(index + 1));
				}

			}

		} else if (arg.startsWith("--context_param")) {
			String keyValue = arg.substring(16);
			int index = -1;
			if (keyValue != null && (index = keyValue.indexOf('=')) > -1) {
				if (fatherPid == null) {
					context_param.put(keyValue.substring(0, index),
							replaceEscapeChars(keyValue.substring(index + 1)));
				} else { // the subjob won't escape the especial chars
					context_param.put(keyValue.substring(0, index),
							keyValue.substring(index + 1));
				}
			}
		} else if (arg.startsWith("--log4jLevel=")) {
			log4jLevel = arg.substring(13);
		}

	}

	private static final String NULL_VALUE_EXPRESSION_IN_COMMAND_STRING_FOR_CHILD_JOB_ONLY = "<TALEND_NULL>";

	private final String[][] escapeChars = { { "\\\\", "\\" }, { "\\n", "\n" },
			{ "\\'", "\'" }, { "\\r", "\r" }, { "\\f", "\f" }, { "\\b", "\b" },
			{ "\\t", "\t" } };

	private String replaceEscapeChars(String keyValue) {

		if (keyValue == null || ("").equals(keyValue.trim())) {
			return keyValue;
		}

		StringBuilder result = new StringBuilder();
		int currIndex = 0;
		while (currIndex < keyValue.length()) {
			int index = -1;
			// judege if the left string includes escape chars
			for (String[] strArray : escapeChars) {
				index = keyValue.indexOf(strArray[0], currIndex);
				if (index >= 0) {

					result.append(keyValue.substring(currIndex,
							index + strArray[0].length()).replace(strArray[0],
							strArray[1]));
					currIndex = index + strArray[0].length();
					break;
				}
			}
			// if the left string doesn't include escape chars, append the left
			// into the result
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
 * 106636 characters generated by Talend Cloud Data Management Platform on the
 * 13 December, 2018 10:02:12 AM IST
 ************************************************************************************************/
