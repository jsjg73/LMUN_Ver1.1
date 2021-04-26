package com.mycompany.myapp.service.kakao;

public class KakaoCategorySearch extends KakaoMapSearch {
	private String category_group_code;
	private String x,y,sort;
	private int page,size,radius;
	
	private KakaoCategorySearch(Builder builder) {
		SEARCH_TYPE = "v2/local/search/category.json";
		this.category_group_code = builder.category_group_code;
		this.x = builder.x;
		this.y = builder.y;
		this.radius = builder.radius;
		this.sort = builder.sort;
		this.page = builder.page;
		this.size = builder.size;
		
		options.put("category_group_code",category_group_code);
		options.put("x",x);
		options.put("y",y);
		options.put("radius",radius+"");
		options.put("sort",sort);
		options.put("page",page+"");
		options.put("size",size+"");
	}
	static class Builder{
		private String category_group_code,x,y;
		private int radius;
		
		private String sort="accuracy";
		private int page=1,size=15;
		
		public Builder(String category_group_code, String x, String y, int radius) {
			this.category_group_code = category_group_code;
			this.radius =radius;
			this.x =x;
			this.y = y;
		}
		public Builder sort(String sort) {
			this.sort = sort;
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
		
		public KakaoCategorySearch build() {
			return new KakaoCategorySearch(this);
		}
	}
}
