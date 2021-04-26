package com.mycompany.myapp.service.kakao;

public class KakaoAddressSearch extends KakaoMapSearch {
	private String query;
	private String analyze_type;
	private int page;
	private int size;
	
	private KakaoAddressSearch(Builder builder) {
		SEARCH_TYPE = "v2/local/search/address.json";
		this.query = builder.query;
		this.analyze_type = builder.analyze_type;
		this.page = builder.page;
		this.size = builder.size;
		
		options.put("query",query);
		options.put("analyze_type",analyze_type);
		options.put("page",page+"");
		options.put("size",size+"");
	}
	static class Builder{
		private String query;

		private String analyze_type="similar";
		private int page=1;
		private int size=10;
		public Builder(String query) {
			this.query = query;
		}
		public Builder analyze_type(String analyze_type) {
			this.analyze_type = analyze_type;
			return this;
		}
		public Builder page(int page) {
			this.page = page;
			return this;
		}
		public Builder s(int size) {
			this.size = size;
			return this;
		}
		
		public KakaoAddressSearch build() {
			return new KakaoAddressSearch(this);
		}
	}
}
