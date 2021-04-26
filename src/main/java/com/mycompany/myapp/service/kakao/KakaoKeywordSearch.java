package com.mycompany.myapp.service.kakao;

public class KakaoKeywordSearch extends KakaoMapSearch {
	private String query,category_group_code;
	private String x,y,sort;
	private int page,size,radius;
	
	private KakaoKeywordSearch(Builder builder) {
		SEARCH_TYPE = "v2/local/search/keyword.json";
		this.query = builder.query;
		this.category_group_code = builder.category_group_code;
		this.x = builder.x;
		this.y = builder.y;
		this.radius = builder.radius;
		this.sort = builder.sort;
		this.page = builder.page;
		this.size = builder.size;
		
		options.put("query",query);
		options.put("sort",sort);
		options.put("page",page+"");
		options.put("size",size+"");

		if(category_group_code!=null)
			options.put("category_group_code",category_group_code);
		if(x!=null)
			options.put("x",x);
		if(y!=null)
			options.put("y",y);
		if(0<radius&&radius<20000)
			options.put("radius",radius+"");
	}
	static class Builder{
		private String query,category_group_code,x,y;
		private int radius;
		
		private String sort="accuracy";
		private int page=1,size=15;
		public Builder(String query) {
			this.query = query;
		}
		public Builder category_group_code(String code) {
			this.category_group_code = code;
			return this;
		}
		public Builder x(String x) {
			this.x = x;
			return this;
		}
		public Builder y(String y) {
			this.y = y;
			return this;
		}
		public Builder sort(String sort) {
			this.sort = sort;
			return this;
		}
		public Builder radius(int radius) {
			this.radius = radius;
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
		
		public KakaoKeywordSearch build() {
			return new KakaoKeywordSearch(this);
		}
	}
}
