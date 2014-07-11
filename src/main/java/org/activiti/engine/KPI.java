package org.activiti.engine;

import java.sql.Timestamp;

public class KPI {
	private Timestamp ALERTTIME ;          
	private Timestamp  OVERTIME;
	private int  NOTICERATE;
	private String  IS_CONTAIN_HOLIDAY;
	private long DURATION_NODE;
	public KPI() {
		// TODO Auto-generated constructor stub
	}
	public Timestamp getALERTTIME() {
		return ALERTTIME;
	}
	public void setALERTTIME(Timestamp aLERTTIME) {
		ALERTTIME = aLERTTIME;
	}
	public Timestamp getOVERTIME() {
		return OVERTIME;
	}
	public void setOVERTIME(Timestamp oVERTIME) {
		OVERTIME = oVERTIME;
	}
	public int getNOTICERATE() {
		return NOTICERATE;
	}
	public void setNOTICERATE(int nOTICERATE) {
		NOTICERATE = nOTICERATE;
	}
	public String getIS_CONTAIN_HOLIDAY() {
		return IS_CONTAIN_HOLIDAY;
	}
	public void setIS_CONTAIN_HOLIDAY(String iS_CONTAIN_HOLIDAY) {
		IS_CONTAIN_HOLIDAY = iS_CONTAIN_HOLIDAY;
	}
	public long getDURATION_NODE() {
		return DURATION_NODE;
	}
	public void setDURATION_NODE(long dURATION_NODE) {
		DURATION_NODE = dURATION_NODE;
	}
	

}
