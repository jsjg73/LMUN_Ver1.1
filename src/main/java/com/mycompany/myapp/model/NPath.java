package com.mycompany.myapp.model;

import org.json.simple.JSONArray;

public class NPath {
		private String sx;
		private String sy;
		private String ex;
		private String ey;
		
		private long stCode;
		private String stMsg;
		private int time;
		
		private String rawPath;
		private JSONArray path;
		public String getSx() {
			return sx;
		}
		public void setSx(String sx) {
			this.sx = sx;
		}
		public String getSy() {
			return sy;
		}
		public void setSy(String sy) {
			this.sy = sy;
		}
		public String getEx() {
			return ex;
		}
		public void setEx(String ex) {
			this.ex = ex;
		}
		public String getEy() {
			return ey;
		}
		public void setEy(String ey) {
			this.ey = ey;
		}
		public long getStCode() {
			return stCode;
		}
		public void setStCode(long stCode) {
			this.stCode = stCode;
		}
		public String getStMsg() {
			return stMsg;
		}
		public void setStMsg(String stMsg) {
			this.stMsg = stMsg;
		}
		public int getTime() {
			return time;
		}
		public void setTime(int time) {
			this.time = time;
		}
		public String getRawPath() {
			return rawPath;
		}
		public void setRawPath(String rawPath) {
			this.rawPath = rawPath;
		}
		public JSONArray getPath() {
			return path;
		}
		public void setPath(JSONArray path) {
			this.path = path;
		}
		

}
